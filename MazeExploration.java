import java.util.ArrayDeque;
import java.util.Deque;

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
			else {
				System.out.println(visitStack.size()); 
				if (visitStack.size() == 1) {
					System.out.println("position");
					System.out.println(visitStack.peekFirst()[0]);
					System.out.println(visitStack.peekFirst()[1]);
				}
			}
		}
		
		System.out.println("finished");
		
	}

	private static void travelTo(int[] position, Maze maze)
	{
		maze.robotPos.x = position[0];
		maze.robotPos.y = position[1];
	}
}
