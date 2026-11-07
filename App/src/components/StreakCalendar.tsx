import React, { useEffect, useMemo, useState } from "react";
import { Dimensions, Text, View } from "react-native";
import { getMyPostDays } from "../repositories/ActivityRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { borderColor, cardBgColor, mutedTextColor } from "../utils/Utils";
import { colors } from "../styles/theme";
import { tierFor } from "./StreakFlame";

type Props = {
  activityId: string;
  streak: number;
};

const WEEKS = 12;
const GAP = 4;

const isoDay = (d: Date) =>
  `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(
    d.getDate()
  ).padStart(2, "0")}`;

/**
 * The last 12 weeks, one square per day, Monday on top. Days with a post burn
 * in the colour of the current flame tier.
 */
const StreakCalendar = ({ activityId, streak }: Props) => {
  const { darkmode, language } = useSettingsContext();
  const [days, setDays] = useState<Set<string>>(new Set());
  const c = colors(darkmode);
  const fill = tierFor(Math.max(1, streak)).outer[1];

  useEffect(() => {
    let alive = true;
    getMyPostDays(activityId, WEEKS * 7)
      .then((list) => alive && setDays(new Set(list)))
      .catch(() => alive && setDays(new Set()));
    return () => {
      alive = false;
    };
  }, [activityId]);

  const columns = useMemo(() => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    // Start on the Monday 11 weeks before this week's Monday.
    const mondayOffset = (today.getDay() + 6) % 7;
    const start = new Date(today);
    start.setDate(today.getDate() - mondayOffset - (WEEKS - 1) * 7);
    return Array.from({ length: WEEKS }, (_, w) =>
      Array.from({ length: 7 }, (_, d) => {
        const date = new Date(start);
        date.setDate(start.getDate() + w * 7 + d);
        return { key: isoDay(date), future: date > today, today: date.getTime() === today.getTime() };
      })
    );
  }, []);

  const inner = Dimensions.get("window").width - 40 - 32;
  const cell = Math.floor((inner - GAP * (WEEKS - 1)) / WEEKS);

  return (
    <View className={`mx-5 mt-4 p-4 rounded-2xl border ${cardBgColor(darkmode)} ${borderColor(darkmode)}`}>
      <Text className={`text-xs mb-3 ${mutedTextColor(darkmode)}`}>
        {translations[language || "en-EN"].screens.Streaks.calendar}
      </Text>
      <View className="flex-row">
        {columns.map((week, w) => (
          <View key={w} style={{ marginRight: w < WEEKS - 1 ? GAP : 0 }}>
            {week.map((day) => {
              const posted = days.has(day.key);
              return (
                <View
                  key={day.key}
                  style={{
                    width: cell,
                    height: cell,
                    marginBottom: GAP,
                    borderRadius: 4,
                    backgroundColor: posted ? fill : day.future ? "transparent" : c.raised,
                    borderWidth: day.today ? 1.5 : 0,
                    borderColor: day.today ? c.textSecondary : "transparent",
                  }}
                />
              );
            })}
          </View>
        ))}
      </View>
    </View>
  );
};

export default StreakCalendar;
