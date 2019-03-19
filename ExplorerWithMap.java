/**
 * <img src="https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/2BAZlr8/young-lost-man-with-a-map-looking-for-direction-in-forest_ek79a5x0__F0011.png" width="250">
 * 
 * @author Tom & Jamie
 *
 */
public class ExplorerWithMap extends Explorer {

	private static final long serialVersionUID = -3002769266276540973L;

	public ExplorerWithMap(int x, int y, Maze maze)
	{
		super(x, y, maze);
		// TODO Auto-generated constructor stub
	}

	public ExplorerWithMap(int x, int y, Maze maze, Maze mapMaze)
	{
		super(x, y, maze);
		setMapMaze(mapMaze);
		// TODO Auto-generated constructor stub
	}

	private Maze mapMaze;
	
	public void setMapMaze(Maze mapMaze) {
		this.mapMaze = mapMaze;
	}
	
	public Boolean[] getCurrentWalls() {
		Boolean[] walls = mapMaze.layout[this.x][this.y].getWalls();
		// we don't look behind ourselves
		// System.out.println(lastDirection);
		if (lastDirection == "N") {
			walls[2] = null;
		}
		else if (lastDirection == "E") {
			walls[3] = null;
		}
		else if (lastDirection == "S") {
			walls[0] = null;
		}
		else if (lastDirection == "W") {
			walls[1] = null;
			// System.out.println("returned null for west");
		}
		return walls;
	}
	
	public Boolean reachedGoal() {
		return mapMaze.layout[this.x][this.y].type.equals("goal");
	}
	
	public Boolean onDanger() {
		System.out.println("Type was: " + mapMaze.layout[this.x][this.y].type);
		return mapMaze.layout[this.x][this.y].type.equals("danger");
	}
}
