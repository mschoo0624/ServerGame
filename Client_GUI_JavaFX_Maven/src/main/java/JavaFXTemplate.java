import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;


public class JavaFXTemplate extends Application {
	static Client clientConnection;
	static PokerInfo pokerInfo;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Three Card Poker");
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/welcome.fxml")));
		Scene scene = new Scene(root, 700,700);
		scene.getStylesheets().add("/CSS/welcome.css");
		primaryStage.setScene(scene);
		primaryStage.show();

		clientConnection = new Client(data->{
			Platform.runLater(()->{

				if (data instanceof PokerInfo) {
					pokerInfo = (PokerInfo) data;
					System.out.println("Received game state: " + pokerInfo.getGameState());
					// The controller will access this info through clientConnection
				} else {
					System.out.println("Client message: " + data);
				}
			});
		});

		clientConnection.start();
	}

}
