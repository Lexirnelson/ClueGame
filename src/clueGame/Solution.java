package clueGame;

public class Solution {

	public Card person;
	public Card room;
	public Card weapon;
	public Player disprover;
	
	//constructor for initializing a solution
	public Solution() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public String displaySolution() {
		return "It was " + person.getName() + " in the " + room.getName() + " with the " + weapon.getName();
	}


	public Player getDisprover() {
		return disprover;
	}

	public void setDisprover(Player disprover) {
		this.disprover = disprover;
	}

	public Card getPerson() {
		return person;
	}
	public void setPerson(Card person) {
		this.person = person;
	}
	public Card getRoom() {
		return room;
	}
	public void setRoom(Card room) {
		this.room = room;
	}
	public Card getWeapon() {
		return weapon;
	}
	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}
	
	
}
