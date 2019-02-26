import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class TouchSensor extends EV3TouchSensor
{
	public TouchSensor(Port port) {
		super(port);
	}
	
	//returns if touch sensor is pressed
	public boolean isPressed() {
		float [] sample = new float[1];
		fetchSample(sample, 0);
		return sample[0] != 0;
	}
	
}
