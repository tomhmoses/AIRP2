import java.io.Serializable;

/**
 * <p>
 * A cell is one square as part of a larger maze
 * </p>
 * <h2> it has: </h2>
 * <ul>
 * <li> visited status
 * <li> South wall status
 * <li> East wall status
 * <li> A pointer to the cell to the west
 * <li> A pointer to the cell to the north
 * </ul>
 * <h2> You can get: </h2>
 * <ul>
 * <li> visited status
 * <li> North wall status (by getting the south status of the north cell)
 * <li> East wall status
 * <li> South wall status
 * <li> West wall status (by getting the east status of the west cell)
 * </ul>
 */
public class Cell implements CellInterface, Serializable
{
	private static final long serialVersionUID = -7452897086311131776L;
	//because we will work from top left we use top (north) and left (west) cells from previously defined.
	Cell NCell;
	Cell WCell;
	Boolean SWall;
	Boolean EWall;
	String type = "normal";
	public int[] position;
	public int DijkstraDistance;
	public Cell DijkstraPrev;
	
	Boolean visited;
	
	
	public Cell(Cell NCell, Cell WCell, int[] position) {
		this.position = position;
		
		this.NCell = NCell;
		this.WCell = WCell;
		
		this.visited = false;
		
		this.SWall = null;
		this.EWall = null;
	}
	
	public String getCellPositionString() {
		return "[" + Integer.toString(position[0]) + "," + Integer.toString(position[1]) + "]";
	}

	
	public Boolean getVisited() {
		return this.visited;
	}
	
	public void setVisited(Boolean value) {
		this.visited = value;
		if (this.visited) {
			this.type = "visited";
		}
	}

	public Boolean getN() {
		return this.NCell.getS();
	}
	
	public Boolean getE() {
		return this.EWall;
	}
	
	public Boolean getS() {
		return this.SWall;
	}
	
	public Boolean getW() {
		return this.WCell.getE();
	}
	
	public Boolean[] getWalls() {
		Boolean[] walls = {this.getN(), this.getE(), this.getS(), this.getW()};
		return walls;
	}

	public void setN(Boolean value) {
		this.NCell.setS(value);
	}
	
	public void setE(Boolean value) {
		this.EWall = value;
	}
	
	public void setS(Boolean value) {
		this.SWall = value;
	}
	
	public void setW(Boolean value) {
		this.WCell.setE(value);
	}
}
