package triviaClient;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import data.LevelData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Optional;

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

	private final int TIME_LIMIT = 15;
	private int currQuestionNum;
	private int answerChosen;
	private int currPointsNum;
	private int secondsRemaining;
	private String ip;
	private int port;
	
	public void initialize() {
		secondsRemaining = TIME_LIMIT;
		currPointsNum = 0;
		Font font = new Font(35); // Button font's size should increase to 40
		questionField.setFont(font);
		font = new Font(20);
		answerBtn1.setFont(font);
		answerBtn2.setFont(font);
		answerBtn3.setFont(font);
		answerBtn4.setFont(font);
		questionField.setEditable(false);
		questionField.setWrapText(true);
		getIPAndPortFromUser();
		clientThread = new ClientThread(this, ip);
		clientThread.start();
	}

	private void getIPAndPortFromUser() {
		ServerConnectionStage secondaryStage = new ServerConnectionStage(this);
		Stage stage = new Stage();
		secondaryStage.start(stage);
		// Wait until the secondary stage is closed
	}

	public void setIpAndPort(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
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
		secondsRemaining = TIME_LIMIT;
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
			secondsRemaining = TIME_LIMIT;
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
