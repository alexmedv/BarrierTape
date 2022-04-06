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
<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/161961033-3caf5631-65d7-4c24-b8fa-319e72dc88f0.jpg"></img>
The `BarrierTapeDrawable` can be used immediately after it is created without any additional setup. The result will be a rectangle with yellow and black lines
<br/>
```kotlin
// create with default settings
imageView.background = BarrierTapeDrawable()
```
<br/>
<hr/>
<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/161953452-6af44bac-94e2-46e4-8095-05d0882d1ef1.jpg"></img>
Now let's add some colors by replacing the default colors. You need at least two colors, the maximum number of colors is unlimited.<br/>
:warning: Note that all parameter changes must be performed in the main thread

```kotlin
val barrierTape = BarrierTapeDrawable()
barrierTape.setColors(listOf(0xcf322e, 0xd7d4d5))
imageView.background = barrierTape
```
<hr/>
<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/161959759-4429bbb4-9696-4401-bc91-0e74cc1d3d77.jpg"></img>
The width of the lines and their incline can be changed. All dimensions are specified in pixels.

```kotlin
val barrierTape = BarrierTapeDrawable().apply {
  setColors(listOf(0x000000, 0xECB537))
  stripeWidth = 80
  isReversed = true
}
```
<hr/>
<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/161949040-6041e590-6c42-4900-8072-f81f0cc0b9d0.jpg"></img>
You can round any corners of a shape independently of each other<br/>
:warning: Can only be used for `Shape.RECTANGLE`

```kotlin
imageView.background = BarrierTapeDrawable().apply {
  setRadius(topLeftRadius = 30F, topRightRadius = 30F, bottomLeftRadius = 0F, bottomRightRadius = 0F)
  // to set the same radius for all corners you can use setRadius(10F)
}
```
<hr/>
<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/161949040-6041e590-6c42-4900-8072-f81f0cc0b9d0.jpg"></img>
The barrier tape can be drawn in any of five shapes: `RECTANGLE`, `OVAL`, `CIRCLE`, `TRIANGLE` and `EQUILATERAL_TRIANGLE`

```kotlin
imageView.background = BarrierTapeDrawable().apply {
  shape = Shape.OVAL
}
```
<hr/>
The barrier tape can be displayed as a frame. To use this mode, `borderWidth` must be greater than zero!


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
Feel free to check [issues page](https://github.com/alexmedv/BarrierTape/issues) if you want to contribute.<br />



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
