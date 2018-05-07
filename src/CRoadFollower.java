import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p> A program that makes a robot follow specified road by its color </p>
 */
public class CRoadFollower {
    public static ColorSensor cs;
    public static ColorDetector cd;

    public static double error = 0.4;

    /**
     * Pick color
     *
     * @param chosen_color
     * @return
     */
    public static boolean getPickedColor(int chosen_color) {
        int nbr_cap = 3;
        int detected = 0;
        for (int i = 0; i < nbr_cap; i++) {
            if (cd.testColor(cs.getColor(), chosen_color)) {
                detected++;
            }
        }
        return (detected > (nbr_cap / 2));
    }

    public static void main(String[] args) {
        System.out.println("Start program ...\nPress any key ...");
        Button.waitForAnyPress();
        /**
         * Enter number of colors
         */
        int number_colors = 1;
        cs = new ColorSensor(SensorPort.S3);
        cd = new ColorDetector(number_colors, cs);
        cd.createMedOfColors();
        /**
         * Chose the color to follow
         */
        int chosen_color = 0;
        //int stop_color = 1;
        System.out.println("Chosen color " + chosen_color + "\nPut the robot in \na right color to \nfollow the line");
        Button.waitForAnyPress();

        /**
         * Start following the line
         */
        int count_right = 1;
        int count_left = 1;
        System.out.println("Start following\nthe line ...");
        OneSensorAlg.walkForward();

        boolean right_color = false;

        do {
            while (getPickedColor(chosen_color)) { // walking color found
                OneSensorAlg.rotate(error);
                right_color = false;
            }

            if (!right_color) {
                error = -error;
            }

            while (!getPickedColor(chosen_color)) { // trying to find walking color
                OneSensorAlg.rotate(error * 2.2);
                right_color = true;
            }

        } while (!Button.ESCAPE.isDown());

        OneSensorAlg.stopWalking();
        System.out.println("Program end ...");
        Button.waitForAnyPress();
    }
}