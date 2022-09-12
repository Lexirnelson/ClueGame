package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class ComputerPlayer extends Player {
	
	private Boolean shouldAccuse = false;

	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}
	
	public void createSuggestion() {
		//get the board card 
		Card room = Board.getInstance().getRoomCard(Board.getInstance().getCell(row, column));
		ArrayList<String> cardStrings = new ArrayList<>();
		ArrayList<Card> possibleWeapons = new ArrayList<Card>(); //array list to store possible weapons
		ArrayList<Card> possiblePeople = new ArrayList<Card>(); //array list to store possible people
		
		
		//add the strings of the card names in seen
		for (Card i : seen) {
			cardStrings.add(i.getName());
		}
		
		//add possibilities by comparing seen hand with the cards in the full deck
		for (Card i : Board.getInstance().getDeck()) {
			if(!cardStrings.contains(i.getName())) {
				if (i.getType() == CardType.WEAPON) {
					possibleWeapons.add(i);
				}
				if (i.getType() == CardType.PERSON) {
					possiblePeople.add(i);
				}
			}
		}
		
		//pick randomly from weapons
		Random rand = new Random();
		int cardPlace = rand.nextInt(possibleWeapons.size());
		Card weapon = possibleWeapons.get(cardPlace);
		
		//pick randomly from people
		int cardPlace1 = rand.nextInt(possiblePeople.size());
		Card person = possiblePeople.get(cardPlace1);
			
		currentSuggestion = new Solution(person, room, weapon);
		
		
	}
	
	public BoardCell selectTarget(Set<BoardCell> targets, Map<Character, Card> roomCards) {
		
		ArrayList<BoardCell> possibleTargets = new ArrayList<BoardCell>();
		
		//Test for possible targets that are rooms
		for(BoardCell cell : targets) {
			if(cell.isRoomCenter()) {				
				//Test that possible room target is already in seen deck
				if(!seen.contains(roomCards.get(cell.getSymbol()))){
					possibleTargets.add(cell);
				}
			}
		}
		
		//Returns randomly chosen room if there are multiple possibles moves
		if(possibleTargets.size() > 0) {
			Random rand = new Random();
			int randIndex = rand.nextInt(possibleTargets.size());	
			isInRoom = true;
			return possibleTargets.get(randIndex);
		}
		//Returns random move from targets if there are no possible rooms to move to
		else {
			isInRoom = false;
			Random rand = new Random();
			int randIndex = rand.nextInt(targets.size());	
			int counter = 0;
			for( BoardCell cell : targets) {
				if(counter == randIndex) {
					return cell;
				}
				counter++;
			}
		}
		return null;
	}
	
	
	public Boolean getShouldAccuse() {
		return shouldAccuse;
	}

	public void setShouldAccuse(Boolean shouldAccuse) {
		this.shouldAccuse = shouldAccuse;
	}
	
}
