public class ExplorerWithMap extends Explorer {
    
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