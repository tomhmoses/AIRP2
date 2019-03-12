import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;

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
	private ColorSensor RIGHT_COLOR;
	private Keys buttons;
	private static MovePilot	pilot;
	private ObjectOutputStream oOut;
	
	private int travelAmount = 40;
	private int turnAmount = 80;
	
	

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
	
		LCD.drawString("starting server...", 0, 0);
		
		try
		{
			setupServer();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LCD.drawString("Press my button,  ", 0, 0);
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

		RIGHT_COLOR = new ColorSensor(BRICK.getPort("S3"));
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
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "S")
		{
			pilot.rotate((turnAmount*2)+5);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "E")
		{
			pilot.rotate(-turnAmount);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "W")
		{
			pilot.rotate(turnAmount);
			pilot.travel(travelAmount);
		}

		currentDirection = "N";
	}

	public void MoveSouth()
	{
		if (currentDirection == "N")
		{
			pilot.rotate((turnAmount*2)+5);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "S")
		{
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "E")
		{
			pilot.rotate(turnAmount);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "W")
		{
			pilot.rotate(-turnAmount);
			pilot.travel(travelAmount);
		}

		currentDirection = "S";
	}

	public void MoveEast()
	{
		if (currentDirection == "N")
		{
			pilot.rotate(turnAmount);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "S")
		{
			pilot.rotate(-turnAmount);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "E")
		{
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "W")
		{
			pilot.rotate((turnAmount*2)+5);
			pilot.travel(travelAmount);
		}

		currentDirection = "E";

	}

	public void MoveWest()
	{
		if (currentDirection == "N")
		{
			pilot.rotate(-turnAmount);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "S")
		{
			pilot.rotate(turnAmount);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "E")
		{
			pilot.rotate((turnAmount*2)+5);
			pilot.travel(travelAmount);
		}
		else if (currentDirection == "W")
		{
			pilot.travel(travelAmount);
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
		LeftDistance = IR_SENSOR.getDistance();
		SPIN_MOTOR.rotate(90);
		ForwardDistance = IR_SENSOR.getDistance();
		SPIN_MOTOR.rotate(90);
		RightDistance = IR_SENSOR.getDistance();
		SPIN_MOTOR.rotate(-90);
		
		LCD.clear();
		LCD.drawString("distances:", 0, 0);
		LCD.drawString("left: " + LeftDistance.toString(), 0, 1);
		LCD.drawString("forward: " + ForwardDistance.toString(), 0, 2);
		LCD.drawString("right: " + RightDistance.toString(), 0, 3);
		LCD.drawString("current Dir: " + currentDirection, 0, 5);
		
		LCD.drawString("waiting for button press:", 0, 6);
		//buttons.waitForAnyPress();
		LCD.drawString("                         ", 0, 6);
		
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
		LCD.clear();
		LCD.drawString("dist array: NESW", 0, 0);
		String temp;
		for (int i = 0; i<4; i++) {
			if (distArray[i] == null)
			{
				temp = "null";
			}
			else
			{
				temp = distArray[i].toString();
			}
			LCD.drawString(temp, 0, i + 1);
		}
		LCD.drawString("waiting for button press:", 0, 6);
		//buttons.waitForAnyPress();
		LCD.drawString("                         ", 0, 6);
		
		
		Boolean[] boolArray = new Boolean[4];
		LCD.clear();
		LCD.drawString("Walls: NESW", 0, 0);
		for (int i = 0; i<4; i++) {
			if (distArray[i] == null)
			{
				boolArray[i] = null;
			}
			else if (distArray[i] < 30) {
				boolArray[i] = true;
			}
			else 
			{
				boolArray[i] = false;
			}
			if (boolArray[i] == null)
			{
				temp = "null";
			}
			else
			{
				temp = boolArray[i].toString();
			}
			LCD.drawString(temp, 0, i + 1);
		}
		LCD.drawString("waiting for button press:", 0, 6);
		//buttons.waitForAnyPress();
		LCD.drawString("                         ", 0, 6);
		return boolArray;
	}
	
	private void setupServer() throws IOException
	{
		
		ServerSocket server = new ServerSocket(1245);
		LCDOut("Awaiting client..", 0);
		Socket client = server.accept();
		LCDOut("Connected", 1);
		OutputStream out = client.getOutputStream();
		oOut = new ObjectOutputStream(out);
		DataOutputStream dOut = new DataOutputStream(out);
		//dOut.writeUTF("Battery: " + Battery.getVoltage());
		dOut.flush();
		//server.close();
	}
	

	@Override
	public void send(Object obj)
	{
		try
		{
			oOut.writeObject(obj);
			oOut.flush();
			oOut.reset();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void drawMazeOnLCD(Maze maze)
	{
		Cell cell;
		int xDraw;
		int yDraw;
		final int xMod = 65;
		final int yMod = 50;
		final int BLACK = 1;
		final int WHITE = 0;
		final int SCALE = 10;
		
		//should be top left
		LCD.setPixel(2, 2, BLACK);
		
		for (int x = 0; x <= maze.width; x++) {
			for (int y = 0; y <= maze.height; y++) {
				cell = maze.layout[x][y];
				xDraw = (x*SCALE) + xMod;
				yDraw = (y*SCALE) + yMod;
				LCD.setPixel(xDraw + SCALE - 1, yDraw + SCALE - 1, BLACK);
				if (cell.type == "edge")
				{
					for (int i = 0; i < SCALE; i++)
					{
						for (int j = 0; j < SCALE; j++)
						{
							LCD.setPixel(xDraw + i, yDraw + j, BLACK);
						}
					}
				}
				else
				{
					if (cell.SWall != null)
					{
						if (cell.SWall == true)
						{
							for (int i = 0; i < SCALE - 1; i++)
							{
								LCD.setPixel(xDraw + i, yDraw + SCALE - 1, BLACK);
							}
						}
					}
					if (cell.EWall != null)
					{
						if (cell.EWall == true)
						{
							for (int i = 0; i < SCALE - 1; i++)
							{
								LCD.setPixel(xDraw + SCALE - 1, yDraw + i, BLACK);
							}
						}
					}
				}
			}
		}
		//draw border right and bottom
		for (int i = 0; i < SCALE * (maze.width + 1); i++)
		{
			for (int j = 0; j < SCALE; j++)
			{
				LCD.setPixel(xMod + i, yMod + (SCALE * (maze.height + 1)) + j, BLACK);
			}
		}
		for (int i = 0; i < SCALE; i++)
		{
			for (int j = 0; j < SCALE* (maze.height + 2); j++)
			{
				LCD.setPixel(xMod + (SCALE * (maze.width + 1)) + i, yMod + j, BLACK);
			}
		}
	}
	
}
