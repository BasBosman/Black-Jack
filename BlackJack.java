import java.util.Scanner;
import java.util.ArrayList; 
import java.util.concurrent.TimeUnit;

public class BlackJack {
	public static void main(String[] args) {
		
		Deck deck = new Deck();
		deck.fillDeck();
		Card c1 = deck.drawCard();

		//Scanner scanner = new Scanner(System.in);
		//System.out.println("What will you play?");
		//outpt = scanner.nextLine();
		//System.out.println(outpt);
		//System.out.println("What will you play?");
		//outpt = scanner.nextLine();
		//System.out.println(outpt);
		
		//scanner.close();
		
		//////////////////
		//Play the game://
		//////////////////
			
		int aantalSpelers;
		//create the players
		System.out.println("How many players?");
		Scanner scanner = new Scanner(System.in);
		String h  = scanner.nextLine();
		aantalSpelers = Integer.parseInt(h) + 1; //dealer also counts as player
		
		ArrayList<Player> players = new ArrayList<>();
		
		//create a dealer who is player 0:
		players.add(new Player("Dealer"));
		players.get(0).money = 1000000;	
		
		for (int i = 1; i < aantalSpelers; i++) {
			System.out.println("Please enter the name for player " + Integer.toString(i) + ":");
			
			players.add(new Player(scanner.nextLine()));
			players.get(i).money = 0;
			
		}	
		//Game loop
		boolean playing = true;
		boolean newgame = true;
		boolean hiddenAce = false;
		boolean dealerHasBlackJack = false;
		Card drawnCard;
		double bet;
		int dealerHandValue;
		Card hiddenCard;
		int playersLeft = aantalSpelers;
		while (playing) {
			if (newgame) {
				deck.refillDeck();
				newgame = false;
				dealerHandValue = 0;
				hiddenAce = false;
				playersLeft = aantalSpelers;
				dealerHasBlackJack = false;
				for (int i = 0; i < aantalSpelers; i++) {
					players.get(i).isDead = false;
					players.get(i).handValue = 0;
					players.get(i).aceCount = 0;
					players.get(i).stillPlaying = true;
				}
			}
		System.out.println("How much to bet?");
		bet = Integer.parseInt(scanner.nextLine());
		
		
		//deal a card to each player
		for (int i = 0; i < aantalSpelers; i++) {
			drawnCard = deck.drawCard();
			addCardValue(players.get(i), drawnCard);
			System.out.println(parseCardName(players.get(i).name + " has drawn " + drawnCard.suit + drawnCard.number + ". "));
			printpoints(players.get(i));
			waitabit();
		}
		//deal a card to each player except the dealer
		for (int i = 1; i < aantalSpelers; i++) {
			drawnCard = deck.drawCard();
			addCardValue(players.get(i), drawnCard);
			System.out.println(parseCardName(players.get(i).name + " has drawn " + drawnCard.suit + drawnCard.number + ". "));
			printpoints(players.get(i));
			//check if player has Black Jack:
			if (players.get(i).handValue == 21) {
				players.get(i).stillPlaying = false;
				players.get(i).money += bet * 1.5;
				playersLeft -= 1;
				players.get(i).isDead = true; //player is considered dead
				System.out.println(parseCardName(players.get(i).name + " has Black Jack and wins " + Double.toString(bet * 1.5) + " money."));
			}
			waitabit();

		}
		//deal a hidden card to the dealer
		hiddenCard = deck.drawCard();
		System.out.println("The dealer has drawn a hidden card.");
		dealerHandValue = addHiddenValue(hiddenCard) + players.get(0).handValue;
		if  (addHiddenValue(hiddenCard) == 11) {
			hiddenAce = true;
		}
		//check if dealer has Black Jack
		if (dealerHandValue == 21) {
			dealerHasBlackJack = true;
		}
		
		//Now the game really starts :)
		while (playersLeft > 0) {
			//do something
			for (int i = 1; i < aantalSpelers; i++) {
				if (players.get(i).stillPlaying) {
					System.out.println("");
					System.out.println(players.get(i).name + "'s turn. Type k to draw a card, type p to pass. Type to q to pass and quit. ");
					printpoints(players.get(i));
					switch(scanner.nextLine()) {
					  case "k":
						  drawnCard = deck.drawCard();
						  addCardValue(players.get(i), drawnCard);
						  System.out.println(parseCardName(players.get(i).name + " has drawn " + drawnCard.suit + drawnCard.number + ". "));
						  printpoints(players.get(i));
						  //check if player is dead.
						  if (players.get(i).handValue - 10 * players.get(i).aceCount > 21) {
							  playersLeft -= 1;
							  players.get(i).stillPlaying = false;
							  players.get(i).isDead = true;
							  players.get(i).money -= bet;
							  System.out.println(players.get(i).name + " is dead. He lost his bet.");
						  }
						  break;
					  case "p":
						  playersLeft -= 1;
						  players.get(i).stillPlaying = false;
						  System.out.println(players.get(i).name + " has passed.");
						  break;
					  case "q":
						  playing = false;
						  playersLeft -= 1;
						  players.get(i).stillPlaying = false;
						  System.out.println(players.get(i).name + " has passed. This will be the final game.");
						  break;
					}
				}
			}
			//dealer reveals his hidden card:
			if (hiddenCard != null) {
				System.out.println(parseCardName("The dealer has revealed his hidden card. It's a " + hiddenCard.suit + hiddenCard.number + ". "));
				players.get(0).handValue = dealerHandValue;
				if (hiddenAce) {
					players.get(0).aceCount += 1;
				}
				printpoints(players.get(0));
				
				//dealer has black jack, all players lose
				if (dealerHasBlackJack) {
					System.out.println("Dealer has Black Jack. All players lose. ");
					playersLeft = 0;
					players.get(0).isDead = true;
					for (int i = 1; i < aantalSpelers; i++) {
						if (!players.get(i).isDead) {
							players.get(i).money -= bet;
						}
					}
				}
				hiddenCard = null;
			}
			//if dealer has not 17 points, he draws another card. Else he passes.
			if (players.get(0).handValue < 17 || (players.get(0).handValue > 21 && (players.get(0).handValue - 10*players.get(0).aceCount) < 17)) { //or max value is higher than 21 and min value lower than 17
				drawnCard = deck.drawCard();
				addCardValue(players.get(0), drawnCard);
				System.out.println(parseCardName(players.get(0).name + " has drawn " + drawnCard.suit + drawnCard.number + ". "));
				printpoints(players.get(0));
				//check if dealer is dead
				if ((players.get(0).handValue - 10*players.get(0).aceCount) > 21) {
					System.out.println("The dealer is dead. All remaining players win. ");
					playersLeft = 0;
					players.get(0).isDead = true;
					for (int i = 1; i < aantalSpelers; i++) {
						if (!players.get(i).isDead) {
							players.get(i).money += bet;
						}
					}
					
				}
			}
			else if (players.get(0).stillPlaying) {
				System.out.println("The dealer has more than 17 points and passes. ");
				playersLeft -= 1;
				players.get(0).stillPlaying = false;
			}
		}
	//if the dealer is not dead or get black jack, all players with more points win
	newgame = true;
	if (!players.get(0).isDead) {
		System.out.println("All players have passed. The game has ended. ");
		PlayerFinalPts(players.get(0));
		System.out.println(parseCardName("The dealer has " + players.get(0).handValue + " points."));
		for (int i = 1; i < aantalSpelers; i++) {
			if (!players.get(i).isDead) {
				PlayerFinalPts(players.get(i));
				if (players.get(i).handValue > players.get(0).handValue) {
					//player wins
					System.out.println(parseCardName(players.get(i).name + " has " + players.get(i).handValue + " points and has won!"));
					players.get(i).money += bet;
				}
				else {
					//player loses
					System.out.println(parseCardName(players.get(i).name + " has " + players.get(i).handValue + " points and has lost!"));
					players.get(i).money -= bet;
				}
			}
		}
	}
	}
	scanner.close();
	for (int i = 1; i < aantalSpelers; i++) {
		System.out.println(players.get(i).name +  " has won " + players.get(i).money + " money!");
	}
	
	}
	public static String parseCardName(String input) {
		input = input.replaceFirst("CLUBS", "♣");
		input = input.replaceFirst("DIAMONDS", "♦");
		input = input.replaceFirst("HEARTS", "♥");
		input = input.replaceFirst("SPADES", "♠");
		input = input.replaceFirst("([♣♦♥♠])11", "$1J");
		input = input.replaceFirst("([♣♦♥♠])12", "$1Q");
		input = input.replaceFirst("([♣♦♥♠])13", "$1K");
		String output = input.replaceFirst("([♣♦♥♠])1\\.", "$1A.");
		return output;
	}

	public static void addCardValue(Player player, Card drawnCard) {
		if (drawnCard.number == 1) {//means the drawn card is an ace!
			player.aceCount += 1;
			player.handValue += 11;
		}
		else if (drawnCard.number >= 10) { //drawn card is worth 10 points
			player.handValue += 10;
		}
		else {player.handValue += drawnCard.number;}
	}
	
	public static int addHiddenValue(Card drawnCard) {
		int valuetoadd;
		if (drawnCard.number == 1) {//means the drawn card is an ace!
			valuetoadd = 11;
		}
		else if (drawnCard.number >= 10) { //drawn card is worth 10 points
			valuetoadd = 10;
		}
		else {valuetoadd = drawnCard.number;}
		return valuetoadd;
	}
	
	public static void waitabit() {
	//wait a short time to make the game more exciting
	try {
		TimeUnit.SECONDS.sleep(1);
	} catch (InterruptedException e) {
		e.printStackTrace();
		}
	}
	public static void printpoints(Player player) {
		  if (player.aceCount > 0) {
			System.out.println(parseCardName(player.name + " has " + player.aceCount+" ace. He has " + Integer.toString(player.handValue - 10 * player.aceCount) + " or " + player.handValue + " points."));
		  }
		  else {
			System.out.println(parseCardName(player.name + " has " + player.handValue + " points."));
		  }
	}
	public static void PlayerFinalPts(Player player) {
		while (player.handValue > 21 && player.aceCount > 1) {
			player.handValue -= 10;
			player.aceCount -= 1;
		}
	}
	
}