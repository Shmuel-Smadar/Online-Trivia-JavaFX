package data;
import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable {

	private static final long serialVersionUID = 217069761137099354L;
	private String question;
	private ArrayList<String> answers;
	private int correctAnswer;
	
	public LevelData(String question, ArrayList<String> answers, int correctAnswer) {
		super();
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}
	public String getQuestion() {
		return question;
	}
	public ArrayList<String> getAnswers() {
		return answers;
	}
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	
}
