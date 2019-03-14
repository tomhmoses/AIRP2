
/**
 * <img src="https://tinyurl.com/y322n7gs" width="250">
 * 
 * @author Tom
 *
 */
public interface ExplorerInterface
{

	void setMaze(Maze maze);

	void teleportTo(int[] position);

	void MoveNorth();

	void MoveSouth();

	void MoveEast();

	void MoveWest();
	
	Boolean[] getCurrentWalls();

	Boolean reachedGoal();
	
	Boolean onDanger();

}
