import { Alert, RefreshControl, ScrollView, Text, View } from "react-native";
import { useEffect, useState } from "react";
import { Event } from "../utils/Event";
import {
  getCurrentPoints,
  getNearestEvent,
} from "../repositories/EventRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { bgColor, borderColor, cardBgColor, textColor } from "../utils/Utils";
import DateFormatString from "../components/DateFormatString";
import { translations } from "../../translations/translation";

type Props = {};

const EventsScreen = (props: Props) => {
  const { darkmode, language } = useSettingsContext();
  const [event, setEvent] = useState<Event>(null);
  const [timeLeft, setTimeLeft] = useState<number>(0);
  const [currentPoints, setCurrentPoints] = useState(0);
  const [barHeight, setBarHeight] = useState(0);
  const [load, setLoad] = useState(false);
  const secToMillis = 1000;
  const totalHight = 550;
  const [milestones, setMilestones] = useState<number[]>([]);

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
    const height = (currentPoints * totalHight) / event.totalRequired;
    setBarHeight(height);
  };

  const getMilestones = () => {
    const milestones = [];
    const milestoneCount = event.totalRequired / 3;
    for (let i = 1; i <= 3; i++) {
      milestones.push(Math.floor(i * milestoneCount));
    }
    setMilestones(milestones);
  };

  useEffect(() => {
    const init = async () => {
      await fetchNearestEvent();
      await fetchEventPoints();
      getBarHeight();
      getMilestones();
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
    } catch (error) {
      Alert.alert("Error", error.response.data);
    } finally {
      setLoad(false);
    }
  };

  const fetchEventPoints = async () => {
    try {
      setCurrentPoints(await getCurrentPoints(event.id));
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
        )} border-2 w-10/12 mt-5 m-auto rounded-3xl`}
        style={{ height: 590 }}
      >
        <View
          style={{ height: totalHight }}
          className="w-8 overflow-hidden rounded-xl m-auto border-2 border-black flex-col-reverse"
        >
          <View className="bg-red-700" style={{ height: barHeight }} />
          <View className="bg-black" style={{ height: totalHight }} />
          {milestones.map((ms) => {
            const y = (ms * totalHight) / event.totalRequired;
            return (
              <View
                key={ms}
                style={{
                  position: "absolute",
                  bottom: y - 1,
                  left: 0,
                  right: 0,
                  height: 2,
                  backgroundColor: "#fff",
                }}
              />
            );
          })}
        </View>
      </View>
    </ScrollView>
  );
};

export default EventsScreen;
