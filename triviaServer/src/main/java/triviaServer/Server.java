package triviaServer;

import java.io.*;
import java.net.*;

public class Server {
    public Server(File file, int port) {
        if (file == null) {
            try {
                file = loadGameDataFromResources();
            } catch (Exception e) {

            }
        }
        ServerSocket sc = null;
        Socket s = null;
        try {
            sc = new ServerSocket(port);
            System.out.println("Server is up and running!");
            while (true) {
                s = sc.accept();
                new ServerThread(s, file).start();
            }
        } catch (IOException e) {
            System.out.println("Port already in use. please either clear the " + port + " port, or use a different one.");
            System.exit(0);
        }

        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File loadGameDataFromResources() throws IOException {
        // Get the URL of the resource
        URL resourceUrl = getClass().getResource("GameData.txt");
        if (resourceUrl == null) {
            throw new FileNotFoundException("GameData.txt not found in resources");
        }
        // Create a temporary file to store the game data
        File tempFile = File.createTempFile("GameData", ".txt");
        InputStream inputStream = resourceUrl.openStream();
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        return tempFile;
    }

    public static void main(String[] args) {
            File file = getFileFromArgs(args);
            int port = getPortFromArgs(args);
            new Server(file, port);
    }

    private static File getFileFromArgs(String[] args) {
        for (int i = 0; i < args.length - 1; ++i) {
            if (args[i].equals("-f")) {
                String filePath = args[i + 1];
                File file = new File(filePath);
                if (!file.exists() || file.isDirectory()) {
                    System.err.println("Invalid file path: " + filePath);
                    System.exit(0);
                }
                return file;
            }
        }
        return null;  // -f was not found. The program will resort to the default GameData
    }

    private static int getPortFromArgs(String[] args) {
        for (int i = 0; i < args.length - 1; ++i) {
            if (args[i].equals("-p")) {
                try {
                    int port = Integer.parseInt(args[i + 1]);
                    if (port < 0 || port > 65535) {
                        System.out.println("Invalid port number: " + port);
                        System.exit(0);
                    }
                    return port;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number: " + args[i + 1]);
                    System.exit(0);
                }
            }
        }
        return 3333;  // Default port if -p option was not found
    }
}
