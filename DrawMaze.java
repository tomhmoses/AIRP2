import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

class MazeCanvas extends JPanel implements Runnable
{
	int		width, height;
	int		rows;
	int		columns;
	Maze	maze;

	public MazeCanvas(int w, int h, Maze maze)
	{
		this.rows = maze.height;
		this.columns = maze.width;
		this.maze = maze;
		this.setPreferredSize(new Dimension(1920 / 2, 1080 / 2));
		setSize(width = this.rows * 100, height = this.height * 100);
		Thread runThread = new Thread(this);
		runThread.start();
	}

	public void paint(Graphics g)
	{
		
		Color[] colors = { Color.black, Color.blue, Color.red, Color.green, Color.orange, Color.MAGENTA };
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		
		width = getSize().width;
		height = getSize().height;

		// set values
		int cellHeight = height / rows;
		int cellWidth = width / columns;
		
		Cell cell;
		
		//draw each cell background
		Color pRed = new Color(255,189,189);
		Color pGreen = new Color(225,247,213);
		Color pPink = new Color(241,203,255);
		Color pBlue = new Color(201,201,255);
		Color robotColor = new Color(76,76,255);
		for (int x = 0; x <= maze.width; x++) {
			for (int y = 0; y <= maze.height; y++) {
				cell = maze.layout[x][y];
				if (cell.type.equals("edge")) {
					g.setColor(pPink);
				}
				else
				{
					if (cell.visited) {
						g.setColor(pGreen);
					}
					else if (cell.type.equals("planned")) {
						g.setColor(pBlue);
					}
					else {
						g.setColor(Color.white);
					}
				}
				g.fillRect((x-1)*cellWidth,(y-1)*cellHeight,(x)*cellWidth,(y)*cellHeight);
			}
		}
		
		Boolean drawLine = true;
		// draw each cell lines
		for (int x = 0; x <= maze.width; x++) {
			for (int y = 0; y <= maze.height; y++) {
				cell = maze.layout[x][y];
				//set East Wall colour
				if (cell.EWall == null) {
					g.setColor(Color.gray);
					drawLine = true;
				}
				else if (cell.EWall == true) {
					g.setColor(Color.black);
					drawLine = true;
				}
				else if (cell.EWall == false) {
					drawLine = false;
				}
				//draw East Wall
				if (y != 0 && drawLine) {
					g.drawLine((x)*cellWidth, (y-1)*cellHeight, (x)*cellWidth, (y)*cellHeight);
				}
				
				//set South Wall colour
				if (cell.SWall == null) {
					g.setColor(Color.gray);
					drawLine = true;
				}
				else if (cell.SWall == true) {
					g.setColor(Color.black);
					drawLine = true;
				}
				else if (cell.SWall == false) {
					drawLine = false;
				}
				//draw South Wall
				if (x != 0 && drawLine) {
					g.drawLine((x-1)*cellWidth, (y)*cellHeight, (x)*cellWidth, (y)*cellHeight);
				}
			}
			
			//draw robot
			g.setColor(robotColor);
			int x1 = (int) Math.round((maze.robotPos.x - 1 + 0.25)*cellWidth);
			int y1 = (int) Math.round((maze.robotPos.y - 1 + 0.25)*cellHeight);
			g.fillOval(x1, y1, cellWidth/2, cellHeight/2);
		}
	}

	@Override
	public void run()
	{
		while(true) {
			this.repaint();
		}
	}
}

public class DrawMaze extends JFrame
{
	public DrawMaze(Maze maze)
	{
		MazeCanvas mazeCanvas = new MazeCanvas(800, 800, maze);
		add(mazeCanvas);
		pack();
	}
	
	private static void MoveInMazeDemo() {
		Maze maze = new Maze(9, 6);
		
		maze.setCurrentWall("N", false);
		maze.setCurrentWall("E", true);
		maze.robotPos.MoveNorth();
		maze.setCurrentWall("N", false);
		maze.setCurrentWall("E", true);
		maze.robotPos.MoveNorth();
		maze.setCurrentWall("N", true);
		maze.setCurrentWall("E", false);
		
		maze.robotPos.MoveEast();
		maze.setCurrentWall("N", true);
		maze.setCurrentWall("E", false);
		maze.setCurrentWall("S", false);
		
		new DrawMaze(maze).setVisible(true);
		
	}
	
	private static void HTMLMazeDemo() {

		
		try
		{
			Maze realMaze = new Maze("maze3.html");
			Maze maze = new Maze(realMaze.width, realMaze.height);
			new DrawMaze(maze).setVisible(true);
			new DrawMaze(realMaze).setVisible(true);
			delay(5000);
			new MazeExploration(maze, realMaze, 1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		delay(10000);
		System.exit(0);
	}
	
	private static void CSVMazeDemo() {

		
		
		try
		{
			Maze realMaze = new Maze("SE_CSV.csv");
			Maze maze = new Maze(realMaze.width, realMaze.height);
			new DrawMaze(maze).setVisible(true);
			new DrawMaze(realMaze).setVisible(true);
			delay(1000);
			new MazeExploration(maze, realMaze, 500);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		delay(10000);
		System.exit(0);
	}

	private static void delay()
	{
		try
		{
			TimeUnit.MILLISECONDS.sleep(1);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void delay(int milliseconds)
	{
		try
		{
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] a)
	{
		CSVMazeDemo();
		
	}
}
