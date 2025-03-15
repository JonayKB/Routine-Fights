import { View, Text } from 'react-native'
import React from 'react'
import { NativeStackScreenProps } from '@react-navigation/native-stack'
import { ActivitiesStackProps } from '../navigation/ActivitiesStackNavigation'

type Props = NativeStackScreenProps<ActivitiesStackProps, 'ActivityForm'>

const ActivityForm = (props: Props) => {
  return (
    <View>
      <Text>ActivityForm</Text>
    </View>
  )
}

export default ActivityForm