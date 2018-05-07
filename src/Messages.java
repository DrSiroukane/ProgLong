import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Messages {
    public static final int PRESS_SCREEN = -1;
    public static final int HELLO_SCREEN = 0;
    public static final int CHOOSE_SENSOR_TYPE_SCREEN = 1;
    public static final int CHOOSE_SENSOR_PORT_SCREEN = 2;
    public static final int STOCK_COLOR_SCREEN = 3;
    public static final int BACK_COMMAND_SCREEN = 4;
    public static final int CONFIG_MODE = 5;
    public static final int CHOOSE_COLOR_NATURE_SCREEN = 6;
    public static final int DETECTED_COLOR_SCREEN = 7;

    // HELLO MESSAGE
    public static final int START_PROGRAM = 0;
    public static final int END_PROGRAM = 1;
    public static final int PUT_ROBOT_ON_LINE = 2;
    public static final int BEGIN_STOCK_COLORS = 3;
    public static final int BEGIN_DETECT_COLORS = 4;
    public static final int READY_TO_FOLLOW_LINE = 5;

    // CHOOSE SENSOR TYPE
    public static final int SENSOR_TYPE_0 = 0;
    public static final int SENSOR_TYPE_1 = 1;

    // CHOOSE SENSOR PORT
    public static final int SENSOR_PORT_1 = 1;
    public static final int SENSOR_PORT_2 = 2;
    public static final int SENSOR_PORT_3 = 3;
    public static final int SENSOR_PORT_4 = 4;

    // STOCK COLOR
    public static final int PUT_ON_COLOR_0 = 0;
    public static final int PUT_ON_COLOR_1 = 1;
    public static final int DETECTOR_COLOR_0 = 2;
    public static final int DETECTOR_COLOR_1 = 3;
    public static final int STOCK_COLOR_0 = 4;
    public static final int STOCK_COLOR_1 = 5;
    public static final int FINISH_STOCK_COLOR = 6;

    // BACK COMMAND
    public static final int GO_BACK = 0;
    public static final int STOP_WALK = 1;

    // CONFIG MODE
    public static final int FOLLOW_LINE = 0;
    public static final int DETECT_COLOR = 1;

    // COLOR NATURE
    public static final int DARK_COLOR = 0;
    public static final int LIGHT_COLOR = 1;

    // DETECTED COLOR
    public static final int FORWARD_COLOR = 0;
    public static final int GO_BACK_COLOR = 1;
    public static final int SPACE_COLOR = 2;


    public static int color = 0;
    public static int detector = 0;

    /**
     * Class for different message to display on NXT screen
     *
     * @param screen
     * @param message
     */
    public static void printScreen(int screen, int message) {
        newScreen();
        switch (screen) {
            case HELLO_SCREEN:
                switch (message) {
                    case START_PROGRAM:
                        LCD.drawString("Start", 5, 2);
                        LCD.drawString("Program", 4, 5);
                        break;
                    case END_PROGRAM:
                        LCD.drawString("End", 6, 2);
                        LCD.drawString("Program", 4, 5);
                        break;
                    case PUT_ROBOT_ON_LINE:
                        LCD.drawString("Put ROBOT on", 2, 3);
                        LCD.drawString("line to start", 2, 5);
                        break;
                    case BEGIN_STOCK_COLORS:
                        LCD.drawString("Stock colors", 2, 3);
                        LCD.drawString("will start", 3, 5);
                        break;
                    case BEGIN_DETECT_COLORS:
                        LCD.drawString("Detect colors", 2, 3);
                        LCD.drawString("will start", 3, 5);
                        break;
                    case READY_TO_FOLLOW_LINE:
                        LCD.drawString("Robot is", 4, 3);
                        LCD.drawString("ready to", 4, 4);
                        LCD.drawString("start", 6, 5);
                        break;
                }
                break;
            case CHOOSE_SENSOR_TYPE_SCREEN:
                sensorScreen();
                switch (message) {
                    case SENSOR_TYPE_0:
                        LCD.drawString(">", 2, 3);
                        break;
                    case SENSOR_TYPE_1:
                        LCD.drawString(">", 2, 4);
                        break;
                }
                break;
            case CHOOSE_SENSOR_PORT_SCREEN:
                portScreen();
                switch (message) {
                    case SENSOR_PORT_1:
                        LCD.drawString(">", 2, 3);
                        break;
                    case SENSOR_PORT_2:
                        LCD.drawString(">", 2, 4);
                        break;
                    case SENSOR_PORT_3:
                        LCD.drawString(">", 2, 5);
                        break;
                    case SENSOR_PORT_4:
                        LCD.drawString(">", 2, 6);
                        break;
                }
                break;
            case STOCK_COLOR_SCREEN:
                stockColorScreen();
                switch (message) {
                    case PUT_ON_COLOR_0:
                        LCD.drawString("+", 2, 3);
                        color = 1;
                        break;
                    case PUT_ON_COLOR_1:
                        LCD.drawString("+", 2, 3);
                        color = 2;
                        break;
                    case DETECTOR_COLOR_0:
                        LCD.drawString("+", 2, 4);
                        detector = 1;
                        break;
                    case DETECTOR_COLOR_1:
                        LCD.drawString("+", 2, 4);
                        detector = 2;
                        break;
                    case STOCK_COLOR_0:
                        LCD.drawString("+", 2, 5);
                        break;
                    case STOCK_COLOR_1:
                        LCD.drawString("+", 2, 5);
                        break;
                    case FINISH_STOCK_COLOR:
                        LCD.drawString("+", 2, 6);
                        break;
                }

                LCD.drawString(color + "", 13, 3);
                LCD.drawString(detector + "", 13, 4);
                LCD.drawString(color + "", 11, 5);
                LCD.drawString(detector + "", 14, 5);
                break;
            case BACK_COMMAND_SCREEN:
                backCommandScreen();
                switch (message) {
                    case GO_BACK:
                        LCD.drawString(">", 2, 4);
                        break;
                    case STOP_WALK:
                        LCD.drawString(">", 2, 5);
                        break;
                }
                break;
            case CONFIG_MODE:
                modeScreen();
                switch (message) {
                    case FOLLOW_LINE:
                        LCD.drawString(">", 2, 4);
                        break;
                    case DETECT_COLOR:
                        LCD.drawString(">", 2, 5);
                        break;
                }
                break;
            case CHOOSE_COLOR_NATURE_SCREEN:
                colorNatureScreen();
                switch (message) {
                    case DARK_COLOR:
                        LCD.drawString(">", 2, 4);
                        break;
                    case LIGHT_COLOR:
                        LCD.drawString(">", 2, 5);
                        break;
                }
                break;
            case DETECTED_COLOR_SCREEN:
                detectedColorScreen();
                switch (message) {
                    case FORWARD_COLOR:
                        LCD.drawString("forward", 4, 5);
                        break;
                    case GO_BACK_COLOR:
                        LCD.drawString("go back", 4, 5);
                        break;
                    case SPACE_COLOR:
                        LCD.drawString("space", 6, 5);
                        break;
                }
                break;
        }
    }


    public static void main(String[] args) {
//        LCD.clear();
        /*LCD.drawString("1234567890123456789", 0, 0);
        LCD.drawString("2*width :" + LCD.SCREEN_WIDTH, 0, 1);
        LCD.drawString("3*height :" + LCD.SCREEN_HEIGHT, 0, 2);
        LCD.drawString("4******************", 0,3);
        LCD.drawString("5******************", 0,4);
        LCD.drawString("6******************", 0,5);
        LCD.drawString("7******************", 0,6);
        LCD.drawString("8******************", 0,7);
        LCD.drawString("9******************", 0,8);
        Button.waitForAnyPress();*/

        do {
            /*Messages.printScreen(HELLO_SCREEN, START_PROGRAM);
            Button.waitForAnyPress();
            Messages.printScreen(HELLO_SCREEN, END_PROGRAM);
            Button.waitForAnyPress();
            Messages.printScreen(HELLO_SCREEN, PUT_ROBOT_ON_LINE);
            Button.waitForAnyPress();*/
            /*do {
                Messages.printScreen(CHOOSE_SENSOR_TYPE_SCREEN, SENSOR_TYPE_0);
                Button.waitForAnyPress();
                Messages.printScreen(CHOOSE_SENSOR_TYPE_SCREEN, SENSOR_TYPE_1);
                Button.waitForAnyPress();
            }while(!Button.ESCAPE.isDown());*/
            /*do {
                Messages.printScreen(CHOOSE_SENSOR_PORT_SCREEN, SENSOR_PORT_1);
                Button.waitForAnyPress();
                Messages.printScreen(CHOOSE_SENSOR_PORT_SCREEN, SENSOR_PORT_2);
                Button.waitForAnyPress();
                Messages.printScreen(CHOOSE_SENSOR_PORT_SCREEN, SENSOR_PORT_3);
                Button.waitForAnyPress();
                Messages.printScreen(CHOOSE_SENSOR_PORT_SCREEN, SENSOR_PORT_4);
                Button.waitForAnyPress();
            }while(!Button.ESCAPE.isDown());*/
            /*do {
                Messages.printScreen(STOCK_COLOR_SCREEN, PUT_ON_COLOR_0);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, DETECTOR_COLOR_0);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, STOCK_COLOR_0);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, FINISH_STOCK_COLOR);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, PUT_ON_COLOR_1);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, DETECTOR_COLOR_1);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, STOCK_COLOR_1);
                Button.waitForAnyPress();
                Messages.printScreen(STOCK_COLOR_SCREEN, FINISH_STOCK_COLOR);
                Button.waitForAnyPress();
            }while(!Button.ESCAPE.isDown());*/
            /*do {
                Messages.printScreen(BACK_COMMAND_SCREEN, GO_BACK);
                Button.waitForAnyPress();
                Messages.printScreen(BACK_COMMAND_SCREEN, STOP_WALK);
                Button.waitForAnyPress();
            }while(!Button.ESCAPE.isDown());*/
            /*do {
                Messages.printScreen(CONFIG_MODE, FOLLOW_LINE);
                Button.waitForAnyPress();
                Messages.printScreen(CONFIG_MODE, DETECT_COLOR);
                Button.waitForAnyPress();
            }while(!Button.ESCAPE.isDown());*/
            /*do {
                Messages.printScreen(CHOOSE_COLOR_NATURE_SCREEN, DARK_COLOR);
                Button.waitForAnyPress();
                Messages.printScreen(CHOOSE_COLOR_NATURE_SCREEN, LIGHT_COLOR);
                Button.waitForAnyPress();
            }while(!Button.ESCAPE.isDown());*/
            do {
                Messages.printScreen(DETECTED_COLOR_SCREEN, FORWARD_COLOR);
                Button.waitForAnyPress();
                Messages.printScreen(DETECTED_COLOR_SCREEN, GO_BACK_COLOR);
                Button.waitForAnyPress();
                Messages.printScreen(DETECTED_COLOR_SCREEN, SPACE_COLOR);
                Button.waitForAnyPress();
            } while (!Button.ESCAPE.isDown());
            Button.waitForAnyPress();
        } while (!Button.ESCAPE.isDown());

        System.exit(0);
    }

    public static void newScreen() {
        LCD.clear();
        LCD.drawString("****************", 0, 0);
        LCD.drawString("****************", 0, 7);
        for (int i = 1; i < 7; i++) {
            LCD.drawString("*", 0, i);
            LCD.drawString("*", 15, i);
        }
    }

    public static void sensorScreen() {
        newScreen();
        LCD.drawString("Sensor Type", 2, 1);
        LCD.drawString("NXT Sensor", 3, 3);
        LCD.drawString("HT Sensor", 3, 4);
    }

    public static void portScreen() {
        newScreen();
        LCD.drawString("Sensor Port", 2, 1);
        LCD.drawString("Port 1", 3, 3);
        LCD.drawString("Port 2", 3, 4);
        LCD.drawString("Port 3", 3, 5);
        LCD.drawString("Port 4", 3, 6);
    }

    public static void stockColorScreen() {
        newScreen();
        LCD.drawString("Stock Color", 2, 1);
        LCD.drawString("-On color", 2, 3);
        LCD.drawString("-Detector", 2, 4);
        LCD.drawString("-Stock C: D:", 2, 5);
        LCD.drawString("-End stock", 2, 6);
    }

    public static void backCommandScreen() {
        newScreen();
        LCD.drawString("Choose 2nd", 3, 1);
        LCD.drawString("color command", 1, 2);
        LCD.drawString("Go back", 3, 4);
        LCD.drawString("Stop walk", 3, 5);
    }

    public static void modeScreen() {
        newScreen();
        LCD.drawString("Choose MODE", 2, 2);
        LCD.drawString("Follow line", 3, 4);
        LCD.drawString("Detect color", 3, 5);
    }

    public static void colorNatureScreen() {
        newScreen();
        LCD.drawString("Choose color", 2, 1);
        LCD.drawString("nature", 5, 2);

        LCD.drawString("Dark color", 3, 4);
        LCD.drawString("Light color", 3, 5);
    }

    public static void detectedColorScreen() {
        newScreen();
        LCD.drawString("Detected", 4, 2);
        LCD.drawString("color is:", 4, 3);
        LCD.drawString("color", 6, 6);
    }

    public static void pressScreen() {
        newScreen();
        LCD.drawString("Press Key", 2, 1);
        LCD.drawString("To", 2, 4);
    }
}