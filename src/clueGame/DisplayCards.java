package clueGame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DisplayCards extends JPanel{
	
	private static Board board = Board.getInstance();
	
	private ArrayList<String> peopleHand = new ArrayList<String>();
	private ArrayList<String> roomHand = new ArrayList<String>();
	private ArrayList<String> weaponHand = new ArrayList<String>();
	
	private ArrayList<String> peopleSeen = new ArrayList<String>();
	private ArrayList<String> roomSeen = new ArrayList<String>();
	private ArrayList<String> weaponSeen = new ArrayList<String>();

	public DisplayCards() {	
		CreateStringLists();
		setLayout(new GridLayout(3,0));
		JPanel people = CreatePeoplePanel();
		JPanel rooms = CreateRoomsPanel();
		JPanel weapons = CreateWeaponsPanel();
		add(people);
		add(rooms);
		add(weapons);
	}
	
	private void CreateStringLists() {

		
		ArrayList<Card> getHand = new ArrayList<Card>();
		ArrayList<Card> getSeen = new ArrayList<Card>();
		
		//get the instance of the human player
		Player human = board.getPlayers().get(0);
		
		getHand = human.getHand();
		getSeen = human.getSeen();
		
		for (Card i : getHand) {
			if (i.getType() == CardType.PERSON) {
				peopleHand.add(i.getName());
			}
			if (i.getType() == CardType.ROOM) {
				roomHand.add(i.getName());
			}
			if (i.getType() == CardType.WEAPON) {
				weaponHand.add(i.getName());
			}
			
		}
		for (Card i : getSeen) {
			if (i.getType() == CardType.PERSON) {
				peopleSeen.add(i.getName());
			}
			if (i.getType() == CardType.ROOM) {
				roomSeen.add(i.getName());
			}
			if (i.getType() == CardType.WEAPON) {
				weaponSeen.add(i.getName());
			}
		}
		
		
	}
	
	private JPanel CreatePeoplePanel() {
	
		JPanel panel = new JPanel();
		// Use a grid layout, 1 row, 1 elements (label, text)
		panel.setLayout(new GridLayout(peopleHand.size()+peopleSeen.size()+4,1));
		JLabel handLabel = new JLabel("In Hand: ");
		JLabel seenLabel = new JLabel("Seen: ");
		//add the people in hand
		panel.add(handLabel);
		if (peopleHand.size() > 0) {
			for (String i : peopleHand) {
				JTextField person = new JTextField(i);
				person.setEditable(false);
				panel.add(person);
			}
		}else {
			JTextField person = new JTextField("None");
			person.setEditable(false);
            panel.add(person);
		}
		
		//add the people in seen
		panel.add(seenLabel);
		if(peopleSeen.size() > 0) {
			for (String i : peopleSeen) {
				JTextField person = new JTextField(i);
				person.setEditable(false);
				panel.add(person);
			}
		}else {
			JTextField person = new JTextField("None");
			person.setEditable(false);
            panel.add(person);
		}
		
		panel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		return panel;
	}
	
	private JPanel CreateRoomsPanel() {
		
		JPanel panel = new JPanel();
		// Use a grid layout, 1 row, 1 elements (label, text)
		panel.setLayout(new GridLayout(roomHand.size()+roomSeen.size()+4,1));
		JLabel handLabel = new JLabel("In Hand: ");
		JLabel seenLabel = new JLabel("Seen: ");
		//add rooms in hand
		panel.add(handLabel);
		if (roomHand.size() > 0 ) {
			for (String i : roomHand) {
				JTextField room = new JTextField(i);
				room.setEditable(false);
				panel.add(room);
			}
		}else {
			JTextField room = new JTextField("None");
			room.setEditable(false);
            panel.add(room);
		}
		
		//add rooms in seen
		panel.add(seenLabel);
		if (roomSeen.size() > 0) {
			for (String i : roomSeen) {
				JTextField room = new JTextField(i);
				room.setEditable(false);
				panel.add(room);
			}
		}else {
			JTextField person = new JTextField("None");
			person.setEditable(false);
            panel.add(person);
		}
		
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		return panel;
	}
	
	private JPanel CreateWeaponsPanel() {

		JPanel panel = new JPanel();
		// Use a grid layout, 2 row, 1 elements
		panel.setLayout(new GridLayout(weaponHand.size()+weaponSeen.size()+4,1));
		JLabel handLabel = new JLabel("In Hand: ");
		JLabel seenLabel = new JLabel("Seen: ");
		
		//add weapons in hand
		panel.add(handLabel);
		if (weaponHand.size() > 0) {
			for (String i : weaponHand) {
				JTextField weapon = new JTextField(i);
				weapon.setEditable(false);
				panel.add(weapon);
			}
		}else {
			JTextField weapon = new JTextField("None");
			weapon.setEditable(false);
            panel.add(weapon);
		}
		
		//add rooms in seen
		panel.add(seenLabel);
		if (weaponSeen.size() > 0) {
			for (String i : weaponSeen) {
				JTextField weapon = new JTextField(i);
				weapon.setEditable(false);
				panel.add(weapon);
			}
		}else {
			JTextField weapon = new JTextField("None");
			weapon.setEditable(false);
			panel.add(weapon);
		}
		
		
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		return panel;
	}
/*
	public static void main(String[] args) {	
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(200, 650);
		// Create the JPanel and add it to the JFrame
		DisplayCards gui = new DisplayCards();
		frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
		}
		*/
}
	

