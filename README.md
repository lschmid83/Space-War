# Space War

Space War is a 2D side-scrolling space arcade game. The objective of this project was to create an exciting action game which is enjoyable to play. It includes a level editor so that people can build their own levels. Travel through space, shooting enemies and gaining points. Prepare for the final battle and try to stay alive. Destroy enemy space ships to receive power-ups including health, weapon upgrades and special weapon capabilities.

This project was my final assignement for a mobile technology module at Kingston University. The source code is all written in the Java with the Android SDK. 

The keyboard controls for the Java SE version of the game in the release are as follows:

Arrow keys = Move spaceship  
Space = Shoot  
Ctrl = Fire powerup  
Enter = Select Menu Item or Pause

Here are some screenshots of the game running:

<img align='left' src='https://drive.google.com/uc?id=170laMtBFEuQRoBAq75SFducvYdjdBQeV' width='240'>
<img align='left' src='https://drive.google.com/uc?id=1GL3wbAsSCSOMF4ebC8JUzy3AmRq_ojpl' width='240'>
<img src='https://drive.google.com/uc?id=1L-SA38Oc_tebqXC455toajCl-Jq5HuRa' width='240'>

Below are the instructions on how to setup and run the various projects.

Importing an Eclipse Project
============================

The following Java projects were built using Eclipse. 

* SpaceWar-Java
* SpaceWar-Applet

Download the required software here:

[Java SE Development Kit 8](https://drive.google.com/file/d/1V_ev8PK-3MofeY2Rk7cX-4J8815zqGsZ/view?usp=drive_link)

[Eclipse Juno ADT Bundle](https://drive.google.com/file/d/1UYRXzRgVR7R7XbJyDK7sxaEAi6Wh_Hmn/view?usp=sharing)

Here are the instructions to install the Java SDK and setup Eclipse:

1. Run the jdk-7u80-windows-x64.exe file to install the SDK
2. Extract the adt-bundle-windows-x86_64-20140702.zip to C:\Program Files\Eclipse

To import the projects in Eclipse follow these instructions:

1. Open Eclipse
2. Select File -> Import... 
3. Choose the import source -> General -> Existing Projects into Workspace
4. Select the directory of the project you want to open in the source code folder
5. Select Finish

You should now be able to choose the project in the Package Explorer and select Run to start the application.

The SpaceWar-Java project includes an editor to create new levels.

1. From the main game menu select Edit Level and 1.lvl to open an example level map
3. The editor includes instructions on how to edit or create a level
4. Press the enter key and select options to edit level settings such as background, tileset and dimensions

Compiling the Android Studio Mobile Version
===========================================

Download the required software here:

[Android Studio](https://drive.google.com/file/d/1ZwvjCGVGCP0qfyri5DHjviEQRVc_IOu1/view?usp=drive_link)

1. Install Android Studio
2. Open the SpaceWar-Android project
3. Select Tools -> SDK Manager -> SDK Platforms tab and ensure Android API 34 is installed
4. Select Tools -> SDK Manager -> SDK Tools tab and ensure Android SDK Build-Tools 34 is installed
5. If there are still build errors try selecting File -> Invalidate Caches...

If you get an error stating SDK Location not found this is because the local.properties file is not found as it should not be checked into version control and should be generated automatically by Android Studio. If this happens create a new text file in the root of the project folder called local.properties and add the following specifying the location of your Android SDK folder:

```
sdk.dir=C\:\\Users\\User\\AppData\\Local\\Android\\Sdk
```

You should now be able to press the run button and launch the application on a connected device.

If you want to test the application using a virtual device:

1. Select Tools -> Device Manager
2. Click Create Device
3. Choose a device. I recommend Nexus S
4. Download a system image 
5. Give the device a name and click Finish

You should now be able to press the run button and launch the application on a AVD.









