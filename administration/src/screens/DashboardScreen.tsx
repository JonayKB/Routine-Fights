import React, { useEffect, useMemo, useState } from "react";
import { LineChart } from "@mui/x-charts/LineChart";
import { BarChart } from "@mui/x-charts/BarChart";
import { ChartData, getCharts, getPushDevices, LanguageCount } from "../api/admin";
import { Button, Card, CardHeader, Empty, PageHeader, Skeleton } from "../ui/kit";
import Icon from "../ui/Icon";
import { useToast } from "../ui/Toast";
import { useDarkMode } from "../ui/theme";

type Charts = Awaited<ReturnType<typeof getCharts>>;

const RANGES = [
  { days: 30, label: "30 days" },
  { days: 90, label: "90 days" },
  { days: 365, label: "1 year" },
];

const RATE_LABELS: Record<string, string> = {
  daily: "Daily",
  weekly: "Weekly",
  monthly: "Monthly",
  yearly: "Yearly",
};

const sum = (values: number[]) => values.reduce((a, b) => a + (Number(b) || 0), 0);

const dayKey = (d: Date) =>
  `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;

/** Per-day counts for the last `days` days, with the missing days at 0. */
const lastDays = (chart: ChartData, days: number) => {
  const byDay = new Map<string, number>();
  chart.labels.forEach((label, i) => byDay.set(String(label).slice(0, 10), Number(chart.data[i]) || 0));
  const dates: Date[] = [];
  const values: number[] = [];
  const today = new Date();
  for (let i = days - 1; i >= 0; i--) {
    const d = new Date(today.getFullYear(), today.getMonth(), today.getDate() - i);
    dates.push(d);
    values.push(byDay.get(dayKey(d)) ?? 0);
  }
  return { dates, values };
};

const Stat = ({ icon, label, value, note }: { icon: string; label: string; value?: number; note?: string }) => (
  <Card className="p-5">
    <div className="flex items-center gap-2 text-ink-2">
      <Icon name={icon} size={16} />
      <span className="text-sm">{label}</span>
    </div>
    {value === undefined ? (
      <Skeleton className="h-9 w-24 mt-3" />
    ) : (
      <p className="font-display text-3xl font-semibold text-ink mt-2 tabular-nums">{value.toLocaleString()}</p>
    )}
    {note && <p className="text-xs text-ink-3 mt-1">{note}</p>}
  </Card>
);

const DashboardScreen = () => {
  const toast = useToast();
  const dark = useDarkMode();
  const ink = dark ? "#EA580C" : "#C2410C";
  const [charts, setCharts] = useState<Charts | null>(null);
  const [devices, setDevices] = useState<LanguageCount[] | null>(null);
  const [range, setRange] = useState(30);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    try {
      const [c, d] = await Promise.all([getCharts(), getPushDevices().catch(() => [])]);
      setCharts(c);
      setDevices(d);
    } catch (e: any) {
      toast.error(e?.message ?? "Could not load the dashboard");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const users = useMemo(() => (charts ? lastDays(charts.users, range) : null), [charts, range]);
  const posts = useMemo(() => (charts ? lastDays(charts.posts, range) : null), [charts, range]);
  const topUsers = useMemo(() => {
    if (!charts) return [];
    return charts.topUsers.labels
      .map((label, i) => ({ label: String(label), value: Number(charts.topUsers.data[i]) || 0 }))
      .sort((a, b) => b.value - a.value)
      .slice(0, 10);
  }, [charts]);
  const rates = useMemo(
    () =>
      charts
        ? charts.rates.labels.map((l, i) => ({ label: RATE_LABELS[String(l)] ?? String(l), value: Number(charts.rates.data[i]) || 0 }))
        : [],
    [charts]
  );

  // Recessive axes and grid in the panel's own ink, in both themes.
  const chartSx = {
    "& .MuiChartsAxis-tickLabel": { fill: "rgb(var(--ink-3)) !important" },
    "& .MuiChartsAxis-line, & .MuiChartsAxis-tick": { stroke: "rgb(var(--ink) / 0.1) !important" },
    "& .MuiChartsGrid-line": { stroke: "rgb(var(--ink) / 0.08)" },
    "& .MuiAreaElement-root": { fillOpacity: 0.1 },
  };
  const shortDate = (d: Date) => d.toLocaleDateString(undefined, { day: "numeric", month: "short" });
  const rangeLabel = RANGES.find((r) => r.days === range)?.label.toLowerCase();

  return (
    <>
      <PageHeader
        title="Dashboard"
        hint="How the community is doing. Figures are refreshed every night."
        action={
          <Button variant="secondary" icon="refresh" onClick={load} loading={loading}>
            Refresh
          </Button>
        }
      />

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <Stat
          icon="users"
          label="Users"
          value={charts ? sum(charts.users.data) : undefined}
          note={users ? `+${sum(users.values)} in the last ${rangeLabel}` : undefined}
        />
        <Stat
          icon="image"
          label="Posts"
          value={charts ? sum(charts.posts.data) : undefined}
          note={posts ? `+${sum(posts.values)} in the last ${rangeLabel}` : undefined}
        />
        <Stat icon="flame" label="Activities" value={charts ? sum(charts.rates.data) : undefined} />
        <Stat
          icon="phone"
          label="Devices with push"
          value={devices ? sum(devices.map((d) => d.devices)) : undefined}
          note={devices ? `${devices.length} language${devices.length === 1 ? "" : "s"}` : undefined}
        />
      </div>

      <div className="flex items-center gap-1 mt-8 mb-4" role="tablist" aria-label="Time range">
        {RANGES.map((r) => (
          <button
            key={r.days}
            role="tab"
            aria-selected={range === r.days}
            onClick={() => setRange(r.days)}
            className={`h-8 px-3 rounded-lg text-sm font-medium transition ${
              range === r.days ? "bg-ink text-page" : "text-ink-2 hover:bg-raised"
            }`}
          >
            {r.label}
          </button>
        ))}
      </div>

      <div className="grid lg:grid-cols-2 gap-4">
        <Card>
          <CardHeader title="New users per day" />
          <div className="px-2 pb-2">
            {users ? (
              <LineChart
                height={260}
                xAxis={[{ scaleType: "time", data: users.dates, valueFormatter: shortDate, height: 28, tickNumber: 6, domainLimit: "strict" }]}
                yAxis={[{ tickMinStep: 1 }]}
                series={[{ data: users.values, color: ink, area: true, showMark: false, curve: "linear", label: "New users" }]}
                grid={{ horizontal: true }}
                hideLegend
                sx={chartSx}
              />
            ) : (
              <Skeleton className="h-[260px] mx-3 mb-3" />
            )}
          </div>
        </Card>

        <Card>
          <CardHeader title="Posts per day" />
          <div className="px-2 pb-2">
            {posts ? (
              <BarChart
                height={260}
                xAxis={[
                  {
                    scaleType: "band",
                    data: posts.dates,
                    valueFormatter: shortDate,
                    categoryGapRatio: 0.35,
                    height: 28,
                    // One label a week: a label per bar would collide.
                    tickLabelInterval: (_value: Date, index: number) => index % 7 === 0,
                  },
                ]}
                yAxis={[{ tickMinStep: 1 }]}
                series={[{ data: posts.values, color: ink, label: "Posts" }]}
                borderRadius={4}
                grid={{ horizontal: true }}
                hideLegend
                sx={chartSx}
              />
            ) : (
              <Skeleton className="h-[260px] mx-3 mb-3" />
            )}
          </div>
        </Card>

        <Card>
          <CardHeader title="Activities by frequency" />
          <div className="px-2 pb-2">
            {charts ? (
              rates.length ? (
                <BarChart
                  height={220}
                  layout="horizontal"
                  yAxis={[{ scaleType: "band", data: rates.map((r) => r.label), categoryGapRatio: 0.55, width: 72 }]}
                  xAxis={[{ tickMinStep: 1 }]}
                  series={[{ data: rates.map((r) => r.value), color: ink, label: "Activities" }]}
                  borderRadius={4}
                  grid={{ vertical: true }}
                  hideLegend
                  sx={chartSx}
                />
              ) : (
                <Empty icon="flame" title="No activities yet" />
              )
            ) : (
              <Skeleton className="h-[240px] mx-3 mb-3" />
            )}
          </div>
        </Card>

        <Card>
          <CardHeader title="Top users by points" />
          <div className="px-5 pt-3 pb-5">
            {!charts ? (
              <div className="flex flex-col gap-3">
                {Array.from({ length: 6 }).map((_, i) => (
                  <Skeleton key={i} className="h-6" />
                ))}
              </div>
            ) : topUsers.length === 0 ? (
              <Empty icon="award" title="Nobody has points yet" />
            ) : (
              <ol className="flex flex-col gap-2.5">
                {topUsers.map((u, i) => (
                  <li key={u.label} className="grid grid-cols-[1.5rem_8rem_1fr_auto] sm:grid-cols-[1.5rem_10rem_1fr_auto] items-center gap-3 text-sm">
                    <span className="text-ink-3 tabular-nums">{i + 1}</span>
                    <span className="truncate text-ink">{u.label}</span>
                    <span className="h-2 rounded-full bg-raised overflow-hidden" aria-hidden="true">
                      <span
                        className="block h-full rounded-full"
                        style={{ width: `${(u.value / topUsers[0].value) * 100}%`, background: ink }}
                      />
                    </span>
                    <span className="tabular-nums text-ink-2 text-right">{u.value.toLocaleString()}</span>
                  </li>
                ))}
              </ol>
            )}
          </div>
        </Card>
      </div>
    </>
  );
};

export default DashboardScreen;
