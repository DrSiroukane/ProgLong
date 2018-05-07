import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.Color;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p> A program that makes a robot learn about colors then detect them for ColorHTSensor </p>
 */
public class ColorHTDetector extends ColorDetectorM {
    public ColorHTSensor cs;

    public ColorHTDetector(int nbr_colors, ColorHTSensor cs) {
        this.nbr_colors = nbr_colors;
        this.cs = cs;
    }

    @Override
    public Color getColor() {
        return cs.getColor();
    }

    @Override
    public void config(int colorType, int mode) {
        if (colorType == DARK_COLOR) {
            errors[0] = 10;
            errors[1] = 10;
        } else {
            errors[0] = 15;
            errors[1] = 15;
        }

        this.mode = mode;
    }
}