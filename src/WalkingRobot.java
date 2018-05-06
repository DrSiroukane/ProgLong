import lejos.nxt.Button;
import lejos.nxt.Motor;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p>
 * A program that makes a robot walk depend on pressed button
 * </p>
 */
public class WalkingRobot {
    /**
     * Make a robot walk forward
     */
    public static void walkingForward() {
        Motor.A.forward();
        Motor.C.forward();
    }

    /**
     * Make a robot stop walking
     */
    public static void stopWalking() {
        Motor.A.stop();
        Motor.C.stop();
    }

    public static void main(String[] args) {
        System.out.println("Start Walking Robot Program\nPress any button ...");
        System.out.println();
        Button.waitForAnyPress();
        while (true) {
            if (Button.ESCAPE.isDown()) {// stop
                System.out.println("Stop");
                stopWalking();
                System.exit(0);
            } else {
                if (Button.ENTER.isDown()) { // forward
                    System.out.println("Go Forward");
                } else if (Button.LEFT.isDown()) { // left
                    System.out.println("Go Left");
                    Motor.A.stop();
                    Motor.C.rotate(480);
                } else if (Button.RIGHT.isDown()) { // right
                    System.out.println("Go Right");
                    Motor.C.stop();
                    Motor.A.rotate(480);
                }
                walkingForward();
            }
        }
    }
}
