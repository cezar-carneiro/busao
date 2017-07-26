# busao
[![Build Status](https://travis-ci.org/cezar-carneiro/busao.svg?branch=master)](https://travis-ci.org/cezar-carneiro/busao) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/c88c6b08f39c497cac2f4e9d83592212)](https://www.codacy.com/app/cezar-carneiro/busao?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cezar-carneiro/busao&amp;utm_campaign=Badge_Grade)

## An Android App for consulting Goiânia's bus schedule.
Goiânia is my home city in Brazil. And with this app I hope to help my community and make people's life a little bit better.
This app is for my portfolio. I opened its source for demonstration purposes. If you found any value in it feel free to contact me.

## How it will work
You will be able to search across all Bus Stops of Goiânia. You can search by the Bus Stop address, code or reference point. Also, you can navigate on a map seeing all the existing Bus Stops in a given area.
You can save favorite Bus Stops and, naturally, check the bus schedule for a given Stop.
Also, I plan to add a list of all Bus Lines of Goiânia.

## Run it
If you have Android Studio installed and Android SDK you can clone it, import it and start tweaking in a few seconds.
minSdkVersion: 16
targetSdkVersion: 24
compileSdkVersion: 24
buildToolsVersion: 24.0.2

This project uses the Google Maps API. Therefore, to build the project you need to:
1. Register to [Google API Console](https://developers.google.com/maps/documentation/android-api/signup)
2. Generate a key for Google Maps
3. Copy the key
4. In your operating system, create an environment variable named *GOOGLE_MAPS_API_KEY* and assign the key to this variable.
5. You are good to Go. When you try to build the project, Gradle will take care of the rest.

### How it looks (for now):
[![List](http://i.imgur.com/CpwvkIt.jpg)](http://i.imgur.com/CpwvkIt.jpg)
[![Map](http://i.imgur.com/kdkNBr1.jpg)](http://i.imgur.com/kdkNBr1.jpg)
[![Details](http://i.imgur.com/1qejDNl.jpg)](http://i.imgur.com/1qejDNl.jpg)

## Contributions
If you want to contribute, please feel free to open an Issue or a Pull Request. All contributions will be much appreciated.

## WORK IN PROGRESS
Once I've finish this (very soon, I hope), I will publish it and you will be able to install it on your Android Device.

### Progress so far:
[x] App's main skeleton (Colors, Menus, Tabs, Drawer, Toolbar, etc).
[x] Search available on the Map.  
[x] Save favorites from the Map.  
[x] Show list of favorite Bus Stops.  
[  ] Search by text (bus stop address, reference point, code, etc).  
[  ] Save favorites from the Search.  
[  ] Show the schedule of a given stop.  
