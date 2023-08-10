package triviaServer;
import data.LevelData;
import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket s = null;
	private File file;

	public ServerThread(Socket socket, File file) {
		this.s = socket;
		this.file = file;
	}

	private boolean gameEnded = false;

	@Override
	public void run() {
		super.run();
		GameData gameData = new GameData(file);
		try {
			handleReadAndWrite(gameData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handleReadAndWrite(GameData gameData) throws Exception {
		OutputStream outputStream = s.getOutputStream();
		ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);
		InputStream inputStream = s.getInputStream();
		ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
		System.out.println("Connection established. game started!");
		while (!gameEnded) {
			objOutputStream.writeObject(gameData.getLevel());
			try { //try and see if the client has declared end of game
				gameEnded = (boolean) objInputStream.readObject();
			} catch (IOException e) {
				System.out.println("Client has been dissconnected...");
				return;
			}
			Thread.sleep(100);
		}
		System.out.println("Game ended. disconnecting...");
		// Close streams and socket
		objInputStream.close();
		inputStream.close();
		objOutputStream.close();
		outputStream.close();

	}
}
