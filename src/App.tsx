/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import { TakeUsEatCallsNative } from 'native'
import React from 'react'
import {
  SafeAreaView,
  StatusBar,
  Button,
} from 'react-native'

const App = () => {

  return (
    <SafeAreaView style={{ padding: 50, height: '100%', justifyContent: 'center' }}>
      <StatusBar barStyle={'dark-content'} />
      <Button
        title={'Native Setup'}
        onPress={() => {
          TakeUsEatCallsNative.setup('111222', '222333')
        }}
      />
    </SafeAreaView>
  )
}

export default App
