/**
 * Juan is a robot
 * 
 * <img src="https://i.imgur.com/GMByDzn.png">
 * 
 * @author <b style="color:blue;background-color:pink;">Tom</b>
 */
public interface RobotInterface
{

	void send(Object obj);
	
	void LCDOut(String string, int line);
	
	/**
	 * The robot will move one tile north
	 */
	void MoveNorth();

	/**
	 * The robot will move one tile South 
	 */
	void MoveSouth();

	/**
	 * The robot will move one tile East
	 */
	void MoveEast();

	/**
	 * The robot will move one tile West
	 */
	void MoveWest();

	/**
	 * @return if it has reached the goal
	 * 
	 * <p>i.e. using the <code>ColorSensor</code> to see if it is on a <b style="color:red;">red</b> tile.</p>
	 */
	Boolean reachedGoal();

	/**
	 * @return the current walls in a boolean array in order <code>[N,E,S,W]</code>.
	 * 
	 * <p>It can and should return <code>null</code> for the wall behind the robot.</p>
	 */
	Boolean[] getCurrentWalls();

	void drawMazeOnLCD(Maze maze);

}
