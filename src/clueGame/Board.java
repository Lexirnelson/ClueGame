package clueGame;

import java.util.Map;
import java.util.Random;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Board extends JPanel{
	
	private int numRows;
	private int numColumns;
	private BoardCell[][] grid;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character,Room> roomMap = new HashMap<Character,Room>();
	private Map<Character, Card> roomCards = new HashMap<Character, Card>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private ArrayList<Player> players;
	private ArrayList<Card> weapons;
	private ArrayList<Card> people;
	private ArrayList<Card> rooms;
	private ArrayList<Card> deck;
	private Solution theAnswer;
	private Solution currentAccusation;
	private boolean isTurnComplete = false; //boolean to keep track of human player's turn to be updated when handling the player movements
	private boolean canAccuse = true;
	private boolean endedLastTurnInRoom;
	private int currentPlayerIndex = -1;
	private static final int DICE = 6;
	private int currentRoll;
	private int cellWidth;
	private int cellHeight;
	private boolean showTargets = true;	
	private AccusationSuggestionDialog accusationDialog;
	private GameControlPanel controlPanel;
	
    /*
     * variable and methods used for singleton pattern
     */
     private static Board theInstance = new Board();
     // constructor is private to ensure only one can be created
     private Board() {
            super();
     }
     
     
     
     // this method returns the only Board
     public static Board getInstance() {
            return theInstance;
     }

     /*
      * initialize the board (since we are using singleton pattern)
      */
     public void initialize()
     {
    	 controlPanel = GameControlPanel.getInstance();
    	 
    	 players = new ArrayList<Player>();
    	 weapons = new ArrayList<Card>();
    	 people = new ArrayList<Card>();
    	 rooms = new ArrayList<Card>();
    	 deck = new ArrayList<Card>();
    	 theAnswer = new Solution();
    	 
    	 try {
    	 loadSetupConfig();
    	 loadLayoutConfig();
    	 shuffleDeck();
    	 currentPlayerIndex = 0;
    	 
    	 }
    	 catch (BadConfigFormatException e) {
    		System.out.println(e); 
    	 }
    	 calcAdjacencies(); 	 
    	 
    	 
    	//Create listeners		
 		mouseListenerHandling listener = new mouseListenerHandling();
 		addMouseListener(listener);
     }   
     
     
     public void loadSetupConfig() throws BadConfigFormatException { //read setup line by line
		try {
		      File myObj = new File(setupConfigFile);
		      Scanner myReader = new Scanner(myObj);
		      
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        
		        if (!data.contains("//")) {
		        	String[] dataArray = data.split(", ");
		        	
		        	if(dataArray[0].equals( "Room" ) || dataArray[0].equals("Space")){
		        		roomMap.put(dataArray[2].charAt(0), new Room(dataArray[1]));
		        		
		        		Card tempRoomCard = new Card(dataArray[1], CardType.ROOM);
		        		roomCards.put(dataArray[2].charAt(0), tempRoomCard);
		        		
		        		if(!dataArray[0].equals("Space")) {
		        			deck.add(tempRoomCard); //only add to deck if a room and not a walkway
		        			rooms.add(new Card(dataArray[1], CardType.ROOM));
		        		}		        		
 		        	}
		        	else if(dataArray[0].equals("Player")) {
		        		players.add(createPlayer(dataArray));
		        		deck.add(new Card(dataArray[1], CardType.PERSON));
		        		people.add(new Card(dataArray[1], CardType.PERSON));
		        	}
		        	else if(dataArray[0].equals("Weapon")){
		        		weapons.add(new Card(dataArray[1], CardType.WEAPON));
		        		deck.add(new Card(dataArray[1], CardType.WEAPON));
		        	}
		        	else {
		        		throw new BadConfigFormatException("Error: Invalid File Format");
		        	}	        	
		        }		        
		      }
		      myReader.close();
		      
		    } catch (FileNotFoundException e) {
		      System.out.println("File not read");
		    }
	}
     

     
     
    //get randomized solution, shuffle deck, and distribute cards
    public void shuffleDeck() {
    	int numRooms = rooms.size();
    	int numPeople = people.size();
    	int numWeapons = weapons.size();
    	
    	Random rand = new Random();
    	ArrayList<Card> tempDeck = new ArrayList<Card>(deck); //tempdeck we can remove cards from 
    	
    	//select room from 0-8 in deck
    	int cardPlace = rand.nextInt(numRooms);
    	theAnswer.setRoom(tempDeck.get(cardPlace));
    	tempDeck.remove(cardPlace);
    	
    	//select player from 8-13 (8-13 because we remove a card from room)
    	if(numPeople > 0) {
    		int cardPlace1 = rand.nextInt(numPeople)+numRooms - 1;
    		theAnswer.setPerson(tempDeck.get(cardPlace1));
    		tempDeck.remove(cardPlace1);
    	}
    	//select weapon from 13-18 (13-18 because we remove a card from people)
    	if(numWeapons > 0) {
    		int cardPlace2 = rand.nextInt(numWeapons)+(numPeople+numRooms) - 2;
    		theAnswer.setWeapon(tempDeck.get(cardPlace2));
    		tempDeck.remove(cardPlace2);
    	}
    	//distribute cards
    	if(numPeople > 0) {
    		while(tempDeck.size() > 0) {
    			for (int i = 0; i < players.size(); i++) {
    				if(tempDeck.size() > 0) {
    					int shufflePlace = rand.nextInt(tempDeck.size());
    					players.get(i).updateHand(tempDeck.get(shufflePlace));
    					tempDeck.remove(shufflePlace);
    				}
    			}
    		}
    	}
    	
    }
	
	//Converts string color to Color color
	public Color convertToColor(String color) {
		
		Color returnColor = null;
		
		switch(color) {
		
		case "Black":
			returnColor = Color.BLACK;
			break;
			
		case "White":
			returnColor = Color.WHITE;
			break;
			
		case "Green":
			returnColor = Color.GREEN;
			break;
			
		case "Blue":
			returnColor = Color.BLUE;
			break;
			
		case "Red":
			returnColor = Color.RED;
			break;
		
		case "Yellow":
			returnColor = Color.YELLOW;
			break;
			
		}
		return returnColor;
	}
	
	//Creates instances of Player with the according information in the setup file
	public Player createPlayer(String[] dataArray) throws BadConfigFormatException {
		if(dataArray[3].equals("C")) {
			int row = Integer.parseInt(dataArray[4]);		
			int column = Integer.parseInt(dataArray[5]);
			return(new ComputerPlayer(dataArray[1], convertToColor(dataArray[2]),row,column));
			
		}
		else if(dataArray[3].equals("H") ){
			int row = Integer.parseInt(dataArray[4]);		
			int column = Integer.parseInt(dataArray[5]);
			return(new HumanPlayer(dataArray[1], convertToColor(dataArray[2]),row,column));
			
		}
		else {
			throw new BadConfigFormatException("Error: Invalid File Format");
		}
	}
	
	
	//Sets the doorway lists for each room
	public void setDoorways() {
		for(int i = 0; i < numRows; i++) {
			
			for(int j = 0; j < numColumns; j++) {
				
				if(grid[i][j].isDoorway()) {
					
					switch( grid[i][j].getDoorDirection() ) {
		
					case DOWN:
						roomMap.get(grid[i+1][j].getSymbol()).addDoorways(grid[i][j]);
						break;
					
					case UP:
						roomMap.get(grid[i-1][j].getSymbol()).addDoorways(grid[i][j]);
						break;
					
					case LEFT:
						roomMap.get(grid[i][j-1].getSymbol()).addDoorways(grid[i][j]);
						break;
						
					case RIGHT:
						roomMap.get(grid[i][j+1].getSymbol()).addDoorways(grid[i][j]);
						break;
					
					case NONE:
						break;
					}
				}
			}
		}
	}
	
	
	
	/*
	 * Fills the given grid BoardCell object with the needed information, ie:
	 * 	- door direction
	 * 	- whether or not it is a room center or room label
	 */
	public void fillCell(int i, int j, String [] currentLine) {
		grid[i][j] = new BoardCell(i,j,currentLine[j]);
		
		if(currentLine[j].length() == 2) {
			char temp = currentLine[j].charAt(1);
			
			switch( temp ) {
			
			case '*': //set center
				grid[i][j].setRoomCenter(true);
				roomMap.get(currentLine[j].charAt(0)).setCenterCell(getCell(i,j));
				break;
			
			case '#'://set label
				grid[i][j].setRoomLabel(true);
				roomMap.get(currentLine[j].charAt(0)).setLabelCell(getCell(i,j));
				break;
				
			case 'v': //set down doorway
				grid[i][j].setDoorDirection(DoorDirection.DOWN);
				grid[i][j].setisDoorway(true);
				break;
				
			case '>'://set right doorway
				grid[i][j].setDoorDirection(DoorDirection.RIGHT);
				grid[i][j].setisDoorway(true);
				break;
				
			case '<'://set left doorway
				grid[i][j].setDoorDirection(DoorDirection.LEFT);
				grid[i][j].setisDoorway(true);
				break;
				
			case '^'://set up doorway
				grid[i][j].setDoorDirection(DoorDirection.UP);
				grid[i][j].setisDoorway(true);
				break;
				
			default://set the secret passage if second char is another letter, set has passage of the room to true, and set the char of where the passage goes
				grid[i][j].setSecretPassage(temp);
				roomMap.get(currentLine[j].charAt(0)).setPassageCell(temp); 
				break;			
			}
		}
	}
	
	
	
	/*
	 * Fills the entire grid with the corresponding boardCell objects
	 */
	private void fillGrid(ArrayList<String> fileContent) throws BadConfigFormatException {		
		
		int ctr = 0;
		
		for(String i : fileContent) {
			String[] tempLine = i.split(",");
			
			//throw exception if one of the columns is not the right length
			if(ctr > 0 && numColumns != tempLine.length) {
				throw new BadConfigFormatException("Error: File requires columns of equal size");
			}
			
			numColumns = tempLine.length; //set column length for the board
			for(int j = 0; j < numColumns; j++) {//for each cell letter in a row, create a new board cell
				grid[ctr][j] = new BoardCell(ctr,j,tempLine[j]);
				
				if (!roomMap.containsKey(tempLine[j].charAt(0))) { //throw exception if there is an unknown character
					throw new BadConfigFormatException("Error: room does not exist");
				}
				fillCell(ctr,j,tempLine);
			}
			ctr++;
		}
	}
	
	
	
	public void loadLayoutConfig() throws BadConfigFormatException{
		try {
			File myObj = new File(layoutConfigFile);
			Scanner myReader = new Scanner(myObj);
			ArrayList<String> fileContent = new ArrayList<>();
			
			//Adds all lines of file content to an ArrayList of strings
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				fileContent.add(line);
			}
			
			//Allocates memory for grid based on the layoutConfigFile
			grid = new BoardCell[fileContent.size()][fileContent.get(0).length()];		
			
			fillGrid(fileContent); //Fills grid with BoardCell objects			
			myReader.close();		
			numRows = grid.length;
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		
		setDoorways();
	}
	
	
	/*
	 * Adds the grid spots directly surrounding the current BoardCell to its adjacency list if they are available
	 * and valid to do so
	 */
	private void addDirectAdjacencies(BoardCell[][] grid, int i, int j) {
		
		/*
		 * Boolean values that determine whether or not the cell directly above,below,left,and right of the current cell
		 * are possible adjacencies.
		 */
		boolean up = true;
		boolean down = true;
		boolean left = true;
		boolean right = true;
		
		//special cases in grid
		if(i == 0) {
			up = false;//Sets up to false if at top of grid
		}
		else if(i == numRows - 1) {
			down = false;//Sets down to false if at bottom of grid
		}
		
		if(j == 0) {
			left = false;//Sets left to false if at left of grid
		}
		else if(j == numColumns - 1) {
			right = false;//Sets right to false if at right of grid
		}
		
		//get the adjacencies
		if (left && isValidMove(i,j-1)){
			grid[i][j].addAdj(grid[i][j-1]);//left
		}
		
		if (right && isValidMove(i,j+1)){
			grid[i][j].addAdj(grid[i][j+1]);//right
		}
		
		if (up && isValidMove(i-1,j)){
			grid[i][j].addAdj(grid[i-1][j]);//up
		}
		
		if (down && isValidMove(i+1,j)){
			grid[i][j].addAdj(grid[i+1][j]);//down
		}
	}
	
	
	
	/*
	 * Returns true if the potential adjacent cell is a valid move, ie:
	 * 	- is a walkway 
	 * 	- is not already occupied
	 */
	private boolean isValidMove(int i, int j) {
		if((grid[i][j].getSymbol() == 'W' && !grid[i][j].isOccupied())) {
			return true;
		}
		
		return false;
	}
	
	
	
	//Calculates and creates adjacency lists for each cell in the board
	private void calcAdjacencies() {
		//set the adjacent lists
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				
				if(roomMap.get(grid[i][j].getSymbol()).getCenterCell() == grid[i][j]) { //if current cell is a room center, add all doorways to adjacencies 
					addSpecialCaseAdjacencies(i,j);
				}
				else {
					addDirectAdjacencies(grid,i,j);
				}
				//Adds center of rooms as adjacencies for cells that are doorways
				if(grid[i][j].isDoorway()){
					setDoorwayAdjacencies(i,j);
				}				
			}
		}
	}
	
	
	
	/*
	 * Adds cells to the adjacency lists when they are special characters ie.
	 * 	- Doorway
	 * 	- Center Cell
	 * 	- Passageway
	 */
	public void addSpecialCaseAdjacencies(int i, int j) {
		
		for(BoardCell b : roomMap.get(grid[i][j].getSymbol()).getDoorways()) {
			grid[i][j].addAdj(b);
		}
		if(roomMap.get(grid[i][j].getSymbol()).hasPassage()) { //if the room has a secret passage, set other side of passage to adjacencies
			char adjRoom = roomMap.get(grid[i][j].getSymbol()).getPassageCell();
			grid[i][j].addAdj(roomMap.get(adjRoom).getCenterCell());
		}
	}
	
	public void setAccusation(Solution accusation) {
		currentAccusation = accusation;
	}
	
	/*
	 * Adds corresponding adjacencies for the special case in which the potential adjacency cell is a door
	 */
	public void setDoorwayAdjacencies(int i, int j) {
		
		switch( grid[i][j].getDoorDirection() ) {
		
		case DOWN:
			grid[i][j].addAdj(roomMap.get(grid[i+1][j].getSymbol()).getCenterCell());
			break;
			
		case UP:
			grid[i][j].addAdj(roomMap.get(grid[i-1][j].getSymbol()).getCenterCell());
			break;
			
		case LEFT:
			grid[i][j].addAdj(roomMap.get(grid[i][j-1].getSymbol()).getCenterCell());
			break;
			
		case RIGHT:
			grid[i][j].addAdj(roomMap.get(grid[i][j+1].getSymbol()).getCenterCell());
			break;
			
		case NONE:
			break;	
		}
	}
	
	
	
	/*
	 * Calculates possible moves
	 */
	public void calcTargets( BoardCell startCell, int pathLength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(pathLength, startCell);
	}
	
	
	private void findAllTargets(int numSteps, BoardCell thisCell) {		
		//Iterates through the adjacency list for the given cell
		for (BoardCell adjCell : thisCell.getAdjList()) {
			
			//Only continues recursive function if the cell has not been visited and is not occupied 
			if (!visited.contains(adjCell) && (!adjCell.isOccupied() || adjCell.isRoomCenter())) {

				visited.add(adjCell);
			
				//Stops recursion if out of steps or in a room
				if(numSteps == 1 || adjCell.isRoom()) {
					targets.add(adjCell);
				}
				else {
					findAllTargets(numSteps-1, adjCell);
				}
				
				visited.remove(adjCell);
			}
		}	
	}
		
	
	/*
	 * Checks an accusation and determines if it is the correct solution or not
	 */
	public boolean checkAccusation(Solution solution) {		
		String gameResult = "The accusation was ," + solution.displaySolution();
		if(solution.getPerson() == theAnswer.getPerson() && solution.getWeapon() == theAnswer.getWeapon() && solution.getRoom() == theAnswer.getRoom())	{
			gameResult += ", and it was correct. ";
			if(getCurrentPlayer() instanceof HumanPlayer) {
				gameResult += "You have won the game!";
			}
			else {
				gameResult += "You have lost the game!";
			}
			JOptionPane.showMessageDialog(null , gameResult, "Game Over!", JOptionPane.INFORMATION_MESSAGE); 
			return true;
		}
		else {
			gameResult += ", and it was not correct. You have lost the game!";
			JOptionPane.showMessageDialog(null , gameResult, "Game Over!", JOptionPane.INFORMATION_MESSAGE); 
		}
		return false;
	}
	
	
	/*
	 * Disproves a suggestion by the player who is next in the order and returns the disproving card unless there is no disproving
	 */
	public Card handleSuggestion(Solution accusation) {
		
		for(int index = currentPlayerIndex + 1; index < players.size(); index ++) {
			if(players.get(index).disproveSuggestion(accusation) != null) {
				accusation.setDisprover(players.get(index));
				getCurrentPlayer().updateSeen(players.get(index).disproveSuggestion(accusation));
				return(players.get(index).disproveSuggestion(accusation));				
			}
		}
		
		for(int index = 0; index < currentPlayerIndex; index ++) {
			if(players.get(index).disproveSuggestion(accusation) != null) {
				accusation.setDisprover(players.get(index));
				getCurrentPlayer().updateSeen(players.get(index).disproveSuggestion(accusation));
				return(players.get(index).disproveSuggestion(accusation));
			}
		}	
		return null;
	}
	
	
	/*
	 * Sets shouldAccuse boolean to true if a computer player's guess was not disproved, and they do not have the room card of the room they are guessing from.
	 */
	public void shouldAccuse() {
		if(handleSuggestion(getCurrentPlayer().getSuggestion()) == null && !getCurrentPlayer().getHand().contains(getCurrentPlayer().getSuggestion().room)) {
			((ComputerPlayer) getCurrentPlayer()).setShouldAccuse(true);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D boardGraphic = (Graphics2D) g; // Cast to Graphics 2D
		
		ArrayList<BoardCell> roomLabels = new ArrayList<BoardCell>();
		
		//Calculate width and height of each cell
		cellWidth = this.getWidth()/numColumns;
		cellHeight = this.getHeight()/numRows;
		
		//loop through all cells and call upon them to draw themselves
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numColumns; col++) {
				getCell(row, col).drawCell(boardGraphic, cellWidth, cellHeight);
				if(getCell(row,col).isLabel()) { //Paints labels of rooms
					roomLabels.add(getCell(row,col));
				}
				if(targets.contains(getCell(row,col)) && showTargets ) { //Colors target cells a different color for the human player
					getCell(row,col).highlightCell(boardGraphic, cellWidth, cellHeight);
				}
				
			}
		}
		
		//Iterate through all players and draw ovals of their corresponding color in the correct cell
		for(Player player : players) {
			player.draw(boardGraphic, cellWidth, cellHeight);
		}
		
		//Iterate through all roomLabels and print names in corresponding cells
		for(BoardCell cell : roomLabels) {
			String name = getRoom(cell.getSymbol()).getName();
			boardGraphic.drawString(name, cell.getColumn() * cellWidth, cell.getRow() * cellHeight);
		}
		
	}
	
	
	/*
	 * Updates everything necessary to move to the next turn ie.
	 *  - update player
	 *  - roll dice
	 *  - calculate targets for the given player position and the current dice roll
	 */
	public void nextTurn() {
		nextPlayer();
		setCurrentRoll(rollDice());	
		calcTargets(getPlayerLocation(players.get(currentPlayerIndex)), currentRoll);
		
		//Add center of room to targets if player was moved into that room because of a suggestion
		if(getCurrentPlayer().getCanStayInRoom()) {
			targets.add(getPlayerLocation(getCurrentPlayer()));
		}
		
		if(!showTargets) {
			ComputerPlayer currentCompPlayer = (ComputerPlayer) getCurrentPlayer();	
			currentCompPlayer.setPlayerLocation(currentCompPlayer.selectTarget(targets, roomCards));			
		}		
		
		calculateInRoom();
		
		//Checks if the player was moved into a room due to a suggestion and makes a note that they have now stayed in that room one turn		
		if(getCurrentPlayer().getIsInRoom()) {	 //if current player is in room	
			
			if(getCurrentPlayer().getCanStayInRoom()) { //if the current player can stay in room
				
				if( getCurrentPlayer() instanceof ComputerPlayer ){ //if current player is computer

					if(((ComputerPlayer) getCurrentPlayer()).getShouldAccuse()) { //Computer player makes an accusation when they know it is right
						JOptionPane.showMessageDialog(null , getCurrentPlayer().getName() + " has won the game. It was " + theAnswer.displaySolution(), "GAME OVER !!", JOptionPane.INFORMATION_MESSAGE);
					}

					((ComputerPlayer) getCurrentPlayer()).createSuggestion(); //Create suggestion from computer player
					movePlayersTogether(getCurrentPlayer(), getPlayerFromSuggestion(getCurrentPlayer().getSuggestion())); //Moves the accused player to corresponding room
					shouldAccuse();
				}				
				getCurrentPlayer().setCanStayInRoom(false); //set the boolean setCanStayInRoom to false
			}
			
			movePlayersTogether(getCurrentPlayer(), getPlayerFromSuggestion(getCurrentPlayer().getSuggestion())); //Moves the accused player to corresponding room
			handleSuggestion(getCurrentPlayer().getSuggestion());
			displayDisprovenCard();			
		}
		else {
			//if not in a room, they can stay in one again
			getCurrentPlayer().setCanStayInRoom(true);
			endedLastTurnInRoom = false;
		}
		repaint();
	}



	//Changes isInRoom value of current player depending on whether or not they just moved into a room
	private void calculateInRoom() {
		if(getPlayerLocation(getCurrentPlayer()).getSymbol() != 'W' && getPlayerLocation(getCurrentPlayer()).getSymbol() != 'X') {
			getCurrentPlayer().setIsInRoom(true);
			}
		else {
			getCurrentPlayer().setIsInRoom(false);
		}
	}
	
	
	
	/*
	 * Returns disproving card to human player if there is one
	 * Returns that a card was used to disprove a suggestion for a computer player if there is one
	 * Returns if there was no disproving card for any given suggestion
	 */
	public String displayDisprovenCard() {
		Card disprovingCard = handleSuggestion(getCurrentPlayer().getSuggestion());
		if(disprovingCard == null) {
			return "The suggestion was not disproven.";
		}
		else {
			if(getCurrentPlayer() instanceof HumanPlayer) {
				return "Your suggestion was disproven by player: " + getCurrentPlayer().getSuggestion().getDisprover().getName() + " with the card: " + disprovingCard.getName();
			}
			else {
				return "The suggestion was disproven by player: " + getCurrentPlayer().getSuggestion().getDisprover().getName();
			}
		}		
	}
	

	//update current player
	public void nextPlayer() {
		
		if ((currentPlayerIndex + 1) > players.size() - 1) { // Start over from beginning of player arrayList if already at the end
			currentPlayerIndex = 0;
		}else { // Move to next player
			currentPlayerIndex ++;
		}	
		
		//Decides if currentPlayer is human and therefore needs to be shown possible targets
		if(getCurrentPlayer() instanceof HumanPlayer) {
			showTargets = true;
			isTurnComplete = false;
			canAccuse = true;
		}
		else {
			showTargets = false;
		}
	}
	
	public int rollDice() {
		Random rand = new Random();
    	int diceRoll = rand.nextInt(DICE + 1);
    	return diceRoll;
	}
	
	private class mouseListenerHandling implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {
			
			if(players.get(currentPlayerIndex) instanceof HumanPlayer && !isTurnComplete) { // Only listens to mouse clicks if the player is human and not finished with their turn
				//Get the x and y coordinates of the mouse click
				int xValue = event.getX();
				int yValue = event.getY();

				//Calculate x and y integer values of the mouse click
				int clickX = xValue / cellWidth;
				int clickY = yValue / cellHeight;
				
				if(targets.contains(getCell(clickY, clickX))) {
					// Move human to new location when they click a valid move
					players.get(currentPlayerIndex).setRow(clickY);
					players.get(currentPlayerIndex).setColumn(clickX);
					isTurnComplete = true;
					canAccuse = false;
					showTargets = false;
					calculateInRoom();
					repaint();
					
					if(getCurrentPlayer().getIsInRoom()) {
						//Prompts for the Accusation/Suggestion dialog to open when the human player moves into a room
						isTurnComplete = false;
						accusationDialog = new AccusationSuggestionDialog();
						accusationDialog.setModal(true);
						accusationDialog.setVisible(true);
						
						//once you get the accusation, move accused
						movePlayersTogether(getCurrentPlayer(),getPlayerFromSuggestion(getCurrentPlayer().getSuggestion()));
						repaint();
					}

				}			
				else {
					JOptionPane.showMessageDialog(null , "That is not a valid move! Choose again.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	
	//Moves an accused player to the room in which they are accused by another player
	public void movePlayersTogether(Player suggester, Player accused) {
		accused.setCanStayInRoom(true);
		Solution suggestion = suggester.getSuggestion();
		accused.setPlayerLocation(getRoom(suggestion.getRoom().getName().charAt(0)).getCenterCell());//Places player at label of accused room
	}
	
	
	public BoardCell getPlayerLocation(Player player) {
		int row = player.getRow();
		int col = player.getColumn();
		return getCell(row, col);
	}
	
	public Card getCardByName(String cardName) {
		for(Card card : deck) {
			if(card.getName().equals(cardName)) {
				return card;
			}
		}
		return null;
	}
	
	//get the files 
	public String getLayoutConfigFile() {
		return layoutConfigFile;
	}
	
	public String getSetupConfigFile() {
		return setupConfigFile;
	}
	
	//set the file path and name
	public void setConfigFiles(String layout, String setup) {
		layoutConfigFile = "data/" + layout;
		setupConfigFile = "data/" + setup;
	}
	
	//getters for rows and columns
	public int getNumRows() {
		return numRows;
	}
	
	public void setCurrentRoll(int num) {
		currentRoll = num;
	}
	
	public int getCurrentRoll() {
		return currentRoll;
	}
	
	public String getCurrentPlayerName() {
		return players.get(currentPlayerIndex).getName();
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	//returns the name of a room based off of its character
	public String getRoomName(char c) {
		return roomMap.get(c).getName();
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	//get room with character input
	public Room getRoom(char c) {
		return roomMap.get(c);
	}
	
	public boolean getIsTurnComplete() {
		return isTurnComplete;
	}

	public void setIsTurnComplete(boolean isTurnComplete) {
		this.isTurnComplete = isTurnComplete;
	}


	public Card getRoomCard(BoardCell cell) {
		return roomCards.get(cell.getSymbol());
	}
	
	public Card getRoomCard(char c) {
		return roomCards.get(c);
	}
	
	//get room cell input
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getSymbol());
	}
	
	//get cell, adj list, and target list
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		return grid[i][j].getAdjList();
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public ArrayList<Card> getWeapons() {
		return weapons;
	}
	

	public ArrayList<Card> getDeck() {
		return deck;
	}


	public Solution getSolution() {
		return theAnswer;
	}

	public Map<Character, Card> getRoomCards(){
		return roomCards;
	}


	public void setAnswer(Card person, Card room, Card weapon) {
		theAnswer.setPerson(person);
		theAnswer.setRoom(room);
		theAnswer.setWeapon(weapon);	
	}

	//a setter for testing only that can override the players in initialize
	public void setPlayers(ArrayList<Player> tempPlayers) {
		players = tempPlayers;
	}

	public ArrayList<Card> getPeople() {
		return people;
	}


	public boolean getDone() {
		return isTurnComplete;
	}


	public int getCellWidth() {
		return cellWidth;
	}

	public Player getPlayerFromSuggestion(Solution solution) {
		for(Player player : players) {
			if(player.getName().equals(solution.getPerson().getName())) {
				return player;
			}
		}
		return null;
	}

	public int getCellHeight() {
		return cellHeight;
	}
	
	public void setPeopleRoomsWeapons0() {
		people.clear();
		rooms.clear();
		weapons.clear();		
	}

	public boolean getCanAccuse() {
		return canAccuse;
	}

	public ArrayList<Card> getRooms(){
		return rooms;
	}
	
}
