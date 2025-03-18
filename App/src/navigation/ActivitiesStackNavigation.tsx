import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Activities from "../screens/Activities";
import ActivityDetails from "../screens/ActivityDetails";
import { Activity } from "../utils/Activity";
import Streaks from "../screens/Streaks";
import ActivityForm from "../screens/ActivityForm";

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
      <Stack.Screen name="Streaks" component={Streaks} />
      <Stack.Screen name="Activities" component={Activities} />
      <Stack.Screen name="ActivityDetails" component={ActivityDetails} />
      <Stack.Screen name="ActivityForm" component={ActivityForm}/>
    </Stack.Navigator>
  );
};

export default ActivitiesStackNavigation;
