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

Using the API documentation [click here](https://lejos.sourceforge.io/nxt/nxj/api/index.html) 
and other sources like this [tutorial](http://www.lejos.org/nxt/nxj/tutorial/).

## Step 3: Differences Programmes
	
### ColorDetectorM.java
A program that makes a robot learn about colors then detect them,
Methods used:
* Pick min and max for each color (RGB) if detected color inside the interval
* Try to find which stocked color is the closest using the median,
* Try to find if its closest to min and max using distance.

<b>Note:</b> distance = Math.sqrt((R - R')² + (G - G')² +(B - B')²)

### WalkingRobot.java
A program that makes a robot walk depend on pressed button. Using a classes :
	[LCD](http://www.lejos.org/nxt/nxj/api/lejos/nxt/LCD.html), 
	[Button](http://www.lejos.org/nxt/nxj/api/lejos/nxt/Button.html), 
	[Motor](http://www.lejos.org/nxt/nxj/api/lejos/nxt/Motor.html).

### CLineFollower.java
A program that makes a robot follow stright  line by its color,
	[ColorDetector](),
	[DifferentialPilot](https://lejos.sourceforge.io/nxt/nxj/api/lejos/robotics/navigation/DifferentialPilot.html),
and other classes.

### CRoadFollower.java
A program that makes a robot follow specified road by its color with only one sensor.
The method was letting robot get out from the road color and change direction each time it get out 
from line going right, left.

### OneSensorAlg.java
A final program that makes a robot follow specified line (color 1)
and return back when it detect (color 2) or stop.

### TwoSensorAlg.java
The same as previews program but with two sensors.

## Step 4: Needed Classes
Helpful classes.

### ColorDetector.java
Class that extends [ColorDetectorM.java]() to be used only for normal Color Sensor.

### ColorHTDetector.java
Class that extends [ColorDetectorM.java]() to be used only for HT Color Sensor.

### ColorDetectorThread.java
Thread that help to stock the detected color index with help of Color Detector 
class.

### Messages.java
Class that generate a great messages and menu to make the programs easy to use.

## Step 5: Manual compilation

Compilation steps: 

1. Open "Terminal" of "Cmd",

2. Go to class *.java folder,

3. Compile "class_name.java" using this command,
    ```
    nxjc class_name.java
    ```
4. "class_name.class" output will be created,
    
5. Be sure that the robot is connected (USB),

6. Do the command below to run the program on the robot,
```
nxjc -r class_name
```

## Created by

* Thomas GUIGNARD
* Slimane SIROUKANE
