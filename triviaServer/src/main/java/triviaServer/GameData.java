package triviaServer;
import data.LevelData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;



public class GameData {

	private ArrayList<LevelData> levelsData;
	int numOfQuestions;
	Random rnd;

	public GameData(File file) {
		rnd = new Random();
		numOfQuestions = 0;
		levelsData = new ArrayList<LevelData>();
		readGameData(file);
	}

	public LevelData getLevel() {
		int randomNumber = rnd.nextInt(numOfQuestions);
		LevelData level = levelsData.get(randomNumber);
		levelsData.remove(randomNumber);
		numOfQuestions--;
		return level;
	}

	private void readGameData(File file) {
		int currentLine = 0;
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				numOfQuestions++;
				String q = scanner.nextLine();
				String a1 = scanner.nextLine();
				String a2 = scanner.nextLine();
				String a3 = scanner.nextLine();
				String a4 = scanner.nextLine();
				int correctAnswer = Integer.parseInt(scanner.nextLine());
				if(correctAnswer < 1 || correctAnswer > 4)
					throw new NumberFormatException();
				currentLine += 6;
				ArrayList<String> answers = new ArrayList<String>();
				answers.add(a1);
				answers.add(a2);
				answers.add(a3);
				answers.add(a4);
				levelsData.add(new LevelData(q, answers, correctAnswer));
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid number at line " + (currentLine + 6) + ". This line should contain a correct answer, represented by an integer between 1 and 4.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("An error occurred while reading data, Please make sure your file is following the correct format.");
			System.exit(0);
		}
	}
}
