package Scroggle;

public class GridSingleton {
	static Grid grid;
	
	public static Grid getGrid() {
		if(grid == null) {
			grid = new Grid();
			}
		return grid;
	}
	

}
