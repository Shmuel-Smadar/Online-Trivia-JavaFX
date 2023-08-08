package triviaClient;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.concurrent.CompletableFuture;

public class DialogUtility {

    public static CompletableFuture<Boolean> showErrorAndWait(String title, String content) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);

            ButtonType okButton = new ButtonType("OK");
            ButtonType exitButton = new ButtonType("Exit");
            alert.getButtonTypes().setAll(okButton, exitButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == exitButton) {
                    Platform.exit();  // Exit the JavaFX application
                    System.exit(0);   // Exit the entire program
                }
                future.complete(buttonType == okButton);
            });
        });

        return future;
    }
}