import java.util.ArrayDeque;
import java.util.Deque;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.AStar;
import org.graphstream.algorithm.AStar.Costs;

public class MazeExploration
{
	public MazeExploration(Maze maze, Maze realMaze) {
		exploreMaze(maze, realMaze, 0);
	}
	
	public MazeExploration(Maze maze, Maze realMaze, int delay) {
		exploreMaze(maze, realMaze, delay);
	}
	
	public static void exploreMaze(Maze maze, Maze realMaze, int delay)
	{
		Deque<int[]> visitStack = new ArrayDeque<>();
		Boolean stillToVisit = true;
		Boolean[] walls;
		visitStack.addFirst(new int[] {maze.robotPos.x,maze.robotPos.y});
		while (stillToVisit) {
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
			walls = realMaze.layout[maze.robotPos.x][maze.robotPos.y].getWalls();
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
			if (visitStack.size() == 0) {
				stillToVisit = false;
			}
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
				maze.robotPos.MoveNorth();
			}
			else if (directions[i] == "E") {
				maze.robotPos.MoveEast();
			}
			else if (directions[i] == "S") {
				maze.robotPos.MoveSouth();
			}
			else if (directions[i] == "W") {
				maze.robotPos.MoveWest();
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
			System.out.print("path: ");
			System.out.println(path);
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
			System.out.print("queue Size: ");
			System.out.println(posQueue.size());
			System.out.print("path after pop: ");
			System.out.println(path);
			while (posQueue.size() > 0) {
				nextPos = posQueue.removeFirst();
				System.out.print("current: ");
				System.out.println(myToString(currentPos));
				System.out.print("next: ");
				System.out.println(myToString(nextPos));
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
			System.out.println("directions empty");
		}
		return directions;
	}
}
