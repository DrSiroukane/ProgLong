import lejos.nxt.ColorSensor;
import lejos.robotics.Color;

/**
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * <p> A program that makes a robot learn about colors then detect them for ColorSensor </p>
 */
public class ColorDetector extends ColorDetectorM {
    public ColorSensor cs;

    public ColorDetector(int nbr_colors, ColorSensor cs) {
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
            errors[0] = 30;
            errors[1] = 30;
        } else {
            errors[0] = 70;
            errors[1] = 30;
        }

        this.mode = mode;
    }
}