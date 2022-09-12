package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.AfterClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

class gameSetupTests {
	
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
	
	//Load people from ClueSetup.txt and insure the data was loaded properly
	//Create Player class with human and computer child classes
	//Use people data to instantiate 6 players (1 human and 5 computer) 
	@Test
	public void testPeople() {
		//create a test that can verify we have 6 players, 1 human and 5 computers
		ArrayList<Player> testPeople = board.getPlayers();
		assertEquals(6,testPeople.size());

		//make sure that the characters are loaded in correctly
		assertEquals(testPeople.get(0).getName(), "Citizen");
		assertEquals(testPeople.get(5).getName(), "Bartender");
		
		//check that there are computers and 1 player		
		assertTrue(testPeople.get(0) instanceof HumanPlayer);
		assertTrue(testPeople.get(5) instanceof ComputerPlayer);
	}
	
	//Load weapons from ClueSetup.txt and insure the data was loaded properly
	//Create complete deck of cards (weapons, people and rooms) 
	//Deal cards to the Answer and the players (all cards dealt, players 
	//have roughly same # of cards, no card dealt twice)
	
	/*
	 * tests that the deck is initialized and loaded properly, and ensures that cards are dealt to answer and players
	 */
	@Test
	public void testCards() {
		ArrayList<Card> weaponsTest = new ArrayList<Card>();
		ArrayList<Card> deckTest = new ArrayList<Card>();
		
		weaponsTest = board.getWeapons();
		deckTest = board.getDeck();
		
		
		//test if Gun is in weapons
		assertEquals(weaponsTest.get(0).getName(), "Gun" );
		//test if horseshoe is in weapons
		assertEquals(weaponsTest.get(2).getName(), "Horseshoe");
		
		//test that deck of cards contains weapons, people, and rooms
		assertEquals(deckTest.get(0).getType(), CardType.ROOM);
		assertEquals(deckTest.get(10).getType(), CardType.PERSON);
		assertEquals(deckTest.get(20).getType(), CardType.WEAPON);		

	}
	
	@Test
	public void testShuffle() {
		Solution answerTest = board.getSolution();
		ArrayList<Player> testPeople = new ArrayList<Player>();
		ArrayList<Card> dealtCards = new ArrayList<Card>();
		ArrayList<Card> tempHand = new ArrayList<Card>();
		
		testPeople = board.getPlayers();
		
		
		//test that there is an answer in format room, person, weapon
		assertEquals(answerTest.getRoom().getType(), CardType.ROOM);
		assertEquals(answerTest.getPerson().getType(), CardType.PERSON);
		assertEquals(answerTest.getWeapon().getType(), CardType.WEAPON);
		//make sure answers is only size 3
		
		//test that players have same amount of cards roughly, there are no repeat dealt cards, and that they add up to 18
		int numCards = testPeople.get(0).getHand().size();
		int numDealt = 0;
		
		for (Player i : testPeople){
			tempHand = i.getHand();
			for(Card j : tempHand) {
				assertFalse(dealtCards.contains(j));
				dealtCards.add(j);
			}
						
			assertTrue(i.getHand().size() == numCards ||  i.getHand().size() == numCards-1);
			numDealt += i.getHand().size();
		}
		assertEquals(numDealt, 18);
		
		//test that players don't have the same cards		
	}

	
}
