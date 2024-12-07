import java.util.ArrayList;

public class Dealer {
    private Deck theDeck;
    private ArrayList<Card> dealersHand;


    Dealer(){
        this.theDeck = new Deck();
        this.dealersHand = new ArrayList<>();

    }
    public ArrayList<Card> dealHand(){
        // Check if the deck has fewer than 34 cards reshuffle
        if (theDeck.size() < 34) {
            theDeck.newDeck();
        }

        // Clear any existing cards in the dealer's hand
        dealersHand.clear();

        for (int i = 0; i < 3; i++) {
            dealersHand.add(theDeck.drawCard());
        }

        return new ArrayList<>(dealersHand);
    }

    // Gets the dealer's current hand
    public ArrayList<Card> getDealersHand() {
        return new ArrayList<>(dealersHand);
    }

    // Get the deck for testing
    public Deck getTheDeck() {
        return theDeck;
    }
}
