// David Allen
// This is just an excuse to build a deck of cards class.  Eventually I hope to write out some card games that utilize this deck.

import java.io.*;
import java.util.*;

class Deck
{
	Deck()
	{
		this.numCards = 0;
		this.bottomCard = null;
		this.topCard = null;
		hashOfCards = new HashSet<Card>();
		rand = new Random();
	}

	private Random rand;

	public int numCards()
	{
		return this.numCards;
	}
	private int numCards;

	Card bottomCard;
	Card topCard;
	private HashSet<Card> hashOfCards;

	public Card removeTopCard()
	{
		// Are there cards to remove?
		if (this.numCards == 0) return null;

		this.numCards--;

		// only one card to return
		if (this.numCards == 1)
		{
			Card temp = this.topCard;
			this.topCard = null;
			this.bottomCard = null;
			// remove this card from the hashset
			hashOfCards.remove(temp);

			return temp;
		}

		// Cards to remove, store the top card in a temp variable
		Card temp = this.topCard;

		// set the new top card
		this.topCard = this.topCard.next;

		// remove this card from the hashset
		hashOfCards.remove(temp);
			

		// return this top card;
		return temp;
	}

	public Card removeBottomCard()
	{
		// No cards to return
		if (this.numCards == 0) return null;

		this.numCards--;

		// only one card to return
		if (this.numCards == 1)
		{
			
			Card temp = this.topCard;
			this.topCard = null;
			this.bottomCard = null;

			// remove this card from the hashset
			hashOfCards.remove(temp);

			return temp;
		}

		Card temp = this.topCard;

		// we can assume if we are here, that means
		// there exists at least 2 cards.  We
		// want to traverse the deck until temp.next.next is null
		// then we want to reset the bottom card and return temp.next
		while (temp.next.next != null) temp = temp.next;

		// set the bottom card to the current temp
		this.bottomCard = temp;

		// remove this card from the hashset
		hashOfCards.remove(temp.next);

		// return the card underneath that one
		return temp.next;
	}

	public void insertCardTop(Card card)
	{
		// check to see if this card is already in the set
		// don't allow multiple copies of the same card;
		if (hashOfCards.contains(card)) return;

		// add to hashset
		hashOfCards.add(card);

		card.next = this.topCard;
		this.topCard = card;

		// was this the only card in the deck?  This new card must be bottom too then
		if (this.numCards == 0) this.bottomCard = card;

		this.numCards++;
	}

	public void insertCardBottom(Card card)
	{
		// TODO
	}

	public void insertCardRandom(Card card)
	{
		// TODO
	}

	public void fillDeck()
	{
		// this should add 52 cards to the deck in order
		Card.Suit[] suits = {Card.Suit.Heart, Card.Suit.Club, Card.Suit.Diamond, Card.Suit.Spade};

		for (int s = 0; s < 2; s++)
			for (int v = 2; v <= 14; v++)
				insertCardTop(new Card(suits[s],v));


		for (int s = 2; s < 4; s++)
			for (int v = 14; v > 1; v--)
				insertCardTop(new Card(suits[s],v));

	}

	public String peekTopCard()
	{
		return this.topCard.simple();
	}

	public String peekBottomCard()
	{
		return this.bottomCard.simple();
	}

	public void showCards()
	{
		Card temp = this.topCard;
		while (temp != null) 
		{
			System.out.print(temp.simple() + " ");
			temp = temp.next;
		}
		System.out.println();
	}

	public Deck[] deal(int numHands, int numCards)
	{
		if (this.numCards < numHands * numCards) return null;

		Deck[] hands = new Deck[numHands];

		for (int i = 0; i < numHands; i++) hands[i] = new Deck();

		int playerPosition = 0;

		while (hands[numHands-1].numCards < numCards)
			hands[(playerPosition++ % numHands)].insertCardTop(this.removeTopCard());

		return hands;
	}

	public int shuffle()
	{
		// can't shuffle 1 or 0 cards
		if (this.numCards < 2) return 0;

		// remove all cards and place into an array of length n, O(n)
		int n = this.numCards;
		int k = n;
		Card[] unshuffledCards = new Card[n];

		HashSet<Card> cardsBackInDeck = new HashSet<Card>();

		int j = 0;
		while (this.topCard != null) 
			unshuffledCards[j++] = this.removeTopCard();
		

		// now to randomly select from this array and place back into the deck.  The runtime
		// of this depends on if we select the same card that we have already placed in the deck.
		// approx. 1+(k+1/n)
		while (this.numCards != n)
		{
			k++;

			j = rand.nextInt(n);
			
			if (unshuffledCards[j] != null) 
			{
				this.insertCardTop(unshuffledCards[j]);
				unshuffledCards[j] = null;
			}
		}

		return k;


	}

}

class Card
{
	// What things would a card need?
	Card next = null;

	// initializer
	Card()
	{
		this.suit = Suit.Heart;
		this.value = 14;
	}

	Card(Suit suit, int value)
	{
		this.suit = suit;
		this.value = value;
	}

	// suit
	public Suit suit;

	// numeric value
	public int value;

	// string representation
	public String toString()
	{
		String temp = "";

		switch (value)
		{
			case 14:
				temp+="Ace";
				break;
			case 2:
				temp+="Two";
				break;
			case 3:
				temp+="Three";
				break;
			case 4:
				temp+="Four";
				break;
			case 5:
				temp+="Five";
				break;
			case 6:
				temp+="Six";
				break;
			case 7:
				temp+="Seven";
				break;
			case 8:
				temp+="Eight";
				break;
			case 9:
				temp+="Nine";
				break;
			case 10:
				temp+="Ten";
				break;
			case 11:
				temp+="Jack";
				break;
			case 12:
				temp+="Queen";
				break;
			case 13:
				temp+="King";
				break;
			default:
				break;
		}

		switch (this.suit)
		{
			case Heart:
			temp+=" of Hearts";
			break;

			case Club:
			temp+=" of Clubs";
			break;

			case Spade:
			temp+=" of Spades";
			break;

			case Diamond:
			temp+=" of Diamonds";
			break;	
		}

		return temp;
	}

	public String simple()
	{
		String temp = "";

		switch (this.value)
		{
			case 14:
				temp+="A";
				break;
			case 11:
				temp+="J";
				break;
			case 12:
				temp+="Q";
				break;
			case 13:
				temp+="K";
				break;
			default:
				temp+=Integer.toString(this.value);
				break;
		}

		switch (this.suit)
		{
			case Heart:
			temp+="♥";
			break;

			case Club:
			temp+="♣";
			break;

			case Spade:
			temp+="♠";
			break;

			case Diamond:
			temp+="♦";
			break;	
		}

		return temp;
	}
	// print

	public enum Suit
	{
		Heart,
		Club,
		Spade,
		Diamond
	}

	public boolean equals(Object o)
	{
		if (o instanceof Card)
		{
			return this.simple().equals(((Card)o).simple());

		}
		else return false;
	}

}

public class CardGame
{
	public static void main(String[] args)
	{

		Deck d = new Deck();
		d.fillDeck();

		int numHands = 2;
		int numCards = 5;
		d.shuffle();
		
		Deck[] hands = d.deal(numHands, numCards);

		for (int i = 0; i < numHands; i++)
		{
			System.out.print("\nPlayer(" + Integer.toString(i+1) + ")  ");
			hands[i].showCards();	
		}
	}


}