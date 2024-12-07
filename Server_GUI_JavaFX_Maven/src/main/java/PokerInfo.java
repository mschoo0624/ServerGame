import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    // Game data
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private int anteBet;
    private int pairPlusBet;
    private int playBet;
    private int totalWinnings;

    // Game state control
    private String gameState;  // NEW_GAME, BETTING, DEALT, FOLDED, etc.
    private boolean playerDecides;
    private boolean playerFolds;
    private String message;    // Messages to display to the player

    public PokerInfo() {
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.anteBet = 0;
        this.pairPlusBet = 0;
        this.playBet = 0;
        this.totalWinnings = 0;
        this.playerDecides = false;
        this.playerFolds = false;
        this.message = "";
        this.gameState = "NEW_GAME";
    }

    // Getters and setters with defensive copying for collections
    public ArrayList<Card> getPlayerHand() { return this.playerHand; }
    public void setPlayerHand(ArrayList<Card> hand) { this.playerHand = hand; }


    public ArrayList<Card> getDealerHand() { return this.dealerHand; }
    public void setDealerHand(ArrayList<Card> hand) { this.dealerHand = hand; }

    // Regular getters and setters for primitive types
    public int getAnteBet() { return anteBet; }
    public void setAnteBet(int anteBet) { this.anteBet = anteBet; }

    public int getPairPlusBet() { return pairPlusBet; }
    public void setPairPlusBet(int pairPlusBet) { this.pairPlusBet = pairPlusBet; }

    public int getPlayBet() { return playBet; }
    public void setPlayBet(int playBet) { this.playBet = playBet; }

    public int getTotalWinnings() { return totalWinnings; }
    public void setTotalWinnings(int winnings) { this.totalWinnings += winnings; }

    public String getGameState() { return gameState; }
    public void setGameState(String gameState) { this.gameState = gameState; }

    public boolean isPlayerDecides() { return playerDecides; }
    public void setPlayerDecides(boolean decides) { this.playerDecides = decides; }

    public boolean isPlayerFolds() { return playerFolds; }
    public void setPlayerFolds(boolean folds) { this.playerFolds = folds; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // Utility methods
    public void resetBets() {
        this.anteBet = 0;
        this.pairPlusBet = 0;
        this.playBet = 0;
    }

    public void clearHands() {
        this.playerHand.clear();
        this.dealerHand.clear();
    }

    // Validation methods
    public boolean areValidBets() {
        boolean anteValid = anteBet >= 5 && anteBet <= 25;
        boolean pairPlusValid = pairPlusBet == 0 || (pairPlusBet >= 5 && pairPlusBet <= 25);
        return anteValid && pairPlusValid;
    }
}