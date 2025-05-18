import { View, Text } from "react-native";
import React, { useEffect, useState } from "react";
import { Event } from "../utils/Event";
import { getNearestEvent } from "../repositories/EventRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import DateFormatString from "../components/DateFormatString";

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
      console.error("Error fetching nearest event:", error);
    }
  };

  return (
    <View className={`flex-1 ${darkmode ? "bg-[#1C1C1E]" : "bg-[#FCFCFC]"}`}>
      <Text className="text-3xl text-black text-center mb-2 mt-4">
        {event?.name}
      </Text>
      <View className="flex-row justify-evenly">
        <DateFormatString date={event?.startDate} />
        <DateFormatString date={event?.finishDate} />
      </View>
      <Text className="text-3xl text-black text-center mt-2">
        {"Ends in: " + formatTime(timeLeft)}
      </Text>
      <View
        className="bg-red-700 w-10/12 mt-5 m-auto rounded-3xl"
        style={{ height: 600 }}
      ></View>
    </View>
  );
};

export default EventsScreen;
