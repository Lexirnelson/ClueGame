package tests;

import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

class BoardTestsExp {
	private static TestBoard board;
	@BeforeEach
	//set up board before tests
	void boardSetup() {
		board = new TestBoard();
	}
	
	@Test
	//test adjacency for correct size and right elements with several different cases
	public void testAdjacency() {
		//test for adjacency in top left cell [0][0]
		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertEquals(2,testList.size());
	}
	
	@Test
	//test for adjacency in bottom right cell [3][3]
	public void testAdjacency1() {
		TestBoardCell cell = board.getCell(3, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertEquals(2,testList.size());
	}
	
	@Test
	public void testAdjacency2() {
		//test for adjacency in a right edge cell [1][3]
		TestBoardCell cell = board.getCell(1,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0, 3)));
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertEquals(3,testList.size());
	}
	
	@Test 
	public void testAdjacency3() {
		//test for adjacency in a left edge cell [3][0]
		TestBoardCell cell = board.getCell(3,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 0)));
		Assert.assertTrue(testList.contains(board.getCell(3, 1)));
		Assert.assertEquals(2,testList.size());
	}
	
	@Test
	public void testAdjacency4() {
		//test for adjacency in the middle at [2][2]
		TestBoardCell cell = board.getCell(2,2);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 1)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertEquals(4,testList.size());
	}
	
	
	@Test
	public void testTargetsNormal() {
		//test for all possible targets from [0][0] with roll 3
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(3,0)));
		Assert.assertTrue(targets.contains(board.getCell(2,1)));
		Assert.assertTrue(targets.contains(board.getCell(0,1)));
		Assert.assertTrue(targets.contains(board.getCell(1,2)));
		Assert.assertTrue(targets.contains(board.getCell(0,3)));
		Assert.assertTrue(targets.contains(board.getCell(1,0)));
	}
	
	@Test 
	public void testTargetsNormal1() {
		//test for all possible targets from [0][0] with roll 2
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0,2)));
		Assert.assertTrue(targets.contains(board.getCell(1,1)));
		Assert.assertTrue(targets.contains(board.getCell(2,0)));
	}
	
	@Test
	public void testTargetsNormal2() {
		//test for all possible targets from [3][3] with roll 3
		TestBoardCell cell = board.getCell(3, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(3,0)));
		Assert.assertTrue(targets.contains(board.getCell(2,1)));
		Assert.assertTrue(targets.contains(board.getCell(3,2)));
		Assert.assertTrue(targets.contains(board.getCell(1,2)));
		Assert.assertTrue(targets.contains(board.getCell(0,3)));
		Assert.assertTrue(targets.contains(board.getCell(2,3)));	
	}
		
		
	
	
	@Test 
	public void testTargetsMixed() {
		//set up occupied cell at [0][2]
		board.getCell(0, 2).setOccupied(true);
		//set up room at [1][2]
		board.getCell(1, 2).setRoom(true);
		//test for targets from [0][3] with roll 3
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell,3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
	}
	
	@Test
	public void testTargetsMixed1() {
		//set up occupied cell at [0][2]
		board.getCell(0, 2).setOccupied(true);
		//set up room at [1][2]
		board.getCell(1, 2).setRoom(true);
		//test for targets from [2][1] with roll 3
		TestBoardCell cell = board.getCell(2, 1);
		board.calcTargets(cell,3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
	}

	

}
