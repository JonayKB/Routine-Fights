import RNSecureKeyStore from "react-native-secure-key-store";
import { uri } from "./Utils";

/** Live event pushed by the API (see UserEvent.java). */
export type UserEvent = {
  kind: "MESSAGE" | "READ" | "TYPING" | "NOTIFICATION";
  conversationId?: string | null;
  message?: {
    id: string;
    conversationId: string;
    text: string;
    senderEmail: string;
    createdAt: string;
    mine: boolean;
  } | null;
  readAt?: string | null;
  unreadNotifications?: number | null;
  unreadMessages?: number | null;
};

type Listener = (event: UserEvent) => void;
type StatusListener = (connected: boolean) => void;

const WS_URL = uri.replace(/^http/, "ws") + "/graphql-ws";
const SUBSCRIPTION = `subscription {
  userEvents {
    kind conversationId readAt unreadNotifications unreadMessages
    message { id conversationId text senderEmail createdAt mine }
  }
}`;
const MAX_BACKOFF = 30000;

/**
 * One WebSocket to the API speaking the graphql-transport-ws protocol, shared
 * by the whole app. It reconnects with backoff until `stop()` is called, and
 * anything interested in live events subscribes with `onEvent`.
 */
class RealtimeClient {
  private socket: WebSocket | null = null;
  private listeners = new Set<Listener>();
  private statusListeners = new Set<StatusListener>();
  private running = false;
  private connected = false;
  private attempts = 0;
  private retry: ReturnType<typeof setTimeout> | null = null;

  onEvent(listener: Listener) {
    this.listeners.add(listener);
    return () => {
      this.listeners.delete(listener);
    };
  }

  onStatus(listener: StatusListener) {
    this.statusListeners.add(listener);
    listener(this.connected);
    return () => {
      this.statusListeners.delete(listener);
    };
  }

  isConnected() {
    return this.connected;
  }

  start() {
    if (this.running) return;
    this.running = true;
    this.attempts = 0;
    this.open();
  }

  stop() {
    this.running = false;
    if (this.retry) clearTimeout(this.retry);
    this.retry = null;
    this.close();
  }

  /** Reconnects now (e.g. the app came back to the foreground). */
  wake() {
    if (!this.running) return;
    if (this.socket && this.socket.readyState <= WebSocket.OPEN) return;
    if (this.retry) clearTimeout(this.retry);
    this.retry = null;
    this.attempts = 0;
    this.open();
  }

  private setConnected(value: boolean) {
    if (this.connected === value) return;
    this.connected = value;
    this.statusListeners.forEach((l) => l(value));
  }

  private async open() {
    let token: string | null = null;
    try {
      token = await RNSecureKeyStore.get("token");
    } catch {
      token = null;
    }
    if (!this.running || !token) return;

    const socket = new WebSocket(WS_URL, "graphql-transport-ws");
    this.socket = socket;

    socket.onopen = () => {
      socket.send(JSON.stringify({ type: "connection_init", payload: { Authorization: `Bearer ${token}` } }));
    };

    socket.onmessage = (raw) => {
      let msg: any;
      try {
        msg = JSON.parse(String(raw.data));
      } catch {
        return;
      }
      switch (msg.type) {
        case "connection_ack":
          this.attempts = 0;
          this.setConnected(true);
          socket.send(JSON.stringify({ id: "events", type: "subscribe", payload: { query: SUBSCRIPTION } }));
          break;
        case "ping":
          socket.send(JSON.stringify({ type: "pong" }));
          break;
        case "next": {
          const event: UserEvent | undefined = msg.payload?.data?.userEvents;
          if (event) this.listeners.forEach((l) => l(event));
          break;
        }
        case "error":
        case "complete":
          // The server ended the subscription: start over.
          socket.close();
          break;
      }
    };

    socket.onclose = () => {
      if (this.socket === socket) this.socket = null;
      this.setConnected(false);
      this.schedule();
    };
    socket.onerror = () => {
      // onclose follows and schedules the retry.
    };
  }

  private close() {
    const socket = this.socket;
    this.socket = null;
    this.setConnected(false);
    if (socket) {
      socket.onclose = null;
      try {
        socket.close();
      } catch {
        // Already closed.
      }
    }
  }

  private schedule() {
    if (!this.running || this.retry) return;
    const delay = Math.min(MAX_BACKOFF, 1000 * 2 ** this.attempts) + Math.random() * 500;
    this.attempts += 1;
    this.retry = setTimeout(() => {
      this.retry = null;
      this.open();
    }, delay);
  }
}

export const realtime = new RealtimeClient();
