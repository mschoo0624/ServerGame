import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private int anteBet;
    private int pairPlusBet;
    private int playBet;
    private int totalWinnings;
    private boolean playerDecides;
    private boolean playerFolds;
    private String message;  // For game status messages
    private String gameState; // To track current game state

    // Constructor
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
        this.gameState = "WAITING_FOR_BET";
    }

    // Getters and Setters
    public ArrayList<Card> getPlayerHand() { return this.playerHand; }
    public void setPlayerHand(ArrayList<Card> hand) { this.playerHand = hand; }


    public ArrayList<Card> getDealerHand() { return this.dealerHand; }
    public void setDealerHand(ArrayList<Card> hand) { this.dealerHand = hand; }

    public int getAnteBet() { return anteBet; }
    public void setAnteBet(int anteBet) { this.anteBet = anteBet; }

    public int getPairPlusBet() { return pairPlusBet; }
    public void setPairPlusBet(int pairPlusBet) { this.pairPlusBet = pairPlusBet; }

    public int getPlayBet() { return playBet; }
    public void setPlayBet(int playBet) { this.playBet = playBet; }

    public int getTotalWinnings() { return totalWinnings; }
    public void setTotalWinnings(int totalWinnings) { this.totalWinnings = totalWinnings; }

    public boolean isPlayerDecides() { return playerDecides; }
    public void setPlayerDecides(boolean playerDecides) { this.playerDecides = playerDecides; }

    public boolean isPlayerFolds() { return playerFolds; }
    public void setPlayerFolds(boolean playerFolds) { this.playerFolds = playerFolds; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getGameState() { return gameState; }
    public void setGameState(String gameState) { this.gameState = gameState; }
}
