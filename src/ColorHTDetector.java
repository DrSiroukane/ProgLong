import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.addon.ColorHTSensor;
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
public class ColorHTDetector {

	final static int RED = 0;
	final static int BLUE = 1;
	final static int GREEN = 2;
	
	final static int MIN = 0;
	final static int MAX = 1;
	final static int MED = 2;
	
	final static int FORWARD_COLOR = 0;
	final static int SPACE_COLOR = 1;
	final static int ROTATE_COLOR = 2;
	
	public int nbr_colors;
	public int[][][] list_colors;
	public ColorHTSensor cs;
	public int error_avg;
	
	public int nbr_tries = 31;
	public int[][] color_data = new int[3][nbr_tries];
	public Color in_color;
	public int current_red, current_blue, current_green;

	public ColorHTDetector(int nbr_colors, ColorHTSensor cs){
		this.nbr_colors = nbr_colors;
		list_colors = new int[nbr_colors][3][3];
		for(int i=0; i<nbr_colors; i++){
			for(int j=0; j<3; j++){
				list_colors[i][MIN][j] = 500;
				list_colors[i][MED][j] = 0;
				list_colors[i][MAX][j] = 0;
			}
			
		}
		
		this.cs = cs;
		error_avg = 25;
	}

	/**
	 * Method that stock red, blue and green value of each color focusing on median value
	 */
	public void stockColor(int i){
//		displayFull("ColorHTDetector Stocking color number "+ i);
//		Button.waitForAnyPress();
		
		for(int j=0; j<nbr_tries; j++){
			in_color = cs.getColor();
			current_red = in_color.getRed();
			current_blue = in_color.getBlue();
			current_green = in_color.getGreen();
			
			if(current_red < list_colors[i][MIN][RED]){
				list_colors[i][MIN][RED] = current_red;
			}else if(list_colors[i][MAX][RED] < current_red){
				list_colors[i][MAX][RED] = current_red;
			}
			
			if(current_blue < list_colors[i][MIN][BLUE]){
				list_colors[i][MIN][BLUE] = current_blue;
			}else if(list_colors[i][MAX][BLUE] < current_blue){
				list_colors[i][MAX][BLUE] = current_blue;
			}
			
			if(current_green < list_colors[i][MIN][GREEN]){
				list_colors[i][MIN][GREEN] = current_green;
			}else if(list_colors[i][MAX][GREEN] < current_green){
				list_colors[i][MAX][GREEN] = current_green;
			}

			color_data[RED][j] = current_red;
			color_data[BLUE][j] = current_blue;
			color_data[GREEN][j] = current_green;

//			Delay.msDelay(50);
		}

		Arrays.sort(color_data[RED]);
		Arrays.sort(color_data[BLUE]);
		Arrays.sort(color_data[GREEN]);

		int med_index = (nbr_tries - 1) / 2;
		
		list_colors[i][MED][RED] = color_data[RED][med_index];
		list_colors[i][MED][BLUE] = color_data[BLUE][med_index];
		list_colors[i][MED][GREEN] = color_data[GREEN][med_index];

		displayFull("MAX - MIN\n R=["+list_colors[i][MIN][RED]+","+list_colors[i][MAX][RED]+"]\n B=["+list_colors[i][MIN][BLUE]+","+list_colors[i][MAX][BLUE]+"]\n G=["+list_colors[i][MIN][GREEN]+","+list_colors[i][MAX][GREEN]);
		Button.waitForAnyPress();
		
		displayFull("MED\n R=["+list_colors[i][MED][RED]+"]\n B=["+list_colors[i][MED][BLUE]+"]\n G=["+list_colors[i][MED][GREEN]+"]\nPress any button to continue ...");
		Button.waitForAnyPress();
	}
	
	/**
	 * Method that stock red, blue and green value of each color focusing on median value
	 */
	public void createMedOfColors(){
		Button.waitForAnyPress();
		for(int i=0; i<nbr_colors; i++){
			displayFull("Start stocking\nColor number "+i);
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
	public float minDistance1D(int x, int y){
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
	public static float minDistance1D(int x, int min, int max){
		if(0 < (x - min)){
			return x - min;
		}else if(0 < max - x){
			return max - x;
		}
		
		return 0;
	}

	/**
	 * Calculate Min distance between color c and the selected color
	 * 
	 * @param c in color
	 * @param color_number index of color to test
	 * @return
	 */
	public float minDistance3D(Color c, int color_number, int obj){
		float r_dis, b_dis, g_dis;

		if(obj == MED){
			r_dis = minDistance1D(c.getRed(), list_colors[color_number][MED][RED]);
			b_dis = minDistance1D(c.getBlue(), list_colors[color_number][MED][BLUE]);
			g_dis = minDistance1D(c.getGreen(), list_colors[color_number][MED][GREEN]);
		}else{
			r_dis = minDistance1D(c.getRed(), list_colors[color_number][MIN][RED], list_colors[color_number][MAX][RED]);
			b_dis = minDistance1D(c.getBlue(), list_colors[color_number][MIN][BLUE], list_colors[color_number][MAX][BLUE]);
			g_dis = minDistance1D(c.getGreen(), list_colors[color_number][MIN][GREEN], list_colors[color_number][MAX][GREEN]);
		}
		
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
//		System.out.print(minDistance3D(c, color_number, MED));
		return (
				(testColorMinMax(c, color_number)) ||
				(minDistance3D(c, color_number, MED) < error_avg) || 
				(minDistance3D(c, color_number, MIN) < error_avg)
				);
	}
	
	/**
	 * Test Color MIN MAX
	 * 
	 * @param  c   Color
	 * @param  min Min values for color
	 * @param  max Max values for color
	 * @return     check weather the color in the interval or not
	 */
	public boolean testColorMinMax(Color c, int color_number){
		if(list_colors[color_number][MIN][RED] <= c.getRed() && c.getRed() <= list_colors[color_number][MAX][RED]){
			if(list_colors[color_number][MIN][BLUE] <= c.getBlue() && c.getBlue() <= list_colors[color_number][MAX][BLUE]){
				if(list_colors[color_number][MIN][GREEN] <= c.getGreen() && c.getGreen() <= list_colors[color_number][MAX][GREEN]){
					return true;
				}
			}
		}

		return false;
	}
	

	/**
	 * Method to detect closest color
	 * @param c
	 * @return
	 */
	public int getCurrentColor(Color c){
		if(testColor(c, FORWARD_COLOR)){
			return FORWARD_COLOR;
		}
		
		if(testColor(c, SPACE_COLOR)){
			return SPACE_COLOR;
		}
		
		if(testColor(c, ROTATE_COLOR)){
			return ROTATE_COLOR;
		}
		
		return SPACE_COLOR;
	}
	
	/**
	 * Method to detect closest color
	 * @param c
	 * @return
	 */
	public int getColorIndex(Color c){
		int detected_color = 0;
		float min = minDistance3D(c,0, MED);
		float d;
		for(int i=1; i<nbr_colors; i++){
			d = minDistance3D(c,i, MED);
			if(d < min){
				min = d;
				detected_color = i;
			}
		}
		
		return detected_color;
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