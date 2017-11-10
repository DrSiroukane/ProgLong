import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
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
public class ColorDetector2 {

	final static int RED = 0;
	final static int BLUE = 1;
	final static int GREEN = 2;
	
	final static int MIN = 0;
	final static int MAX = 1;
	final static int MED = 2;
	
	public static int[][][] list_colors ={
			{{500,500,500}, {0,0,0}, {0,0,0}}, // color 0
			{{500,500,500}, {0,0,0}, {0,0,0}}, // color 1
			{{500,500,500}, {0,0,0}, {0,0,0}}, // color 2
			{{500,500,500}, {0,0,0}, {0,0,0}}  // color 3
	};
	
	public static int[][][] median_calculator = new int [4][3][21];

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

	/**
	 * Test Color
	 * 
	 * @param  c   Color
	 * @param  min Min values for color
	 * @param  max Max values for color
	 * @return     check weather the color in the interval or not
	 */
	public static boolean testColor(Color c, int[] min, int[] max){
		if(min[RED] <= c.getRed() && c.getRed() <= max[RED]){
			if(min[BLUE] <= c.getBlue() && c.getBlue() <= max[BLUE]){
				if(min[GREEN] <= c.getGreen() && c.getGreen() <= max[GREEN]){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * return Min distance between min or max
	 * 
	 * @param x
	 * @param min
	 * @param max
	 * @return
	 */
	public static float minDistance1D(int x, int med){
		
		return Math.abs(med - x);
	}
	
	/**
	 * Calculate Min distance between color c and the selected color
	 * 
	 * @param c in color
	 * @param min mins interval
	 * @param max maxs interval
	 * @return
	 */
	public static float minDistance3D(Color c, int[] med){
		float r_dis, b_dis, g_dis;

		r_dis = minDistance1D(c.getRed(), med[RED]);
		b_dis = minDistance1D(c.getBlue(), med[BLUE]);
		g_dis = minDistance1D(c.getGreen(), med[GREEN]);
		
		return (float) Math.sqrt(r_dis*r_dis + b_dis*b_dis + g_dis*g_dis);
	}
	
	/**
	 * Update min and max interval for i color
	 * 
	 * @param i index of color
	 * @param c in color
	 */
	public static void updateColor(int i, Color c){
		int r = c.getRed();
		int b = c.getBlue();
		int g = c.getGreen();
		
		if(r < list_colors[i][MIN][RED]){
			list_colors[i][MIN][RED] = r;
		}else if(list_colors[i][MAX][RED] < r){
			list_colors[i][MAX][RED] = r;
		}
		
		if(b < list_colors[i][MIN][BLUE]){
			list_colors[i][MIN][BLUE] = b;
		}else if(list_colors[i][MAX][BLUE] < b){
			list_colors[i][MAX][BLUE] = b;
		}
		
		if(g < list_colors[i][MIN][GREEN]){
			list_colors[i][MIN][GREEN] = g;
		}else if(list_colors[i][MAX][GREEN] < g){
			list_colors[i][MAX][GREEN] = g;
		}
	}
	
	
	/**
	 * Main Program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ColorSensor cs = new ColorSensor(SensorPort.S3);
		Color in_color;
		int current_red, current_blue, current_green;
		
		displayFull("Start Color Detecter Program\nPress ENTER to start Color learning");
		Button.waitForAnyPress();

		do{
			if(!Button.ESCAPE.isDown()){
				/*
					start collect data about colors
				 */
				for(int i=0; i<4; i++){
					displayFull("Start stocking\ncolor number "+i);
					for(int j=0; j<21; j++){
						in_color = cs.getColor();
						current_red = in_color.getRed();
						current_blue = in_color.getBlue();
						current_green = in_color.getGreen();
						
						if(list_colors[i][MIN][RED] > current_red){
							list_colors[i][MIN][RED] = current_red;
						}
						if(list_colors[i][MIN][BLUE] > current_blue){
							list_colors[i][MIN][BLUE] = current_blue;
						}
						if(list_colors[i][MIN][GREEN] > current_green){
							list_colors[i][MIN][GREEN] = current_green;
						}
						if(list_colors[i][MAX][RED] < current_red){
							list_colors[i][MAX][RED] = current_red;
						}
						if(list_colors[i][MAX][BLUE] < current_blue){
							list_colors[i][MAX][BLUE] = current_blue;
						}
						if(list_colors[i][MAX][GREEN] < current_green){
							list_colors[i][MAX][GREEN] = current_green;
						}
						
						median_calculator[i][RED][j] = current_red;
						median_calculator[i][BLUE][j] = current_blue;
						median_calculator[i][GREEN][j] = current_green;
						
						Delay.msDelay(500);
					}
					
					Arrays.sort(median_calculator[i][RED]);
					Arrays.sort(median_calculator[i][BLUE]);
					Arrays.sort(median_calculator[i][GREEN]);
					
					list_colors[i][MED][RED] = median_calculator[i][RED][10];
					list_colors[i][MED][BLUE] = median_calculator[i][BLUE][10];
					list_colors[i][MED][GREEN] = median_calculator[i][GREEN][10];

					displayFull("MAX - MIN\n R = ["+list_colors[i][MIN][RED]+","+list_colors[i][MAX][RED]+"]\n B = ["+list_colors[i][MIN][BLUE]+","+list_colors[i][MAX][BLUE]+"]\n G = ["+list_colors[i][MIN][GREEN]+","+list_colors[i][MAX][GREEN]);
					Button.waitForAnyPress();
					displayFull("MED\n R = ["+list_colors[i][MED][RED]+"]\n B = ["+list_colors[i][MED][BLUE]+"]\n G = ["+list_colors[i][MED][GREEN]+"]\nPress any button to stock next color ...");
					Button.waitForAnyPress();
				}
				
				
				/*
					start detect color
				 */
				boolean color_detected;
				int i;
				do{
					displayFull("All Colors get stocked\nPress any Button to detecte Color ...");
					Button.waitForAnyPress();

					in_color = cs.getColor();
					displayFull("in Color values\n R = ["+in_color.getRed()+"]\n B = ["+in_color.getBlue()+"]\n G = ["+in_color.getGreen()+"]\n");
					Button.waitForAnyPress();

					
					
					/*
						start detect color from data
					 */
					color_detected = false;
					i=0;
					float[] min_distances;
					float min_value;
					int color_detected_number;
					while(!color_detected && i<4){
						if(testColor(in_color, list_colors[i][MIN], list_colors[i][MAX])){
							displayFull("Color detecte is \n'Color number "+i+"'");
							color_detected = true;
						}
						i++;
					}

					/*
						start detect color from guessing
					 */
					min_distances = new float[4];
					min_value = 1000;
					color_detected_number = -1;
					if(!color_detected){
						for(i=0; i<4; i++){
							min_distances[i] = minDistance3D(in_color, list_colors[i][MED]);
							if(min_value > min_distances[i]){
								min_value = min_distances[i];
								color_detected_number = i;
							}
						}

						displayFull("d(c0) = "+min_distances[0]+"\nd(c1) = "+min_distances[1]+"\nd(c2) = "+min_distances[2]+"\nd(c3) = "+min_distances[3]);
						Button.waitForAnyPress();

						color_detected = true;

						updateColor(color_detected_number, in_color); // update new detected values
						displayFull("Color detecte is \n'Color number "+color_detected_number+"'");
					}

					Button.waitForAnyPress();
					LCD.clear();
				}while(!Button.ESCAPE.isDown());		
			}
			
			displayFull("Press ESCAPE to exit !!");
			Button.waitForAnyPress();
		}while(!Button.ESCAPE.isDown());
	}
}