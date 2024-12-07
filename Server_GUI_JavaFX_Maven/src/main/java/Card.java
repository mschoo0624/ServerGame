import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final char suit; // Suit of the card (‘C’, ‘D’, 'S', ‘H’ )
    private final int value; //2 to 14, where 14 is Ace, king 13, queen 12, jack 11, ten 10…..and so on.


    Card(char suit, int value){
        this.suit = suit;
        this.value = value;
    }

    public char getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }
}
