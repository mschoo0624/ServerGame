import java.util.ArrayList;
import java.util.Collections;

public class ThreeCardLogic {

    // Method to evaluate the type of hand
    public static int evalHand(ArrayList<Card> hand) {
        if (isStraightFlush(hand)) return 1; // Straight flush
        if (isThreeOfAKind(hand)) return 2; // Three of a kind
        if (isStraight(hand)) return 3; // Straight
        if (isFlush(hand)) return 4; // Flush
        if (isPair(hand)) return 5;// Pair
        return 0;// High card
    }

    // Method to evaluate PairPlus winnings
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int handType = evalHand(hand);
        switch (handType) {
            case 1: return bet * 40; // Straight flush payout
            case 2: return bet * 30; // Three of a kind payout
            case 3: return bet * 6;  // Straight payout
            case 4: return bet * 3;  // Flush payout
            case 5: return bet;      // Pair payout
            default: return 0;       // Loss
        }
    }

    // Method to compare hands between dealer and player
    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerHand = evalHand(dealer);
        int playerHand = evalHand(player);

        // If hand types are both HighCards
        if(playerHand == 0){
            if (dealerHand == 0) {
                int dealerHighCard = getHighestCard(dealer);
                int playerHighCard = getHighestCard(player);

                if (dealerHighCard > playerHighCard) return 1;
                if (playerHighCard > dealerHighCard) return 2;
            }
            return 1; // Dealer wins
        }

        if (dealerHand == 0) return 2;
        if (playerHand < dealerHand) return 2; // Player wins
        if (dealerHand < playerHand) return 1; // Dealer wins

        return 0; // Tie
    }

    // Determines if a hand is a Straight Flush
    private static boolean isStraightFlush(ArrayList<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    // Determines if a hand is Three of a Kind
    private static boolean isThreeOfAKind(ArrayList<Card> hand) {
        return hand.get(0).getValue() == hand.get(1).getValue() &&
                hand.get(1).getValue() == hand.get(2).getValue();
    }

    // Determines if a hand is a Straight
    private static boolean isStraight(ArrayList<Card> hand) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Card card : hand) {
            values.add(card.getValue());
        }
        Collections.sort(values);
        return values.get(1) == values.get(0) + 1 && values.get(2) == values.get(1) + 1;
    }

    // Determines if a hand is a Flush
    private static boolean isFlush(ArrayList<Card> hand) {
        char suit = hand.get(0).getSuit();
        return hand.get(1).getSuit() == suit && hand.get(2).getSuit() == suit;
    }

    // Determines if a hand is a Pair
    private static boolean isPair(ArrayList<Card> hand) {
        int val1 = hand.get(0).getValue();
        int val2 = hand.get(1).getValue();
        int val3 = hand.get(2).getValue();
        return val1 == val2 || val2 == val3 || val1 == val3;
    }

    // Gets the highest card value in a hand
    private static int getHighestCard(ArrayList<Card> hand) {
        int max = hand.get(0).getValue();
        for (Card card : hand) {
            if (card.getValue() > max) max = card.getValue();
        }
        return max;
    }

    // Checks if the dealer's hand qualifies (i.e., at least a Queen high)
    public static boolean dealerQualifies(ArrayList<Card> dealerHand) {
        for (Card card : dealerHand) {
            if (card.getValue() >= 12) { // Queen or higher
                return true;
            }
        }
        return false; // Dealer does not qualify if no Queen or higher card
    }
}