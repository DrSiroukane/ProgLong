import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p>
 * A Robot with two color sensor,
 * A program that makes a robot follow specified line (color 1)
 * and return back when it detect (color 2) or stop
 * </p>
 */
public class TwoSensorsAlg {
    public static final int BACK_CAMMAND = 0;
    public static final int STOP_CAMMAND = 1;

    public static ColorSensor colorSensor; // first sensor
    public static ColorHTSensor colorHTSensor; // second sensor
    public static ColorDetectorMain[] colorDetectors; // color detectors
    public static ColorDetectorThread[] colorDetectorThreads; // color detector threads
    public static volatile int[] results = {0, 0};

    public static int nbr_colors = 2;
    public static int Kp = 400; // rotate speed
    public static int Tp = 350; // forward speed
    public static int turn = 0;
    public static double error = 0.5;

    public final static int GO_FORWARD = 0;
    public final static int ROTATE_LEFT = 1;
    public final static int ROTATE_RIGHT = 2;
    public final static int GO_BACK = 3;

    public static int last_rotation = ROTATE_RIGHT;

    public static boolean go_back = false;
    public static boolean go_back_exec = false;
    public static int goBackCommand = 0;

    /**
     * Make a robot walk forward
     */
    public static void walkForward() {
        Motor.A.setSpeed(Tp);
        Motor.C.setSpeed(Tp);
        Motor.A.forward();
        Motor.C.forward();
    }

    /**
     * Make a robot turn right or left depend error value
     *
     * @param error
     */
    public static void rotate(double error) {
        turn = (int) (Kp * error);
        Motor.A.setSpeed(Tp + turn);
        Motor.C.setSpeed(Tp - turn);
        Motor.A.forward();
        Motor.C.forward();
    }

    /**
     * Make a robot turn back
     */
    public static void goBack() {
        if (!go_back_exec) {
            DifferentialPilot pilot = new DifferentialPilot(1f, 5.12f, Motor.A, Motor.C);
            pilot.setTravelSpeed(1);
            pilot.rotate(80);
            go_back_exec = true;
        }
    }

    /**
     * Make a robot stop walking
     */
    public static void stopWalking() {
        Motor.A.stop();
        Motor.C.stop();
    }

    /**
     * Get current action (ROATATE_LEFT, ROTATE_RIGHT, ...) depend on sensors result
     *
     * @return
     */
    public static int getCurrentAction() {
        int currentAction = GO_FORWARD;
        System.out.println("l:" + results[1] + ", r:" + results[2]);
        if ((results[0] == 1) || (results[1] == 1)) { // one of sensor catch go back color
            go_back = true;
            last_rotation = GO_BACK;
            return GO_BACK;
        } else {
            if ((results[0] == 0) && (results[1] == 0)) { // both sensors on line
                currentAction = GO_FORWARD;
            } else if ((results[0] == 0) && (results[1] == 2)) { // right sensor out of line
                last_rotation = ROTATE_LEFT;
                currentAction = ROTATE_LEFT;
            } else if ((results[0] == 2) && (results[1] == 0)) { // left sensor out of line
                last_rotation = ROTATE_RIGHT;
                currentAction = ROTATE_RIGHT;
            } else { // else
                return last_rotation;
            }

            if (go_back) {
                go_back = false;
                go_back_exec = false;
            }
            return currentAction;
        }
    }

    public static void main(String[] args) {
        System.out.println("Start program ...\nPress any key ...");
        System.out.println();
        Button.waitForAnyPress();

        colorSensor = new ColorSensor(SensorPort.S2);
        colorHTSensor = new ColorHTSensor(SensorPort.S3);

        colorDetectors = new ColorDetectorMain[2];
        colorDetectors[0] = new ColorHTDetector(nbr_colors, colorHTSensor);
        colorDetectors[1] = new ColorDetector(nbr_colors, colorSensor);

        colorDetectorThreads = new ColorDetectorThread[2];
        colorDetectorThreads[0] = new ColorDetectorThread(results, 0, colorDetectors[0]);
        colorDetectorThreads[1] = new ColorDetectorThread(results, 1, colorDetectors[1]);

        for (int i = 0; i < nbr_colors; i++) {
            System.out.println("Place the robot on color number " + (i + 1) + "\n----\nThen press any button");
            System.out.println();
            Button.waitForAnyPress();

            for (int j = 0; j < colorDetectors.length; j++) {
                System.out.println("Stocking color number " + (i + 1) + "\nfor color detector (" + (j + 1) + ")");
                System.out.println();
                Button.waitForAnyPress();
                colorDetectors[j].stockColor(i);
            }
        }

        do {
            System.out.println("Choosen command is : " + ((goBackCommand == BACK_CAMMAND) ? "go back" : "stop"));
            System.out.println();

            Button.waitForAnyPress();
            if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
                goBackCommand = (goBackCommand == BACK_CAMMAND) ? STOP_CAMMAND : BACK_CAMMAND;
            }
        } while (!Button.ENTER.isDown());

        colorDetectorThreads[0].start();
        colorDetectorThreads[1].start();

        do {
            System.out.println("Start following\nthe line ...");
            System.out.println();
            Button.waitForAnyPress();
            walkForward();
            do {
                if (getCurrentAction() == GO_FORWARD) {
                    walkForward();
                    System.out.println("go forward");
                } else if (getCurrentAction() == ROTATE_LEFT) {
                    rotate(error);
                    System.out.println("go left");
                } else if (getCurrentAction() == ROTATE_RIGHT) {
                    rotate(-error);
                    System.out.println("go right");
                } else if (getCurrentAction() == GO_BACK) {
                    System.out.println("go back");
                    if (goBackCommand == BACK_CAMMAND) {
                        goBack();
                    } else {
                        break;
                    }
                } else {
                    rotate(-error);
                    System.out.println("go right");
                }
                System.out.println("l(" + results[1] + "), r(" + results[2] + ")");
            } while (!Button.ESCAPE.isDown());
            stopWalking();
            colorDetectorThreads[0].interrupt();
            colorDetectorThreads[1].interrupt();
            System.out.println("Press ESCAPE to leave program");
            System.out.println();
            Button.waitForAnyPress();
        } while (!Button.ESCAPE.isDown());

        System.out.println("Press ESCAPE to end program ...");
        System.out.println();
        Button.waitForAnyPress();
    }
}