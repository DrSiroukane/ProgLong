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
		do{
			if(/*getPickedColor(chosen_color)*/ colorDetector.testColor(cs.getColor(), chosen_color)){
				result[id] = 1;
			}else{
				result[id] = 0;
			}
//			System.out.println("Thread Number " + id + " - result = " + result[id]);
		}while(!Button.ESCAPE.isDown());
	}
}
