import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightDetector {

	LightSensor sensor;
	double pickedValue;
	
	public LightDetector(LightSensor sensor) {
		this.sensor = sensor;
	}
	
	public boolean isWhite() {
		return (sensor.readNormalizedValue() > 510);
	}
	
	public static void main(String [] args) {
		
		LightDetector leftLightDetector = new LightDetector(new LightSensor(SensorPort.S3));
		
		while(true) {
			
			int value = leftLightDetector.sensor.readNormalizedValue();
			System.out.println(value);
			Button.waitForAnyPress();
		}
	}
	
}
