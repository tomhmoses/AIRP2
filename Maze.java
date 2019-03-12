import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;


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
public class Maze implements Serializable
{
	private static final long serialVersionUID = -1950168016120932196L;

	public Cell[][] layout;
	
	/**
	 * The actual width of the physical maze.
	 */
	public int width;
	
	/**
	 * The actual height of the physical maze.
	 */
	public int height;
	public Explorer explorer;
	
	public Maze(int width, int height) {
		createMazeLayout(width, height);
		//we start in bottom left
		this.explorer = new ExplorerWithMap(1, this.height, this);
		setCurrentPosVisited();
	}
	
	public Maze(int width, int height, Explorer explorer) {
		createMazeLayout(width, height);
		//we start with a custom robot position
		this.explorer = explorer;
		setCurrentPosVisited();
	}
	
	public void setExplorer(Explorer explorer) {
		this.explorer = explorer;
	}
	
	/**
	 * <p>
	 * Allows a maze to be created from a CSV file containing in each cell:
	 * </p>
	 * <ul>
	 * <li> TT for a wall on the south and east
	 * <li> TF for a wall on just the south
	 * <li> FT for a wall on just the east
	 * <li> FF for no walls on south or east
	 * </ul>
	 * 
	 * <p>
	 * <strong>OR</strong> allows a maze to be created from a HTML file generated at https://xefer.com/maze-generator
	 * </p>
	 */
	public Maze(String file_path) throws IOException {
		String text = readFile(file_path, Charset.defaultCharset());
		if (file_path.contains("csv")) {
			String csv = text;
			String[] csvRows = csv.split("\n");
			this.height = csvRows.length - 1;
			this.width = csvRows[0].split(",").length - 1;
			createMazeLayout(width,height);
			this.explorer = new ExplorerWithMap(1, this.height, this);
			
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
		}
		else if (file_path.contains("html")) {
			String html = text;
			String[] htmlRows = html.split("mrow");
			this.height = htmlRows.length - 1;
			this.width = htmlRows[1].split("cell").length - 1;
			createMazeLayout(width,height);
			this.explorer = new ExplorerWithMap(1, this.height, this);
			
			Boolean S;
			Boolean E;
			for (int y = 1; y < height + 1; y++) {
				String[] values = htmlRows[y].split("cell");
				for (int x = 1; x < width + 1; x++) {
					if (values[x].contains("nobottom")) {
						S = false;
					} else {
						S = true;
					}
					if (values[x].contains("noright")) {
						E = false;  
					} else {
						E = true;
					}
					this.layout[x][y].setS(S);
					this.layout[x][y].setE(E);
				}
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

	/**
	 * Populates the maze layout with empty cells.
	 */
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
		this.layout[this.explorer.x][this.explorer.y].setVisited(true);
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
		return layout[this.explorer.x][this.explorer.y];
	}
	
	public Cell getNorthCell() {
		return layout[this.explorer.x][this.explorer.y - 1];
	}
	
	public Cell getEastCell() {
		return layout[this.explorer.x + 1][this.explorer.y];
	}
	
	public Cell getSouthCell() {
		return layout[this.explorer.x][this.explorer.y + 1];
	}
	
	public Cell getWestCell() {
		return layout[this.explorer.x - 1][this.explorer.y];
	}
	
	
	public Graph getGraphStreamGraph() {
		Graph graph = new SingleGraph("Maze Graph");
		for (int x = 1; x <= this.width; x++) {
			for (int y = 1; y <= this.height; y++) {
				graph.addNode(layout[x][y].getCellPositionString());
				if (layout[x][y].getN() != null) {
					if (layout[x][y].getN() == false) {
						try {
							graph.addEdge(layout[x][y].getCellPositionString() + "N", layout[x][y].getCellPositionString(), layout[x][y-1].getCellPositionString());
						}
						catch (Exception e)
						{}
					}
				}
				if (layout[x][y].getW() != null) {
					if (layout[x][y].getW() == false) {
						try {
							graph.addEdge(layout[x][y].getCellPositionString() + "W", layout[x][y].getCellPositionString(), layout[x-1][y].getCellPositionString());
						}
						catch (Exception e)
						{}
					}
				}
			}
		}
		
		return graph;
	}
	
	
	
}
