import lejos.nxt.Button;
import lejos.nxt.addon.ColorHTSensor;
import lejos.util.Delay;

public class ColorHTSensorThread extends Thread {
	
	public ColorHTSensor cs;
	public ColorHTDetector colorHTDetector;
	public int command = 0;
	public int nbr_colors = 1;
	public int[] result;
	public int id;
	public int chosen_color = 0;
	
	public ColorHTSensorThread(ColorHTSensor cs, int[] result, int id, int nbr_colors, ColorHTDetector colorHTDetector){
		this.id = id;
		this.nbr_colors = nbr_colors;
		this.result = result;
		this.cs = cs;
		if(colorHTDetector != null){
			this.colorHTDetector = colorHTDetector;
		}else{
			this.colorHTDetector = new ColorHTDetector(nbr_colors, cs);
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
			if(colorHTDetector.testColor(cs.getColor(), chosen_color)){
				detected++;
			}
		}
//		System.out.println("Tp[" + id + "] = " + detected + " / " + nbr_cap);
		return (detected > (nbr_cap/2));
	}
	
	public void run(){		
		do{
			if(/*getPickedColor(chosen_color)*/ colorHTDetector.testColor(cs.getColor(), chosen_color)){
				result[id] = 1;
			}else{
				result[id] = 0;
			}
//			System.out.println("Thread Number " + id + " - result = " + result[id]);
		}while(!Button.ESCAPE.isDown());
	}
}
