package triviaClient;
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

	public GameData() {
		rnd = new Random();
		numOfQuestions = 0;
		levelsData = new ArrayList<LevelData>();
		readGameData();
	}

	public LevelData getLevel() {
		int randomNumber = rnd.nextInt(numOfQuestions);
		LevelData level = levelsData.get(randomNumber);
		levelsData.remove(randomNumber);
		numOfQuestions--;
		return level;
	}

	private void readGameData() {
		try {
			Scanner scanner = new Scanner(new File("GameData.txt")); 

			while (scanner.hasNextLine()) {
				numOfQuestions++;
				String q = scanner.nextLine();
				String a1 = scanner.nextLine();
				String a2 = scanner.nextLine();
				String a3 = scanner.nextLine();
				String a4 = scanner.nextLine();
				int correntAnswer = Integer.parseInt(scanner.nextLine());

				ArrayList<String> answers = new ArrayList<String>();
				answers.add(a1);
				answers.add(a2);
				answers.add(a3);
				answers.add(a4);
				levelsData.add(new LevelData(q, answers, correntAnswer));
			}
			scanner.close();
		} catch (IOException e) {
		}
	}
}
