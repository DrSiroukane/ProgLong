import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p>
 * A Robot with one color sensor,
 * A program that makes a robot follow specified line (color 1)
 * and return back when it detect (color 2) or stop
 * </p>
 */
public class OneSensorAlg {
    public static final int BACK_CAMMAND = 0;
    public static final int STOP_CAMMAND = 1;

    public static ColorHTSensor colorHTSensor;
    public static ColorHTDetector colorHTDetector;

    public static int nbr_colors = 2;
    public static int Kp = 400;
    public static int Tp = 400;
    public static int turn = 0;
    public static double error = 0.5;
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

    public static void main(String[] args) {
        Messages.printScreen(Messages.HELLO_SCREEN, Messages.START_PROGRAM);
        Button.waitForAnyPress();

        colorHTSensor = new ColorHTSensor(SensorPort.S3);
        colorHTDetector = new ColorHTDetector(nbr_colors, colorHTSensor);

        for (int i = 0; i < nbr_colors; i++) {
            Messages.printScreen(Messages.STOCK_COLOR_SCREEN,
                    (i == 0) ? Messages.PUT_ON_COLOR_0 : Messages.PUT_ON_COLOR_1);
            Button.waitForAnyPress();
            Messages.printScreen(Messages.STOCK_COLOR_SCREEN,
                    (i == 0) ? Messages.STOCK_COLOR_0 : Messages.STOCK_COLOR_1);
            Messages.printScreen(Messages.STOCK_COLOR_SCREEN, Messages.DETECTOR_COLOR_0);
            colorHTDetector.stockColor(i);
            Messages.printScreen(Messages.STOCK_COLOR_SCREEN, Messages.FINISH_STOCK_COLOR);
        }

        do {
            Messages.printScreen(Messages.BACK_COMMAND_SCREEN,
                    (goBackCommand == BACK_CAMMAND) ? Messages.GO_BACK : Messages.STOP_WALK);
            Button.waitForAnyPress();
            if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
                goBackCommand = (goBackCommand == BACK_CAMMAND) ? STOP_CAMMAND : BACK_CAMMAND;
            }
        } while (!Button.ENTER.isDown());

        do {
            System.out.println("Start following\nthe line ...");
            System.out.println();
            Button.waitForAnyPress();
            walkForward();

            int result = colorHTDetector.getCurrentColor();
            if (result == 1) {
                System.out.println("go back");
                if (goBackCommand == BACK_CAMMAND) {
                    goBack();
                } else {
                    break;
                }
            } else {
                if (go_back_exec) {
                    go_back_exec = false;
                }
                if (result == 0) {
                    rotate(error);
                    System.out.println("go left");
                } else if (result == 2) {
                    rotate(-error);
                    System.out.println("go right");
                } else {
                    rotate(-error);
                    System.out.println("go right");
                }
            }
        } while (!Button.ESCAPE.isDown());
        stopWalking();

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.END_PROGRAM);
        System.out.println();
        Button.waitForAnyPress();
    }
}