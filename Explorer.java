
public abstract class Explorer implements ExplorerInterface
{
	public int x;
	public int y;
	private Maze maze;
	
	public Explorer(int x, int y, Maze maze) {
		this.x = x;
		this.y = y;
		this.maze = maze;
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
		this.y = this.y - 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveSouth()
	 */
	@Override
	public void MoveSouth() {
		this.y = this.y + 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveEast()
	 */
	@Override
	public void MoveEast() {
		this.x = this.x + 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	/* (non-Javadoc)
	 * @see ExplorerInterface#MoveWest()
	 */
	@Override
	public void MoveWest() {
		this.x = this.x - 1;
		maze.getCurrentCell().setVisited(true);
	}
	
	
}
