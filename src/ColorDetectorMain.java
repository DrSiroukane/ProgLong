import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.Color;

import java.util.Arrays;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p> A program that makes a robot learn about colors then detect them </p>
 */
abstract class ColorDetectorMain {
    public final int RED = 0;
    public final int BLUE = 1;
    public final int GREEN = 2;

    public final int MIN = 0;
    public final int MAX = 1;
    public final int MED = 2;

    public final int FORWARD_COLOR = 0;
    public final int BACK_COLOR = 1;
    public final int SPACE_COLOR = 2;

    public final int DARK_COLOR = 0;
    public final int LIGHT_COLOR = 1;

    public static final int FOLLOW_LINE_MODE = 0;
    public static final int DETECT_COLOR_MODE = 1;

    public int[] errors = {15, 15};
    public int mode = FOLLOW_LINE_MODE;

    public int nbr_colors;
    public int[][][] list_colors = {
            {
                    {500, 500, 500}, // MIN
                    {0, 0, 0}, // MAX
                    {0, 0, 0}, // MED
            }, // FORWARD_COLOR
            {
                    {500, 500, 500}, // MIN
                    {0, 0, 0}, // MAX
                    {0, 0, 0}, // MED
            } // BACK_COLOR
    };

    public int nbr_tries = 31;
    public int[][] color_data = new int[3][nbr_tries];
    public Color in_color;
    public int current_red, current_blue, current_green;

    public int[][] dists_test = {};

    /**
     * Method that stock red, blue and green value of each color focusing on median value
     */
    public void stockColor(int i) {
        for (int j = 0; j < nbr_tries; j++) {
            in_color = getColor();
            current_red = in_color.getRed();
            current_blue = in_color.getBlue();
            current_green = in_color.getGreen();

            if (current_red < list_colors[i][MIN][RED]) {
                list_colors[i][MIN][RED] = current_red;
            } else if (list_colors[i][MAX][RED] < current_red) {
                list_colors[i][MAX][RED] = current_red;
            }

            if (current_blue < list_colors[i][MIN][BLUE]) {
                list_colors[i][MIN][BLUE] = current_blue;
            } else if (list_colors[i][MAX][BLUE] < current_blue) {
                list_colors[i][MAX][BLUE] = current_blue;
            }

            if (current_green < list_colors[i][MIN][GREEN]) {
                list_colors[i][MIN][GREEN] = current_green;
            } else if (list_colors[i][MAX][GREEN] < current_green) {
                list_colors[i][MAX][GREEN] = current_green;
            }

            color_data[RED][j] = current_red;
            color_data[BLUE][j] = current_blue;
            color_data[GREEN][j] = current_green;
        }

        Arrays.sort(color_data[RED]);
        Arrays.sort(color_data[BLUE]);
        Arrays.sort(color_data[GREEN]);

        int med_index = (nbr_tries - 1) / 2;

        list_colors[i][MED][RED] = color_data[RED][med_index];
        list_colors[i][MED][BLUE] = color_data[BLUE][med_index];
        list_colors[i][MED][GREEN] = color_data[GREEN][med_index];

        System.out.println("MAX - MIN\n R=[" + list_colors[i][MIN][RED] + "," + list_colors[i][MAX][RED] + "]\n B=[" + list_colors[i][MIN][BLUE] + "," + list_colors[i][MAX][BLUE] + "]\n G=[" + list_colors[i][MIN][GREEN] + "," + list_colors[i][MAX][GREEN]);
        System.out.println();
        Button.waitForAnyPress();

        System.out.println("MED\n R=[" + list_colors[i][MED][RED] + "]\n B=[" + list_colors[i][MED][BLUE] + "]\n G=[" + list_colors[i][MED][GREEN] + "]\nPress any button to continue ...");
        System.out.println();
        Button.waitForAnyPress();
    }

    /**
     * Method that stock red, blue and green value of each color focusing on median value
     */
    public void createMedOfColors() {
        Button.waitForAnyPress();
        for (int i = 0; i < nbr_colors; i++) {
            System.out.println("Start stocking\nColor number " + i);
            Button.waitForAnyPress();
            stockColor(i);
        }
    }

    /**
     * return distance between x or y
     *
     * @param x
     * @param y
     * @return
     */
    public float minDistance1D(int x, int y) {
        return Math.abs(y - x);
    }

    /**
     * return Min distance between min or max
     *
     * @param x
     * @param min
     * @param max
     * @return
     */
    public static float minDistance1D(int x, int min, int max) {
        if (0 < (x - min)) {
            return x - min;
        } else if (0 < max - x) {
            return max - x;
        }

        return 0;
    }

    /**
     * Calculate Min distance between color c and the selected color
     *
     * @param c            in color
     * @param color_number index of color to test
     * @return
     */
    public float minDistance3D(Color c, int color_number, int obj) {
        float r_dis, b_dis, g_dis;

        if (obj == MED) {
            r_dis = minDistance1D(c.getRed(), list_colors[color_number][MED][RED]);
            b_dis = minDistance1D(c.getBlue(), list_colors[color_number][MED][BLUE]);
            g_dis = minDistance1D(c.getGreen(), list_colors[color_number][MED][GREEN]);
        } else {
            r_dis = minDistance1D(c.getRed(), list_colors[color_number][MIN][RED], list_colors[color_number][MAX][RED]);
            b_dis = minDistance1D(c.getBlue(), list_colors[color_number][MIN][BLUE], list_colors[color_number][MAX][BLUE]);
            g_dis = minDistance1D(c.getGreen(), list_colors[color_number][MIN][GREEN], list_colors[color_number][MAX][GREEN]);
        }

        return (float) Math.sqrt(r_dis * r_dis + b_dis * b_dis + g_dis * g_dis);
    }

    /**
     * Test Color
     *
     * @param c            Color
     * @param color_number index of color to test with c
     * @return check weather the color can be the same of color_number
     */
    public boolean testColor(Color c, int color_number) {
        System.out.println("color_number: " + color_number);
        System.out.println("d(MinMax):" + minDistance3D(c, color_number, MIN));
        System.out.println("d(Med):" + minDistance3D(c, color_number, MED));
        return (
                (testColorMinMax(c, color_number)) ||
                        (minDistance3D(c, color_number, MED) < errors[color_number]) ||
                        (minDistance3D(c, color_number, MIN) < errors[color_number])
        );
    }

    /**
     * Test Color MIN MAX
     *
     * @param c Color
     * @return check weather the color in the interval or not
     */
    public boolean testColorMinMax(Color c, int color_number) {
        if (list_colors[color_number][MIN][RED] <= c.getRed() && c.getRed() <= list_colors[color_number][MAX][RED]) {
            if (list_colors[color_number][MIN][BLUE] <= c.getBlue() && c.getBlue() <= list_colors[color_number][MAX][BLUE]) {
                if (list_colors[color_number][MIN][GREEN] <= c.getGreen() && c.getGreen() <= list_colors[color_number][MAX][GREEN]) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Method to detect closest color
     *
     * @return detected color index
     */
    public int getCurrentColor() {
        Color c = getColor();
        int[][] dists = {
                {0, 0},
                {0, 0}
        };

        double min_dist_3d = 500;
        double current_dist = 0;
        int[][] color_detected = {
                {0, 0, 0}, // FORWARD_COLOR
                {0, 0, 0} // ROTATE_COLOR
        };

        for (int i = 0; i < nbr_colors; i++) { // calculate min max distance
            current_dist = minDistance3D(c, i, MIN);
            dists[i][0] = (int) current_dist;
            if (min_dist_3d > current_dist) {
                min_dist_3d = current_dist;
                if (current_dist < errors[i]) {
                    color_detected[i][0] = 1;
                }
            }
        }

        min_dist_3d = 500;
        for (int i = 0; i < nbr_colors; i++) { // calculate med distance
            current_dist = minDistance3D(c, i, MED);
            dists[i][1] = (int) current_dist;
            if (min_dist_3d > current_dist) {
                min_dist_3d = current_dist;
                if (current_dist < errors[i]) {
                    color_detected[i][1] = 1;
                }
            }
        }

        if(mode == DETECT_COLOR_MODE){
            for(int i=0; i<nbr_colors; i++){ // check value between min and max
                color_detected[i][2] = testColorMinMax(c, i) ? 1 : 0;
            }

            for(int i=0; i<color_detected.length; i++){
                System.out.println("color " + i + ": ");

                System.out.println("MMD : " + color_detected[i][0]);
                System.out.println("MD : " + color_detected[i][1]);
                System.out.println("MM : " + color_detected[i][2]);

                System.out.println();
                Button.waitForAnyPress();
            }

            Button.waitForAnyPress();
            for(int i=0; i<dists.length; i++){
                System.out.println("color " + i + ": ");

                System.out.println("MMD : " + dists[i][0]);
                System.out.println("MD : " + dists[i][1]);

                System.out.println();
                Button.waitForAnyPress();
            }
        }

        dists_test = dists;

        for (int i = 0; i < color_detected.length; i++) {
            if ((color_detected[i][0] == 1) || (color_detected[i][1] == 1)) {
                if(mode == DETECT_COLOR_MODE){
                    Messages.printScreen(Messages.STOCK_COLOR_SCREEN, Messages.DETECTOR_COLOR_0);
                }

                return i;
            }
        }

        return SPACE_COLOR;
    }

    public Color getColor() {
        return null;
    }

    public void config(int colorType, int mode){
        return;
    }

    public static void main(String[] args) {
        int nbr_color = 2;
        int chosen_sensor = 0;
        String[] sensors = {"colorSensor", "colorHTSenser"};
        int chosen_port = 0;
        String[] ports = {"S2", "S3"};
        SensorPort[] sensorPorts = {SensorPort.S2, SensorPort.S3};
        ColorSensor colorSensor = null;
        ColorHTSensor colorHTSensor = null;
        ColorDetectorMain colorDetector;
        do {
            System.out.println("Choosen senser type : " + sensors[chosen_sensor]);
            System.out.println();
            Button.waitForAnyPress();
            if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
                chosen_sensor = (chosen_sensor == 0) ? 1 : 0;
            }
        } while (!Button.ENTER.isDown());

        do {
            System.out.println("Choosen port : " + ports[chosen_port]);
            System.out.println();
            Button.waitForAnyPress();
            if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
                chosen_port = (chosen_port == 0) ? 1 : 0;
            }
        } while (!Button.ENTER.isDown());

        if (chosen_sensor == 0) {
            colorSensor = new ColorSensor(sensorPorts[chosen_port]);
        } else {
            colorHTSensor = new ColorHTSensor(sensorPorts[chosen_port]);
        }

        colorDetector = (chosen_sensor == 0) ?
                new ColorDetector(nbr_color, colorSensor) :
                new ColorHTDetector(nbr_color, colorHTSensor);

        System.out.println("Stock color using Color Sensor detector");
        System.out.println();
        Button.waitForAnyPress();

        colorDetector.createMedOfColors();

        System.out.println("Start testing");
        System.out.println();
        Button.waitForAnyPress();

        int current_color = 0;
        do {
            current_color = colorDetector.getCurrentColor();
            Button.waitForAnyPress();
            System.out.println("Current color detected " + current_color);
            System.out.println();
            Button.waitForAnyPress();
        } while (!Button.ESCAPE.isDown());
    }
}
