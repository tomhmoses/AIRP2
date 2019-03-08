public class RobotMainTest
{
	public void main(String[] args)
	{
		Juan juan = new Juan();
		Boolean[] walls = juan.getCurrentWalls();
		jusn.LCDOut(walls.toString(), 0);
		
		juan.MoveNorth();
		walls = juan.getCurrentWalls();
		jusn.LCDOut(walls.toString(), 1);

		juan.MoveEast();
		walls = juan.getCurrentWalls();
		jusn.LCDOut(walls.toString(), 2);
		
		juan.MoveWest();
		walls = juan.getCurrentWalls();
		jusn.LCDOut(walls.toString(), 3);
		
		juan.MoveSouth();
		walls = juan.getCurrentWalls();
		jusn.LCDOut(walls.toString(), 4);
	}	
}