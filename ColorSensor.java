import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;

public class ColorSensor extends EV3ColorSensor
{


	public ColorSensor(Port port)
	{
		super(port);
		this.setCurrentMode("RGB");
		this.getRGBMode();
		setFloodlight(false);
	}

	public String getColorName()
	{
		//get rgb values
		int r = this.getRed();
		int g = this.getGreen();
		int b = this.getBlue();
		
		//sets colour name to unknown
		String colorName = "unknown";
			
		//set colour to white with following sensor restrictions
		if (r > 30 && g > 30 && b > 30)
		{
			colorName = "white";
		}
		//set colour to green with following sensor restrictions
		else if (r < 12 && g > 12)
		{
			colorName = "green";
		}
		//set colour to black with following sensor restrictions
		else if (r < 25 && g < 25 && b < 25)
		{
			colorName = "black";
		}
		//set colour to red with following sensor restrictions
		else if (r > 30 && g < 12 && b < 12)
		{
			colorName = "red";
		}
		
		//returns the colour name, if none of the colour apply - it will be "unknown"
		return colorName;
	}

	public boolean onBlack()
	{
		//returns true if colour is black
		String colorName = getColorName();
		if (colorName == "black")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean onWhite()
	{
		//returns true if colour is white
		String colorName = getColorName();
		if (colorName == "white")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean onGreen()
	{
		//returns true if colour is green
		String colorName = getColorName();
		if (colorName == "green")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean onRed()
	{
		//returns true if colour is red
		String colorName = getColorName();
		if (colorName == "red")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean onYellow()
	{
		//returns true if colour is yellow
		String colorName = getColorName();
		if (colorName == "yellow")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean onBlue()
	{
		//returns true if colour is blue
		String colorName = getColorName();
		if (colorName == "blue")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean knowsColor()
	{
		//returns true if colour is unknown
		String colorName = getColorName();
		if (colorName != "unknown")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int getRed()
	{
		//gets the red value via the sample value 0
		float[] sample = new float[3];
		this.fetchSample(sample, 0);
		int value = (int) (sample[0] * 255);
		return value;

	}

	public int getGreen()
	{
		//gets the red value via the sample value 1
		float[] sample = new float[3];
		this.fetchSample(sample, 0);
		int value = (int) (sample[1] * 255);
		return value;
	}

	public int getBlue()
	{
		//gets the red value via the sample value 2
		float[] sample = new float[3];
		this.fetchSample(sample, 0);
		int value = (int) (sample[2] * 255);
		return value;
	}

}
