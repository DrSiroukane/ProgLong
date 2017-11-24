import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

/**
 * 
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * A program that makes a robot follow specified line by its color
 *
 */
public class CLineFollower {

	public static DifferentialPilot pilot;
	
	/**
	 * Make a robot walk forward
	 */
	public static void walkForward(){
		pilot.travel(20, true);
	}

	/**
	 * Make a robot stop walking
	 */
	public static void stopWalking(){
		pilot.stop();
	}


	public static void main(String[] args) {
		displayFull("Start program ...\nPress any key ...");
		Button.waitForAnyPress();
		/**
		 * Enter number of colors
		 */
		int number_colors = 1;
		do{
			if(Button.RIGHT.isDown()){
				number_colors++;
			}else if(Button.LEFT.isDown() && (number_colors != 1)){
				number_colors--;
			}
			
			displayFull("number of color\nis " + number_colors + "\nPress:\n Left: add color\n Right: remove color\n ENTER: to keep going");
			Button.waitForAnyPress();
		}while(!Button.ENTER.isDown());
		
		ColorSensor cs = new ColorSensor(SensorPort.S3);
		ColorDetector3 cd = new ColorDetector3(number_colors, cs);
		cd.createMedOfColors();
		/**
		 * Chose the color to follow
		 */
		int chosen_color = 0;
		do{
			if(Button.RIGHT.isDown()){
				chosen_color++;
			}else if(Button.LEFT.isDown()){
				chosen_color--;
			}
			
			if(chosen_color < 0){
				chosen_color = number_colors - 1;
			}else if(chosen_color >= number_colors){
				chosen_color = 0;
			}
			
			displayFull("Chosen color " + chosen_color + "\nPress:\n Left: next color\n Right: previous color\n ENTER: to keep going");
			Button.waitForAnyPress();
		}while(!Button.ENTER.isDown());
		
		displayFull("Chosen color " + chosen_color + "\nPut the robot in \na right color to \nfollow the line");
		Button.waitForAnyPress();
		
		boolean test_color = true;
		boolean rotated = true;
		int first_time = 1;
		int nbr_angle = 1;
		int angle = 2;
		int right_way = 0;
		/**
		 * Start following the line
		 */
		pilot = new DifferentialPilot(1f, 5.12f, Motor.A, Motor.C);
		displayFull("Start following\nthe line ...");
		do{
			if(cd.testColor(cs.getColor(), chosen_color)){
				walkForward();
				right_way++;
				if(right_way > 1){
					nbr_angle = 1;
				}
			}else{
				right_way = 0;
				stopWalking();
				if(rotated){
					pilot.rotate(angle*nbr_angle*first_time);
					rotated = false;
				}else{
					pilot.rotate(angle*nbr_angle*first_time);
					rotated = true;
				}
				nbr_angle++;
				first_time *= -1;
//				test_color = false;
			}
			Delay.msDelay(200);
		}while(test_color && !Button.ESCAPE.isDown());
		
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
