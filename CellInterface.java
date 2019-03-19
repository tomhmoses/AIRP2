public interface CellInterface
{
	/**
	 * @return if the cell has been visited yet
	 */
	public boolean getVisited();
	
	/**
	 * Sets if the cell has been visited yet
	 */
	public void setVisited(boolean value);
	
	/**
	 * @return the values of all of the walls surrounding the current cell
	 * in the order N, E, S, W
	 */
	public Boolean[] getWalls();

	/**
	 * Regarding the wall to the north of the current cell
	 * <ul>
	 * <li> Will return true if it exists,
	 * <li> False if it doesn't exist
	 * <li> Null if it is not known yet.
	 * </ul>
	 */
	public Boolean getN();
	
	/**
	 * Regarding the wall to the east of the current cell
	 * <ul>
	 * <li> Will return true if it exists,
	 * <li> False if it doesn't exist
	 * <li> Null if it is not known yet.
	 * </ul>
	 */
	public Boolean getE();
	
	/**
	 * Regarding the wall to the south of the current cell
	 * <ul>
	 * <li> Will return true if it exists,
	 * <li> False if it doesn't exist
	 * <li> Null if it is not known yet.
	 * </ul>
	 */
	public Boolean getS();
	
	/**
	 * Regarding the wall to the west of the current cell
	 * <ul>
	 * <li> Will return true if it exists,
	 * <li> False if it doesn't exist
	 * <li> Null if it is not known yet.
	 * </ul>
	 */
	public Boolean getW();

	public void setN(Boolean value);
	public void setE(Boolean value);
	public void setS(Boolean value);
	public void setW(Boolean value);
}
