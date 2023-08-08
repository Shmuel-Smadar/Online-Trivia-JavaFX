module triviaClient {
    requires javafx.controls;
    requires javafx.fxml;
requires sharedData;
    opens triviaClient to javafx.fxml;
    exports triviaClient;
}