import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;

public class IRSensor extends EV3IRSensor
{
	
	public IRSensor(Port port) {
		super(port);
	}
	
	//get distance of IR sensor to object
	public double getDistance() 
	{
		float[] sample = new float[sampleSize()];
		fetchSample(sample,0);
		return sample[0];
	
	}
}