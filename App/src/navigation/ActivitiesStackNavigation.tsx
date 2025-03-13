import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Activities from "../screens/Activities";
import ActivityDetails from "../screens/ActivityDetails";
import { Activity } from "../utils/Category";

type Props = {};

export type ActivitiesStackProps = {
  Activities: undefined;
  ActivityDetails: { activity: Activity };
};

const ActivitiesStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ActivitiesStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Activities" component={Activities} />
      <Stack.Screen name="ActivityDetails" component={ActivityDetails} />
    </Stack.Navigator>
  );
};

export default ActivitiesStackNavigation;
