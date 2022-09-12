package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
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

	// Ensure that player does not move around within room
	// These cells are YELLOW on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the church that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(2, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(2, 6)));
		assertTrue(testList.contains(board.getCell(21, 11)));
		
		// now test the general store 
		testList = board.getAdjList(11, 20);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(16, 22)));
		assertTrue(testList.contains(board.getCell(12, 17)));
		
		// one more room, the Barber shop
		testList = board.getAdjList(22, 23);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(22, 20)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are YELLOW on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(12, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(11, 6)));
		assertTrue(testList.contains(board.getCell(13, 6)));
		assertTrue(testList.contains(board.getCell(12, 7)));
		assertTrue(testList.contains(board.getCell(12, 2)));

		testList = board.getAdjList(25, 9);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(25, 8)));
		assertTrue(testList.contains(board.getCell(21, 11)));

		
		testList = board.getAdjList(16, 22);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 21)));
		assertTrue(testList.contains(board.getCell(16, 23)));
		assertTrue(testList.contains(board.getCell(17, 22)));
		assertTrue(testList.contains(board.getCell(11, 20)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are RED on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(25, 16);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(25, 17)));
		assertTrue(testList.contains(board.getCell(25, 15)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(16, 12);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 13)));
		assertTrue(testList.contains(board.getCell(16, 11)));
		assertTrue(testList.contains(board.getCell(17, 12)));
		assertTrue(testList.contains(board.getCell(15, 12)));

		// Test adjacent to walkways
		testList = board.getAdjList(4, 19);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(4, 20)));
		assertTrue(testList.contains(board.getCell(4, 18)));
		assertTrue(testList.contains(board.getCell(5, 19)));
		assertTrue(testList.contains(board.getCell(3, 19)));

		// Test next to closet
		testList = board.getAdjList(10,14);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(11, 14)));
		assertTrue(testList.contains(board.getCell(9, 14)));
		assertTrue(testList.contains(board.getCell(10, 15)));
	
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are DARK GREEN on the planning spreadsheet
	@Test
	public void testTargetsInPostOffice() {
		// test a roll of 1
		board.calcTargets(board.getCell(6, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(4, 2)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(6, 2), 3);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(4, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));	
		assertTrue(targets.contains(board.getCell(4, 4)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(6, 2), 4);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(4, 1)));	
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(4, 5)));	
	}
	
	@Test
	public void testTargetsInBank() {
		// test a roll of 1
		board.calcTargets(board.getCell(19, 1), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(19, 3)));
		assertTrue(targets.contains(board.getCell(2, 22)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(19, 1), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(18, 4)));	
		assertTrue(targets.contains(board.getCell(19, 5)));
		assertTrue(targets.contains(board.getCell(2, 22)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(19, 1), 4);
		targets = board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(16, 3)));
		assertTrue(targets.contains(board.getCell(19, 4)));	
		assertTrue(targets.contains(board.getCell(18, 3)));
		assertTrue(targets.contains(board.getCell(2, 22)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are DARK GREEN on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(17, 11), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(17, 10)));
		assertTrue(targets.contains(board.getCell(17, 12)));	
		assertTrue(targets.contains(board.getCell(21, 11)));	
		assertTrue(targets.contains(board.getCell(16, 11)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(17, 11), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(17, 8)));
		assertTrue(targets.contains(board.getCell(17, 10)));
		assertTrue(targets.contains(board.getCell(16, 11)));	
		assertTrue(targets.contains(board.getCell(16, 13)));
		assertTrue(targets.contains(board.getCell(21, 11)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(17, 11), 4);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(15, 11)));
		assertTrue(targets.contains(board.getCell(17, 15)));
		assertTrue(targets.contains(board.getCell(16, 14)));	
		assertTrue(targets.contains(board.getCell(17, 7)));
		assertTrue(targets.contains(board.getCell(21, 11)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(10, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(10, 1)));
		assertTrue(targets.contains(board.getCell(10, 3)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(10, 2), 3);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(10, 5)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(10, 2), 4);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(9, 5)));
		assertTrue(targets.contains(board.getCell(10, 6)));	
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(13, 7), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(13, 6)));
		assertTrue(targets.contains(board.getCell(12, 7)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(13, 7), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(12, 2)));
		assertTrue(targets.contains(board.getCell(14, 7)));
		assertTrue(targets.contains(board.getCell(11, 8)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(13, 7), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(14, 4)));
		assertTrue(targets.contains(board.getCell(12, 2)));
		assertTrue(targets.contains(board.getCell(11, 7)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(15, 7).setOccupied(true);
		board.calcTargets(board.getCell(13, 7), 4);
		board.getCell(15, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(14, 4)));
		assertTrue(targets.contains(board.getCell(12, 2)));
		assertTrue(targets.contains(board.getCell(11, 7)));	
		assertFalse(targets.contains(board.getCell(14, 5))) ;
		assertFalse(targets.contains(board.getCell(16, 7))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(2, 11).setOccupied(true);
		board.getCell(7, 13).setOccupied(true);
		board.calcTargets(board.getCell(7, 12), 1);
		board.getCell(2, 11).setOccupied(false);
		board.getCell(7, 13).setOccupied(false);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(2, 11)));	
		assertTrue(targets.contains(board.getCell(7, 11)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(22, 20).setOccupied(true);
		board.calcTargets(board.getCell(22, 23), 3);
		board.getCell(22, 20).setOccupied(false);
		targets= board.getTargets();
		assertEquals(0, targets.size());

	}
	


}
