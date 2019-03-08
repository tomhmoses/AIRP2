public class RobotMainTest
{
	public void main(String[] args)
	{
		Juan juan = new Juan();
		Boolean[] walls = juan.getCurrentWalls();
		System.out.println(walls);
		
		juan.MoveNorth();
		walls = juan.getCurrentWalls();
		System.out.println(walls);

		juan.MoveEast();
		walls = juan.getCurrentWalls();
		System.out.println(walls);
		
		juan.MoveWest();
		walls = juan.getCurrentWalls();
		System.out.println(walls);
		
		juan.MoveSouth();
		walls = juan.getCurrentWalls();
		System.out.println(walls);
	}	
}