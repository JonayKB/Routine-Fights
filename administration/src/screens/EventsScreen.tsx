import React, { FormEvent, useEffect, useMemo, useState } from "react";
import {
  Activity,
  Badge,
  CommunityEvent,
  createBadge,
  createEvent,
  getActivities,
  getBadges,
  getEvents,
  uploadImage,
} from "../api/admin";
import { Button, Card, Empty, Field, Input, Modal, PageHeader, Skeleton, StoredImage } from "../ui/kit";
import Icon from "../ui/Icon";
import { useToast } from "../ui/Toast";

type Status = "active" | "upcoming" | "finished";

const statusOf = (e: CommunityEvent): Status => {
  const now = Date.now();
  if (now < new Date(e.startDate).getTime()) return "upcoming";
  if (now > new Date(e.finishDate).getTime()) return "finished";
  return "active";
};

const STATUS: Record<Status, { label: string; className: string }> = {
  active: { label: "Active", className: "bg-success/15 text-success" },
  upcoming: { label: "Upcoming", className: "bg-accent/10 text-accent-ink" },
  finished: { label: "Finished", className: "bg-raised text-ink-3" },
};

const formatDate = (value: string) =>
  new Date(value).toLocaleDateString(undefined, { day: "numeric", month: "short", year: "numeric" });

/** Image picker with a preview. */
const ImageInput = ({ file, onChange, id }: { file: File | null; onChange: (f: File | null) => void; id: string }) => {
  const preview = useMemo(() => (file ? URL.createObjectURL(file) : null), [file]);
  useEffect(() => () => {
    if (preview) URL.revokeObjectURL(preview);
  }, [preview]);
  return (
    <label
      htmlFor={id}
      className="flex items-center gap-4 p-3 rounded-xl border border-dashed border-ink/20 cursor-pointer hover:bg-raised transition"
    >
      {preview ? (
        <img src={preview} alt="" className="w-16 h-16 rounded-lg object-cover" />
      ) : (
        <span className="w-16 h-16 rounded-lg bg-raised flex items-center justify-center text-ink-3">
          <Icon name="image" />
        </span>
      )}
      <span className="text-sm text-ink-2">{file ? file.name : "Choose an image"}</span>
      <input
        id={id}
        type="file"
        accept="image/*"
        className="sr-only"
        onChange={(e) => onChange(e.target.files?.[0] ?? null)}
      />
    </label>
  );
};

const EventCard = ({ event }: { event: CommunityEvent }) => {
  const toast = useToast();
  const [badges, setBadges] = useState<Badge[] | null>(null);
  const [adding, setAdding] = useState(false);
  const [level, setLevel] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const status = statusOf(event);

  const load = () =>
    getBadges(event.id)
      .then((b) => setBadges([...b].sort((a, z) => a.level - z.level)))
      .catch(() => setBadges([]));

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [event.id]);

  const open = () => {
    setLevel(String((badges?.reduce((m, b) => Math.max(m, b.level), 0) ?? 0) + 1));
    setFile(null);
    setError(null);
    setAdding(true);
  };

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    const n = Number(level);
    if (!Number.isInteger(n) || n < 1) return setError("The level must be a whole number from 1");
    if (badges?.some((b) => b.level === n)) return setError(`There is already a level ${n} badge`);
    if (!file) return setError("Choose the badge image");
    setSaving(true);
    try {
      await createBadge(event.id, await uploadImage(file), n);
      await load();
      setAdding(false);
      toast.success(`Level ${n} badge added`);
    } catch (err: any) {
      setError(err?.message ?? "Could not add the badge");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Card className="overflow-hidden flex flex-col">
      <div className="relative">
        <StoredImage name={event.image} alt="" className="w-full aspect-[16/9]" />
        <span className={`absolute top-3 left-3 px-2.5 py-1 rounded-full text-xs font-semibold backdrop-blur ${STATUS[status].className}`}>
          {STATUS[status].label}
        </span>
      </div>
      <div className="p-5 flex flex-col gap-4 flex-1">
        <div>
          <h3 className="font-display text-lg font-medium text-ink">{event.name}</h3>
          <p className="text-sm text-ink-2 mt-1">
            {formatDate(event.startDate)} – {formatDate(event.finishDate)}
          </p>
          <p className="text-sm text-ink-3 mt-0.5">Goal: {event.totalRequired.toLocaleString()} points</p>
        </div>

        <div className="mt-auto">
          <p className="text-xs font-medium text-ink-3 mb-2">Badges</p>
          <div className="flex flex-wrap items-center gap-2">
            {badges === null ? (
              <Skeleton className="h-10 w-28" />
            ) : (
              badges.map((b) => (
                <div key={b.id} className="flex flex-col items-center gap-1" title={`Level ${b.level}`}>
                  <StoredImage name={b.image} alt={`Level ${b.level} badge`} size="small" className="w-10 h-10 rounded-full border border-ink/10" />
                  <span className="text-[11px] text-ink-3 tabular-nums">{b.level}</span>
                </div>
              ))
            )}
            <button
              onClick={open}
              aria-label="Add badge"
              className="w-10 h-10 mb-5 rounded-full border border-dashed border-ink/25 text-ink-3 hover:text-ink hover:bg-raised flex items-center justify-center"
            >
              <Icon name="plus" size={16} />
            </button>
          </div>
        </div>
      </div>

      <Modal
        open={adding}
        title={`New badge · ${event.name}`}
        onClose={() => !saving && setAdding(false)}
        footer={
          <>
            <Button variant="secondary" onClick={() => setAdding(false)} disabled={saving}>
              Cancel
            </Button>
            <Button type="submit" form={`badge-${event.id}`} loading={saving}>
              Add badge
            </Button>
          </>
        }
      >
        <form id={`badge-${event.id}`} onSubmit={submit} className="flex flex-col gap-4">
          <Field label="Level" htmlFor={`level-${event.id}`} hint="Users earn it when they reach this level in the event.">
            <Input id={`level-${event.id}`} type="number" min={1} value={level} onChange={(e) => setLevel(e.target.value)} />
          </Field>
          <Field label="Image" htmlFor={`badge-image-${event.id}`}>
            <ImageInput id={`badge-image-${event.id}`} file={file} onChange={setFile} />
          </Field>
          {error && <p role="alert" className="text-sm text-danger">{error}</p>}
        </form>
      </Modal>
    </Card>
  );
};

const NewEventModal = ({ open, onClose, onCreated }: { open: boolean; onClose: () => void; onCreated: () => void }) => {
  const toast = useToast();
  const [activities, setActivities] = useState<Activity[] | null>(null);
  const [name, setName] = useState("");
  const [start, setStart] = useState("");
  const [finish, setFinish] = useState("");
  const [total, setTotal] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [selected, setSelected] = useState<string[]>([]);
  const [filter, setFilter] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!open) return;
    setName("");
    setStart("");
    setFinish("");
    setTotal("");
    setFile(null);
    setSelected([]);
    setFilter("");
    setError(null);
    if (activities === null) {
      getActivities()
        .then((a) => setActivities([...a].sort((x, y) => x.name.localeCompare(y.name))))
        .catch(() => setActivities([]));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open]);

  const shown = (activities ?? []).filter((a) => a.name.toLowerCase().includes(filter.trim().toLowerCase()));
  const toggle = (id: string) =>
    setSelected((prev) => (prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]));

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    const startDate = new Date(`${start}T00:00:00`);
    const finishDate = new Date(`${finish}T23:59:59`);
    const goal = Number(total);
    if (!name.trim()) return setError("Give the event a name");
    if (!start || !finish || finishDate <= startDate) return setError("The end date must be after the start date");
    if (!Number.isInteger(goal) || goal < 1) return setError("The goal must be a whole number of points from 1");
    if (!selected.length) return setError("Choose at least one activity");
    if (!file) return setError("Choose the event image");
    setSaving(true);
    try {
      await createEvent({
        name: name.trim(),
        image: await uploadImage(file),
        startDate,
        finishDate,
        totalRequired: goal,
        activitiesIDs: selected,
      });
      toast.success("Event created");
      onCreated();
      onClose();
    } catch (err: any) {
      setError(err?.message ?? "Could not create the event");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal
      open={open}
      wide
      title="New community event"
      onClose={() => !saving && onClose()}
      footer={
        <>
          <Button variant="secondary" onClick={onClose} disabled={saving}>
            Cancel
          </Button>
          <Button type="submit" form="new-event" loading={saving}>
            Create event
          </Button>
        </>
      }
    >
      <form id="new-event" onSubmit={submit} className="grid sm:grid-cols-2 gap-4">
        <div className="sm:col-span-2">
          <Field label="Name" htmlFor="event-name">
            <Input id="event-name" value={name} onChange={(e) => setName(e.target.value)} maxLength={80} />
          </Field>
        </div>
        <Field label="Starts" htmlFor="event-start">
          <Input id="event-start" type="date" value={start} onChange={(e) => setStart(e.target.value)} />
        </Field>
        <Field label="Ends" htmlFor="event-finish">
          <Input id="event-finish" type="date" value={finish} min={start || undefined} onChange={(e) => setFinish(e.target.value)} />
        </Field>
        <Field label="Goal (points)" htmlFor="event-total" hint="Points the whole community has to add up.">
          <Input id="event-total" type="number" min={1} value={total} onChange={(e) => setTotal(e.target.value)} />
        </Field>
        <Field label="Image" htmlFor="event-image">
          <ImageInput id="event-image" file={file} onChange={setFile} />
        </Field>
        <div className="sm:col-span-2">
          <Field
            label="Activities that count"
            htmlFor="event-activity-filter"
            hint={selected.length ? `${selected.length} selected` : "Posts in these activities add points to the event."}
          >
            <div className="rounded-xl border border-ink/10 overflow-hidden">
              <div className="flex items-center gap-2 px-3 border-b border-ink/10 text-ink-3">
                <Icon name="search" size={16} />
                <input
                  id="event-activity-filter"
                  value={filter}
                  onChange={(e) => setFilter(e.target.value)}
                  placeholder="Search activities"
                  className="flex-1 h-10 bg-transparent text-sm text-ink placeholder:text-ink-3 focus:outline-none"
                />
              </div>
              <div className="max-h-48 overflow-y-auto p-1">
                {activities === null ? (
                  <Skeleton className="h-24 m-2" />
                ) : shown.length === 0 ? (
                  <p className="text-sm text-ink-3 p-3">No activities match.</p>
                ) : (
                  shown.map((a) => (
                    <label key={a.id} className="flex items-center gap-3 px-3 py-2 rounded-lg text-sm cursor-pointer hover:bg-raised">
                      <input
                        type="checkbox"
                        checked={selected.includes(a.id)}
                        onChange={() => toggle(a.id)}
                        className="w-4 h-4 accent-[rgb(var(--accent))]"
                      />
                      <span className="text-ink">{a.name}</span>
                    </label>
                  ))
                )}
              </div>
            </div>
          </Field>
        </div>
        {error && (
          <p role="alert" className="sm:col-span-2 text-sm text-danger">
            {error}
          </p>
        )}
      </form>
    </Modal>
  );
};

const FILTERS: { value: Status | "all"; label: string }[] = [
  { value: "all", label: "All" },
  { value: "active", label: "Active" },
  { value: "upcoming", label: "Upcoming" },
  { value: "finished", label: "Finished" },
];

const EventsScreen = () => {
  const toast = useToast();
  const [events, setEvents] = useState<CommunityEvent[] | null>(null);
  const [creating, setCreating] = useState(false);
  const [filter, setFilter] = useState<Status | "all">("all");

  const load = () =>
    getEvents()
      .then((list) =>
        // Active first, then upcoming, then the most recently finished.
        setEvents(
          [...list].sort((a, b) => {
            const order = { active: 0, upcoming: 1, finished: 2 };
            const d = order[statusOf(a)] - order[statusOf(b)];
            return d !== 0 ? d : new Date(b.startDate).getTime() - new Date(a.startDate).getTime();
          })
        )
      )
      .catch((e) => {
        setEvents([]);
        toast.error(e?.message ?? "Could not load the events");
      });

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const shown = (events ?? []).filter((e) => filter === "all" || statusOf(e) === filter);

  return (
    <>
      <PageHeader
        title="Community events"
        hint="Challenges the whole community works on together."
        action={
          <Button icon="plus" onClick={() => setCreating(true)}>
            New event
          </Button>
        }
      />

      <div className="flex gap-1 mb-5" role="tablist" aria-label="Filter events">
        {FILTERS.map((f) => (
          <button
            key={f.value}
            role="tab"
            aria-selected={filter === f.value}
            onClick={() => setFilter(f.value)}
            className={`h-8 px-3 rounded-lg text-sm font-medium transition ${
              filter === f.value ? "bg-ink text-page" : "text-ink-2 hover:bg-raised"
            }`}
          >
            {f.label}
            {events && (
              <span className="ml-1.5 tabular-nums opacity-60">
                {f.value === "all" ? events.length : events.filter((e) => statusOf(e) === f.value).length}
              </span>
            )}
          </button>
        ))}
      </div>

      {events === null ? (
        <div className="grid sm:grid-cols-2 xl:grid-cols-3 gap-4">
          {[0, 1, 2].map((i) => (
            <Skeleton key={i} className="h-80" />
          ))}
        </div>
      ) : shown.length === 0 ? (
        <Card>
          <Empty
            icon="calendar"
            title={events.length ? "No events here" : "No community events yet"}
            hint={events.length ? undefined : "Create one so everybody works towards the same goal."}
            action={
              !events.length && (
                <Button icon="plus" onClick={() => setCreating(true)}>
                  New event
                </Button>
              )
            }
          />
        </Card>
      ) : (
        <div className="grid sm:grid-cols-2 xl:grid-cols-3 gap-4">
          {shown.map((e) => (
            <EventCard key={e.id} event={e} />
          ))}
        </div>
      )}

      <NewEventModal open={creating} onClose={() => setCreating(false)} onCreated={load} />
    </>
  );
};

export default EventsScreen;
