import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;

/**
 * 
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * A program that makes a robot follow specified road by its color
 *
 */
public class CRFMultiSensors{

	public static ColorSensor cs1;
	public static ColorHTSensor cs2;
	public static LightSensor ls1;
	public static LightSensor ls2;
//	public static ColorDetector4 cd;
	public static volatile int[] results = {0, 0, 0, 0};
//	public static int speed_fast = 300; // 120
//	public static int speed_slow = 180; // 50
//	public static int angle_plus = 8; // 
	
	public static int nbr_colors = 1;
	public static int Kp = 450;
	public static int offset = 0;
	public static int Tp = 450;
	public static int turn = 0;
	public static double error = 0.5;
	
	public final static int GO_FORWARD = 0;
	public final static int ROTATE_LEFT = 1;
	public final static int ROTATE_RIGHT = 2;
	
	public static int last_rotation = ROTATE_RIGHT;
	
	
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
//		System.out.println(turn);
		Motor.A.setSpeed(Tp + turn);
		Motor.C.setSpeed(Tp - turn);
	}
	
	/**
	 * Make a robot stop walking
	 */
	public static void stopWalking(){
		Motor.A.stop();
		Motor.C.stop();
	}

	
	/**
	 * Method that 
	 * @return
	 */
	public static int getCurrentAction(){
		if((results[1] == 1) && (results[2] == 1)){
			return GO_FORWARD;
		}else if((results[1] == 0) && (results[2] == 1)){
			last_rotation = ROTATE_LEFT;
			return ROTATE_LEFT;
		}else if((results[1] == 1) && (results[2] == 0)){
			last_rotation = ROTATE_RIGHT;
			return ROTATE_RIGHT;
		}else{
			return last_rotation;
		}
	}

	public static void main(String[] args) {
		displayFull("Start program ...\nPress any key ...");
		Button.waitForAnyPress();
		
		ls1 = new LightSensor(SensorPort.S1);
		cs1 = new ColorSensor(SensorPort.S2);
		cs2 = new ColorHTSensor(SensorPort.S3);
		ls2 = new LightSensor(SensorPort.S4);
		
		ColorDetector4 colorDetector = new ColorDetector4(nbr_colors, cs1);
		ColorHTDetector colorHTDetector = new ColorHTDetector(nbr_colors, cs2);
		
//		LightSensorThread ls1Thread = new LightSensorThread(ls1, results, 0);
		ColorSensorThread  cs1Thread = new ColorSensorThread(cs1, results, 1, nbr_colors, colorDetector);
		ColorHTSensorThread  cs2Thread = new ColorHTSensorThread(cs2, results, 2, nbr_colors, colorHTDetector);
//		LightSensorThread ls2Thread = new LightSensorThread(ls2, results, 3);
		
		
		colorDetector.createMedOfColors();
		colorHTDetector.createMedOfColors();
		
		
		displayFull("On Stocking colors");
		Button.waitForAnyPress();
		

		cs1Thread.start();
		cs2Thread.start();
		
		/**
		 * Start following the line
		 */
		displayFull("Start following\nthe line ...");
		Button.waitForAnyPress();
		walkForward();
		do{
			if(getCurrentAction() == GO_FORWARD){
				walkForward();
				System.out.println("go forward");
			}else if(getCurrentAction() == ROTATE_LEFT){
				rotate(error);
				System.out.println("go left");
			}else{
				rotate(-error);
				System.out.println("go right");
			}
		}while(!Button.ESCAPE.isDown());
		
		cs1Thread.interrupt();
		cs2Thread.interrupt();
		
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