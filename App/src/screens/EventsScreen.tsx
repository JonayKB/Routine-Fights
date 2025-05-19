import { View, Text, StyleSheet, Alert } from "react-native";
import React, { useEffect, useState } from "react";
import { Event } from "../utils/Event";
import { getNearestEvent } from "../repositories/EventRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import DateFormatString from "../components/DateFormatString";
import { bgColor, borderColor, cardBgColor, textColor } from "../utils/Utils";

type Props = {};

const EventsScreen = (props: Props) => {
  const { darkmode } = useSettingsContext();
  const [event, setEvent] = useState<Event>(null);
  const [timeLeft, setTimeLeft] = useState<number>(0);
  const secsToMillis = 1000;

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

  useEffect(() => {
    fetchNearestEvent();
  }, []);

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
    }, secsToMillis);

    return () => clearInterval(interval);
  }, [event?.finishDate]);

  const fetchNearestEvent = async () => {
    try {
      const response = await getNearestEvent();
      setEvent(response);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
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
          {"Ends in: " + formatTime(timeLeft)}
        </Text>
      </View>
      <View
        className={`${cardBgColor(darkmode)} ${borderColor(
          darkmode
        )} border-2 w-10/12 mt-5 m-auto rounded-3xl`}
        style={{ height: 590 }}
      >
        <View style={{ height: 550 }} className="w-8 pos-relative overflow-hidden rounded-xl m-auto border-2 border-black">
          <View style={styles.barBackground} />
          <View className="bg-red-700 h-16" />
        </View>
      </View>
    </View>
  );
};

export default EventsScreen;

const styles = StyleSheet.create({
  barBackground: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor:'#EEEEEE'
  },
});
