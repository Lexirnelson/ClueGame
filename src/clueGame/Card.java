package clueGame;

public class Card{

	private String cardName;
	private CardType type;
	
	
	public Card(String cardName, CardType type) {
		super();
		this.cardName = cardName;
		this.type = type;
	}

	public Card() {
		super();
	}

	public boolean equals(Card target) {
		return false;
	}

	public String getName() {
		return cardName;
	}

	public CardType getType() {
		return type;
	}
	
}
