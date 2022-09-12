package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class AccusationSuggestionDialog extends JDialog {
	
	private JComboBox<String> roomCombo, personCombo, weaponCombo;
	private Board board;
	private GameControlPanel controlPanel;
	private Solution solution = new Solution();
	private String guess;
	private String guessResult;
	public AccusationSuggestionDialog() {
		
		board = Board.getInstance();
		controlPanel = GameControlPanel.getInstance();

		
		//get the room
		ArrayList<Card> rooms = new ArrayList<Card>(board.getRooms());
		//get the players
		ArrayList<Player> players = new ArrayList<Player>(board.getPlayers());
		//get the weapons
		ArrayList<Card> weapons = new ArrayList<Card>(board.getWeapons());
	
		setTitle("Make a suggestion");
		setSize(300,200);
		setLayout(new GridLayout(4,4));
		JLabel roomLabel = new JLabel("Room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");	
		JButton cancelButton = new JButton("Cancel");
		JButton submitButton = new JButton("Submit");
		JLabel roomText = new JLabel(board.getRoomCard(board.getPlayerLocation(board.getCurrentPlayer())).getName());
		roomCombo = new JComboBox<String>();
		personCombo = new JComboBox<String>();
		weaponCombo = new JComboBox<String>();
		
		for( Card room : rooms) {
			roomCombo.addItem(room.getName());
		}
		
		for (Player player : players) {
			personCombo.addItem(player.getName());
		}
		
		for (Card weapon : weapons) {
			weaponCombo.addItem(weapon.getName());
		}
		
		
		//Add corresponding listeners
		SubmitButtonListener submitListener = new SubmitButtonListener();
		submitButton.addActionListener(submitListener);
		
		CancelButtonListener cancelListener = new CancelButtonListener();
		cancelButton.addActionListener(cancelListener);
		
		SubmitAccuseListener listener = new SubmitAccuseListener();
		
		roomCombo.addActionListener(listener);
		personCombo.addActionListener(listener);
		weaponCombo.addActionListener(listener);
				
		add(roomLabel);
		
		//Adds either the room comboBox or TextField depending on if the user is making an accusation or a suggestion
		if(board.getCanAccuse()) {
			setTitle("Make an Accusation");
			add(roomCombo);
		}
		else {
			add(roomText);
		}
		
		add(personLabel);
		
		add(personCombo);
		
		add(weaponLabel);
		
		add(weaponCombo);
	
		add(cancelButton);
		
		add(submitButton);
		
	}


	//Combo Listener for accusation/suggestion dialogs
	private class SubmitAccuseListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {		
				
				if(!board.getCanAccuse()) {
					solution.setRoom(board.getRoomCard(board.getPlayerLocation(board.getCurrentPlayer()))); //Default sets accusation/solution answer to the current room
				}
				
				/*
				 * Sets room,person,and weapon cards of solution depending on the users selections
				 */
				if(e.getSource() == roomCombo) {
					solution.setRoom(board.getCardByName(roomCombo.getSelectedItem().toString()));
				}
				else if(e.getSource() == personCombo) {
					solution.setPerson(board.getCardByName(personCombo.getSelectedItem().toString()));
				}
				else if(e.getSource() == weaponCombo) {
					solution.setWeapon(board.getCardByName(weaponCombo.getSelectedItem().toString()));
				}
		}
	}
		
		//Submit button listener on accusation dialog listener
		private class SubmitButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				/*
				 * Sets current suggestion of human player to comboBox selections
			 	* Displays the guess
			 	* Displays the result of the guess
			 	*/
				board.getCurrentPlayer().setSuggestion(solution);
			
				if(board.getCanAccuse()) { //Accusation tested against answer
					if(!board.checkAccusation(solution)) {
					}
				}
				else { //Suggestion displayed
					board.setIsTurnComplete(true);
					guess = board.getCurrentPlayer().getSuggestion().displaySolution();
					board.handleSuggestion(solution);
					guessResult = board.displayDisprovenCard();
					controlPanel.setGuess(guess);
					controlPanel.setGuessResult(guessResult);
				}	
			}
		}
		
		//Cancel button listener on accusation dialog listener
		private class CancelButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(board.getCanAccuse()) {
					setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null , "You must make a suggestion before ending your turn!", "ERROR", JOptionPane.ERROR_MESSAGE); 
				}
			}
		}
		
		public String getGuess() {
			return guess;
		}

		public String getGuessResult() {
			return guessResult;
		}
	
		
}
