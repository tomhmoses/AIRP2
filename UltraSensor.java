import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;

public class UltraSensor extends EV3UltrasonicSensor
{
	
	public UltraSensor(Port port) {
		super(port);
	}
	
	//get distance of ultra sensor to object
	public double getDistance() 
	{
		float[] sample = new float[sampleSize()];
		fetchSample(sample,0);
		return sample[0];
	
	}
}