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
 * A program that makes a robot follow specified road by its color
 *
 */
public class CRoadFollower {

	public static DifferentialPilot pilot;
	
	/**
	 * Make a robot walk forward
	 */
	public static void walkForward(){
		pilot.travel(15, true);
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
		
//		boolean rotated_updated = true;
		int rotation_side = 1;
		int nbr_angle = 1;
		int angle = 2;
		int right_way = 0;
		int turn_right = 0;
		int turn_left = 0;
		int test_nbr_tries = 0;
		boolean update_turn = true;
		/**
		 * Start following the line
		 */
		pilot = new DifferentialPilot(1f, 5.12f, Motor.A, Motor.C);
		displayFull("Start following\nthe line ...");
		do{
			if(cd.testColor(cs.getColor(), chosen_color)){
				if(!pilot.isMoving()){
					walkForward();
				}
				
				right_way++;
				if(right_way > 1){
					nbr_angle = 1;
				}
				
				if(update_turn){
//					if(turn_right == turn_left){
						rotation_side *= -1; // right
//					}
					
					if(rotation_side == 1){
						turn_right = 2;
						turn_left  = 0;
					}else{
						turn_right = 0;
						turn_left = 2;
					}
				}
				
				update_turn = false;
				test_nbr_tries++;
			}else{
				if(pilot.isMoving()){
					stopWalking();
//					rotated_updated = false;
					update_turn = true;
				}
				
				pilot.rotate(angle*nbr_angle*rotation_side); // right
				
				System.out.println(test_nbr_tries + ": " + turn_right + " ? " + turn_left);
				
				if(turn_right < turn_left){
					turn_left--;
					rotation_side = -1;
				}else if (turn_right > turn_left){
					turn_right--;
					rotation_side = 1;
				}
				
				if(turn_left == turn_right){
					rotation_side *= -1; // left
					update_turn = true;
					nbr_angle++;
				}
				
				nbr_angle++;
			}
			Delay.msDelay(35);
		}while(!Button.ESCAPE.isDown());
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
