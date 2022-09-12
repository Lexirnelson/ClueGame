package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class GameControlPanel extends JPanel{
	
	//Initialize all buttons, labels, and text fields that will be used to create the layout of the entire panel
	private JLabel whoseTurn = new JLabel("Whose turn?");
	private JTextField player = new JTextField();
	private JLabel roll = new JLabel("Roll:");
	private JTextField diceNumber = new JTextField();
	private JButton makeAccusation = new JButton("Make accusation");
	private JButton next = new JButton("NEXT!");
	private JTextField guess = new JTextField();
	private JTextField guessResult = new JTextField();
	private Board board;
	private AccusationSuggestionDialog accusationDialog;
	
	//Implement singleton design pattern
    private static GameControlPanel theInstance = new GameControlPanel();
    // constructor is private to ensure only one can be created
    private GameControlPanel() {
           super();
    }
    
       
    // this method returns the only Board
    public static GameControlPanel getInstance() {
           return theInstance;
    }

	public void initialize() {
		
		board = Board.getInstance();
		
		//Handle initial human player turn 
		board.setCurrentRoll(board.rollDice());
		setDiceNumber(String.valueOf(board.getCurrentRoll()));
		setPlayerName(board.getCurrentPlayerName());
		board.calcTargets(board.getPlayerLocation(board.getCurrentPlayer()), board.getCurrentRoll());
		
		//creates the panel
		setLayout( new GridLayout(2,2));
		add(createTurnPanel());
		add(decisionPanel());
		add(guessPanel());
		add(guessResultPanel());
		
		//Makes it so that none of the text fields can be edited when the panel is presented to the user
		diceNumber.setEditable(false);
		player.setEditable(false);
		guess.setEditable(false);
		guessResult.setEditable(false);
		
		//Create listeners		
		NextButtonListener listener = new NextButtonListener();
		next.addActionListener(listener);
		MakeAccusationListener accusationListener = new MakeAccusationListener();
		makeAccusation.addActionListener(accusationListener);
				
	}
	
	//Creates upper left panel
	private JPanel createTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(createNamePanel());
		panel.add(createRollPanel());
		return panel;
	}
	
	//Creates panel with the current player 
	private JPanel createNamePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		panel.add(whoseTurn);
		panel.add(player);
		return panel;
	}
	
	//Creates panel with Roll and diceNumber
	private JPanel createRollPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(roll);
		panel.add(diceNumber);
		return panel;
	}
	
	//Creates upper right panel
	private JPanel decisionPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		
		panel.add(makeAccusation);
		panel.add(next);
		return panel;
	}
	
	
	//Creates lower left panel
	private JPanel guessPanel() {
		Border border = BorderFactory.createTitledBorder("Guess");
		JPanel panel = new JPanel();
		panel.setBorder(border);
		panel.setLayout(new GridLayout(1,1));
		panel.add(guess);
		return panel;
	}
	
	
	//Creates lower right panel
	private JPanel guessResultPanel() {
		Border border = BorderFactory.createTitledBorder("Guess Result");
		JPanel panel = new JPanel();
		panel.setBorder(border);
		panel.setLayout(new GridLayout(1,1));
		panel.add(guessResult);
		return panel;
	}
	
	// Setters for all text fields
	public void setGuess(String message) {
		guess.setText(message);
	}
	
	public void setGuessResult(String message) {
		guessResult.setText(message);
	}
	
	private void setPlayerName(String message) {
		player.setText(message);
	}
	
	private void setDiceNumber(String num) {
		diceNumber.setText(num);
	}
	
	
	private class NextButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent event)
		{ 
				if(board.getDone()) {		
					board.nextTurn(); // Update turn information
					setDiceNumber(String.valueOf(board.getCurrentRoll())); // Set a new dice roll
					setPlayerName(board.getCurrentPlayerName()); // Sets the current player name
				}
				else {
					JOptionPane.showMessageDialog(null , "The player's turn is not done yet!", "ERROR", JOptionPane.ERROR_MESSAGE); 
				}
				
				if(board.getCurrentPlayer().getIsInRoom()) {
					if(board.getCurrentPlayer().getSuggestion()!= null) {
						setGuess(board.getCurrentPlayer().getSuggestion().displaySolution());
						setGuessResult(board.displayDisprovenCard());
					}
					
				}
				else {
					setGuess("");
					setGuessResult("");
				}
		}
	}
	
	
	//listens for the make accusation button
	private class MakeAccusationListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {		
			if(board.getCanAccuse()) {
				accusationDialog = new AccusationSuggestionDialog();
				accusationDialog.setVisible(true);			
			}
			else {
				JOptionPane.showMessageDialog(null , "You cannot make an accusation at this time, wait until your next turn.", "ERROR", JOptionPane.ERROR_MESSAGE); 
			}			
		}
	}
	
			
	
}
