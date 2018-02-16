import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

/**
 * 
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * A program that makes a robot follow specified road by its color
 *
 */
public class CRoadFollower4 {

	public static ColorSensor cs;
	public static ColorDetector4 cd;
//	public static int speed_fast = 300; // 120
//	public static int speed_slow = 180; // 50
//	public static int angle_plus = 8; // 

	public static int Kp = 200;
	public static int offset = 0;
	public static int Tp = 270;
	public static int turn = 0;
	public static double error = 0;
	public static double init_error = 0.25;
	public static double step_error = 0.05;
	
	/**
	 * Make a robot walk forward
	 */
	public static void walkForward(){
		Motor.A.setSpeed(Tp);
		Motor.C.setSpeed(Tp);
		Motor.A.forward();
		Motor.C.forward();
	}

	public static void rotate(double error){
		turn = (int) (Kp * error);
		System.out.println(turn);
		Motor.A.setSpeed(Tp + turn);
		Motor.C.setSpeed(Tp - turn);
	}
	
	/**
	 * Rotate Right
	 * 
	 * @param i
	 */
	/*public static void rotateRight(int i){
		Motor.A.setSpeed(speed_fast + (angle_plus * i));
		Motor.C.setSpeed(speed_slow);
	}
	
	*//**
	 * Rotate Left
	 * 
	 * @param i
	 *//*
	public static void rotateLeft(int i){
		Motor.A.setSpeed(speed_slow);
		Motor.C.setSpeed(speed_fast + (angle_plus * i));
	}*/
	
	/**
	 * Make a robot stop walking
	 */
	public static void stopWalking(){
		Motor.A.stop();
		Motor.C.stop();
	}

	/**
	 * Pick color
	 * 
	 * @param chosen_color
	 * @return
	 */
	public static boolean getPickedColor(int chosen_color){
		int nbr_cap = 5;
		int detected = 0;
		for(int i=0; i<nbr_cap; i++){
			if(cd.testColor(cs.getColor(), chosen_color)){
				detected++;
			}
		}
		return (detected > (nbr_cap/2));
	}
	
	public static void main(String[] args) {
		displayFull("Start program ...\nPress any key ...");
		Button.waitForAnyPress();
		/**
		 * Enter number of colors
		 */
		int number_colors = 2;
		cs = new ColorSensor(SensorPort.S3);	
		cd = new ColorDetector4(number_colors, cs);
		cd.createMedOfColors();
		/**
		 * Chose the color to follow
		 */
		int chosen_color = 0;
		int stop_color = 1;
		displayFull("Chosen color " + chosen_color + "\nPut the robot in \na right color to \nfollow the line");
		Button.waitForAnyPress();
		
		/**
		 * Start following the line
		 */
		int count_right = 1;
		int count_left = 1;
		displayFull("Start following\nthe line ...");
		walkForward();
		
		do{
			if(getPickedColor(stop_color)){ // stop color found
				break;
			}else if(getPickedColor(chosen_color)){ // walking color found
				if(-1 != error){
					error = -1 * (init_error + step_error * count_right);
					rotate(error);
					// rotateRight(count_right / 2);
					count_right++;
					count_left = 0;
				}
			}else{ // color not found
				if(1 != error){
					error = init_error + step_error * count_left;
					rotate(error);
					// rotateLeft(count_left / 2);
					count_left++;
					count_right = 0;
				}	
			}
		}while(!Button.ESCAPE.isDown());
		
		stopWalking();
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
