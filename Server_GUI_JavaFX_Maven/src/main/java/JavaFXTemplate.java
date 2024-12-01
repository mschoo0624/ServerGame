import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;


public class JavaFXTemplate extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Three Card Poker Server");
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/IntroScreen.fxml")));
		Scene scene = new Scene(root, 700,700);
		scene.getStylesheets().add("/CSS/IntroScreen.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
