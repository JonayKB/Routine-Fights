import React, { createContext, ReactNode, useCallback, useContext, useMemo, useRef, useState } from "react";
import Icon from "./Icon";

type Kind = "success" | "error" | "info";
type Toast = { id: number; kind: Kind; message: string };

const ToastContext = createContext<Record<Kind, (message: string) => void>>({
  success: () => {},
  error: () => {},
  info: () => {},
});

export const useToast = () => useContext(ToastContext);

/** Results and errors at the bottom of the screen, instead of alert(). */
const ToastProvider = ({ children }: { children: ReactNode }) => {
  const [toasts, setToasts] = useState<Toast[]>([]);
  const next = useRef(1);

  const show = useCallback((kind: Kind, message: string) => {
    const id = next.current++;
    setToasts((prev) => [...prev, { id, kind, message }]);
    setTimeout(() => setToasts((prev) => prev.filter((t) => t.id !== id)), kind === "error" ? 6000 : 3500);
  }, []);

  const api = useMemo(
    () => ({
      success: (m: string) => show("success", m),
      error: (m: string) => show("error", m),
      info: (m: string) => show("info", m),
    }),
    [show]
  );

  return (
    <ToastContext.Provider value={api}>
      {children}
      <div aria-live="polite" className="fixed bottom-4 inset-x-4 sm:left-auto sm:right-6 sm:w-96 z-[60] flex flex-col gap-2">
        {toasts.map((t) => (
          <div
            key={t.id}
            role={t.kind === "error" ? "alert" : "status"}
            className="flex items-start gap-3 bg-card border border-ink/10 rounded-xl shadow-pop px-4 py-3 text-sm"
          >
            <span className={t.kind === "error" ? "text-danger" : t.kind === "success" ? "text-success" : "text-ink-2"}>
              <Icon name={t.kind === "success" ? "check" : "alert"} />
            </span>
            <span className="flex-1 text-ink">{t.message}</span>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
};

export default ToastProvider;
