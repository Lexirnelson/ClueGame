package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	final static int COLS = 4;
	final static int ROWS = 4;
	private TestBoardCell[][] grid = new TestBoardCell[ROWS][COLS];
	private Set<TestBoardCell> targets = new HashSet<TestBoardCell>();
	private Set<TestBoardCell> visited = new HashSet<TestBoardCell>();
	
	/*
	 * helper function to initialize adjacency left right up down
	 */
	private void LRUD(TestBoardCell[][] grid, int i, int j, boolean left, boolean right, boolean up, boolean down) {
		if (left){
			grid[i][j].addAdjacency(grid[i][j-1]);//left
		}
		if (right){
			grid[i][j].addAdjacency(grid[i][j+1]);//right
		}
		if (up){
			grid[i][j].addAdjacency(grid[i-1][j]);//up
		}
		if (down){
			grid[i][j].addAdjacency(grid[i+1][j]);//down
		}
	}
	/*
	 * Fills a 2D array with TestBoardCell objects to set up board
	 */
	public TestBoard(){
		//fill board
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i,j);
			}
		}
		
		//set the adjacent lists
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				
				/*
				 * Boolean values that determine whether or not the cell directly above,below,left,and right of the current cell
				 * are possible adjacencies.
				 */
				boolean up = true;
				boolean down = true;
				boolean left = true;
				boolean right = true;
				
				if(i == 0) {
					up = false;//Sets up to false if at top of grid
				}
				else if(i == ROWS - 1) {
					down = false;//Sets up to false if at bottom of grid
				}
				
				if(j == 0) {
					left = false;//Sets up to false if at left of grid
				}
				else if(j == COLS - 1) {
					right = false;//Sets up to false if at right of grid
				}
				
				LRUD(grid,i,j,left,right,up,down);
			}
		}
		
	}
	
	/*
	 * Calculates possible moves
	 */
	public void calcTargets( TestBoardCell startCell, int pathLength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(pathLength, startCell);
		
	}
	
	private void findAllTargets(int numSteps, TestBoardCell thisCell) {		
		//Iterates through the adjacency list for the given cell
		for (TestBoardCell adjCell : thisCell.getAdjList()) {
			
			//Only continues recursive function if the cell has not been visited and is not occupied 
			if (!visited.contains(adjCell) && !adjCell.getOccupied()) {

				visited.add(adjCell);
				
				//Stops recursion if out of steps or in a room
				if(numSteps == 1 || adjCell.isRoom()) {
					targets.add(adjCell);
				}
				else {
					findAllTargets(numSteps-1, adjCell);
				}
				
				visited.remove(adjCell);
			}
		}
		
	}
	
	/*
	 * Returns list of possible moves
	 */
	 public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	/*
	 * Returns the cell object at a given point
	 */
	public TestBoardCell getCell( int row, int col ) {
		return grid[row][col];
	}
}
