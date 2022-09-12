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
import javax.swing.JTextField;


public class AccusationDialog extends JDialog{
	private JButton submitButton;
	private JButton cancelButton;

	private JComboBox<String> personCombo;
	private JComboBox<String> weaponCombo;
	Board board = Board.getInstance();
	
	public AccusationDialog() {
		
		//get the players
		ArrayList<Player> players = new ArrayList<Player>(board.getPlayers());
		//get the weapons
		ArrayList<Card> weapons = new ArrayList<Card>(board.getWeapons());

		//listen for submit or cancel
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new submitListener());

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new cancelListener());

		setTitle("Make an Accusation");
		setSize(300,200);
		setLayout(new GridLayout(4,4));

		JLabel roomLabel = new JLabel("Current Room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");		
		JTextField room = new JTextField(board.getRoomCard(board.getPlayerLocation(board.getCurrentPlayer())).getName());
		room.setEditable(false);

		personCombo = new JComboBox<String>();
		for (Player player : players) {
			personCombo.addItem(player.getName());
		}

		weaponCombo = new JComboBox<String>();
		for (Card weapon : weapons) {
			weaponCombo.addItem(weapon.getName());
		}

		add(roomLabel);
		add(room);
		add(personLabel);
		add(personCombo);
		add(weaponLabel);
		add(weaponCombo);	
		add(submitButton);
		add(cancelButton);
	}

	//for the cancel buttons on accusation and suggestion
	private class cancelListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	}

	//for the submit buttons on accusation and suggestion
	private class submitListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			//string selected from the combo box
			String personAccused = personCombo.getSelectedItem().toString();
			String weaponAccused = weaponCombo.getSelectedItem().toString();

			//card variables to be used in an accusation
			Card room = board.getRoomCard(board.getPlayerLocation(board.getCurrentPlayer()));
			Card person = null;
			Card weapon = null;

			for(Card c: Board.getInstance().getDeck()) {
				if(c.getName() == personAccused) {
					person = c;
				}
				if(c.getName() == weaponAccused) {
					weapon = c;
				}
			}
			Solution accusation = new Solution(person,room,weapon);
			boolean check = board.checkAccusation(accusation);
			if(check) {
				JButton ok = new JButton();
				JOptionPane.showMessageDialog(ok, "You guessed correct! \n You win!");
				System.exit(0);
			}else {
				JButton ok = new JButton();
				JOptionPane.showMessageDialog(ok, "You guessed wrong... \n You lose.");
				System.exit(0);
			}


		}
	}


}

