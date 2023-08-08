package triviaClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Client extends Application {

	public void start(Stage stage) throws Exception {
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("Client.fxml"));
		Scene scene = new Scene(root, 600, 400);
		stage.setTitle("Trivia");
		stage.setScene(scene);
		stage.setOnCloseRequest(e -> {
			System.exit(0);
		});
		stage.show();
		root.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
		System.out.println();
	}
}
