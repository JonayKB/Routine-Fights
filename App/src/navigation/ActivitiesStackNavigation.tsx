import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import ActivitiesScreen from "../screens/ActivitiesScreen";
import ActivityDetailsScreen from "../screens/ActivityDetailsScreen";
import { Activity } from "../utils/Activity";
import StreaksScreen from "../screens/StreaksScreen";
import ActivityFormScreen from "../screens/ActivityFormScreen";

type Props = {};

export type ActivitiesStackProps = {
  Streaks: undefined;
  Activities: undefined;
  ActivityDetails: { activity: Activity };
  ActivityForm: undefined;
};

const ActivitiesStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ActivitiesStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Streaks" component={StreaksScreen} />
      <Stack.Screen name="Activities" component={ActivitiesScreen} />
      <Stack.Screen name="ActivityDetails" component={ActivityDetailsScreen} />
      <Stack.Screen name="ActivityForm" component={ActivityFormScreen}/>
    </Stack.Navigator>
  );
};

export default ActivitiesStackNavigation;
