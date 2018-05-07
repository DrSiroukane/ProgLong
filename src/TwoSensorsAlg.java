import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;

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
public class TwoSensorsAlg extends OneSensorAlg {
    public static ColorSensor colorSensor; // first sensor
    public static ColorHTSensor colorHTSensor; // second sensor
    public static ColorDetectorM[] colorDetectors; // color detectors
    public static ColorDetectorThread[] colorDetectorThreads; // color detector threads
    public static volatile int[] results = {0, 0};

    public final static int GO_FORWARD = 0;
    public final static int ROTATE_LEFT = 1;
    public final static int ROTATE_RIGHT = 2;
    public final static int GO_BACK = 3;

    public static int last_rotation = ROTATE_RIGHT;
    public static boolean go_back = false;

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
        Messages.printScreen(Messages.HELLO_SCREEN, Messages.START_PROGRAM);
        Button.waitForAnyPress();

        int chosen_color_nature = ColorDetectorM.chooseColorNatureScreen();

        colorSensor = new ColorSensor(SensorPort.S2);
        colorHTSensor = new ColorHTSensor(SensorPort.S3);

        colorDetectors = new ColorDetectorM[2];
        colorDetectors[0] = new ColorHTDetector(nbr_colors, colorHTSensor);
        colorDetectors[1] = new ColorDetector(nbr_colors, colorSensor);

        colorDetectors[0].config(chosen_color_nature, ColorDetectorM.FOLLOW_LINE_MODE);
        colorDetectors[1].config(chosen_color_nature, ColorDetectorM.FOLLOW_LINE_MODE);

        colorDetectorThreads = new ColorDetectorThread[2];
        colorDetectorThreads[0] = new ColorDetectorThread(results, 0, colorDetectors[0]);
        colorDetectorThreads[1] = new ColorDetectorThread(results, 1, colorDetectors[1]);

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.BEGIN_STOCK_COLORS);
        Button.waitForAnyPress();

        for (int i = 0; i < nbr_colors; i++) {
            for (int j = 0; j < colorDetectors.length; j++) {
                ColorDetectorM.stockColorScreen(i, j, colorDetectors[j]);
            }
        }

        chooseCommandScreen();

        colorDetectorThreads[0].start();
        colorDetectorThreads[1].start();

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.READY_TO_FOLLOW_LINE);
        Button.waitForAnyPress();
        walkForward();
        Messages.newScreen();
        do {
            if (getCurrentAction() == GO_FORWARD) {
                walkForward();
                LCD.drawString("go forward", 3, 3);
            } else if (getCurrentAction() == ROTATE_LEFT) {
                rotate(error);
                LCD.drawString(" go left  ", 3, 3);
            } else if (getCurrentAction() == ROTATE_RIGHT) {
                rotate(-error);
                LCD.drawString(" go right ", 3, 3);
            } else if (getCurrentAction() == GO_BACK) {
                LCD.drawString(" go back  ", 3, 3);
                if (goBackCommand == BACK_CAMMAND) {
                    goBack();
                } else {
                    break;
                }
            } else {
                rotate(-error);
                LCD.drawString(" go back  ", 3, 3);
            }
        } while (!Button.ESCAPE.isDown());
        stopWalking();
        colorDetectorThreads[0].interrupt();
        colorDetectorThreads[1].interrupt();

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.END_PROGRAM);
        System.out.println();
        Button.waitForAnyPress();
    }
}