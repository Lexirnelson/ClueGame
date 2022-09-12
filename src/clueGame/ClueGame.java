package clueGame;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ClueGame extends JFrame {
	
	GameControlPanel gameControlPanel;
	DisplayCards cardsPanel;
	
	public ClueGame( Board board, GameControlPanel controlPanel ) {
		JOptionPane.showMessageDialog(null , "You are the citizen \n can you find the solution \n before the computers?", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE); //displays welcome to clue game
		DisplayCards cardPanel = new DisplayCards(); // create the cards panel
		add(board, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
		add(cardPanel, BorderLayout.EAST);
		setSize(new Dimension(1900,1050));	// set size of frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		setVisible(true); // make it visible
	}
		
	
	public static void main (String[] args) {		
		//Create board instance
		Board board;
		GameControlPanel controlPanel;		
		board = Board.getInstance();
		controlPanel = GameControlPanel.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();	
		controlPanel.initialize();
		
		JFrame frame = new ClueGame(board, controlPanel);  // create the frame				
	}	
	
}
