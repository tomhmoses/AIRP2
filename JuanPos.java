
public class JuanPos extends RobotPos
{

	public int x;
	public int y;
	private Maze maze;
	Juan juan;
	
	public JuanPos(int x, int y, Maze maze)
	{
		super(x, y, maze);
	}
	
	public void setJuan(Juan juan) {
		this.juan = juan;
	}

	@Override
	public void MoveNorth() {
		this.y = this.y - 1;
		juan.MoveNorth();
		maze.getCurrentCell().setVisited(true);
	}

	@Override
	public void MoveSouth() {
		this.y = this.y + 1;
		juan.MoveSouth();
		maze.getCurrentCell().setVisited(true);
	}

	@Override
	public void MoveEast() {
		this.x = this.x + 1;
		juan.MoveEast();
		maze.getCurrentCell().setVisited(true);
	}

	@Override
	public void MoveWest() {
		this.x = this.x - 1;
		juan.MoveWest();
		maze.getCurrentCell().setVisited(true);
	}

}
