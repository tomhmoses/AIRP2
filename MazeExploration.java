import java.util.ArrayDeque;
import java.util.Deque;
import org.graphstream.graph.*;
import org.graphstream.algorithm.AStar;

public class MazeExploration
{

	private Boolean withRobot = false;
	private Maze maze;
	
	/**
	 * used to demo exploring the maze using an explorer with a map and with no delay
	 */
	public MazeExploration(Maze maze, Maze mapMaze) {
		this.maze = maze;
		maze.setExplorer(new ExplorerWithMap(this.maze);
		this.maze.explorer.setMapMaze(mapMaze);
		exploreMaze(0);
	}
	
	/**
	 * used to demo exploring the maze using an explorer with a map and with a chosen delay
	 */
	public MazeExploration(Maze maze, Maze mapMaze, int delay) {
		this.maze = maze;
		this.maze.setExplorer(new ExplorerWithMap(this.maze);
		this.maze.explorer.setMapMaze(mapMaze);
		exploreMaze(delay);
	}
	
	/**
	 * used when robot is exploring maze
	 */
	public MazeExploration(Maze maze) {
		this.maze = maze;
		withRobot = true;
		exploreMaze(0);
	}
	
	
	public static void exploreMaze(int delay)
	{
		Deque<int[]> visitStack = new ArrayDeque<>();
		Boolean stillToVisit = true;
		Boolean[] walls;
		visitStack.addFirst(new int[] {maze.explorer.x,maze.explorer.y});
		while (stillToVisit) {
			if (withRobot) {
				sendMaze()
			}
			try
			{
				Thread.sleep(delay);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			travelTo(visitStack.removeFirst(), maze);
			maze.getCurrentCell().setVisited(true);
			walls = maze.explorer.getCurrentWalls();
			maze.setCurrentWall("N", walls[0]);
			maze.setCurrentWall("E", walls[1]);
			maze.setCurrentWall("S", walls[2]);
			maze.setCurrentWall("W", walls[3]);
			if (maze.getCurrentCell().getN() == false) {
				if (maze.getNorthCell().getVisited() == false) {
					if (maze.getNorthCell().type.equals("normal")) {
						visitStack.addFirst(maze.getNorthCell().position);
						maze.getNorthCell().type = "planned";
					}
				}
			}
			if (maze.getCurrentCell().getE() == false) {
				if (maze.getEastCell().getVisited() == false) {
					if (maze.getEastCell().type.equals("normal")) {
						visitStack.addFirst(maze.getEastCell().position);
						maze.getEastCell().type = "planned";
					}
				}
			}
			if (maze.getCurrentCell().getS() == false) {
				if (maze.getSouthCell().getVisited() == false) {
					if (maze.getSouthCell().type.equals("normal")) {
						visitStack.addFirst(maze.getSouthCell().position);
						maze.getSouthCell().type = "planned";
					}
				}
			}
			if (maze.getCurrentCell().getW() == false) {
				if (maze.getWestCell().getVisited() == false) {
					if (maze.getWestCell().type.equals("normal")) {
						visitStack.addFirst(maze.getWestCell().position);
						maze.getWestCell().type = "planned";
					}
				}
			}
			if (withRobot) {
				sendMaze()
			}
			if (visitStack.size() == 0 || maze.explorer.reachedGoal()) {
				stillToVisit = false;
			}
		}
		if (maze.explorer.reachedGoal()) {
			maze.getCurrentCell().type = "goal";
			travelTo(new int[] {1, maze.height}, maze);
		}
		
		System.out.println("finished");
		
	}
	
	private static String myToString(int[] position) {
		return "[" + Integer.toString(position[0]) + "," + Integer.toString(position[1]) + "]";
	}
	
	private static int[] myToIntArr(String string) {
		int[] position = new int[2];
		string = string.substring(1, string.length()-1);
		String[] posString = string.split(",");
		//System.out.print("toIntArr: ");
		//System.out.println(string);
		position[0] = Integer.valueOf(posString[0]);
		position[1] = Integer.valueOf(posString[1]);
		return position;
	}

	private static void travelTo(int[] goalPos, Maze maze)
	{
		int[] currentPos = maze.getCurrentCell().position;
		Graph graph = maze.getGraph();
		AStar astar = new AStar(graph);
		//astar.setCosts(new Costs());
		astar.compute(myToString(currentPos), myToString(goalPos));
		Path path = astar.getShortestPath();
		String[] directions = toDirectionArray(path);
		for (int i = 0; i < directions.length; i++) {
			System.out.println("Moving " + directions[i]);
			if (directions[i] == "N") {
				maze.explorer.MoveNorth();
			}
			else if (directions[i] == "E") {
				maze.explorer.MoveEast();
			}
			else if (directions[i] == "S") {
				maze.explorer.MoveSouth();
			}
			else if (directions[i] == "W") {
				maze.explorer.MoveWest();
			}
			if (withRobot) {
				sendMaze()
			}
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static String[] toDirectionArray(Path path)
	{
		String[] directions;
		if (path.size() > 1) {
			directions = new String[path.size()-1];
			String pathStr = path.toString();
			pathStr = pathStr.substring(1, pathStr.length()-1);
			String[] nodeStrings = pathStr.split(", ");
			Deque<int[]> posQueue = new ArrayDeque<>();
			for (int i = 0; i < nodeStrings.length; i++) {
				posQueue.addLast(myToIntArr(nodeStrings[i]));
			}
			int[] currentPos = posQueue.removeFirst();
			int[] nextPos;
			int counter = 0;
			while (posQueue.size() > 0) {
				nextPos = posQueue.removeFirst();
				if (nextPos[1] < currentPos[1]) {
					directions[counter] = "N";
				}
				else if (nextPos[1] > currentPos[1]) {
					directions[counter] = "S";
				}
				else if (nextPos[0] > currentPos[0]) {
					directions[counter] = "E";
				}
				else if (nextPos[0] < currentPos[0]) {
					directions[counter] = "W";
				}
				
				counter += 1;
				currentPos = nextPos;
			}
		}
		else {
			directions = new String[] {};
		}
		return directions;
	}
	
	private void sendMaze() {
		this.maze.explorer.robot.EV3Server.sendObj(this.maze);
	}
}
