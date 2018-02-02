import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * 
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * A program that makes a robot follow specified road by its color
 *
 */
public class CRoadFollower3 {

	public static DifferentialPilot pilot;
	public static ColorSensor cs;
	public static ColorDetector4 cd;
	public static int speed_fast = 300; // 120
	public static int speed_slow = 180; // 50
	public static int angle_plus = 8; // 
	
	/**
	 * Make a robot walk forward
	 */
	public static void walkForward(){
//		pilot.forward();
		Motor.A.setSpeed(speed_fast);
		Motor.C.setSpeed(speed_fast);
		Motor.A.forward();
		Motor.C.forward();
	}
	
	public static void rotateRight(int i){
		Motor.A.setSpeed(speed_fast + (angle_plus * i));
		Motor.C.setSpeed(speed_slow);
	}
	
	public static void rotateLeft(int i){
		Motor.A.setSpeed(speed_slow);
		Motor.C.setSpeed(speed_fast + (angle_plus * i));
	}

	/**
	 * Make a robot stop walking
	 */
	public static void stopWalking(){
		pilot.stop();
//		Motor.A.stop();
//		Motor.C.stop();
	}

	public static boolean getPickedColor(int chosen_color){

		return cd.testColor(cs.getColor(), chosen_color);
	}
	
	
	//@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		displayFull("Start program ...\nPress any key ...");
		Button.waitForAnyPress();
		/**
		 * Enter number of colors
		 */
		int number_colors = 1;
		
		cs = new ColorSensor(SensorPort.S3);	
		cd = new ColorDetector4(number_colors, cs);
		cd.createMedOfColors();
		/**
		 * Chose the color to follow
		 */
		int chosen_color = 0;
		
		displayFull("Chosen color " + chosen_color + "\nPut the robot in \na right color to \nfollow the line");
		Button.waitForAnyPress();
		
		/**
		 * Start following the line
		 */
		pilot = new DifferentialPilot(1f, 5.12f, Motor.A, Motor.C);
		pilot.setTravelSpeed(2);
		
		int count_right = 1;
		int count_left = 1;
		
		displayFull("Start following\nthe line ...");
		walkForward();
		do{
			if(getPickedColor(chosen_color)){ // color found
				
				/*if(count_left == 0){
					//rotate_angle = (angle * count_right);
				}*/
				rotateRight(count_right / 2);
				count_right++;
				count_left = 0;
			}else{ // color not found
				
				/*if(count_right == 0){
					//rotate_angle = (angle * count_left);
				}*/
				rotateLeft(count_left / 2);
				count_left++;
				count_right = 0;
			}
			
		}while(!Button.ESCAPE.isPressed());
		Button.waitForAnyPress();
		
		displayFull("Program end ...");
		Button.waitForAnyPress();
	}
	
	
	/**
	 * Display only the phrase on screen
	 * 
	 * @param phrase
	 */
	public static void displayFull(String phrase){
		LCD.clear();
		LCD.drawString(phrase, 0, 0);
	}
	
	/**
	 * Add phrase to display
	 * 
	 * @param phrase
	 */
	public static void println(String phrase){
		System.out.println(phrase);
	}
}
