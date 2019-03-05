import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Juan extends IRSensor
{
	private String currentDirection = "N";
	private static MovePilot pilot;
	
	public Juan(Port port)
	{
		super(port);
		// TODO Auto-generated constructor stub
	}

	//IR sensor to stay 8cm away from wall
	//spin motor to move IR sensor to detect in front of it 
	//wheel motors to go 30cm forwards and rotate 90/180 degrees to move
	
	public static void main(String[] args) throws IOException
	{
		@SuppressWarnings("resource")
		EV3LargeRegulatedMotor SPIN_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.C);

		EV3 BRICK = (EV3) BrickFinder.getLocal();

		Sound.beepSequenceUp();

		LCD.drawString("Press my button,", 0, 0);
		LCD.drawString("turn me on ;)", 0, 1);
		LCD.drawString("vJuan.3.31.9", 0, 2);

		Keys buttons = BRICK.getKeys();
		Button.LEDPattern(2);
		buttons.waitForAnyPress();
		Button.LEDPattern(3);

		LCD.clear();
		LCD.drawString("This is working", 0, 0);

		//wheel offset from centre
		double offset = 7.3;

		Wheel LEFT_WHEEL = WheeledChassis.modelWheel(LEFT_MOTOR, 5.6).offset(-offset);
		Wheel RIGHT_WHEEL = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.6).offset(offset);

		Chassis chassis = new WheeledChassis(new Wheel[] { LEFT_WHEEL, RIGHT_WHEEL }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);

		@SuppressWarnings("resource")
		ColorSensor LEFT_COLOR = new ColorSensor(BRICK.getPort("S2"));
		@SuppressWarnings("resource")
		ColorSensor RIGHT_COLOR = new ColorSensor(BRICK.getPort("S3"));
		@SuppressWarnings("resource")
		TouchSensor TOUCH = new TouchSensor(BRICK.getPort("S1"));
		@SuppressWarnings("resource")
		IRSensor IR_SENSOR = new IRSensor(BRICK.getPort("S4"));

		double SPEED = 10;
		pilot.setLinearSpeed(SPEED);
		pilot.setAngularSpeed(SPEED * SPEED);

		LCD.clear();
		Sound.setVolume(1);

		while (buttons.getButtons() != Keys.ID_ESCAPE)
		{
			SPIN_MOTOR.rotate(90);
			Delay.msDelay(180);
			LCD.drawString(Double.toString(IR_SENSOR.getDistance()), 0, 1);
			
			SPIN_MOTOR.rotate(-90);
			Delay.msDelay(180);
			LCD.drawString(Double.toString(IR_SENSOR.getDistance()), 0, 2);
			
			SPIN_MOTOR.rotate(-90);
			Delay.msDelay(180);
			LCD.drawString(Double.toString(IR_SENSOR.getDistance()), 0, 3);
			
			SPIN_MOTOR.rotate(90);
			Delay.msDelay(180);
			LCD.drawString(Double.toString(IR_SENSOR.getDistance()), 0, 4);
		}
	}
	
	/*final int port = 1234;
	DataOutputStream dOut;
	ObjectOutputStream oOut;
	
	ServerSocket server = new ServerSocket(port);
	LCD.drawString("Awaiting client..", 0, 5);
	Socket client = server.accept();
	LCD.clear();
	LCD.drawString("CONNECTED", 0, 5);
	OutputStream out = client.getOutputStream();
	dOut = new DataOutputStream(out);
	// dOut.writeUTF("Battery: " + Battery.getVoltage());
	// dOut.flush();server.close();
	oOut = new ObjectOutputStream(out);
	
	}
	
	public void sendUTF(String string) throws IOException
	{
		DataOutputStream dOut = null;
		dOut.writeUTF(string);
	}
	
	public void sendObj(ObjectOutputStream obj) throws IOException
	{
		oOut.writeObject(obj);
	}*/

	public void MoveNorth()
	{
		//take into account what direction it is facing and then rotate how much to move forwards
		// TODO Auto-generated method stub
		if (currentDirection == "N") 
		{
			pilot.travel(30);
		}
		else if (currentDirection == "S")
		{
			pilot.rotate(180);
			pilot.travel(30);
		}
		else if (currentDirection == "E")
		{
			pilot.rotate(-90);
			pilot.travel(30);
		}
		else if (currentDirection == "W")
		{
			pilot.rotate(90);
			pilot.travel(30);
		}
		
		currentDirection = "N";
	}

	public void MoveSouth()
	{
		// TODO Auto-generated method stub
		if (currentDirection == "N") 
		{
			pilot.rotate(180);
			pilot.travel(30);
		}
		else if (currentDirection == "S")
		{
			pilot.travel(30);
		}
		else if (currentDirection == "E")
		{
			pilot.rotate(90);
			pilot.travel(30);
		}
		else if (currentDirection == "W")
		{
			pilot.rotate(-90);
			pilot.travel(30);
		}
		
		currentDirection = "S";
	}

	public void MoveEast()
	{
		// TODO Auto-generated method stub
		if (currentDirection == "N") 
		{
			pilot.rotate(-90);
			pilot.travel(30);
		}
		else if (currentDirection == "S")
		{
			pilot.rotate(90);
			pilot.travel(30);
		}
		else if (currentDirection == "E")
		{
			pilot.travel(30);
		}
		else if (currentDirection == "W")
		{
			pilot.rotate(180);
			pilot.travel(30);
		}
		
		currentDirection = "E";
		
	}

	public void MoveWest()
	{
		// TODO Auto-generated method stub
		if (currentDirection == "N") 
		{
			pilot.rotate(90);
			pilot.travel(30);
		}
		else if (currentDirection == "S")
		{
			pilot.rotate(-90);
			pilot.travel(30);
		}
		else if (currentDirection == "E")
		{
			pilot.rotate(180);
			pilot.travel(30);
		}
		else if (currentDirection == "W")
		{
			pilot.travel(30);
		}
		
		currentDirection = "W";
	}
	
}
