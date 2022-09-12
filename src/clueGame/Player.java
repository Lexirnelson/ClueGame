package clueGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Player {
	private String name;
	private Color color;
	protected int row,column;
	protected Boolean isInRoom;
	private ArrayList<Card> hand = new ArrayList<Card>();
	protected ArrayList<Card> seen = new ArrayList<Card>();
	protected ArrayList<Card> possibleCards = new ArrayList<Card>();
	protected Solution currentSuggestion;
	private Boolean canStayInRoom = false;
	
	
	
	public Player(String name, Color color, int row, int column) {
		super();
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		this.isInRoom = false;
		canStayInRoom = false;
	}

	public void updateHand(Card card) {
		hand.add(card);
	}

	
	public void draw(Graphics boardGraphic, int cellWidth, int cellHeight) {
		boardGraphic.setColor(color);
		boardGraphic.fillOval(cellWidth * column, cellHeight * row, cellWidth, cellHeight);
	}
	
	public void setPlayerLocation(BoardCell cell) {
		row = cell.getRow();
		column = cell.getColumn();
	}
	
	
	public String getName() {
		return name;
	}

	public Boolean getIsInRoom() {
		return isInRoom;
	}
	
	public void setIsInRoom(boolean b) {
		isInRoom = b;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public void updateSeen(Card card) {
		seen.add(card);
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public ArrayList<Card> getSeen(Card card) {
		return seen;
	}
	
	public ArrayList<Card> getSeen() {
		return seen;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
		
	public void setSuggestion(Solution solution) {
		currentSuggestion = solution;
	}
	
	public Solution getSuggestion() {		
		return currentSuggestion;
	}

	
	public Boolean getCanStayInRoom() {
		return canStayInRoom;
	}

	public void setCanStayInRoom(Boolean canStayInRoom) {
		this.canStayInRoom = canStayInRoom;
	}

	public Card disproveSuggestion(Solution solution) {
		if(hand.contains(solution.getPerson())){
			possibleCards.add(solution.getPerson());
		}
		
		if(hand.contains(solution.getWeapon())){
			possibleCards.add(solution.getWeapon());
		}
		
		if(hand.contains(solution.getRoom())){
			possibleCards.add(solution.getRoom());
		}
		
		if(possibleCards.size() > 0 ) {
			Random rand = new Random();
			int randIndex = rand.nextInt(possibleCards.size());	
			return possibleCards.get(randIndex);
		} 
		return null;
	}



	
	
}
