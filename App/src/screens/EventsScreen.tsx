import { Alert, RefreshControl, ScrollView, Text, View } from "react-native";
import { useEffect, useRef, useState } from "react";
import { Event } from "../utils/Event";
import {
  getCurrentPoints,
  getNearestEvent,
} from "../repositories/EventRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { bgColor, borderColor, cardBgColor, textColor } from "../utils/Utils";
import DateFormatString from "../components/DateFormatString";
import { translations } from "../../translations/translation";
import { getBadgesByEvent } from "../repositories/BadgeRepository";
import Picture from "../components/Picture";

type Props = {};

const EventsScreen = (props: Props) => {
  const { darkmode, language } = useSettingsContext();
  const [event, setEvent] = useState<Event>(null);
  const [timeLeft, setTimeLeft] = useState<number>(0);
  const [currentPoints, setCurrentPoints] = useState(0);
  const barHeight = useRef(0);
  const [load, setLoad] = useState(false);
  const secToMillis = 1000;
  const totalHight = 550;
  const [milestones, setMilestones] = useState<number[]>([250, 500]);
  const [badges, setBadges] = useState([]);

  const formatTime = (millis: number): string => {
    const totalSeconds = Math.floor(millis / 1000);
    const totalMinutes = Math.floor(totalSeconds / 60);

    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;
    const seconds = totalSeconds % 60;
    return `${Math.max(0, hours)}h ${Math.max(0, minutes)}m ${Math.max(
      0,
      seconds
    )}s`;
  };

  const getBarHeight = () => {
    barHeight.current = (currentPoints * totalHight) / event.totalRequired;
  };

  const getMilestones = () => {
    const milestonePoints = [];
    const milestoneCount = event.totalRequired / badges.length;
    for (let i = 1; i <= badges.length; i++) {
      milestonePoints.push(Math.floor(i * milestoneCount));
    }
    setMilestones([...milestonePoints]);
  };

  useEffect(() => {
    const init = async () => {
      await fetchNearestEvent();
      await fetchEventPoints();
      await fetchBadges();
    };
    init();
  }, [load === true]);

  useEffect(() => {
    if (!event?.finishDate) return;

    const interval = setInterval(() => {
      const nowMillis = new Date().getTime();
      const endDateMillis = Date.parse(event.finishDate);
      const timeRemaining = endDateMillis - nowMillis;

      if (timeRemaining <= 0) {
        clearInterval(interval);
        setTimeLeft(0);
      } else {
        setTimeLeft(timeRemaining);
      }
    }, secToMillis);

    return () => clearInterval(interval);
  }, [event?.finishDate]);

  const fetchNearestEvent = async () => {
    try {
      const response = await getNearestEvent();
      setEvent(response);
      getMilestones();
    } catch (error) {
      Alert.alert("Error", error.response.data);
    } finally {
      setLoad(false);
    }
  };

  const fetchEventPoints = async () => {
    try {
      const response = await getCurrentPoints(event.id);
      setCurrentPoints(response);
      getBarHeight();
    } catch (error) {
      Alert.alert("Error", error.response.data);
    } finally {
      setLoad(false);
    }
  };

  const fetchBadges = async () => {
    try {
      const response = await getBadgesByEvent(event.id);
      setBadges(response);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    } finally {
      setLoad(false);
    }
  };

  return (
    <ScrollView
      className={`flex-1 ${bgColor(darkmode)}`}
      refreshControl={
        <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
      }
    >
      <View className={`p-4 ${cardBgColor(darkmode)}`}>
        <Text
          className={`text-3xl font-bold ${textColor(
            darkmode
          )} text-center mb-2 mt-4`}
        >
          {event?.name}
        </Text>
        <View className="flex-row justify-evenly">
          <DateFormatString date={event?.startDate} />
          <DateFormatString date={event?.finishDate} />
        </View>
        <Text className={`text-2xl ${textColor(darkmode)} text-center mt-2`}>
          {translations[language || "en-EN"].screens.Events.timeLeft}
          {": "}
          {formatTime(timeLeft)}
        </Text>
      </View>
      <View
        className={`${cardBgColor(darkmode)} ${borderColor(
          darkmode
        )} border-2 w-10/12 mt-2 m-auto rounded-3xl relative overflow-hidden`}
        style={{ height: 600 }}
      >
        <View
          style={{ height: totalHight }}
          className={`w-10 overflow-hidden rounded-xl m-auto border-2 ${borderColor(
            darkmode
          )} flex-col-reverse`}
        >
          <View
            className="bg-[#F65261]"
            style={{ height: barHeight.current }}
          />

          <View
            className={`${darkmode ? "bg-[#E8E2F0]" : "bg-[#4B294F]"}`}
            style={{ height: totalHight }}
          />

          {milestones.map((ms, i) => {
            const y = (ms * totalHight) / (event?.totalRequired ?? 1);
            return (
              <View
                key={`line-${ms}`}
                style={{
                  position: "absolute",
                  bottom: y - 1,
                  left: 0,
                  right: 0,
                  height: 2,
                }}
              >
                <View
                  style={{
                    flex: 1,
                    backgroundColor: darkmode ? "#4B294F" : "#E8E2F0",
                    height: 2,
                  }}
                />
              </View>
            );
          })}
        </View>

        {milestones.map((ms, i) => {
          const y = (ms * totalHight) / (event?.totalRequired ?? 1);
          return (
            <View
              key={`badge-${ms}`}
              style={{
                position: "absolute",
                bottom: y - 4,
                left: i % 2 ? 60 : 200,
              }}
            >
              <Picture
                image={badges[(badges.length - 1) - i]?.image}
                size={60}
                style={`rounded-full ${barHeight.current < y ? "opacity-100" : "opacity-40"}`}
              />
            </View>
          );
        })}
      </View>
    </ScrollView>
  );
};

export default EventsScreen;
