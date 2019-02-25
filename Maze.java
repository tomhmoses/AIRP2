import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * A Maze is made up of cells.
 * </p>
 * <h2> layout</h2>
 * <ul>
 * <li> a 2D array of cells called layout...
 * <li> layout[x][y]
 * <li> therefore each layout[x] contains one vertical column of Cells, beginning with an edge cell.
 * <li> the layout is generated when you create the maze. Its North edge and its west edge is made up of EdgeCells
 * <li> due to having edge cells, the usable layout starts at 1 and ends at either width or height.
 * </ul>
 */
public class Maze
{
	public Cell[][] layout;
	public int width;
	public int height;
	public RobotPos robotPos;
	
	public Maze(int width, int height) {
		createMazeLayout(width, height);
		//we start in bottom left
		this.robotPos = new RobotPos(1, this.height, this);
		setCurrentPosVisited();
	}
	
	public Maze(int width, int height, RobotPos robotPos) {
		createMazeLayout(width, height);
		//we start with a custom robot position
		this.robotPos = robotPos;
		setCurrentPosVisited();
	}
	
	public Maze(String CSV_path) throws IOException {
		String csv = readFile(CSV_path, Charset.defaultCharset());
		String[] csvRows = csv.split("\n");
		this.height = csvRows.length - 1;
		System.out.println(height);
		this.width = csvRows[0].split(",").length - 1;
		System.out.println(width);
		createMazeLayout(width,height);
		this.robotPos = new RobotPos(1, this.height, this);
		
		Boolean S;
		Boolean E;
		for (int y = 0; y < height + 1; y++) {
			String[] values = csvRows[y].split(",");
			for (int x = 0; x < width + 1; x++) {
				char[] SE = values[x].toCharArray();
				if (SE[0] == 'T') {
					S = true;
				} else {
					S = false;
				}
				if (SE[1] == 'T') {
					E = true;  
				} else {
					E = false;
				}
				this.layout[x][y].setS(S);
				this.layout[x][y].setE(E);
			}
		}

		setCurrentPosVisited();
		
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

	private void createMazeLayout(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.layout = new Cell[this.width + 1][this.height + 1];
		this.layout[0] = CreateEdgeColumn();
		for (int x = 1; x < width + 1; x++) {
			this.layout[x][0] = new EdgeCell();
			for (int y = 1; y < height + 1; y++) {
				this.layout[x][y] = new Cell(this.layout[x][y - 1], this.layout[x - 1][y], new int[] {x,y});
			}
			this.layout[x][height].setS(true);
		}
		for (int y = 1; y < height + 1; y++) {
			this.layout[width][y].setE(true);
		}
	}
	
	private Cell[] CreateEdgeColumn() {
		Cell[] column = new Cell[height + 1];
		for (int i = 0; i < height + 1; i++) {
			column[i] = new EdgeCell();
		}
		return column;
	}
	
	private void setCurrentPosVisited() {
		this.layout[this.robotPos.x][this.robotPos.y].setVisited(true);
	}
	
	/**
	 * @param Direction - N, S, E or W
	 */
	public void setCurrentWall(String Direction, Boolean value) {
		Cell cell = getCurrentCell();
		switch (Direction.toUpperCase()) {
			case "N":
				cell.setN(value);
				break;
			case "E":
				cell.setE(value);
				break;
			case "S":
				cell.setS(value);
				break;
			case "W":
				cell.setW(value);
				break;
		}
	}

	public Cell getCurrentCell() {
		return layout[this.robotPos.x][this.robotPos.y];
	}
	
	public Cell getNorthCell() {
		return layout[this.robotPos.x][this.robotPos.y - 1];
	}
	
	public Cell getEastCell() {
		return layout[this.robotPos.x + 1][this.robotPos.y];
	}
	
	public Cell getSouthCell() {
		return layout[this.robotPos.x][this.robotPos.y + 1];
	}
	
	public Cell getWestCell() {
		return layout[this.robotPos.x - 1][this.robotPos.y];
	}
}
