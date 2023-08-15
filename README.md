# Online-Trivia-JavaFX
## Prerequisites
You need at least **Java 11** or above to run the code.  
You need to use maven to compile the code. https://maven.apache.org/
## Building
```
git clone https://github.com/Shmuel-Smadar/Online-Trivia-JavaFX.git
cd Online-Trivia-JavaFX
mvn clean compile package
```
To get the server running, execute: 
```
java -jar .\triviaServer\shade\triviaServer.jar
```
To get the client running, execute (or double click):
```
java -jar triviaClient\shade\triviaClient.jar
```
As the game opens you'll need to provide the server's IP. if you run it locally, just press "local".
## Usage
the GameData.txt file is located in:
 ```
triviaServer\src\main\resources\triviaServer
```
and contains the trivia questions. Feel free to add more!  
you can also provide your own file when running the server:
```
java -jar .\triviaServer\shade\triviaServer.jar -f /path/to/your/gameData.txt
```
enjoy :)
