import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.util.Delay;

public class ColorSensorThread extends Thread {
	
	public ColorSensor cs;
	public ColorDetector4 colorDetector;
	public int command = 0;
	public int nbr_colors = 1;
	public int[] result;
	public int id;
	public int chosen_color = 0;
	public int blank_color = 1;
	public int go_back_color = 2;
	
	public ColorSensorThread(ColorSensor cs, int[] result, int id, int nbr_colors, ColorDetector4 colorDetector){
		this.id = id;
		this.nbr_colors = nbr_colors;
		this.result = result;
		this.cs = cs;
		if(colorDetector != null){
			this.colorDetector = colorDetector;
		}else{
			this.colorDetector = new ColorDetector4(nbr_colors, cs);
		}
	}
	
	/**
	 * Pick color
	 * 
	 * @param chosen_color
	 * @return
	 */
	public boolean getPickedColor(int chosen_color){
		int nbr_cap = 3;
		int detected = 0;
		for(int i=0; i<nbr_cap; i++){
			if(colorDetector.testColor(cs.getColor(), chosen_color)){
				detected++;
			}
		}
//		System.out.println("Tp[" + id + "] = " + detected + " / " + nbr_cap);
		return (detected > (nbr_cap/2));
	}
	
	public void run(){
		int current_color = 0;
		do{
			/*if(getPickedColor(chosen_color) colorDetector.testColor(cs.getColor(), chosen_color)){
				result[id] = 1;
			}else if(colorDetector.testColor(cs.getColor(), blank_color)){
				result[id] = 0;
			}else if(colorDetector.testColor(cs.getColor(), go_back_color)){
				result[id] = 2;
			}else{
				result[id] = 0;
			}*/
			
			/*if(getPickedColor(chosen_color)){
				result[id] = 1;
			}else if(getPickedColor(blank_color)){
				result[id] = 0;
			}else if(getPickedColor(go_back_color)){
				result[id] = 2;
			}else{
				result[id] = 0;
			}*/
			
			current_color = colorDetector.getColorIndex(cs.getColor());
			if(current_color == colorDetector.FORWARD_COLOR){
				result[id] = 1;
			}else if(current_color == colorDetector.SPACE_COLOR){
				result[id] = 0;
			}else if(current_color == colorDetector.ROTATE_COLOR){
				result[id] = 2;
			}else{
				result[id] = 0;
			}
//			System.out.println("Thread Number " + id + " - result = " + result[id]);
		}while(!Button.ESCAPE.isDown());
	}
}
