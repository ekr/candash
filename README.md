What is CANdash?

CANdash is an Android app that turns your Android device into an instrument cluster for your Tesla Model 3 or Y. It is designed to fit in with the Tesla user experience, and add capabilities that are not in your current instrumentation, like live blind spot monitoring. For more visit https://candash.app

![Screenshot_20220117-111104(1)](https://user-images.githubusercontent.com/523563/150214706-b8a09117-093d-4b32-a459-79da69aa4502.png)
![Screenshot_20220117-111906](https://user-images.githubusercontent.com/523563/150214790-605ee20e-3476-44c4-b3ee-4e4ba719deb6.png)
![Screenshot_20220117-215708](https://user-images.githubusercontent.com/523563/150214798-1ab54da6-b6b5-4706-8313-37d6294a09ed.png)
![Screenshot_20220117-111104(1)](https://user-images.githubusercontent.com/523563/150214811-041b5a21-c847-4523-bcc9-dc4fb804bd6c.png)


Compile

Clone the repo and make sure you open the project from the android subdirectory. It should then compile normally.

Setup

First, you must purchase and install a CANserver in your tesla, if you want blind spot monitoring you will need the dual bus Tesla version. http://www.jwardell.com/canserver/
Please update your CANserver to Version 2.1 of the firmware. Easiest way is to connect your canserver to your home wifi and using a desktop browser to "check for updates" in the CANserver webui.
Two options for running this app, either connect to the hotspot on the CANserver (you will lose internet connectivity, even with mobile service) or create a hotspot on your phone and connect the CANserver to it via the network settings tab.
Turning on hotspot functionality on your phone is preferred if you want to use cloud connected apps like music and nav simultaneously
Sideload the app to your phone, it will appear as ‘CAN Dash’
Launch the CAN Dash and long press any blank area on the screen to bring up the info pane. 
Once you are sure you are connected to the CANserver, click ‘scan’ and if the phone and CANserver are on the same network, the IP address should auto populate and you should start seeing data appear on the right side of the screen. Once you see this, press the ‘DASH’ button to start the dashboard.
If you do not plug in the phone it will follow your display preferences for sleep and time out. If the device has power then it will keep the display on with any auto-brightness you have enabled.

Dash View

This screen should work without any user interaction required.
Tap the speed units (MPH or km/h) to toggle the performance gauges
Power meter on the bottom has max regen on the left and max power on the right, tap either to reset, like an odometer. Tap the center to change units from kW to HP
Long press center power display to hide/show max/min
Tap the battery to change range from percent to distance
Tap speed to force night mode on, otherwise it will copy what the car does
Blind spot monitoring is based on the ultrasonic sensors in the rear bumper, as the car does not return any blind spot data from the cameras unless the turn signal is on. 
If you turn on your signal and the car sees a car in your blind spot, the background of the dashboard will turn red
If the phone is plugged in the screen should never turn off- let me know if it does

Auto launch
If you want to use this on a device that is always in your car, consider installing Tasker. With Tasker you can have the phone automatically turn on the hotspot when plugged in or connected to vehicle bluetooth, and turn off the hotspot when the phone is unplugged (if you have it plugged into your car, this will happen when the car sleeps)
To have the phone power on automatically when plugged in, use this fastboot command: fastboot oem off-mode-charge 0

Known issues:
Speed will occasionally pause for a second or two (possible CANServer issue)


Contact me with  feedback or questions: info@candash.app

Discord for live feedback: https://discord.gg/hNnPHVZj
