import java.io.*;
import java.util.*;

public class Blackjack {
	private Role player, dealer;
	private Card deck;
	private int chip = 100;
	private int bet;
	private String current;
	private static final int MAX_VALUE = 17;//decision boundary for hitting
	BufferedReader br;
	
	public Blackjack() {//initializing the class
		player = new Role();
		dealer = new Role();
		
		deck = new Card();
		
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void startGame() {//main procedure of the game
		System.out.println("Welcome to Blackjack!");
		
		deck.shuffle();//make sure this bet is not influenced by the last one
		
		while (chip > 0) {
			deck.shuffle();
			System.out.println("Your chips now: "+chip);
			System.out.println("Please enter your bet (enter 0 if you don't want to play any more): ");
			try {
				bet = Integer.parseInt(br.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (bet > chip) {
				System.out.println("Not enough chips!");
				continue;
			}
			if (bet < 0) {
				System.out.println("Please bet more than 1 chip!");
				continue;
			}
			if (bet == 0) {
				return;
			}
			
			player.start();//two starting cards for player
			System.out.println("Your cards: "+player.showAll());
			dealer.start();//two starting cards for dealer
			System.out.println("Dealer's first card: "+dealer.showFirst());
			
			System.out.println("Your point is: " + player.points());
			
			//check if Blackjack occurs
			if (player.blackjack() && !dealer.blackjack()) {
				System.out.println("YOU WIN A BLACKJACK!");
				System.out.println("Dealer's cards: " + dealer.showAll());
				doubleWin();
				continue;
			}
			
			if (player.blackjack() && dealer.blackjack()) {
				System.out.println("TWO BLACKJACKS!");
				System.out.println("Dealer's cards: " + dealer.showAll());
				tie();
				continue;
			}		
			
			if (!player.blackjack() && dealer.blackjack()) {
				System.out.println("DEALER WINS A BLACKJACK!");
				System.out.println("Dealer's cards: " + dealer.showAll());
				lose();
			}
			
			while (true){//hitting procedure for the player
				System.out.println("Wanna hit? (y for hitting, others for not)");
				try {
					if (!br.readLine().equals("y")) break;
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.hit();
				if (player.bust()) {
					System.out.println("YOU BUST!");
					lose();
					break;
				}
			}
			
			if (player.bust()) continue;//start a new bet

			System.out.println("Dealer stage: ");
			System.out.println("Dealer's cards: " + dealer.showAll());
			
			while (dealer.points() < MAX_VALUE) {//hitting procedure for dealer
				System.out.println("Dealer hits: ");
				dealer.hit();
				if (dealer.bust()) {
					System.out.println("DEALER BUSTS!");
					System.out.println("Your point is: " + player.points());
					System.out.println("Dealer's point is: " + dealer.points());
					win();
				}
			}
			
			if (dealer.bust()) continue; 
			
			System.out.println("Dealer ends");
			System.out.println("Your point is: " + player.points());
			System.out.println("Dealer's point is: " + dealer.points());
			
			//compare stage to decide who wins
			if (player.points() < dealer.points()) {
				lose();
				continue;
			}
			
			if (player.points() == dealer.points()) {
				tie();
				continue;
			}
			
			if (player.points() > dealer.points()) {
				win();
				continue;
			}
		}
		System.out.println("GAME OVER! You have no chips!");
	}
	
	private void doubleWin() {//win a Blackjack
		System.out.println("DOUBLE WIN! The chips you win is " + 2*bet);
		chip += 2*bet;
	}
	
	private void win() {//win a bet without Blackjack
		System.out.println("YOU WIN! The chips you win is " + bet);
		chip += bet;
	}
	
	private void tie() {//equal hand value
		System.out.println("NOBODY WIN! The chips remain the same.");
	}
	
	private void lose() {//lose a bet
		System.out.println("YOU LOSE! The chips you lose is " + bet);
		chip -= bet;
	}
	
	public static void main (String [] args) {//client code
		Blackjack game = new Blackjack();
		game.startGame();
		System.out.println("Thanks for coming!");
	}
	
	private class Role {//class for player and dealer
		private List<String> card = new ArrayList<String>();//hand card 
		
		public void start(){//start by distributing two cards 
			card.removeAll(card);
			card.add(deck.next());
			card.add(deck.next());			
		}
		
		public boolean blackjack() {//check whether the hand cards is a Blackjack 
			return points() == 21; 
		}
		
		public int points() {//counting the hand value, where Ace is counted as 1 or 11
								//to achieve larger hand value while not bust
			//counting is based on the last character of the card
			int sum = 0;
			int numOfA = 0;
			for (String s : card) {
				Character tmp = s.charAt(s.length() - 1);
				if (tmp == 'A') {
					numOfA++;
					sum += 11;
					continue;
				}
				if (tmp == 'K' || tmp == 'Q' || tmp == 'J' || tmp == '0') {
					//King, Queen and Jack are calculated as 10, '0' stands for 10 since no 
					//other cards end with 0
					sum += 10;
					continue;
				}
				String value = tmp.toString();
				sum += Integer.parseInt(value);
			}
			
			for (int i = 0; i < numOfA; i++) {
				if (sum > 21) sum -= 10;
				else break;
			}
			
			return sum;
		}
		
		public void hit() {//method for hitting
			card.add(deck.next());
			System.out.println(showAll());
			System.out.println("Sum: " + points());
		}
		
		public String showFirst() {//show the first hand card to others
			return card.get(0);
		}
		
		public String showAll() {//display all the hand cards
			String tmp = "";
			for (String s : card) {
				tmp += s;
				tmp += "    ";
			}
			return tmp;
		}
		
		public boolean bust() {//check if the hand value bust or not
			return points() > 21;
		}
	}
	
	private class Card {//class for the deck
		private int [] oneDeck = new int [52];
		private int num = 0;
		
		public void shuffle() {//conducting Knuth shuffling on deck   
			for (int i = 0; i < oneDeck.length; i++) oneDeck[i] = i;
			Random rand = new Random();
	        for (int i = 0; i < oneDeck.length; i++) {
	        	int r = i + rand.nextInt(oneDeck.length - i);
	            int tmp = oneDeck[i];
	            oneDeck[i] = oneDeck[r];
	            oneDeck[r] = tmp;
	        }
		}
		
		public String next() {//hand out a card from deck
			int tmp = oneDeck[num];
			num++;
			current = numToPoker(tmp);
			return current;
		}
		
		private String numToPoker(int index) {//convert card index to poker name
			String [] face = {"Heart", "Spade", "Club", "Diamond"};
			String [] cardValue = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
			int faceIndex = index / 13;
			int valueIndex = index % 13;
			return face[faceIndex] + " " + cardValue[valueIndex];
		}
	}
	
}









