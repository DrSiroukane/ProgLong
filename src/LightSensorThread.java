import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.addon.ColorHTSensor;

public class LightSensorThread extends Thread {
	
	public LightSensor ls;
	public LightDetector lightDetector;
	public int[] result;
	public int id;
	
	public LightSensorThread(LightSensor ls, int[] result, int id){
		this.id = id;
		this.result = result;
		this.ls = ls;
		this.lightDetector = new LightDetector(ls);
	}
	
	public void run(){
		do{
			if(lightDetector.isWhite()){
				result[id] = 1;
			}else{
				result[id] = 0;
			}
		}while(Button.ESCAPE.isDown());
	}
}
