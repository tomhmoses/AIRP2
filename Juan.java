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

public class Juan implements RobotInterface
{
	private String				currentDirection	= "N";
	
	private EV3LargeRegulatedMotor SPIN_MOTOR;
	private EV3LargeRegulatedMotor LEFT_MOTOR;
	private EV3LargeRegulatedMotor RIGHT_MOTOR;
	private EV3 BRICK;
	private IRSensor IR_SENSOR;
	private ColorSensor LEFT_COLOR;
	private ColorSensor RIGHT_COLOR;
	private TouchSensor TOUCH;
	private Keys buttons;
	private static MovePilot	pilot;
	
	

	//IR sensor to stay 8cm away from wall
	//spin motor to move IR sensor to detect in front of it 
	//wheel motors to go 30cm forwards and rotate 90/180 degrees to move

	public Juan()
	{
		SPIN_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
		LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.B);
		RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.C);

		BRICK = (EV3) BrickFinder.getLocal();

		Sound.beepSequenceUp();

		LCD.drawString("Press my button,", 0, 0);
		LCD.drawString("turn me on ;)", 0, 1);
		LCD.drawString("vJuan.3.31.9", 0, 2);

		buttons = BRICK.getKeys();
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

		LEFT_COLOR = new ColorSensor(BRICK.getPort("S2"));
		RIGHT_COLOR = new ColorSensor(BRICK.getPort("S3"));
		TOUCH = new TouchSensor(BRICK.getPort("S1"));
		IR_SENSOR = new IRSensor(BRICK.getPort("S4"));

		double SPEED = 10;
		pilot.setLinearSpeed(SPEED);
		pilot.setAngularSpeed(SPEED * SPEED);

		LCD.clear();
		//Sound.setVolume(1);
	}
	
	public void LCDOut(String string, int line) {
		LCD.drawString(string, 0, line);
	}

	public void MoveNorth()
	{
		//take into account what direction it is facing and then rotate how much to move forwards
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

	@Override
	public Boolean reachedGoal()
	{
		return RIGHT_COLOR.onRed();
	}

	@Override
	public Boolean[] getCurrentWalls()
	{
		Double RightDistance = null;
		Double ForwardDistance = null;
		Double LeftDistance = null;
		
		SPIN_MOTOR.rotate(-90);
		RightDistance = IR_SENSOR.getDistance();
		SPIN_MOTOR.rotate(90);
		ForwardDistance = IR_SENSOR.getDistance();
		SPIN_MOTOR.rotate(90);
		LeftDistance = IR_SENSOR.getDistance();
		SPIN_MOTOR.rotate(-90);
		
		Double NorthDistance = null;
		Double EastDistance = null;
		Double SouthDistance = null;
		Double WestDistance = null;
		
		if (currentDirection == "N")
		{
			NorthDistance = ForwardDistance;
			EastDistance = RightDistance;
			SouthDistance = null;
			WestDistance = LeftDistance;
			
		}
		else if (currentDirection == "E")
		{
			NorthDistance = LeftDistance;
			EastDistance = ForwardDistance;
			SouthDistance = RightDistance;
			WestDistance = null;
		}
		else if (currentDirection == "S")
		{
			NorthDistance = null;
			EastDistance = LeftDistance;
			SouthDistance = ForwardDistance;
			WestDistance = RightDistance;
		}
		else if (currentDirection == "W")
		{
			NorthDistance = RightDistance;
			EastDistance = null;
			SouthDistance = LeftDistance;
			WestDistance = ForwardDistance;
		}

		
		Double[] distArray = new Double[] {NorthDistance, EastDistance, SouthDistance, WestDistance};
		Boolean[] boolArray = new Boolean[4];
		
		for (int i = 0; i<4; i++) {
			if (distArray[i] > 0) {
				boolArray[i] = true;
			} else {
				boolArray[i] = false;
			}
		}
		
		return boolArray;
	}
}
