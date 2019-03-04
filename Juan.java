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

public class Juan extends IRSensor implements RobotInterface
{
	private String currentDirection = "N";
	
	/**
	 * Used to send the maze back to the laptop
	 */
	public EV3Server = new EV3Server();
	
	public Juan(Port port)
	{
		super(port);
		// TODO Auto-generated constructor stub
	}

	//IR sensor to stay 8cm away from wall
	//spin motor to move IR sensor to detect in front of it 
	//wheel motors to go 30cm forwards and rotate 90/180 degrees to move
	
	public static void main(String[] args)
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
		MovePilot pilot = new MovePilot(chassis);

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

	public void MoveNorth()
	{
		// TODO Auto-generated method stub
		
	}

	public void MoveSouth()
	{
		// TODO Auto-generated method stub
		
	}

	public void MoveEast()
	{
		// TODO Auto-generated method stub
		
	}

	public void MoveWest()
	{
		// TODO Auto-generated method stub
		
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
		return null;
	}
	
}