package triviaServer;
import java.io.IOException;
import java.net.*;
import java.io.File;

public class Server {
	public Server(File file) {
		ServerSocket sc = null;
		Socket s = null;
		try {
			sc = new ServerSocket(3333);
			System.out.println("Server is up and running!");
			while(true) {
				s = sc.accept();
				new ServerThread(s, file).start();
			}
		} catch (IOException e) {
			System.out.println("Address already in use, please clear the 3333 port.");
			System.exit(0);
		} 
		
		try {
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  
	public static void main(String[] args) {
		if (args.length != 1) {
	            System.out.println("GameData file was not provided.");
	            return;
	        }
	        String filePath = args[0];
	        File file = new File(filePath);
	        new Server(file);
	}
}
