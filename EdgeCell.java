
/**
 * <p>
 * A type of cell that will be in the first row and down the first column.
 * </p>
 * <h2> it has: </h2>
 * <ul>
 * <li> no north cell
 * <li> no west cell
 * <li> an east wall
 * <li> a south wall
 * </ul>
 */
public class EdgeCell extends Cell
{
	/**
	 * <p>
	 * Creates a cell that has:
	 * </p>
	 * <ul>
	 * <li> no north cell
	 * <li> no west cell
	 * <li> an east wall
	 * <li> a south wall
	 * </ul>
	 */
	public EdgeCell()
	{
		super(null, null, new int[] {-1,-1});
		this.NCell = null;
		this.WCell = null;
		this.EWall = true;
		this.SWall = true;
		this.type = "edge";
	}

}
