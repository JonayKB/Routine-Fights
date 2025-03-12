import { View, Text } from 'react-native'
import React from 'react'

type Props = {}

const LoginNavigation = (props: Props) => {
  return (
    <View className="flex-1 items-center justify-center bg-white">
      <Text className="text-lg font-bold text-red-500">¡Hola, NativeWind!</Text>
    </View>
  )
}

export default LoginNavigation