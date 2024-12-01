import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private int anteBet;
    private int playBet;
    private int pairPlusBet;
    private int totalWinnings;

    Player(){
        this.hand = new ArrayList<>();
        this.anteBet = 0;
        this.playBet = 0;
        this.pairPlusBet = 0;
        this.totalWinnings = 0;
    }

    public ArrayList<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }

    public int getAnteBet() {
        return anteBet;
    }

    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    public int getPlayBet() {
        return playBet;
    }

    public void setPlayBet(int playBet) {
        this.playBet = playBet;
    }

    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    public int getPairPlusBet() {
        return pairPlusBet;
    }

    public int getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(int totalWinnings) {
        this.totalWinnings += totalWinnings;
    }

    // Reset the player's hand for a new round
    public void resetHand() {
        hand.clear();
    }

    // Reset bets for a new round
    public void resetBets() {
        anteBet = 0;
        playBet = 0;
        pairPlusBet = 0;
    }
}
