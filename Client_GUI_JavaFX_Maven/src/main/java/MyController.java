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
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        ppBet1.setDisable(false);
        anteBet1.setDisable(false);
        player1Decides = false;
        player1Folds = false;
        player1.resetBets();
        player1.resetHand();;
//        dealCards();
        //Show players cards on screen
        showCards(player1, player1Card1, player1Card2, player1Card3);
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

    // Handle submit action for the bet buttons
    public void handleSubmitBet(ActionEvent e) {
        // Validate Player 1's bets
        boolean isPlayer1AnteValid = isValidBet(anteBet1.getText());
        boolean isPlayer1PPValid = ppBet1.getText().isEmpty() || isValidBet(ppBet1.getText());

        if (isPlayer1AnteValid && isPlayer1PPValid /*&& isPlayer2AnteValid && isPlayer2PPValid*/) {
            // All bets are valid; set bets for both players
            player1.setAnteBet(Integer.parseInt(anteBet1.getText().trim()));
            player1.setPairPlusBet(ppBet1.getText().isEmpty() ? 0 : Integer.parseInt(ppBet1.getText().trim()));

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
    public void handleDeal1(ActionEvent e){
        player1.setPlayBet(player1.getAnteBet()); //Sets players bet to play bet
        gameInfoLabel.setText("Player 1 Deals: Play wager set to " + player1.getPlayBet() + "$");
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        player1Decides = true;
    }

    //Allows Player1 fold during game
    public void handleFold1(ActionEvent e){
        gameInfoLabel.setText("Player 1 Fold: Loses All Wagers ");
        player1.resetBets();
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        player1Decides = true;
        player1Folds = true;
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
            player1Decides = false;
            dealButton.setDisable(false);
            foldButton.setDisable(false);
            //Show players cards on screen
            showCards(player1, player1Card1, player1Card2, player1Card3);
        }

        if (!player1Folds){
            player1Decides = false;
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
    private void printCards(){
        //Player1
        System.out.println("Player  1");
        for (Card card : player1.getHand()) {
            System.out.println(card.getSuit() + ":" +card.getValue());
        }
    }
}