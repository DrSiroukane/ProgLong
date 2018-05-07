import lejos.nxt.*;
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

        SensorPort[] sensorPorts = {SensorPort.S1, SensorPort.S2, SensorPort.S3, SensorPort.S4};
        ColorSensor colorSensor = null;
        ColorHTSensor colorHTSensor = null;
        ColorDetectorM colorDetector;

        int chosen_sensor = ColorDetectorM.chooseSensorScreen();
        int chosen_port = ColorDetectorM.choosePortScreen();
        int chosen_color_nature = ColorDetectorM.chooseColorNatureScreen();

        if (chosen_sensor == 0) {
            colorSensor = new ColorSensor(sensorPorts[chosen_port]);
        } else {
            colorHTSensor = new ColorHTSensor(sensorPorts[chosen_port]);
        }

        colorDetector = (chosen_sensor == 0) ?
                new ColorDetector(nbr_colors, colorSensor) :
                new ColorHTDetector(nbr_colors, colorHTSensor);

        colorDetector.config(chosen_color_nature, ColorDetectorM.DETECT_COLOR_MODE);

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.BEGIN_STOCK_COLORS);
        Button.waitForAnyPress();

        for (int i = 0; i < 2; i++) {
            ColorDetectorM.stockColorScreen(i, 1, colorDetector);
        }

        chooseCommandScreen();

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.READY_TO_FOLLOW_LINE);
        Button.waitForAnyPress();
        walkForward();
        Messages.newScreen();
        do {
            int result = colorDetector.getCurrentColor();
            if (result == 1) {
                LCD.drawString("go back ", 3, 3);
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
                    LCD.drawString("go left ", 3, 3);
                } else {
                    rotate(-error);
                    LCD.drawString("go right", 3, 3);
                }
            }
        } while (!Button.ESCAPE.isDown());
        stopWalking();

        Messages.printScreen(Messages.HELLO_SCREEN, Messages.END_PROGRAM);
        System.out.println();
        Button.waitForAnyPress();
    }

    public static void chooseCommandScreen() {
        do {
            Messages.printScreen(Messages.BACK_COMMAND_SCREEN,
                    (goBackCommand == BACK_CAMMAND) ? Messages.GO_BACK : Messages.STOP_WALK);
            Button.waitForAnyPress();
            if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
                goBackCommand = (goBackCommand == BACK_CAMMAND) ? STOP_CAMMAND : BACK_CAMMAND;
            }
        } while (!Button.ENTER.isDown());
    }
}