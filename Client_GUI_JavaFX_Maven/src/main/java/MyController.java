import javafx.animation.PauseTransition;
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
    private final Player player1;
    private final Player player2;
//    private final Dealer theDealer;
    MyController gameController;

    boolean player1Decides, player2Decides;
    boolean player1Folds, player2Folds;
    private static String text;

    private boolean isGame1Style = false; //Tracks the current CSS state

    @FXML
    private AnchorPane root3;
    @FXML
    private VBox root;
    // Buttons for gameplay actions
    @FXML
    private Button dealButton, deal2Button, betButton, bet2Button, foldButton, fold2Button;
    @FXML
    private Button cancelExitButton, confirmExitButton , backButton;
    // Text fields for player bets
    @FXML
    private TextField anteBet1, anteBet2, ppBet1, ppBet2;
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
    //player2 Image
    @FXML
    private ImageView player2Card1, player2Card2, player2Card3;
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
        // Initialize players
        player1 = new Player();
        player2 = new Player();
//        theDealer = new Dealer();
    }

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
    public void gameButtonMethod(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/game.fxml"));
        Parent root3 = fxmlLoader.load();
        MyController controller = fxmlLoader.getController();
        gameController = controller;
        controller.setUp();// Reinitialize the game screen setup
        controller.clientPlayer.send("Has Started A Game");
        root3.getStylesheets().add("/CSS/game.css");

        root.getScene().setRoot(root3);
//        Stage stage = (Stage) root.getScene().getWindow();FIXME
//        stage.setMinWidth(3000);
//        stage.setMinHeight(3000);

    }

    //Method for Exit Button
    public void exitButtonMethod(ActionEvent e) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    // GAME SCREEN SETUP
    public void setUp() {
        // Disable all action buttons except for the bet buttons initially
        betButton.setDisable(false);
        bet2Button.setDisable(false);
        dealButton.setDisable(true);
        deal2Button.setDisable(true);
        foldButton.setDisable(true);
        fold2Button.setDisable(true);
        ppBet1.setDisable(false);
        ppBet2.setDisable(false);
        anteBet1.setDisable(false);
        anteBet2.setDisable(false);
        player1Decides = false;
        player2Decides = false;
        player1Folds = false;
        player2Folds = false;
        player1.resetBets();
        player2.resetBets();
        player1.resetHand();
        player2.resetHand();
//        dealCards();
        //Show players cards on screen
        showCards(player1, player1Card1, player1Card2, player1Card3);
        showCards(player2, player2Card1, player2Card2, player2Card3);
        gameInfoLabel.setText("Place your Wagers and hit the bet button start game");
    }

    //MenuButton Items
    public void newLookMenu(ActionEvent e){
        // Clear existing stylesheets
        root3.getStylesheets().clear();

        // Toggle between game.css and game1.css
        if (isGame1Style) {
            root3.getStylesheets().add("/CSS/game.css");
            gameController.clientPlayer.send("Has Changed looks");
        } else {
            root3.getStylesheets().add("/CSS/game1.css");
            gameController.clientPlayer.send("Has Changed looks");
        }

        // Update the next change
        isGame1Style = !isGame1Style;
    }

    public void freshStartMenu(ActionEvent e){

        setUp();
        gameController.clientPlayer.send("Has Made A Fresh Game");
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

//    // Method to deal cards to the dealer and both players
//    private void dealCards() {
//        // Deal to players (implement similar logic as needed)
////        player1.setHand(theDealer.dealHand()); // Example of setting a player's hand
////        player2.setHand(theDealer.dealHand()); // Example of setting a player's hand
////
////        theDealer.dealHand();// Deal to dealer
////        showDealerCards("hide"); //Hide Dealer's card
//    }

    // Handle submit action for the bet buttons
    public void handleSubmitBet(ActionEvent e) {
        // Validate Player 1's bets
        boolean isPlayer1AnteValid = isValidBet(anteBet1.getText());
        boolean isPlayer1PPValid = ppBet1.getText().isEmpty() || isValidBet(ppBet1.getText());

        // Validate Player 2's bets
        boolean isPlayer2AnteValid = isValidBet(anteBet2.getText());
        boolean isPlayer2PPValid = ppBet2.getText().isEmpty() || isValidBet(ppBet2.getText());

        if (isPlayer1AnteValid && isPlayer1PPValid && isPlayer2AnteValid && isPlayer2PPValid) {
            // All bets are valid; set bets for both players
            player1.setAnteBet(Integer.parseInt(anteBet1.getText().trim()));
            player1.setPairPlusBet(ppBet1.getText().isEmpty() ? 0 : Integer.parseInt(ppBet1.getText().trim()));

            player2.setAnteBet(Integer.parseInt(anteBet2.getText().trim()));
            player2.setPairPlusBet(ppBet2.getText().isEmpty() ? 0 : Integer.parseInt(ppBet2.getText().trim()));

            // Enable the buttons to proceed with the game
            dealButton.setDisable(false);
            foldButton.setDisable(false);
            deal2Button.setDisable(false);
            fold2Button.setDisable(false);

            //Disable the bet buttons to prevent multiple submissions
            betButton.setDisable(true);
            bet2Button.setDisable(true);

            //Clear and disable Wager Input
            ppBet1.clear();
            ppBet1.setDisable(true);
            ppBet2.clear();
            ppBet2.setDisable(true);
            anteBet1.clear();
            anteBet1.setDisable(true);
            anteBet2.clear();
            anteBet2.setDisable(true);

            gameInfoLabel.setText("DEAL or FOLD");

        } else {// Invalid bets
            gameInfoLabel.setText("Invalid bets: Ante must be $5-$25, Pair Plus is optional but also $5-$25 if entered.");
        }
    }

    //Allows Player1 play card
    public void handleDeal1(ActionEvent e){
        player1.setPlayBet(player1.getAnteBet()); //Sets players bet to play bet
        gameInfoLabel.setText("Player 1 Deals: Play wager set to " + player1.getPlayBet() + "$");
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        player1Decides = true;
//        checkWinning();
    }

    //Allows Player2 play card
    public void handleDeal2(ActionEvent e){
        player2.setPlayBet(player2.getAnteBet());  //Sets players bet to play bet
        gameInfoLabel.setText("Player 2 Deals: Play wager set to " + player2.getPlayBet() + "$");
        deal2Button.setDisable(true);
        fold2Button.setDisable(true);
        player2Decides = true;
//        checkWinning();
    }

    //Allows Player1 fold during game
    public void handleFold1(ActionEvent e){
        gameInfoLabel.setText("Player 1 Fold: Loses All Wagers ");
        player1.resetBets();
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        player1Decides = true;
        player1Folds = true;
//        checkWinning();
    }

    //Allows Player2 fold during game
    public void handleFold2(ActionEvent e){
        gameInfoLabel.setText("Player 2 Fold: Loses All Wagers ");
        player2.resetBets();
        deal2Button.setDisable(true);
        fold2Button.setDisable(true);
        player2Decides = true;
        player2Folds = true;
//        checkWinning();
    }

    //This screen shows the wagers of player 1
    public void handleWager1(ActionEvent e) throws IOException {
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
    //This screen shows the wagers of player 2
    public void handleWager2(ActionEvent e) throws IOException {
        // Loads player1's wager screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/wager.fxml"));
        Parent wagerRoot = fxmlLoader.load();
        MyController controller = fxmlLoader.getController();
        Stage wagerStage = new Stage();
        wagerStage.setTitle("Player Wager Info");
        wagerStage.setScene(new Scene(wagerRoot));
        wagerRoot.getStylesheets().add("/CSS/wager.css");
        //Set screen Info
        controller.playersName.setText("Player2's Wagers");
        controller.anteWagerInfo.setText("Ante Wager: $" + player2.getAnteBet());
        controller.playWagerInfo.setText("Play Wager: $" + player2.getPlayBet());
        controller.pairPlusWagerInfo.setText("Pair Plus Wager: $" + player2.getPairPlusBet());

        wagerStage.initOwner(root3.getScene().getWindow()); // Set owner to main window
        wagerStage.show();
    }
    public void backToGame(ActionEvent e) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void handleStats1(ActionEvent e) throws IOException {
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

    public void handleStats2(ActionEvent e) throws IOException {
        // Loads player1's wager screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/win.fxml"));
        Parent winRoot = fxmlLoader.load();
        MyController controller = fxmlLoader.getController();
        Stage winStage = new Stage();
        winStage.setTitle("Player Winnings");
        winStage.setScene(new Scene(winRoot));
        winRoot.getStylesheets().add("/CSS/wager.css");
        //Set screen Info
        controller.playersNameWin.setText("Player2's Total Winnings");
        controller.winningLabel.setText("You have won a total of $" + player2.getTotalWinnings());

        winStage.initOwner(root3.getScene().getWindow()); // Set owner to main window
        winStage.show();
    }

//    private void checkWinning(){
//        if (player1Decides && player2Decides) {
//            showDealerCards("show");//Show player's Card
//            if(player1Folds && player2Folds) {
//                gameInfoLabel.setText("Both players loses wager");
//            }
//            else if (ThreeCardLogic.dealerQualifies(theDealer.getDealersHand())) {
//                setGameInfoLabelDelay("Checking Winnings", 1);
//                //Comparing each player hand
//                int p1result = ThreeCardLogic.compareHands(theDealer.getDealersHand(), player1.getHand());
//                int p2result = ThreeCardLogic.compareHands(theDealer.getDealersHand(), player2.getHand());
//
//                //Player1 wins and doesn't fold
//                if (p1result == 2 && !player1Folds) {
//                    player1.setTotalWinnings(player1.getAnteBet() * 2);
//                    player1.setTotalWinnings(player1.getPlayBet() * 2);
//
//                    if (player2.getPairPlusBet() != 0) {//Checks for pair plus winning
//                        int newPP = ThreeCardLogic.evalPPWinnings(player1.getHand(), player1.getPairPlusBet());
//                        player1.setTotalWinnings(newPP);
//                        text = "Player1 Wins AntePair $" + (player1.getAnteBet() * 2) + " and PairPlus: $" + newPP;
//                        setGameInfoLabelDelay(text, 2.5);
//                    } else {
//                        text = "Player1 Wins: $" + player1.getTotalWinnings();
//                        setGameInfoLabelDelay(text, 2.5);
//                    }
//                } else if (p1result == 1 && !player1Folds) {//Dealer Wins against Player1
//                    text = "Dealer Wins against Player1";
//                    setGameInfoLabelDelay(text, 2.5);
//                    player1.resetBets();
//                } else if (p1result == 0 && !player1Folds){//Dealer Ties with Player
//                    text = "Dealer ties Player1";
//                    setGameInfoLabelDelay(text, 2.5);
//                }
//                //Player2 wins and doesn't fold
//                if (p2result == 2 && !player2Folds) {
//                    player2.setTotalWinnings(player2.getAnteBet() * 2);
//                    player2.setTotalWinnings(player2.getPlayBet() * 2);
//
//                    if (player2.getPairPlusBet() != 0) {//Check PairPlus Winning
//                        int newPP = ThreeCardLogic.evalPPWinnings(player2.getHand(), player2.getPairPlusBet());
//                        player2.setTotalWinnings(newPP);
//                        text = "Player2 Wins $" + (player2.getAnteBet() * 2) + " and PairPlus: $" + newPP;
//                        setGameInfoLabelDelay(text, 5);
//                    }
//                    else {
//                        text = "Player2 Wins: $" + (player2.getAnteBet() * 2);
//                        setGameInfoLabelDelay(text, 5);
//                    }
//                } else if (p2result == 1 && !player2Folds) {//Dealer Wins against Player2
//                    text = "Dealer Wins against Player2";
//                    setGameInfoLabelDelay(text, 5);
//                    player2.resetBets();
//                } else if(p2result == 0 && !player2Folds){//Dealer Ties with Player2
//                    text = "Player 2 Ties with Dealer";
//                    setGameInfoLabelDelay(text, 5);
//                }
//            }
//            else{//Dealer does not qualify
//                text = "Dealer does not have at least Queen high; ante wager is pushed";
//                setGameInfoLabelDelay(text, 1);
//                PauseTransition pause = new PauseTransition(Duration.seconds(4));//4 sec delay
//                pause.setOnFinished(event -> {
//                    continueGame();
//                });
//                pause.play();
//                return;
//            }
//            setGameInfoLabelDelay("Game End!", 8);
//            setGameInfoLabelDelay("Click 'Fresh Start' in the menu to begin a new game", 9);
//
//            PauseTransition pause = new PauseTransition(Duration.seconds(10));//10 sec delay
//            pause.setOnFinished(event -> {
//                try {
//                    endGameScreen();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            pause.play();
//
//        }
//    }
    // Helper method to validate a bet amount (between $5 and $25)
    private boolean isValidBet(String betText) {
        try {
            int betAmount = Integer.parseInt(betText.trim());
            return betAmount >= 5 && betAmount <= 25;
        } catch (NumberFormatException e) {
            return false; // Invalid number format, bet is not valid
        }
    }

    //Helper to check if player should continue game
    private void continueGame(){

//        dealCards();//Deal new hands
        text = "DEAL OR FOLD";
        setGameInfoLabelDelay(text, 3);

        //Push Player1 to the next hand
        if(!player1Folds){
            player2Decides = true; //To enable checkWinning
            player1Decides = false;
            dealButton.setDisable(false);
            foldButton.setDisable(false);
            //Show players cards on screen
            showCards(player1, player1Card1, player1Card2, player1Card3);
        }
        //Push Player2 to the next hand
        if (!player2Folds){
            player1Decides = true; //To enable checkWinning
            player2Decides = false;
            deal2Button.setDisable(false);
            fold2Button.setDisable(false);
            showCards(player2, player2Card1, player2Card2, player2Card3);
        }

        if (!player1Folds && !player2Folds){
            player1Decides = false;
            player2Decides = false;
        }
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

//    //Method to show or hide Dealer's card
//    private void  showDealerCards(String display) {
//        ImageView[] cardView = new ImageView[]{dealerCard1, dealerCard2, dealerCard3};
//
//        if (Objects.equals(display, "hide")){//Hides Dealers card
//            for (int i = 0; i < theDealer.getDealersHand().size(); i++) {
//                String path= "/IMAGES/cards/BACK.png";
//                cardView[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
//            }
//        }
//        else if (Objects.equals(display, "show")){//Shows Dealers card
//            for (int i = 0; i < theDealer.getDealersHand().size(); i++) {
//                Card card = theDealer.getDealersHand().get(i);
//                Image cardImage = getCardImage(card.getSuit(), card.getValue());
//                cardView[i].setImage(cardImage);
//            }
//        }
//    }

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

//    //Helper Function to determine each player and the dealer's hand
//    private void printCards(){
//        //The Dealer
//        System.out.println("theDealer");
//        for (Card card : theDealer.getDealersHand()) {
//            System.out.println(card.getSuit() + ":" +card.getValue());
//        }
//        //Player1
//        System.out.println("Player  1");
//        for (Card card : player1.getHand()) {
//            System.out.println(card.getSuit() + ":" +card.getValue());
//        }
//        //Player2
//        System.out.println("Player  2");
//        for (Card card : player2.getHand()) {
//            System.out.println(card.getSuit() + ":" +card.getValue());
//        }
//    }
}