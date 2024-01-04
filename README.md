## Yesoul Companion App for QZ app http://qzfitness.com

Yesoul G1MAX comes with MStar Android TV 8.0 with 32-bit armv7 architecture on board and 32" Display with 1366x768 resolution.

Preparation
To connect Yesoul G1MAX to ZWIFT, you need to:
	Install the EShare app on your tablet/phone - with its help you can launch applications installed on Android TV used on Yesoul G1MAX and also manage the Android TV interface.
	Install the qdomyos-zwift app on your tablet/phone - it is needed to connect to QZ Yesoul Companion and receive data from sensors and then transmit it to ZWIFT.
	Install the ZWIFT program on Mac/PC. You can also install it on a tablet if it has a 16:9 aspect ratio; otherwise, when relaying to the G1MAX screen, the screen will not be fully filled.
	Install android ADB on Mac/PC - for connect to Yesoul G1MAX over the network, install/uninstall APK from the command line without dealing with the Android TV interface.
	Get the QZ Yesoul Companion.apk to install it in the following steps. It is needed to receive sensor data and transmit it to qdomyos-zwift.
	Get AirReceiver.apk to install it in the following steps. It is needed to be able to broadcast ZWIFT from Mac/PC to the Yesoul G1MAX screen, because built-in tools do not work on modern versions of iOS/iPadOS/MacOS.

Installing ADB
	Installing ADB on Mac
		Install brew, which can be obtained here https://brew.sh/, then:
			brew install android-platform-tools
	Installing ADB on Windows, which can be obtained here
		https://dl.google.com/android/repository/platform-tools-latest-windows.zip
		Check installation instructions on the internet. To make adb work from 				anywhere, you need to add its path to the PATH variable.

G1MAX Configuration
	Perform basic setup and connect to the G1MAX network to work with the Yesoul app.
	When devices are detected in the Yesoul app, pay attention to the IP address under the device name and write it down, the IP address can also be viewed in the EShare app, also under the device name. The IP address will be needed to connect to G1MAX using ADB.

![IMG_9577](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/31249284-33ba-4bc8-b642-a14e03487c37)

Open the EShare app, wait for G1MAX to appear in the list of devices, and connect to it.
Go to the Apps tab - all applications installed on G1MAX will be displayed here.

![IMG_9578](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/defc7486-f559-4066-8618-dc6beac30876)

Open Settings - the Android panel will appear on the right side of the G1MAX screen.


In the EShare app, go to the Remote -> Keys tab and use the arrows to navigate to the About section, then press the center button in EShare to open the About section.

![IMG_9579](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/86be82a4-c375-4791-a781-d06b76d6ed10)

![IMG_9580](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/48d34937-fcd6-4864-909d-912f6aa2d2a7)

![IMG_9581](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/1c2978d1-0412-4204-8ec2-ba98cd9ba1eb)

Using the arrows, scroll down to the Build section and press the center button 7 times until the message "You already developer" appears.

Press the large back arrow to return to the previous menu.
Scroll down to the Developer section and press the center button.

![IMG_9582](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/cfa121e4-ede6-4fc6-96d0-bc573c6c05e9)

Scroll down to USB Debugging and enable it.

![IMG_9583](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/8cdf243a-7daa-4502-a884-24da263f818b)

Exit the Settings menu using the large back arrow.

Now you can connect to G1MAX over the network. Go to a Mac/PC with ADB installed and execute:

	adb connect device_IP_address

To ensure that the device is connected, you can enter the command:

	adb devices

and make sure that adb is connected to G1MAX.

Now let's install 2 apps

```
adb install app-debug.apk
adb install AirReceiver.apk
```

In the EShare program, go to the Apps tab, and all installed applications will be displayed on the screen.

Launch QZ Yesoul Companion - a window with the program name and the readings for Power, Cadence, and Resistance will appear on the G1MAX screen. You can pedal and adjust the resistance for a few seconds to ensure that the readings change.

Launch AirReceiver - the program interface will appear on the screen.
	
On the tablet/smartphone, launch qdomyos-zwift and open the menu in the upper left corner. Select Settings and scroll down to bike options -> Proform/Nordictrack, then in the TDF Companion IP field, enter the G1MAX IP address. 

![IMG_9590](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/f18e3d70-403b-47e0-8ae9-e9a9bd70c6c6)
![IMG_9591](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/5f87b9c0-5180-4721-9308-84aa4067683d)
![IMG_9592](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/324e7d3e-4859-4c3a-b79c-d9d2e60e5e7e)


Click the upper left corner again - the program will prompt you to save the settings and restart. Open it again. 
The program must be open to transmit data.

If you plan to use ZWIFT Companion on an iPhone/iPad, you need to run them on different devices, tablet/smartphone. In the background, qdomyos-zwift can work on Android, and then you can use both ZWIFT Companion and qdomyos-zwift on one device.

Now go to Mac/PC and launch ZWIFT, in the settings, you can set the resolution to 1920x1080, as the G1MAX screen has a resolution of 1366x768, so setting it higher does not make sense.
At the start, select Power and choose Wahoo in Power, Cadence, Resistance.

![IMG_9589](https://github.com/cagnulein/qdomyos-zwift/assets/25452579/a1ac88f6-781b-47f8-bb87-5b63b220e750)


Then everything is as usual in ZWIFT.

Start AirPlay and go to G1MAX.
