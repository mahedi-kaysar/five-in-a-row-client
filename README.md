# Game Client
This is a sample client of five-in-row game server (https://github.com/mahedi-kaysar/five-in-a-row-server).

## Required Software 
* java 8+
* Maven 3.6+

## how to run
```$xslt
mvn clean package
java -jar target/five-in-a-row-client-0.0.1-SNAPSHOT.jar
```
## Note

* Please make sure server is up before running the client.
* Run server in localhost:8080
* If you put any invalid input please restart the server and start the client again. 
* if you have completed the game and want to play again please restart server.
* Because, the client did not handle the error response properly.
* However, server handles the invalid input and send appropriate error response.
* Please follow the readme of https://github.com/mahedi-kaysar/five-in-a-row-server for details of the endpoints.

## Sample output
![Alt text](docs/sampleOutput.JPG?raw=true "Title")
## Future work
* Test 
* Error response handling
* Improve client
* Add JavaDoc