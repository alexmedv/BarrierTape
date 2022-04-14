# :construction: BarrierTape


<img align="left" src="https://user-images.githubusercontent.com/2558551/162724132-2ed06ebc-0e06-4f9e-9468-9533a2165cc0.png" width="43%">

## :information_source: About 
Barrier tape is a great way to get a user's attention or alert them when a feature of your app is unavailable. This library allows you to create a barrier tape of different colors and shapes. It can be used as a background for any `View` or as content of `ImageView`

## :wrench: Installation
Add the following dependency to your `build.gradle` file:
```groovy
dependencies {
  implementation 'com.github.alexmedv:BarrierTape:1.0.0'
}
```
:warning: Don't forget to add the JitPack maven repository to the list of repositories: `maven { url "https://jitpack.io" }`
## :joystick: Playground
For a better understanding of the library's capabilities, there is a playground in the demo app. The user can change all the parameters in real time: color palettes, sizes, width of stripes, etc. 

<img width="2000" height="0">


## :sparkles: Examples
<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/162727616-c3ade675-8a75-4f46-b097-214ac142c738.png"></img>
The `BarrierTapeDrawable` can be used immediately after it is created without any additional setup. The result will be a rectangle with yellow and black lines
<br/>
```kotlin
// create with default settings
imageView.background = BarrierTapeDrawable()
```
<br/>
<hr/>

<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/162728339-6a1e23ef-58a1-4fb5-a132-b913a3424c0c.png"></img>
Now let's add some colors by replacing the default colors. You need at least two colors, the maximum number of colors is unlimited.<br/>
:warning: Note that all parameter changes must be performed in the main thread

```kotlin
val barrierTape = BarrierTapeDrawable()
barrierTape.setColors(listOf(0xcf322e, 0xd7d4d5))
imageView.background = barrierTape
```
<hr/>

<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/162728573-42c526d0-f36c-4def-b979-097cbac8ab28.png"></img>
The width of the lines and their incline can be changed. All dimensions are specified in pixels.

```kotlin
val barrierTape = BarrierTapeDrawable().apply {
  setColors(listOf(0x000000, 0xECB537))
  stripeWidth = 80
  isReversed = true
}

```
<hr/>

<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/162728814-319f7266-231f-4648-924e-8dc08be66391.png"></img>
You can round any corners of a shape independently of each other<br/>
:warning: Can only be used for `Shape.RECTANGLE`

```kotlin
imageView.background = BarrierTapeDrawable().apply {
  setColors(listOf(0x408d57,0x47a157, 0x6dba5e, 0x8fcf61, 0x43b7ad, 0x489ad8, 0x3777bd, 0x2e6187, 0x9a3f40, 0xe03d3c, 0xf0892f, 0xf3c73d))
  stripeWidth = 90
  setRadius(topLeftRadius = 40F, topRightRadius = 40F, bottomLeftRadius = 0F, bottomRightRadius = 0F)
  // to set the same radius for all corners you can use setRadius(40F)
}
```

<hr/>

<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/162730423-0064571e-58b6-4f5e-887e-33156e761f08.png"></img>
The barrier tape can be drawn in any of five shapes: `RECTANGLE`, `OVAL`, `CIRCLE`, `TRIANGLE` and `EQUILATERAL_TRIANGLE`

```kotlin
imageView.background = BarrierTapeDrawable().apply {
  shape = Shape.EQUILATERAL_TRIANGLE
  triangleOrientation = TriangleOrientation.TOP
}
```
<hr/>

<img align="right" width="40%" src="https://user-images.githubusercontent.com/2558551/162735498-a336ce90-5d92-439a-b13c-ebece52453e2.png"></img>

The barrier tape can be displayed as a frame. To use this mode, `borderWidth` must be greater than zero.
```kotlin
imageView.background = BarrierTapeDrawable().apply {
  borderWidth = 15
}
```


## :handshake: Contributions are welcome
Please use the [issues page](https://github.com/alexmedv/BarrierTape/issues) to report any bug you find or to request a new feature.

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
