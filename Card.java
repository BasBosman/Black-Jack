public class Card {
	//Suits ♦♣♥♠
	enum Suit {
		CLUBS, 
		DIAMONDS, 
		HEARTS, 
		SPADES 
	}
	
	public int number;
	public Suit suit;
	
	Card(int a, Suit b) {
		this.number = a;
		this.suit = b;
	}
	
	//static void CreateCard(int a, Suit b) {
	//	number = a;
	//	suit = b;
	//}
}