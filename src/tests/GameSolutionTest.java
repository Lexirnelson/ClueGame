package tests;


import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

class GameSolutionTest {

	private static Board board;
	private static Card sheriff, banker, cowboy, bandit, bartender, citizen, saloon, sheriffStation, bank, townHall, 
	generalStore, church, gun, knife, horseShoe, bottle, bible, pitchfork;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		board.setPeopleRoomsWeapons0();
		
		
		//Create cards for all persons
		sheriff = new Card("Sheriff", CardType.PERSON);
		banker = new Card("Banker", CardType.PERSON);
		cowboy = new Card("Cowboy", CardType.PERSON);
		bandit = new Card("Bandit", CardType.PERSON);
		bartender = new Card("Bartender", CardType.PERSON);
		citizen = new Card("Citzen", CardType.PERSON);
		
	
		//Create room cards
		saloon = new Card("Saloon", CardType.ROOM);
		sheriffStation = new Card("Sheriff Station", CardType.ROOM);
		bank = new Card("Bank", CardType.ROOM);
		townHall = new Card("Town Hall", CardType.ROOM);
		generalStore = new Card("General Store", CardType.ROOM);
		church = new Card("Church", CardType.ROOM);
		
		//6 weapons 
		gun = new Card("Gun", CardType.WEAPON);
		knife = new Card("Knife", CardType.WEAPON);
		horseShoe = new Card("HorseShoe", CardType.WEAPON);
		bottle = new Card("Bottle", CardType.WEAPON);
		bible = new Card("Bible", CardType.WEAPON);
		pitchfork = new Card("Pitchfork", CardType.WEAPON);
		
		
	}
	
	@Test
	public void checkAccusation() {
		
		board.setAnswer(banker, bank, bible);
		
		//Solution that is correct
		assertTrue(board.checkAccusation(new Solution(banker, bank, bible)));
		
		//Wrong person
		assertFalse(board.checkAccusation(new Solution(bandit, bank, bible)));
		
		//Wrong room
		assertFalse(board.checkAccusation(new Solution(banker, church, bible)));
		
		//Wrong weapon
		assertFalse(board.checkAccusation(new Solution(banker, bank, gun)));	
		
	}
	
	
	@Test
	public void checkDisproveSuggestions() {
		Player player = new HumanPlayer("player", Color.RED, 0, 0);
		
		// Player has no matching cards
		player.updateHand(bank);
		assertEquals(player.disproveSuggestion(new Solution(bandit, townHall, gun)), null);
		
		// Player has one matching card
		player.updateHand(bandit);
		assertEquals(player.disproveSuggestion(new Solution(bandit, townHall, gun)), bandit);
		
		/*
		 * Player has two matching cards
		 * Loop through 100 times and make sure each possible card is being shown at least five times
		 */
		player.updateHand(gun);
		int card1 = 0;
		int card2 = 0;
		for(int i = 0; i < 100; i++) {
			if(player.disproveSuggestion(new Solution(bandit, townHall, gun)) == bandit ){
				card1++;
			}
			else if( player.disproveSuggestion(new Solution(bandit, townHall, gun)) == gun ){
				card2++;
			}
		}
		assertTrue(card1 > 5);
		assertTrue(card2 > 5);
		
	}

	@Test
	public void testHandleSuggestion() {
		//a group of players to test disproving hands
		Player human = new HumanPlayer("player", Color.RED, 0, 0);
		Player computer1 = new ComputerPlayer("1", Color.WHITE, 1,1);
		Player computer2 = new ComputerPlayer("2", Color.GREEN, 1,2);
		
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		tempPlayers.add(human);
		tempPlayers.add(computer1);
		tempPlayers.add(computer2);
		
		board.setPlayers(tempPlayers);
		
		//create a solution
		board.setAnswer(banker, bank, bottle);
		
		//add some cards to hands
		human.updateHand(bandit);
		computer1.updateHand(church);
		computer2.updateHand(gun);
		
		//test when no players can disprove
		assertEquals(board.handleSuggestion(new Solution(bartender, saloon, pitchfork)), null);
		
		//test when only accusing player can disprove, ensure null
		assertEquals(board.handleSuggestion(new Solution(bandit, saloon, pitchfork)), null);
		
		//player 0 = accuser & player 1 and 2 can disprove
		assertEquals(board.handleSuggestion(new Solution(bandit, church, gun)).getName(), "Church");	

	}
	
	

	
}
