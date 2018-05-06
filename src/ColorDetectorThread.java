import lejos.nxt.*;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p> thread help to detect color and stock it F </p>
 */
public class ColorDetectorThread extends Thread{
    public ColorDetectorMain colorDetector;
    public int[] result;
    public int id;

    public ColorDetectorThread(int[] result, int id, ColorDetectorMain colorDetector){
        this.id = id;
        this.result = result;
        this.colorDetector = colorDetector;
    }

    @Override
    public void run(){
        do{
            result[id] = colorDetector.getCurrentColor(); // get detected color
        }while(!Button.ESCAPE.isDown());
    }
}
