
public abstract class Explorer implements ExplorerInterface
{
	public int x;
	public int y;
	private Maze maze;
	public Object robot;
	public String lastDirection = "N";
	
	public Explorer(int x, int y, Maze maze) {
		this.x = x;
		this.y = y;
		this.maze = maze;
	}
	
	public Explorer(Maze maze) {
		this.maze = maze;
		this.x = 1;
		this.y = maze.height;
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#setMaze(Maze)
	 */
	@Override
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#teleportTo(int[])
	 */
	@Override
	public void teleportTo(int[] position) {
		this.x = position[0];
		this.y = position[1];
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveNorth()
	 */
	@Override
	public void MoveNorth() {
		lastDirection = "N";
		this.y = this.y - 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveSouth()
	 */
	@Override
	public void MoveSouth() {
		lastDirection = "S";
		this.y = this.y + 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveEast()
	 */
	@Override
	public void MoveEast() {
		lastDirection = "E";
		this.x = this.x + 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveWest()
	 */
	@Override
	public void MoveWest() {
		lastDirection = "W";
		this.x = this.x - 1;
		maze.getCurrentCell().setVisited(true);
	}

	public void out(String string, int line)
	{
		System.out.println(string);
		
	}

	public void send(Object obj)
	{
		// TODO Auto-generated method stub
		
	}


	
	
}
