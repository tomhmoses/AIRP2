
public interface RobotInterface
{

	void MoveNorth();

	void MoveSouth();

	void MoveEast();

	void MoveWest();

	Boolean reachedGoal();

	Boolean[] getCurrentWalls();

}
