import React, {useEffect, useState} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Pressable,
  ToastAndroid, Platform,
  AppRegistry,
  NativeModules
} from 'react-native';
import {Colors} from "react-native/Libraries/NewAppScreen";
// import { Flow, StateFlow, install } from 'flatinvoker-react';

// install();

// const { MyCustomModule } = NativeModules;
// Shibasis.hello

// MyCustomModule.sayHello('World', (greeting) => {
//     console.log(greeting); // Output: "Hello, World"
// });
// console.log("Shibasis", MyCustomModule.shibasis())
// console.log("Reaktor", NativeModules, MyCustomModule)

// network.get().collect(data => {
//   const { result, time } = measure(() =>
//     JSON.parse(data)
//   )
//   console.log("Time to parse search response", time);
// })

// const { result, time } = measure(() => network.getObject());
// console.log("Shibasis" + JSON.stringify(result), time);
const backgroundStyle = {
  backgroundColor: Colors.darker
};

const App = () => {
  const message = "not working jsi"

  return (
    <SafeAreaView style={backgroundStyle}>
      <View>
          <Text>
            {message}
          </Text>
      </View>
    </SafeAreaView>
  );
};


export default App;

AppRegistry.registerComponent("ReaktorTester", () => App);
