/**
 * <img src="https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/2BAZlr8/young-lost-man-with-a-map-looking-for-direction-in-forest_ek79a5x0__F0011.png" width="250">
 * 
 * @author Tom
 *
 */
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
