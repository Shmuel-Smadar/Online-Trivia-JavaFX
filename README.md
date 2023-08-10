# Online-Trivia-JavaFX
## Prerequisites
You need at least **Java 11** or above to run the code.  
You need to use maven to compile the code. https://maven.apache.org/
## Building & Usage
```
git clone https://github.com/Shmuel-Smadar/Online-Trivia-JavaFX.git
cd Online-Trivia-JavaFX
mvn clean compile package
```
To get the server running, write: 
```
java -jar .\triviaServer\shade\triviaServer.jar GameData.txt
```
GameData.txt contains the trivia questions. Feel free to add more!  
To get the client running, write:
```
java -jar triviaClient\shade\triviaClient.jar
```
As the game opens you'll need to provide the server's IP. if you run it locally, just press "local". enjoy! (:
