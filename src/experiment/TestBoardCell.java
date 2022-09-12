package experiment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestBoardCell {
	private int row;
	private int column;
	private boolean isRoom;
	private boolean isOccupied;
	private Set<TestBoardCell> adjList = new HashSet<TestBoardCell>();
	
	public TestBoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	 public void addAdjacency( TestBoardCell cell ) {
		 adjList.add(cell);
	 }
	 
	 public Set<TestBoardCell> getAdjList() {
		 return adjList;
	 }
	 
	 /*
	  * Sets inRoom to true if cell is in a room, or false if not in a room
	  */
	 public void setRoom(boolean room) {
		isRoom = room; 
	 }
	
	 
	 /*
	  * Returns true if the cell is part of a room
	  */
	 public boolean isRoom() {
		return isRoom; 
	 }
	 
	 /*
	  * Sets isOccupied to true if the cell is occupied by a player, and false if it is not
	  */
	 public void setOccupied(boolean occupied) {
		 isOccupied = occupied;
	 }
	 
	 /*
	  * Returns occupation status of a cell by a player
	  */
	 public boolean getOccupied() {
		 return isOccupied;
	 }
	 
	 
	 
	 
	 
}

