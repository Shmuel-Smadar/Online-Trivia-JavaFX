package triviaServer;
import data.LevelData;
import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket s = null;
	private File file;
	private final int NUM_OF_LEVELS = 20;

	public ServerThread(Socket socket, File file) {
		this.s = socket;
		this.file = file;
	}

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
		for(int i = 0; i < NUM_OF_LEVELS; ++i)
		{
			objOutputStream.writeObject(gameData.getLevel());
			try {
		        	objInputStream.readObject(); // Read and discard the signal
		   	} catch (IOException e) {
		        System.out.println("Client has been disconnected...");
		        return;
		    }
		}
		objOutputStream.writeObject(null);
		System.out.println("Game ended. disconnecting...");
		// Close streams and socket
		objInputStream.close();
		inputStream.close();
		objOutputStream.close();
		outputStream.close();
	}
}