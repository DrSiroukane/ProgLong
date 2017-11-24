import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.robotics.Color;
import lejos.util.Delay;
import java.util.*;
import java.lang.Math;


/**
 * 
 * @author Slimane SIROUKANE
 * @author Thomas GUIGNARD
 *
 * A program that makes a robot learn about colors then detect them
 *
 */
public class ColorDetector3 {

	final static int RED = 0;
	final static int BLUE = 1;
	final static int GREEN = 2;
	
	public int nbr_colors;
	public int[][] list_colors;
	public ColorSensor cs;
	public int error_avg;

	public ColorDetector3(int nbr_colors, ColorSensor cs){
		this.nbr_colors = nbr_colors;
		list_colors = new int[nbr_colors][3];
		this.cs = cs;
		error_avg = 30;
	}

	/**
	 * Method that stock red, blue and green value of each color focusing on median value
	 */
	public void createMedOfColors(){
		int[][] color_data = new int[3][21];
		Color in_color;
		int current_red, current_blue, current_green;
		Button.waitForAnyPress();
		for(int i=0; i<nbr_colors; i++){
			displayFull("Start stocking\nColor number "+i);
			for(int j=0; j<21; j++){
				in_color = cs.getColor();
				current_red = in_color.getRed();
				current_blue = in_color.getBlue();
				current_green = in_color.getGreen();

				color_data[RED][j] = current_red;
				color_data[BLUE][j] = current_blue;
				color_data[GREEN][j] = current_green;

				Delay.msDelay(500);
			}

			Arrays.sort(color_data[RED]);
			Arrays.sort(color_data[BLUE]);
			Arrays.sort(color_data[GREEN]);

			list_colors[i][RED] = color_data[RED][10];
			list_colors[i][BLUE] = color_data[BLUE][10];
			list_colors[i][GREEN] = color_data[GREEN][10];

			displayFull("MED\n R = ["+list_colors[i][RED]+"]\n B = ["+list_colors[i][BLUE]+"]\n G = ["+list_colors[i][GREEN]+"]\nPress any button to stock next color ...");
			Button.waitForAnyPress();
		}
	}
	
	/**
	 * return distance between x or y
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float minDistance1D(int x, int y){
		return Math.abs(y - x);
	}

	/**
	 * Calculate Min distance between color c and the selected color
	 * 
	 * @param c in color
	 * @param color_number index of color to test
	 * @return
	 */
	public float minDistance3D(Color c, int color_number){
		float r_dis, b_dis, g_dis;

		r_dis = minDistance1D(c.getRed(), list_colors[color_number][RED]);
		b_dis = minDistance1D(c.getBlue(), list_colors[color_number][BLUE]);
		g_dis = minDistance1D(c.getGreen(), list_colors[color_number][GREEN]);

		return (float) Math.sqrt(r_dis*r_dis + b_dis*b_dis + g_dis*g_dis);
	}

	/**
	 * Test Color
	 * 
	 * @param  c   Color
	 * @param  color_number index of color to test with c
	 * @return     check weather the color can be the same of color_number
	 */
	public boolean testColor(Color c, int color_number){
		return (minDistance3D(c,color_number) < error_avg);
	}
	
	
	/**
	 * Display only the phrase on screen
	 * 
	 * @param phrase
	 */
	public static void displayFull(String phrase){
		LCD.clear();
		LCD.drawString(phrase, 0, 0);
	}
	
	/**
	 * Add phrase to display
	 * 
	 * @param phrase
	 */
	public static void println(String phrase){
		System.out.println(phrase);
	}
}