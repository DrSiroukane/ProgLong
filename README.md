# Projet Long Lego Mindstorms NXT

Objective: Create Line Follower Robot


## Step 1: Installation and Configuration

Install Lego Mindstorms NXT driver from [here](https://www.lego.com/en-us/mindstorms/downloads/nxt-software-download)

Install download Eclipse from [here](http://www.eclipse.org/downloads/)

Install package LeJOS, see more [here](http://lejos.org/)

Update the firmware of NXT using the command 

```
nxjflash
```


## Step 2: Programming

Using the API documentation [click here](https://lejos.sourceforge.io/nxt/nxj/api/index.html) and other sources like this [tutorial](http://www.lejos.org/nxt/nxj/tutorial/).

## Step 3: Deferences Programmes

### HelloWorld.java

A programme that display Hello World.

### WalkingRobot.java

A program that makes a robot walk depend on pressed button. Using a classes :
	[LCD](http://www.lejos.org/nxt/nxj/api/lejos/nxt/LCD.html), 
	[Button](http://www.lejos.org/nxt/nxj/api/lejos/nxt/Button.html), 
	[Motor](http://www.lejos.org/nxt/nxj/api/lejos/nxt/Motor.html), 
	[Delay](http://www.lejos.org/nxt/nxj/api/lejos/util/Delay.html).

### WalkingRobot2.java

A program that makes a robot walk depend on pressed button. Using a classes :
	[LCD](http://www.lejos.org/nxt/nxj/api/lejos/nxt/LCD.html), 
	[Button](http://www.lejos.org/nxt/nxj/api/lejos/nxt/Button.html), 
	[Motor](http://www.lejos.org/nxt/nxj/api/lejos/nxt/Motor.html).


### ColorDetector.java

A program that makes a robot learn about colors then detect them, 
using min and max for each color (RGB) if detected color inside the interval
else try to find which stocked color is the closest using also min and max.
	[SensorPort](http://www.lejos.org/nxt/nxj/api/lejos/nxt/SensorPort.html)
	[ColorSensor](http://www.lejos.org/nxt/nxj/api/lejos/nxt/ColorSensor.html),
	[Color](http://www.lejos.org/nxt/pc/api/lejos/robotics/Color.html).


### ColorDetector2.java

A program that makes a robot learn about colors then detect them, 
using min and max for each color (RGB) if detected color inside the interval
else try to find which stocked color is the closest using the median.
	[SensorPort](http://www.lejos.org/nxt/nxj/api/lejos/nxt/SensorPort.html)
	[ColorSensor](http://www.lejos.org/nxt/nxj/api/lejos/nxt/ColorSensor.html),
	[Color](http://www.lejos.org/nxt/pc/api/lejos/robotics/Color.html).

## Created by

* Thomas GUIGNARD
* Slimane SIROUKANE
