import React, { FormEvent, useEffect, useMemo, useState } from "react";
import { broadcast, getPushDevices, getPushLanguages, LanguageCount, notifyUser, PushText } from "../api/admin";
import { Button, Card, CardHeader, Field, Input, Modal, PageHeader, Skeleton, TextArea } from "../ui/kit";
import Icon from "../ui/Icon";
import { useToast } from "../ui/Toast";

const MAX_TITLE = 120;
const MAX_BODY = 1000;
const HISTORY_KEY = "pushHistory";
const DEFAULT = "default";

const LANGUAGE_NAMES: Record<string, string> = {
  default: "Everyone else",
  en: "English",
  es: "Español",
  fr: "Français",
  de: "Deutsch",
};

/** Ready-made texts; the test one checks the whole chain end to end. */
const PRESETS: { name: string; texts: Record<string, { title: string; body: string }> }[] = [
  {
    name: "Test",
    texts: {
      en: { title: "Routine Fights test", body: "If you can read this, push notifications work." },
      es: { title: "Prueba de Routine Fights", body: "Si lees esto, las notificaciones push funcionan." },
      fr: { title: "Test Routine Fights", body: "Si tu lis ceci, les notifications push fonctionnent." },
      de: { title: "Routine Fights Test", body: "Wenn du das liest, funktionieren Push-Benachrichtigungen." },
    },
  },
  {
    name: "Streak reminder",
    texts: {
      en: { title: "Keep your flame alive 🔥", body: "Have you posted today? Your streaks are waiting." },
      es: { title: "Mantén viva tu llama 🔥", body: "¿Ya has publicado hoy? Tus rachas te esperan." },
      fr: { title: "Garde ta flamme allumée 🔥", body: "As-tu publié aujourd'hui ? Tes séries t'attendent." },
      de: { title: "Halte deine Flamme am Leben 🔥", body: "Schon heute gepostet? Deine Serien warten." },
    },
  },
];

type Sent = { at: string; title: string; languages: number; result: string };

const readHistory = (): Sent[] => {
  try {
    return JSON.parse(localStorage.getItem(HISTORY_KEY) ?? "[]");
  } catch {
    return [];
  }
};

const writeHistory = (items: Sent[]) => {
  try {
    localStorage.setItem(HISTORY_KEY, JSON.stringify(items.slice(0, 10)));
  } catch {
    // Not important: history is a convenience.
  }
};

/** How the notification looks on a phone. */
const Preview = ({ title, body }: { title: string; body: string }) => (
  <div className="rounded-2xl bg-raised p-4">
    <p className="text-xs text-ink-3 mb-2">Preview</p>
    <div className="flex gap-3 rounded-xl bg-card border border-ink/10 shadow-card p-3">
      <img src="/Logo.png" alt="" className="w-9 h-9 rounded-lg" />
      <div className="min-w-0 flex-1">
        <div className="flex items-center justify-between gap-2">
          <p className="text-sm font-semibold text-ink truncate">{title || "Title"}</p>
          <span className="text-[11px] text-ink-3 shrink-0">now</span>
        </div>
        <p className={`text-sm line-clamp-3 ${body ? "text-ink-2" : "text-ink-3"}`}>{body || "Message"}</p>
      </div>
    </div>
  </div>
);

const Counter = ({ value, max }: { value: number; max: number }) => (
  <span className={value > max ? "text-danger" : ""}>
    {value}/{max}
  </span>
);

const NotificationsScreen = () => {
  const toast = useToast();
  const [devices, setDevices] = useState<LanguageCount[] | null>(null);
  const [languages, setLanguages] = useState<string[]>(["en", "es", "fr", "de"]);
  const [tab, setTab] = useState<string>(DEFAULT);
  const [texts, setTexts] = useState<Record<string, { title: string; body: string }>>({});
  const [confirming, setConfirming] = useState(false);
  const [sending, setSending] = useState(false);
  const [history, setHistory] = useState<Sent[]>(readHistory);

  const [email, setEmail] = useState("");
  const [testTitle, setTestTitle] = useState("");
  const [testBody, setTestBody] = useState("");
  const [testing, setTesting] = useState(false);

  const loadDevices = () => {
    setDevices(null);
    getPushDevices()
      .then(setDevices)
      .catch((e) => {
        setDevices([]);
        toast.error(e?.message ?? "Could not load devices");
      });
  };

  useEffect(() => {
    loadDevices();
    getPushLanguages()
      .then((l) => l.length && setLanguages(l))
      .catch(() => {});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const total = devices?.reduce((a, d) => a + d.devices, 0) ?? 0;
  const tabs = [DEFAULT, ...languages];
  const current = texts[tab] ?? { title: "", body: "" };
  const setCurrent = (patch: Partial<{ title: string; body: string }>) =>
    setTexts((prev) => ({ ...prev, [tab]: { ...current, ...patch } }));

  const filled = useMemo(
    () => tabs.filter((l) => (texts[l]?.title ?? "").trim()),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [texts, languages]
  );
  const missing = languages.filter((l) => !filled.includes(l));
  const fallbackSet = filled.includes(DEFAULT);
  const tooLong = tabs.some((l) => (texts[l]?.title.length ?? 0) > MAX_TITLE || (texts[l]?.body.length ?? 0) > MAX_BODY);
  const canSend = filled.length > 0 && !tooLong;

  const applyPreset = (preset: (typeof PRESETS)[number]) => {
    const next: Record<string, { title: string; body: string }> = {};
    languages.forEach((l) => preset.texts[l] && (next[l] = { ...preset.texts[l] }));
    next[DEFAULT] = { ...preset.texts.en };
    setTexts(next);
  };

  const send = async () => {
    setSending(true);
    try {
      const payload: PushText[] = filled.map((language) => ({
        language,
        title: texts[language].title.trim(),
        body: texts[language].body.trim(),
      }));
      const result = await broadcast(payload);
      const entry: Sent = {
        at: new Date().toISOString(),
        title: (texts[DEFAULT]?.title || texts[filled[0]]?.title || "").trim(),
        languages: filled.length,
        result,
      };
      const next = [entry, ...history];
      setHistory(next.slice(0, 10));
      writeHistory(next);
      setConfirming(false);
      setTexts({});
      toast.success("Notification sent to everyone");
    } catch (e: any) {
      toast.error(e?.message ?? "Could not send the notification");
    } finally {
      setSending(false);
    }
  };

  const sendTest = async (event: FormEvent) => {
    event.preventDefault();
    setTesting(true);
    try {
      const delivered = await notifyUser(email.trim(), testTitle.trim(), testBody.trim());
      if (delivered > 0) {
        toast.success(`Delivered to ${delivered} device${delivered === 1 ? "" : "s"}`);
      } else {
        toast.error("Firebase didn't accept it for any of that user's devices");
      }
    } catch (e: any) {
      toast.error(e?.message ?? "Could not send the test");
    } finally {
      setTesting(false);
    }
  };

  return (
    <>
      <PageHeader title="Notifications" hint="Push notifications to the app, in each user's language." />

      <div className="grid lg:grid-cols-[1fr_20rem] gap-4 items-start">
        <div className="flex flex-col gap-4 min-w-0">
          {/* Everyone */}
          <Card>
            <CardHeader
              title="Send to everyone"
              hint="Each phone gets the text of its language; the rest get “Everyone else”."
            />
            <div className="px-5 pt-4 flex flex-wrap items-center gap-2">
              <span className="text-xs text-ink-3 mr-1">Start from</span>
              {PRESETS.map((p) => (
                <button
                  key={p.name}
                  onClick={() => applyPreset(p)}
                  className="h-7 px-3 rounded-full text-xs font-medium bg-raised text-ink-2 hover:text-ink"
                >
                  {p.name}
                </button>
              ))}
            </div>

            <div className="px-5 pt-4">
              <div role="tablist" aria-label="Language" className="flex gap-1 overflow-x-auto border-b border-ink/10">
                {tabs.map((l) => {
                  const done = filled.includes(l);
                  return (
                    <button
                      key={l}
                      role="tab"
                      aria-selected={tab === l}
                      onClick={() => setTab(l)}
                      className={`relative flex items-center gap-1.5 h-10 px-3 text-sm whitespace-nowrap transition ${
                        tab === l ? "text-ink font-medium" : "text-ink-3 hover:text-ink"
                      }`}
                    >
                      {done && <span className="w-1.5 h-1.5 rounded-full bg-success" aria-label="has text" />}
                      {LANGUAGE_NAMES[l] ?? l.toUpperCase()}
                      {tab === l && <span className="absolute inset-x-2 -bottom-px h-0.5 rounded-full bg-accent" />}
                    </button>
                  );
                })}
              </div>
            </div>

            <div className="grid md:grid-cols-2 gap-5 p-5">
              <div className="flex flex-col gap-4">
                <Field label="Title" htmlFor="title" hint={<Counter value={current.title.length} max={MAX_TITLE} />}>
                  <Input
                    id="title"
                    value={current.title}
                    onChange={(e) => setCurrent({ title: e.target.value })}
                    placeholder={tab === DEFAULT ? "Shown to languages without their own text" : ""}
                  />
                </Field>
                <Field label="Message" htmlFor="body" hint={<Counter value={current.body.length} max={MAX_BODY} />}>
                  <TextArea id="body" value={current.body} onChange={(e) => setCurrent({ body: e.target.value })} />
                </Field>
              </div>
              <Preview title={current.title} body={current.body} />
            </div>

            <div className="flex flex-wrap items-center justify-between gap-3 px-5 py-4 border-t border-ink/10">
              <p className="text-xs text-ink-3">
                {!filled.length
                  ? "Write at least one title."
                  : missing.length && !fallbackSet
                  ? `No text for ${missing.map((l) => LANGUAGE_NAMES[l] ?? l).join(", ")}: they won't get it.`
                  : missing.length
                  ? `${missing.map((l) => LANGUAGE_NAMES[l] ?? l).join(", ")} will get “Everyone else”.`
                  : "Every language has its own text."}
              </p>
              <Button icon="send" disabled={!canSend} onClick={() => setConfirming(true)}>
                Send to everyone
              </Button>
            </div>
          </Card>

          {/* One user */}
          <Card>
            <CardHeader title="Test on one user" hint="Goes only to the phones where that account is signed in." />
            <form onSubmit={sendTest} className="grid sm:grid-cols-2 gap-4 p-5">
              <Field label="User email" htmlFor="test-email">
                <Input id="test-email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
              </Field>
              <Field label="Title" htmlFor="test-title">
                <Input
                  id="test-title"
                  value={testTitle}
                  maxLength={MAX_TITLE}
                  onChange={(e) => setTestTitle(e.target.value)}
                  required
                />
              </Field>
              <div className="sm:col-span-2">
                <Field label="Message" htmlFor="test-body">
                  <Input id="test-body" value={testBody} maxLength={MAX_BODY} onChange={(e) => setTestBody(e.target.value)} />
                </Field>
              </div>
              <div className="sm:col-span-2 flex justify-end">
                <Button type="submit" variant="secondary" icon="send" loading={testing}>
                  Send test
                </Button>
              </div>
            </form>
          </Card>
        </div>

        {/* Side: devices and history */}
        <div className="flex flex-col gap-4">
          <Card>
            <CardHeader
              title="Devices"
              action={
                <button onClick={loadDevices} aria-label="Refresh devices" className="p-1.5 rounded-lg text-ink-3 hover:bg-raised">
                  <Icon name="refresh" size={16} />
                </button>
              }
            />
            <div className="p-5 pt-3">
              {devices === null ? (
                <Skeleton className="h-20" />
              ) : (
                <>
                  <p className="font-display text-3xl font-semibold tabular-nums">{total}</p>
                  <p className="text-sm text-ink-3">phones can receive pushes</p>
                  {total === 0 ? (
                    <p className="mt-4 text-xs text-ink-2 bg-raised rounded-xl p-3">
                      No phone has registered yet. Install the latest app, sign in and allow notifications.
                    </p>
                  ) : (
                    <ul className="mt-4 flex flex-col gap-2">
                      {devices.map((d) => (
                        <li key={d.language} className="flex items-center justify-between text-sm">
                          <span className="text-ink-2">
                            {LANGUAGE_NAMES[d.language.split(/[-_]/)[0]] ?? d.language}
                          </span>
                          <span className="tabular-nums text-ink">{d.devices}</span>
                        </li>
                      ))}
                    </ul>
                  )}
                </>
              )}
            </div>
          </Card>

          <Card>
            <CardHeader title="Recently sent" hint="From this browser." />
            <div className="p-5 pt-3">
              {history.length === 0 ? (
                <p className="text-sm text-ink-3">Nothing sent yet.</p>
              ) : (
                <ul className="flex flex-col divide-y divide-ink/10">
                  {history.map((h) => (
                    <li key={h.at} className="py-2.5 first:pt-0 last:pb-0">
                      <p className="text-sm text-ink truncate">{h.title}</p>
                      <p className="text-xs text-ink-3">
                        {new Date(h.at).toLocaleString()} · {h.languages} text{h.languages === 1 ? "" : "s"}
                      </p>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </Card>
        </div>
      </div>

      <Modal
        open={confirming}
        title="Send to everyone?"
        onClose={() => !sending && setConfirming(false)}
        footer={
          <>
            <Button variant="secondary" onClick={() => setConfirming(false)} disabled={sending}>
              Cancel
            </Button>
            <Button icon="send" onClick={send} loading={sending}>
              Send now
            </Button>
          </>
        }
      >
        <p className="text-sm text-ink-2">
          Every user with notifications on will get it right away
          {total ? ` (${total} registered phone${total === 1 ? "" : "s"})` : ""}. This can't be undone.
        </p>
        <div className="mt-4">
          <Preview
            title={(texts[DEFAULT]?.title || texts[filled[0]]?.title) ?? ""}
            body={(texts[DEFAULT]?.title ? texts[DEFAULT]?.body : texts[filled[0]]?.body) ?? ""}
          />
        </div>
      </Modal>
    </>
  );
};

export default NotificationsScreen;
