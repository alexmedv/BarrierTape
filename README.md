## :construction: About 
Barrier tape is a great way to get a user's attention or alert them when a feature of your app is unavailable. This library allows you to create a barrier tape of different colors and shapes. It can be used as a background for any `View` or as content of `ImageView`

## :wrench: Installation
Add the following dependency to your `build.gradle` file:
```groovy
dependencies {
  implementation 'ru.angryrobot.barriertape:1.0.0'
}
```

## :sparkles: Examples
The `BarrierTapeDrawable` can be used immediately after it is created without any additional setup. The result will be a rectangle with yellow and black lines

```kotlin
imageView.background = BarrierTapeDrawable()
```

Now let's add some colors by replacing the default colors. You need at least two colors, the maximum number of colors is unlimited.<br/>
:warning: Note that all parameter changes must be performed in the main thread

```kotlin
val barrierTape = BarrierTapeDrawable()
barrierTape.setColors(listOf(0xcf322e, 0xd7d4d5))
imageView.background = barrierTape
```

The width of the lines and their incline can be changed. All dimensions are specified in pixels.

```kotlin
val barrierTape = BarrierTapeDrawable().apply {
    stripeWidth = 40
    isReversed = true
}
```

You can round any corners of a shape independently of each other<br/>
:warning: Can only be used for `Shape.RECTANGLE`

```kotlin
imageView.background = BarrierTapeDrawable().apply {
    setRadius(topLeftRadius = 30F, topRightRadius = 30F, bottomLeftRadius = 0F, bottomRightRadius = 0F)
    // to set the same radius for all corners you can use setRadius(10F)
}
```

The barrier tape can be drawn in any of five shapes: `RECTANGLE`, `OVAL`, `CIRCLE`, `TRIANGLE` and `EQUILATERAL_TRIANGLE`

```kotlin
imageView.background = BarrierTapeDrawable().apply {
    shape = Shape.OVAL
}
```

The barrier tape can be displayed as a frame. To use this mode, `borderWidth` must be greater than zero

```kotlin
imageView.background = BarrierTapeDrawable().apply {
    shape = Shape.EQUILATERAL_TRIANGLE
    borderWidth = 10
}
```


## :joystick: Playground
For a better understanding of the library's capabilities, there is a playground in the demo app. The user can change all the parameters in real time: color palettes, sizes, width of stripes, etc. 


## :handshake: Contributing

Contributions, issues and feature requests are welcome.<br />
Feel free to check [issues page](./issues) if you want to contribute.<br />



## :memo: License
```
  Copyright 2022 Alexander Medvedev
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```