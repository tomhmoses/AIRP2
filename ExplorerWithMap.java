public class ExplorerWithMap extends Explorer {
    
	public ExplorerWithMap(int x, int y, Maze maze)
	{
		super(x, y, maze);
		// TODO Auto-generated constructor stub
	}

	private Maze mapMaze;
	
	public void setMapMaze(Maze mapMaze) {
		this.mapMaze = mapMaze;
	}
	
	public Boolean[] getCurrentWalls() {
		return mapMaze.layout[this.x][this.y].getWalls();
	}
	
	public Boolean reachedGoal() {
		return mapMaze.layout[this.x][this.y].type.equals("goal");
	}
}