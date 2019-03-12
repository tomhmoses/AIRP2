import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
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
	boolean remote = false;
	//EV3Client client = new EV3Client();
	ObjectInputStream oIn;
	Socket sock;

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
	
	public MazeCanvas(int w, int h, Maze maze, boolean remote)
	{
		this.remote = remote;
		// TODO: figure out if this is the right way to instanciate the client:
		try
		{
			setupClient();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Color darkGrey = new Color(200,200,200);
		for (int x = 0; x <= maze.width; x++) {
			for (int y = 0; y <= maze.height; y++) {
				cell = maze.layout[x][y];
				if (cell.type.equals("edge")) {
					g.setColor(pPink);
				}
				else
				{
					if (cell.type.equals("goal")) {
						g.setColor(pRed);
					}
					else if (cell.type.equals("danger")) {
						g.setColor(darkGrey);
					}
					else if (cell.type.equals("planned")) {
						g.setColor(pBlue);
					}
					else if (cell.visited) {
						g.setColor(pGreen);
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
			
			//draw robot circle
			g.setColor(robotColor);
			int x1 = (int) Math.round((maze.explorer.x - 1 + 0.25)*cellWidth);
			int y1 = (int) Math.round((maze.explorer.y - 1 + 0.25)*cellHeight);
			g.fillOval(x1, y1, cellWidth/2, cellHeight/2);
			//draw robot direction line
			int x2 = (int) Math.round((maze.explorer.x - 1 + 0.5)*cellWidth);
			int y2 = (int) Math.round((maze.explorer.y - 1 + 0.5)*cellHeight);
			int x3 = 0;
			int y3 = 0;
			if (maze.explorer.lastDirection == "N") {
				x3 = (int) Math.round((maze.explorer.x - 1 + 0.5)*cellWidth);
				y3 = (int) Math.round((maze.explorer.y - 1 + 0.2)*cellHeight);
			}
			else if (maze.explorer.lastDirection == "E") {
				x3 = (int) Math.round((maze.explorer.x - 1 + 0.8)*cellWidth);
				y3 = (int) Math.round((maze.explorer.y - 1 + 0.5)*cellHeight);
			}
			else if (maze.explorer.lastDirection == "S") {
				x3 = (int) Math.round((maze.explorer.x - 1 + 0.5)*cellWidth);
				y3 = (int) Math.round((maze.explorer.y - 1 + 0.8)*cellHeight);
			}
			else if (maze.explorer.lastDirection == "W") {
				x3 = (int) Math.round((maze.explorer.x - 1 + 0.2)*cellWidth);
				y3 = (int) Math.round((maze.explorer.y - 1 + 0.5)*cellHeight);
			}
			
			g.drawLine(x2,y2,x3,y3);
			
		}
	}

    @Override
    public void run()
    {
        while(true) {
            if (remote) {
                // should just wait until it receives the maze
                try
                {
                	System.out.println("WAITING for maze");
                    this.maze = getMaze();
                    System.out.println("I have got the maze");
                }
                catch (ClassNotFoundException | IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } // TODO: figure out how to receive the object
            }
            this.repaint();
        }
    }
    
    private void setupClient() throws IOException {
    	System.out.println("waiting for connection");
	    String ip = "10.0.1.1"; // BT
	    sock = new Socket(ip, 1245);
	    System.out.println("Connected");
	    InputStream in = sock.getInputStream();
		oIn = new ObjectInputStream(in);
    }
    
    public Maze getMaze() throws ClassNotFoundException, IOException {
    	Maze obj = (Maze) oIn.readObject();
        return obj;
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
	
	public DrawMaze(Maze maze, boolean remote)
	{
		MazeCanvas mazeCanvas = new MazeCanvas(800, 800, maze, remote);
		add(mazeCanvas);
		pack();
	}
	
	private static void MoveInMazeDemo() {
		Maze maze = new Maze(9, 6);
		
		maze.setCurrentWall("N", false);
		maze.setCurrentWall("E", true);
		maze.explorer.MoveNorth();
		maze.setCurrentWall("N", false);
		maze.setCurrentWall("E", true);
		maze.explorer.MoveNorth();
		maze.setCurrentWall("N", true);
		maze.setCurrentWall("E", false);
		
		maze.explorer.MoveEast();
		maze.setCurrentWall("N", true);
		maze.setCurrentWall("E", false);
		maze.setCurrentWall("S", false);
		
		new DrawMaze(maze).setVisible(true);
		
	}
	
	private static void HTMLMazeDemo() {
		try
		{
			Maze mapMaze = new Maze("maze1.html");
			Maze maze = new Maze(mapMaze.width, mapMaze.height);
			new DrawMaze(mapMaze).setVisible(true);
			new DrawMaze(maze).setVisible(true);
			delay(2000);
			new MazeExploration(maze, mapMaze, 500);
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
			Maze mapMaze = new Maze("SE_CSV.csv");
			Object realMaze;
			Maze maze = new Maze(mapMaze.width, mapMaze.height);
			new DrawMaze(mapMaze).setVisible(true);
			new DrawMaze(maze).setVisible(true);
			delay(1000);
			new MazeExploration(maze, mapMaze, 500);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		delay(10000);
		System.exit(0);
	}
	
	private static void RemoteMazeViewer() {
		//sets a temp Maze
		Maze maze = new Maze(9,6);
		new DrawMaze(maze, true).setVisible(true);
		
	}

	private static void delay()
	{
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void delay(int milliseconds)
	{
		try
		{
			if (milliseconds > 100) {
				Thread.sleep(milliseconds);
			}
			
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] a)
	{
		RemoteMazeViewer();
		//HTMLMazeDemo();
	}
}
