import java.util.Random;
import java.util.ArrayList; 


public class Deck {
	ArrayList<Card> deck = new ArrayList<Card>();
	ArrayList<Card> ordered_deck = new ArrayList<Card>();
	int pos = 0; //position in deck
	
	public void fillDeck() {
		for (Card.Suit suits : Card.Suit.values()) {
			for (int i = 1; i < 14; i++) {
				ordered_deck.add(new Card(i, suits));
			}
		}
		//Shuffle the deck:
		Random r = new Random();
		int pos2;
		while (ordered_deck.size() != 0) {
			pos2 = r.nextInt(ordered_deck.size()); 
			deck.add(ordered_deck.get(pos2));
			ordered_deck.remove(pos2);
		}
		
	}
	public void refillDeck() {
		deck.clear();
		pos = 0;
		fillDeck();
	}
	public Card drawCard() {
		Card drawncard = deck.get(pos);
		pos += 1;
		return drawncard;
	}
}