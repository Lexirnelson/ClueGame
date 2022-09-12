package tests;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;


import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.Solution;


public class ComputerAITest {

	
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	
	@Test
	public void checkComputerSuggestion( ) {
		
		/*
		 * set computer to be in the saloon center of the room 
		 */
		ComputerPlayer player1 = new ComputerPlayer("player1", Color.BLUE, 21,11);
		
		//another computer to test for multiple weapons and people missing
		ComputerPlayer player2 = new ComputerPlayer("player1", Color.BLUE, 21,11);
		
		/*
		 * test if only one weapon or person not seen, it's selected
		 */
		
			
		ArrayList<Card> mostWeapons = new ArrayList<Card>(board.getWeapons());
		mostWeapons.remove(0); //simply remove the first weapon
		
		ArrayList<Card> mostPeople = new ArrayList<Card>(board.getPeople());
		mostPeople.remove(0); //remove first person
		
		for (Card i : mostWeapons) {
			player1.updateSeen(i); //add all of the weapns left to seen
		}
		for (Card i : mostPeople) {
			player1.updateSeen(i); //add all of the people left to seen
		}
		
		
		player1.createSuggestion();
		assertEquals(player1.getSuggestion().weapon.getName() , board.getWeapons().get(0).getName()); //test that the weapon suggestion is the one removed 
		assertEquals(player1.getSuggestion().person.getName() , board.getPeople().get(0).getName()); //test that the suggestion is the one removed //test that the person suggested is on removed
		assertEquals(player1.getSuggestion().getRoom() , board.getRoomCard('S')); //test that the room is the one the player is in
		
		/*
		 * If multiple weapons not seen, one of them is randomly selected
		 */
		mostWeapons.remove(0);
		for (Card i : mostWeapons) {
			player2.updateSeen(i); //add all of the weapns left to seen
		}
		//test that the weapon suggested is either weapons 0 or weapons 1
		assertTrue(player1.getSuggestion().person.getName() == board.getPeople().get(0).getName() || player1.getSuggestion().person.getName() == board.getPeople().get(1).getName());
		
		
		/*
		 * If multiple persons not seen, one of them is randomly selected
		 */
		mostPeople.remove(0);
		for (Card i : mostPeople) {
			player2.updateSeen(i); //add all of the people left to seen
		}
		//test that the person suggested is either people 0 or people 1
		assertTrue(player1.getSuggestion().person.getName() == board.getPeople().get(0).getName() || player1.getSuggestion().person.getName() == board.getPeople().get(1).getName());
		
	}
	
	@Test
	public void checkComputerTarget() {
		
		ComputerPlayer player1 = new ComputerPlayer("player1", Color.BLUE, 12,7);
		
		//Check when there is an option of going into a room
		board.calcTargets(board.getCell(12,7), 3);
		Set<BoardCell> testTargets = board.getTargets();
		assertEquals(player1.selectTarget(testTargets, board.getRoomCards()), board.getCell(12,2));
		
		//Check when there is an option of going into a room but it is in the player's seen cards
		player1.updateSeen(board.getRoomCard('I'));
		assertFalse(player1.selectTarget(testTargets, board.getRoomCards()) == board.getCell(12,2));
		
		//Check when there is no option of a room to move into
		ComputerPlayer player2 = new ComputerPlayer("player2", Color.BLUE, 10,17);
		board.calcTargets(board.getCell(10,17), 1);
		testTargets = board.getTargets();
	
		
		/*
		 * Loop through and choose a target 100 times and check that each possible spot is 
		 * is chosen at least 5 times
		 */	
		int spot1 = 0;
		int spot2 = 0;
		int spot3 = 0;
	
		for(int i = 0; i < 100; i++) {
			if(player2.selectTarget(testTargets, board.getRoomCards()) == board.getCell(10,16)) {
				spot1++;
			}
			else if(player2.selectTarget(testTargets, board.getRoomCards()) == board.getCell(11,17)) {
				spot2++;
			}
			else if(player2.selectTarget(testTargets, board.getRoomCards()) == board.getCell(9,17)) {
				spot3++;
			}
		}
		
		assertTrue(spot1 > 5);
		assertTrue(spot2 > 5);
		assertTrue(spot3 > 5);
		
		
	}
	
	
	
	
	
	
	
	
}






