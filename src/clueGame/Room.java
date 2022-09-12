package clueGame;

import java.util.ArrayList;
import java.awt.Graphics;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private char passageName;
	private ArrayList<BoardCell> doorways = new ArrayList<>();
	private boolean hasPassage = false;
	
	//initialize room with the string name
	public Room(String n) {
		name = n;
	}
	
	public void drawLabel(Graphics boardGraphic) {
		//Draws the name of a room starting at the label cell
		boardGraphic.drawString(name, labelCell.getRow(), labelCell.getColumn());
	}
	
	//add doorways to room
	public void addDoorways( BoardCell cell) {
		doorways.add(cell);
	}
	
	//return list of doors
	public ArrayList<BoardCell> getDoorways(){
		return doorways;
	}
	
	//getters and setters for label cell, center cell, and passageway, and getter for name
	public void setLabelCell(BoardCell cell) {
		labelCell = cell;
	}
	
	public void setCenterCell(BoardCell cell) {
		centerCell = cell;
	}
	
	public void setPassageCell(char roomName) {
		hasPassage = true;
		passageName = roomName;
	}
	
	public char getPassageCell() {
		return passageName;
	}
	
	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public boolean hasPassage() {
		return hasPassage;
	}
	
	public String getName() {
		return name;
	}


}
