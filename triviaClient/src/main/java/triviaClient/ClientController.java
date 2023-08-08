package triviaClient;

import javafx.scene.control.TextInputDialog;
import data.LevelData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class ClientController {

	@FXML
	private Button answerBtn1;
	@FXML
	private Button answerBtn2;
	@FXML
	private Button answerBtn3;
	@FXML
	private Button answerBtn4;
	@FXML
	private Text pointsNumberTxt;
	@FXML
	private TextArea questionField;
	@FXML
	private Text questionNumberTxt;
	@FXML
	private Text timerTxt;

	ClientThread clientThread;

	LevelData currLevel;
	private Timeline timeline;

	private int currQuestionNum;
	private int answerChosen;
	private int currPointsNum;
	private int secondsRemaining;
	private String ip;
	
	public void initialize() {
		secondsRemaining = 30;
		currPointsNum = 0;
		Font font = new Font(35); // Button font's size should increase to 40
		questionField.setFont(font);
		this.ip = getIPFromUser();
		font = new Font(20);
		answerBtn1.setFont(font);
		answerBtn2.setFont(font);
		answerBtn3.setFont(font);
		answerBtn4.setFont(font);
		questionField.setEditable(false);
		questionField.setWrapText(true);
		clientThread = new ClientThread(this, ip);
		clientThread.start();
	}
	
	private String getIPFromUser() {
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Server IP");
	    dialog.setHeaderText(null);
	    dialog.setContentText("Please insert the server's IP");
	    Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
	    cancelButton.setText("Local");
	    return dialog.showAndWait().orElse("127.0.0.1");
	}



	@FXML
	private synchronized void answerPressed(ActionEvent event) {
		if (currLevel == null)
			return;

		if (timeline != null) {
			timeline.stop();
			timeline.getKeyFrames().clear();
		}
		Button button = (Button) event.getSource();
		if (button.equals(answerBtn1)) {
			answerChosen = 1;
		} else if (button.equals(answerBtn2)) {
			answerChosen = 2;
		} else if (button.equals(answerBtn3)) {
			answerChosen = 3;
		} else if (button.equals(answerBtn4)) {
			answerChosen = 4;
		}
		calculatePoints();
		updatePoints();
		clientThread.notifyAllThreads();
	}

	public int getAnswerChosen() {
		return answerChosen; 
	}

	public void restartGame() {
		answerChosen = 0;
		secondsRemaining = 30;
		currQuestionNum = 0;
		currPointsNum = 0;
		updateTimer();
		updatePoints();
		clientThread = new ClientThread(this, ip);
		clientThread.start();
	}

	public void newLevel(LevelData levelData) {
		answerChosen = 0;
		Platform.runLater(() -> {
			if (timeline != null) {
				timeline.stop();
				timeline.getKeyFrames().clear();
			}
			currLevel = levelData;
			updateQuestionNumText();
			questionField.setText("Question " + currQuestionNum);
			questionField.setText(currLevel.getQuestion());
			answerBtn1.setText(currLevel.getAnswers().get(0));
			answerBtn2.setText(currLevel.getAnswers().get(1));
			answerBtn3.setText(currLevel.getAnswers().get(2));
			answerBtn4.setText(currLevel.getAnswers().get(3));
			secondsRemaining = 30;
			updateTimer();
			timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
				secondsRemaining--;
				updateTimer();
				if (secondsRemaining == 0) {
					timeline.stop();
					timeline.getKeyFrames().clear();
					answerChosen = 5;
					currPointsNum = currPointsNum - 10;
					updatePoints();
					clientThread.notifyAllThreads();
				}
			}));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		});
	}

	
	public void calculatePoints()
	{
		if (answerChosen == currLevel.getCorrectAnswer()) {
			currPointsNum += 10;
		} else {
			currPointsNum -= 5;
		}
	}
	public void updatePoints() {
		pointsNumberTxt.setText("Points: " + currPointsNum);
	}

	public void updateTimer() {
		timerTxt.setText("Timer: " + secondsRemaining);
	}

	public void updateQuestionNumText() {
		currQuestionNum++;
		questionNumberTxt.setText("Quesion: " + currQuestionNum);
	}

	public void gameEndedMsg() {
	    Platform.runLater(() -> {
	        Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Game Over");
	        alert.setHeaderText(null);
	        alert.setContentText("You ended up with " + currPointsNum + " points.");

	        ButtonType rstBtn = new ButtonType("Restart");
	        ButtonType extBtn = new ButtonType("Exit");

	        alert.getButtonTypes().setAll(rstBtn, extBtn);

	        DialogPane dialogPane = alert.getDialogPane();
	        dialogPane.setStyle("-fx-font-size: 16px;");

	        alert.showAndWait().ifPresent(response -> {
	            if (response == rstBtn) {
	                restartGame();
	            } else if (response == extBtn) {
	                System.exit(0);
	            }
	        });
	    });
	}

}
