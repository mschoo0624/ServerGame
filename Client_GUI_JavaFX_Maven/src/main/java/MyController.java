import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MyController implements Initializable {
    Client clientPlayer = JavaFXTemplate.clientConnection;
//    PokerInfo info;
    private final Player player1;
    private final Player theDealer;
    MyController gameController;
    boolean player1Decides; //player2Decides;
    boolean player1Folds; //player2Folds;
    private static String text;
    private boolean isGame1Style = false; //Tracks the current CSS state

    @FXML
    private AnchorPane root3;
    @FXML
    private VBox root;
    // Buttons for gameplay actions
    @FXML
    private Button dealButton, betButton, foldButton;
    @FXML
    private Button cancelExitButton, confirmExitButton , backButton;
    // Text fields for player bets
    @FXML
    private TextField anteBet1, ppBet1;
    //game's info label
    @FXML
    private Label gameInfoLabel;
    //Wager info label
    @FXML
    private Label anteWagerInfo, playWagerInfo, pairPlusWagerInfo, playersName;
    //Stats info label
    @FXML
    private Label playersNameWin, winningLabel;
    //dealerCard Image
    @FXML
    private ImageView dealerCard1, dealerCard2, dealerCard3;
    //player1 Image
    @FXML
    private ImageView player1Card1, player1Card2, player1Card3;
    //rule Image
    @FXML
    private ImageView ruleImageView;
    //Rule button
    @FXML
    private Button backToMenuButton;


    public void initialize() {
        // Load the image
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/IMAGES/Rule/rule.png")).toExternalForm());
        ruleImageView.setImage(image);

        // Set up game callback

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // Load the rules image
        if (ruleImageView != null) {
            try {
                Image ruleImage = new Image(Objects.requireNonNull(getClass().getResource("/IMAGES/Rule/rule.png")).toExternalForm());
                ruleImageView.setImage(ruleImage);
            } catch (NullPointerException e) {
                System.err.println("Image not found: " + e.getMessage());
            }
        }
    }

    //Controller constructor
    public MyController() {
        theDealer = new Player();
        // Initialize players
        player1 = new Player();
//        info = new PokerInfo();
    }

//    //Method to handle game update
//    private void handleGameUpdate(PokerInfo info) {//FIXME
//        // Update UI based on received game info
//        if (info.getPlayerHand() != null) {
//            System.out.println(info.getPlayerHand());
//            player1.setHand(info.getPlayerHand());
//            showCards(player1, player1Card1,player1Card2,player1Card3);//Show player card
//        }
//        if (info.getDealerHand() != null) {//Shows Dealer card
//            theDealer.setHand(info.getDealerHand());
//
////            showCards(theDealer, player1Card1,player1Card2,player1Card3);
//        }
//        if (info.getMessage() != null) {
//            gameInfoLabel.setText(info.getMessage());
//        }
//
//        player1.setTotalWinnings(info.getTotalWinnings());
//    }


    // WELCOME SCREEN METHODS
    public void rulesButtonMethod(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/rule.fxml"));
        Parent root2 = fxmlLoader.load();
        root2.getStylesheets().clear();
        root2.getStylesheets().add("/CSS/rule.css");
        root.getScene().setRoot(root2);
    }

    @FXML
    public void handleBackToMenu(ActionEvent e) throws IOException {
        // Load the welcome FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/welcome.fxml"));
        Parent welcomeRoot = fxmlLoader.load();

        // Set the root of the current scene to the welcome screen
        Stage stage = (Stage) backToMenuButton.getScene().getWindow();
        stage.getScene().setRoot(welcomeRoot);
    }

    //Method for Play Game button
    public void gameButtonMethod(ActionEvent e) throws IOException {//FIXME
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/game.fxml"));
        Parent root3 = fxmlLoader.load();
        MyController controller = fxmlLoader.getController();
        gameController = controller;
        try {
            controller.setUp();// Reinitialize the game screen setup
        }catch (Exception event){
            System.err.println("Error: " + event.getMessage());
        }
        // controller.clientPlayer.send("Has Started A Game");
//        controller.handleGameUpdate(info);
        root3.getStylesheets().add("/CSS/game.css");



        root.getScene().setRoot(root3);
    }

    //Method for Exit Button
    public void exitButtonMethod(ActionEvent e) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    // GAME SCREEN SETUP
    public void setUp() throws IOException, ClassNotFoundException {//FIXME
        // Disable all action buttons except for the bet buttons initially
        betButton.setDisable(false);
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        ppBet1.setDisable(false);
        anteBet1.setDisable(false);
        player1Decides = false;
        player1Folds = false;
        player1.resetBets();
        player1.resetHand();

        // Send the NEW_GAME request to the server
        PokerInfo info = new PokerInfo();
        info.setGameState("NEW_GAME");
        clientPlayer.send(info);

        dealCards();

        gameInfoLabel.setText("Place your Wagers and hit the bet button start game");
    }

    public void handleGameUpdate(PokerInfo info) {
        if (info.getGameState().equals("WAITING_FOR_BET")) {
            player1.setHand(info.getPlayerHand());
            showCards(player1, player1Card1, player1Card2, player1Card3);
            System.out.println("Player hand size: " + player1.getHand().size());
            printCards();
        }
    }

            //MenuButton Items
    public void newLookMenu(ActionEvent e){
        // Clear existing stylesheets
        root3.getStylesheets().clear();
        if (isGame1Style) {
            root3.getStylesheets().add("/CSS/game.css");
        } else {
            root3.getStylesheets().add("/CSS/game1.css");
        }
        // Update the next change
        isGame1Style = !isGame1Style;
    }

    public void freshStartMenu(ActionEvent e) throws IOException, ClassNotFoundException {//FIXME
        setUp();
    }

    public void exitMenu(ActionEvent e) throws IOException {
        // Load the exit screen FXML
        endGameScreen();

    }
    //Closes the Screen
    public void handleConfirmExit(ActionEvent e){
        // Get the exit confirmation window
        Stage exitStage = (Stage) confirmExitButton.getScene().getWindow();
        exitStage.close();

        // Find the main window and close it
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                if (stage.getOwner() == null) {  // main window
                    stage.close();
                    break;
                }
            }
        }
    }

    //Goes back to game
    public void handleCancelExit(ActionEvent e){
        Stage stage = (Stage) cancelExitButton.getScene().getWindow();
        stage.close();
    }

    // Handle submit action for the bet buttons
    public void handleSubmitBet(ActionEvent e) {
        // Validate Player 1's bets
        boolean isPlayer1AnteValid = isValidBet(anteBet1.getText());
        boolean isPlayer1PPValid = ppBet1.getText().isEmpty() || isValidBet(ppBet1.getText());

        if (isPlayer1AnteValid && isPlayer1PPValid /*&& isPlayer2AnteValid && isPlayer2PPValid*/) {
            // All bets are valid; set bets for both players
            player1.setAnteBet(Integer.parseInt(anteBet1.getText().trim()));
            player1.setPairPlusBet(ppBet1.getText().isEmpty() ? 0 : Integer.parseInt(ppBet1.getText().trim()));

            // Create and send PokerInfo
            PokerInfo info = new PokerInfo();
            info.setAnteBet(player1.getAnteBet());
            info.setPairPlusBet(player1.getPairPlusBet());
            info.setGameState("BETTING");
            clientPlayer.send(info);
            // Enable the buttons to proceed with the game
            dealButton.setDisable(false);
            foldButton.setDisable(false);

            //Disable the bet buttons to prevent multiple submissions
            betButton.setDisable(true);

            //Clear and disable Wager Input
            ppBet1.clear();
            ppBet1.setDisable(true);
            anteBet1.clear();
            anteBet1.setDisable(true);

            gameInfoLabel.setText("DEAL or FOLD");

        } else {// Invalid bets
            gameInfoLabel.setText("Invalid bets: Ante must be $5-$25, Pair Plus is optional but also $5-$25 if entered.");
        }
    }

    //Allows Player1 play card
    public void handleDeal(ActionEvent e){
        player1.setPlayBet(player1.getAnteBet());//Set PlayWager

        //Send Server Info
        PokerInfo info = new PokerInfo();
        info.setAnteBet(player1.getAnteBet());
        info.setPairPlusBet(player1.getPairPlusBet());
        info.setPlayBet(player1.getPlayBet());
        info.setPlayerDecides(true);
        info.setPlayerFolds(false);
        info.setGameState("DEALT");
        info.setPlayerHand(player1.getHand());
        info.setDealerHand(theDealer.getHand());
        clientPlayer.send(info);

        // Update UI
        gameInfoLabel.setText("Player 1 Deals: Play wager set to $" + player1.getPlayBet());
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        player1Decides = true;
        checkWinning();
    }

    //Player folds
    public void handleFold(ActionEvent e){
        player1.resetBets();//Reset Bets
        //Send Server Info
        PokerInfo info = new PokerInfo();
        info.setPlayBet(0);
        info.setPairPlusBet(0);
        info.setAnteBet(0);
        info.setPlayerDecides(true);
        info.setPlayerFolds(true);
        info.setGameState("FOLD");
        clientPlayer.send(info);

        // Update UI
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        player1Decides = true;
        player1Folds = true;
        gameInfoLabel.setText("Player 1 Fold: Loses All Wagers ");
        checkWinning();
    }

    //This screen shows the player's wagers
    public void handleWager(ActionEvent e) throws IOException {
        // Loads player1's wager screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/wager.fxml"));
        Parent wagerRoot = fxmlLoader.load();
        MyController controller = fxmlLoader.getController();
        Stage wagerStage = new Stage();
        wagerStage.setTitle("Player Wager Info");
        wagerStage.setScene(new Scene(wagerRoot));
        wagerRoot.getStylesheets().add("/CSS/wager.css");
        //Set screen Info
        controller.playersName.setText("Player1's Wagers");
        controller.anteWagerInfo.setText("Ante Wager: $" + player1.getAnteBet());
        controller.playWagerInfo.setText("Play Wager: $" + player1.getPlayBet());
        controller.pairPlusWagerInfo.setText("Pair Plus Wager: $" + player1.getPairPlusBet());

        wagerStage.initOwner(root3.getScene().getWindow()); // Set owner to main window
        wagerStage.show();
    }

    public void backToGame(ActionEvent e) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void handleStats(ActionEvent e) throws IOException {
        // Loads player1's wager screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/win.fxml"));
        Parent winRoot = fxmlLoader.load();
        MyController controller = fxmlLoader.getController();
        Stage winStage = new Stage();
        winStage.setTitle("Player Winnings");
        winStage.setScene(new Scene(winRoot));
        winRoot.getStylesheets().add("/CSS/wager.css");
        //Set screen Info
        controller.playersNameWin.setText("Player1's Total Winnings");
        controller.winningLabel.setText("You have won a total of $" + player1.getTotalWinnings());

        winStage.initOwner(root3.getScene().getWindow()); // Set owner to main window
        winStage.show();
    }

    // Helper method to validate a bet amount (between $5 and $25)
    private boolean isValidBet(String betText) {
        try {
            int betAmount = Integer.parseInt(betText.trim());
            return betAmount >= 5 && betAmount <= 25;
        } catch (NumberFormatException e) {
            return false; // Invalid number format, bet is not valid
        }
    }

    private void checkWinning(){
        showCards(theDealer, dealerCard1, dealerCard2, dealerCard3);
        Platform.runLater(()-> {
            PokerInfo info2 = clientPlayer.getPokerInfo();
            switch(info2.getMessage()) {
                case "Dealer not qualified":
                    dealerNotQualified();
                    break;
                case "Dealer wins":
                    try {
                        dealerWins();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Player Wins":
                    try {
                        playerWins(info2);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Tie":
                    try {
                        tie();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Folds":
                    try {
                        endGameScreen();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }

        });

    }

    private void dealerWins() throws IOException {
        player1.resetBets();
        gameInfoLabel.setText("Dealer Win!!");
        setGameInfoLabelDelay("Game End!", 3);
        setGameInfoLabelDelay("Click 'Fresh Start' in the menu to begin a new game", 7);
        endGameScreen();
    }
    private void dealerNotQualified(){
        text = "Dealer does not have at least Queen high; ante wager is pushed";
        setGameInfoLabelDelay(text, 1);
        PauseTransition pause = new PauseTransition(Duration.seconds(4));//4 sec delay
        pause.setOnFinished(event -> {
            continueGame();
        });
        pause.play();
    }
    private void playerWins(PokerInfo info) throws IOException {
        player1.setAnteBet(info.getAnteBet());
        player1.setPairPlusBet(info.getPairPlusBet());
        player1.setPlayBet(info.getPlayBet());
        player1.setTotalWinnings(info.getTotalWinnings());
        gameInfoLabel.setText("Player Wins $" + player1.getTotalWinnings());
        setGameInfoLabelDelay("Game End!", 3);
        setGameInfoLabelDelay("Click 'Fresh Start' in the menu to begin a new game", 7);
        endGameScreen();

    }
    private void tie() throws IOException, ClassNotFoundException {
        text = "Player Ties with Dealer";
        setUp();
    }
    //Helper to pause between game info change
    private void setGameInfoLabelDelay(String newText, double delaySeconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(delaySeconds));
        pause.setOnFinished(event -> gameInfoLabel.setText(newText));
        pause.play();
    }

    //Helper to Show cards
    private Image getCardImage(char suit, int value) {
        String suitName = Character.toString(suit);
        String cardName = value + suitName + ".png";
        String path = "/IMAGES/cards/" + cardName;  // Assuming images are in resources/cards/
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    // Method to show the cards for a player or dealer
    private void showCards(Player player, ImageView... cardViews) {
        for (int i = 0; i < player.getHand().size(); i++) {
            Card card = player.getHand().get(i);
            Image cardImage = getCardImage(card.getSuit(), card.getValue());
            cardViews[i].setImage(cardImage);
        }
    }

    private void hideDealerCard(Player player, ImageView... cardViews){
        for (int i = 0; i < player.getHand().size(); i++) {
            String path= "/IMAGES/cards/BACK.png";
            cardViews[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
        }
    }

    //Method get the close screen
    private void endGameScreen() throws IOException {
        // Load the exit screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/exit.fxml"));
        Parent endRoot = fxmlLoader.load();
        Stage endStage = new Stage();
        endStage.setTitle("End Game");
        endStage.setScene(new Scene(endRoot));
        endRoot.getStylesheets().add("/CSS/exit.css");
        endStage.initOwner(root3.getScene().getWindow()); // Set owner to main window
        endStage.show();
    }

    private void dealCards() {
        Platform.runLater(()-> {
            PokerInfo info2 = clientPlayer.getPokerInfo();
            player1.setHand(info2.getPlayerHand());
            showCards(player1, player1Card1,player1Card2,player1Card3);

            theDealer.setHand(info2.getDealerHand());
            hideDealerCard(theDealer, dealerCard1, dealerCard2, dealerCard3);

        });
    }
    //Helper to check if player should continue game
    private void continueGame(){
        hideDealerCard(theDealer, dealerCard1, dealerCard2, dealerCard3);
        dealCards();
        dealButton.setDisable(false);
        foldButton.setDisable(false);
    }

//    //Helper Function to determine each player and the dealer's hand
    private void printCards(){
        //Player1
        System.out.println("Player  1");
        for (Card card : player1.getHand()) {
            System.out.println(card.getSuit() + ":" +card.getValue());
        }
    }
}