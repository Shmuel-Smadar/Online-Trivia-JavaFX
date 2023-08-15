package triviaClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ServerConnectionStageController {

    @FXML
    private TextField IPTextField;

    @FXML
    private ImageView internetIcon;

    @FXML
    private TextField portTextField;

    private ClientController clientCont;


    public ServerConnectionStageController(ClientController clientCont)
    {
        this.clientCont = clientCont;
    }


    public void initialize()
    {
        Image internetIconImage = new Image(getClass().getResourceAsStream("icon.jpg"));
        internetIcon.setImage(internetIconImage);
    }

    @FXML
    void LocalPressed(ActionEvent event) {
        clientCont.setIpAndPort("127.0.0.1", 3333);
        Stage currentStage = (Stage) IPTextField.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void OKPressed(ActionEvent event) {
        String ip = this.IPTextField.getText();
        int port;
        try{
            port = Integer.parseInt(this.portTextField.getText());
            if (port < 0 || port > 65535) {
                showMsg("Invalid port number", "Port number must be between 0 - 65535");
                return;
            }
        } catch(NumberFormatException e)
        {
            showMsg("Illegal Port", "The port you entered is illegal, please try again");
            return;
        }
        clientCont.setIpAndPort(ip, port);
        Stage currentStage = (Stage) IPTextField.getScene().getWindow();
        currentStage.close();
    }

    public void showMsg(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

