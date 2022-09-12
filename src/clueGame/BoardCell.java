package clueGame;

import java.util.HashSet;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class BoardCell {
	private int row;
	private int col;
	private DoorDirection doorDirection;
	private char symbol;
	private boolean roomLabel = false;
	private boolean roomCenter = false;
	private char secretPassage;
	private boolean isRoom = false;
	private boolean isOccupied = false;
	private boolean isDoorway = false;
	private Set<BoardCell> adjList = new HashSet<BoardCell>();
	private int xLocation;
	private int yLocation;

	public BoardCell() {
		super();
	}
	
	//initialize cell with row num, column num, and the char symbol
	public BoardCell(int r, int c, String s) {
		row = r;
		col = c;
		symbol = s.charAt(0);
		if(!(symbol == 'W') && !(symbol == 'X')) {
			isRoom = true;
		}
	}
	
	public void drawCell(Graphics boardGraphic, int cellWidth, int cellHeight) {
		
		xLocation = cellWidth * col;
		yLocation = cellHeight * row;
		
		if(isRoom) {
			//Paint room cells gray
			boardGraphic.setColor(Color.LIGHT_GRAY);
			boardGraphic.fillRect(xLocation, yLocation, cellWidth, cellHeight);
		}
		else if( symbol == 'W' ){
			//Paint walkways light blue with outline in black
			boardGraphic.setColor(Color.BLACK);
			boardGraphic.fillRect(xLocation, yLocation, cellWidth, cellHeight);
			
			Color pastelBlue = new Color(174, 198, 207);
			boardGraphic.setColor(pastelBlue);
			boardGraphic.fillRect(xLocation + 1, yLocation + 1, cellWidth - 1, cellHeight - 1);
			
		}
		else {
			//Paint unused spaces black
			boardGraphic.setColor(Color.BLACK);
			boardGraphic.fillRect(xLocation, yLocation, cellWidth, cellHeight);
		}
		
		if(isDoorway) {
			/*
			 * If cell is a doorway, add a darker line to the associated direction in which the door points
			 */
			switch (doorDirection){	
			
			case UP:
				boardGraphic.setColor(Color.DARK_GRAY);
				boardGraphic.fillRect(xLocation, yLocation - 3, cellWidth, 3);
				break;
				
			case DOWN:
				boardGraphic.setColor(Color.DARK_GRAY);
				boardGraphic.fillRect(xLocation, yLocation + cellHeight - 3, cellWidth, 3);
				break;
				
			case LEFT:
				boardGraphic.setColor(Color.DARK_GRAY);
				boardGraphic.fillRect(xLocation - 3, yLocation, 3, cellHeight);
				break;
				
			case RIGHT:
				boardGraphic.setColor(Color.DARK_GRAY);
				boardGraphic.fillRect(xLocation + cellWidth - 3, yLocation, 3, cellHeight);
				break;					
			}
		}
		
	}
	
	public void highlightCell(Graphics boardGraphic, int cellWidth, int cellHeight) {
		
		int xLocation = cellWidth * col;
		int yLocation = cellHeight * row;
		
		Color pastelGreen = new Color(119, 221, 119);
		boardGraphic.setColor(pastelGreen);
		boardGraphic.fillRect(xLocation + 1, yLocation + 1, cellWidth - 1, cellHeight - 1);

	}
	/*
	 * checks if a cell contains a click
	 */
	public boolean containsClick(int mouseX, int mouseY) {
		Rectangle rect = new Rectangle(xLocation, yLocation, Board.getInstance().getCellWidth(), Board.getInstance().getCellHeight());
		if (rect.contains(new Point(mouseX, mouseY))) {
			return true;
		}
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}
	
	public void addAdj( BoardCell cell ) {
		 adjList.add(cell);
	 }
	
	public Set<BoardCell> getAdjList() {
		 return adjList;
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
	 public boolean isOccupied() {
		 return isOccupied;
	 }
	 
	 //various getters and setters
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public boolean isDoorway() {
		return isDoorway;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public boolean isRoomCenter() {
		return roomCenter;
	}

	public boolean isLabel() {		
		return roomLabel;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setRoomCenter(boolean b) {
		roomCenter = b;
	}

	public void setRoomLabel(boolean b) {
		roomLabel = b;		
	}

	public void setDoorDirection(DoorDirection d) {
		doorDirection = d;
	}

	public void setisDoorway(boolean b) {
		isDoorway = b;		
	}

	public void setSecretPassage(char temp) {
		secretPassage = temp;		
	}
}
