import { View, Text } from 'react-native'
import React from 'react'
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { LoginStackProps } from '../navigation/LoginStackNavigation';

type Props = NativeStackScreenProps<LoginStackProps, "Home">;

const Home = (props: Props) => {
  return (
    <View>
      <Text>Home</Text>
    </View>
  )
}

export default Home