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

public class Juan implements RobotInterface
{
	private String				currentDirection	= "N";
	private static MovePilot	pilot;

	public Juan(Port port)
	{
		super();
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
	}

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

	@Override
	public Boolean reachedGoal()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean[] getCurrentWalls()
	{
		// TODO Auto-generated method stub
		EV3 BRICK = (EV3) BrickFinder.getLocal();
		EV3LargeRegulatedMotor SPIN_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
		IRSensor IR_SENSOR = new IRSensor(BRICK.getPort("S4"));
		
		Double NorthDistance = null;
		Double EastDistance = null;
		Double SouthDistance = null;
		Double WestDistance = null;

		if (currentDirection == "N")
		{
			NorthDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			EastDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			SouthDistance = null;
			SPIN_MOTOR.rotate(90);
			WestDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
		}
		else if (currentDirection == "E")
		{
			EastDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			SouthDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			WestDistance = null;
			SPIN_MOTOR.rotate(90);
			NorthDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
		}
		else if (currentDirection == "S")
		{
			SouthDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			WestDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			NorthDistance = null;
			SPIN_MOTOR.rotate(90);
			EastDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
		}
		else if (currentDirection == "W")
		{
			WestDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			NorthDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
			EastDistance = null;
			SPIN_MOTOR.rotate(90);
			SouthDistance = IR_SENSOR.getDistance();
			SPIN_MOTOR.rotate(90);
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
