import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

/**
 * 
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * A program that makes a robot walk depend on pressed button
 *
 */
public class WalkingRobot {
	/**
	 * Display only the phrase on screen
	 * 
	 * @param phrase
	 */
	public static void displayFull(String phrase){
		LCD.clear();
		LCD.drawString(phrase, 0, 1);
	}
	
	/**
	 * Add phrase to display
	 * 
	 * @param phrase
	 */
	public static void println(String phrase){
		System.out.println(phrase);
	}
	
	/**
	 * Make a robot walk forward
	 */
	public static void walkingForward(){
		Motor.A.forward();
	    Motor.C.forward();
	}

	/**
	 * Make a robot stop walking
	 */
	public static void stopWalking(){
		Motor.A.stop();
	    Motor.C.stop();
	}

	
	public static void main(String[] args) {
		displayFull("Start Walking Robot Program\nPress any button ...");
		Button.waitForAnyPress();
		
		while(true){
			if(Button.ENTER.isDown()){ // forward
				displayFull("Go Forward");
				walkingForward();
			}else if(Button.LEFT.isDown()){ // left
				displayFull("Go Left");
				Motor.A.stop();
				Delay.msDelay(1300);
				walkingForward();
			}else if(Button.RIGHT.isDown()){ // right
				displayFull("Go Right");
				Motor.C.stop();
				Delay.msDelay(1300);
				walkingForward();
			}else if(Button.ESCAPE.isDown()){ // stop
				displayFull("Stop");
				stopWalking();
			    System.exit(0);
			}
		}
	}

}
