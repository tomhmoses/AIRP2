import java.io.IOException;

public class RobotMain
{

	public static void main(String[] args)
	{
		int width = 9;
		int height = 6;
		Maze maze = new Maze(width, height);
		RobotExplorer robotExplorer = new RobotExplorer(maze);
		robotExplorer.setRobot(new Juan());
		maze.setExplorer(robotExplorer);
		try
		{
			new MazeExploration(maze);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
