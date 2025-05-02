import { Image, Text, TouchableOpacity } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";
import style from "../styles/Styles.json";

type Props = {
  navigateFunction: () => void;
  item: Activity;
};

const ActivityCard = (props: Props) => {
  return (
    <TouchableOpacity
      onPress={props.navigateFunction}
      className={style.screens.Activities.card}
    >
      <Image
        source={{
          uri: props.item.image,
        }}
        className={style.screens.Activities.image}
      />
      <Text className={style.screens.Activities.text}>{props.item.name}</Text>
    </TouchableOpacity>
  );
};

export default ActivityCard;
