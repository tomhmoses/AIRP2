
public class RobotPos
{
	public int x;
	public int y;
	private Maze maze;
	
	public RobotPos(int x, int y, Maze maze) {
		this.x = x;
		this.y = y;
		this.maze = maze;
	}
	
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	public void MoveNorth() {
		this.y = this.y - 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	public void MoveSouth() {
		this.y = this.y + 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	public void MoveEast() {
		this.x = this.x + 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	public void MoveWest() {
		this.x = this.x - 1;
		maze.getCurrentCell().setVisited(true);
	}
}
