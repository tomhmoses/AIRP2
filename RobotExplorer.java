
public class RobotExplorer extends Explorer
{

	public int x;
	public int y;
	private Maze maze;
	RobotInterface robot;
	
	public RobotExplorer(int x, int y, Maze maze)
	{
		super(x, y, maze);
	}
	
	public RobotExplorer(Maze maze)
	{
		super(maze);
	}

	public void setRobot(RobotInterface robot) {
		this.robot = robot;
	}

	@Override
	public void MoveNorth() {
		super.MoveNorth();
		robot.MoveNorth();
	}

	@Override
	public void MoveSouth() {
		super.MoveSouth();
		robot.MoveSouth();
	}

	@Override
	public void MoveEast() {
		super.MoveEast();
		robot.MoveEast();
	}

	@Override
	public void MoveWest() {
		super.MoveWest();
		robot.MoveWest();
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#reachedGoal()
	 */
	@Override
	public Boolean reachedGoal() {
		return robot.reachedGoal();
	}
	
	@Override
	public Boolean[] getCurrentWalls() {
		return robot.getCurrentWalls();
	}
	
	public void out(String string, int line)
	{
		robot.LCDOut(string, line);
		
	}
	
	public void send(Object obj)
	{
		robot.send(obj);
		
	}

}
