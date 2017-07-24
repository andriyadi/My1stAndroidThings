# My1stAndroidThings
My 1st attempt to explore development with Android Things, to read I2C BMP280 sensor and display the readings to OLED I2C display. To add more complexity to my learning process, I use Kotlin programming language.

## Demo
How it works and how to install the app: https://www.youtube.com/watch?v=1N22_RTsVx8

## Pre-requisites

* Android Studio 2.2+.
* Android Things compatible board. I use Raspberry Pi 3
* BME/BMP280 sensor (temperature, pressure)
* SSD1306 OLED display

## My Setup
Quite frankly, I hate jumpers :) So I made a simple Raspberry Pi HAT by soldering everything on protoboard as following. 
![MySetup](https://github.com/andriyadi/My1stAndroidThings/raw/master/MySetup.jpg)
But you can just connect the sensor and display using jumper cable as following schematics.

## Schematics
The connection between Raspberry Pi 3, BMP/BME 280 sensor, and OLED display is following.

![Schematics](https://github.com/andriyadi/My1stAndroidThings/raw/master/Schematics.jpg)

You don't have to use Sparkfun's BME280 or Seeed's OLED, any modules will do, as long as you connect the right pins.

## Run the sample
Open the project with Android Studio and click Run. Or you can do it via Terminal/command line by typing:
```
./gradlew installDebug
adb shell am start andriyadi.me.my1stthingslab/.SensorActivity
```
That's it. Enjoy!
