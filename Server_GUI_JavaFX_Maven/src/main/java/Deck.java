import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {

    Deck(){// random deck of 52 cards
        newDeck();
    }

    public void newDeck(){
        this.clear();

        char[] suits = {'C', 'D', 'S', 'H'};

        for (char suit : suits){
            for(int val = 2; val <= 14; val++){
                this.add(new Card(suit, val));
            }
        }

        // Shuffle the deck to randomize the order of cards
        Collections.shuffle(this);
    }

    //Draw a random card from the top
    public Card drawCard() {
        return this.isEmpty() ? null : this.remove(0);
    }

}
