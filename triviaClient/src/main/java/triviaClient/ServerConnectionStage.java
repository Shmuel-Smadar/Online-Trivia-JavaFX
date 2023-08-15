package triviaClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ServerConnectionStage extends Application {

    ClientController clientCont;
    public ServerConnectionStage(ClientController clientCont) {
        this.clientCont = clientCont;
    }

    @Override
    public void start(Stage stage) {
        ServerConnectionStageController cont = new ServerConnectionStageController(clientCont);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerConnectionStage.fxml"));
        loader.setController(cont);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("Server Connection");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        stage.showAndWait();
        root.requestFocus();
    }
}
