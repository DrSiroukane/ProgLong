import lejos.nxt.Button;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p> thread help to detect color and stock its result </p>
 */
public class ColorDetectorThread extends Thread {
    public ColorDetectorM colorDetector;
    public int[] result;
    public int id;

    public ColorDetectorThread(int[] result, int id, ColorDetectorM colorDetector) {
        this.id = id;
        this.result = result;
        this.colorDetector = colorDetector;
    }

    @Override
    public void run() {
        do {
            result[id] = colorDetector.getCurrentColor(); // get detected color
        } while (!Button.ESCAPE.isDown());
    }
}
