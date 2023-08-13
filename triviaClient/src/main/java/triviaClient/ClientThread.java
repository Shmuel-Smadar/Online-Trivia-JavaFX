package triviaClient;
import java.io.*;
import java.net.*;
import data.LevelData;

public class ClientThread extends Thread {

	private ClientController cont;
	private String ip;
	private LevelData levelData;
	private Object lock;
	private Socket s;
	private boolean connectionEstablished;

	public ClientThread(ClientController cont, String ip) {
		connectionEstablished = false;
		this.cont = cont;
		this.ip = ip;
		lock = new Object();
	}

	public void notifyAllThreads() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void run() {
		super.run();
		while (!connectionEstablished) {
			try {
				s = new Socket(ip, 3333);
				if (s != null)
					connectionEstablished = true;
			} catch (UnknownHostException e) {
				DialogUtility.showErrorAndWait("Error Connecting", "Cannot connect to server, press OK to try again.")
						.join();
			} catch (IOException e) {
				DialogUtility.showErrorAndWait("Error Connecting", "Cannot connect to server, press OK to try again.")
						.join();
			}
		}
		try {
			handleReadAndWrite();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public synchronized void handleReadAndWrite() throws Exception {
		OutputStream outputStream = s.getOutputStream();
		ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);
		InputStream inputStream = s.getInputStream();
		ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
		boolean gameEnded = false;
		
		while (!gameEnded) {
			levelData = (LevelData) objInputStream.readObject();
			if (levelData == null)
				gameEnded = true;
			else {
				cont.newLevel(levelData);
				synchronized (lock) {
					while (cont.getAnswerChosen() == 0) {
						lock.wait();
					}
					lock.notifyAll();
				}
				objOutputStream.writeObject("Level Ended");
			}
		}
		outputStream.close();
		objOutputStream.close();
		inputStream.close();
		objInputStream.close();
		cont.gameEndedMsg();
	}
}
