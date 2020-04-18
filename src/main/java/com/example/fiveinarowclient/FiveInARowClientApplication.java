package com.example.fiveinarowclient;

import com.example.fiveinarowclient.rest.Client;
import com.google.gson.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@Log4j2
@SpringBootApplication
public class FiveInARowClientApplication implements CommandLineRunner {

	@Autowired
	Client client;
	Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		new SpringApplicationBuilder(FiveInARowClientApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Override
	public void run(String... args) throws Exception {

		try{
			System.out.print("[Input] Player 1: Name=");
			registerPlayerToGame(sc.next());
			System.out.print("[Input] Player 2: Name=");
			registerPlayerToGame(sc.next());
		} catch (Exception e) {
			log.info("Please make sure server is running, error=", e);
		}

		try {
			while (isGameRunning()) {
				printBoard();
				playNextTurn();
			}
			printGameResult();
		} catch (Exception e) {
			log.info("Please restart your server, if your input was wrong, error=", e);
		}

	}

	private void printGameResult() {
		System.out.println("Result Summary:");
		JsonObject response = new JsonParser()
				.parse(client.getGameStatus()).getAsJsonObject();

		String gameStatus = response.get("payload")
				.getAsJsonObject().get("gameStatus").getAsString();
		System.out.println(String.format("GameStatus:%s",gameStatus));
		if(gameStatus.equals("FINISHED")) {
			String winner = response.get("payload").getAsJsonObject().get("champion").getAsJsonObject()
					.get("name").getAsString();
			String color = response.get("payload").getAsJsonObject().get("champion").getAsJsonObject()
					.get("color").getAsString();
			System.out.println(String.format("Winner: %s (%s)",winner, color));
		}
	}

	private void registerPlayerToGame(String name) {
		client.registerPlayer(name);
	}

	private void playNextTurn() {
		JsonObject response = new JsonParser()
				.parse(client.getGameStatus()).getAsJsonObject();

		String playerNameToNextTurn = response.get("payload").getAsJsonObject()
				.get("playerToNextTurn").getAsJsonObject()
				.get("name").getAsString();
		int totalColumn = response.get("payload").getAsJsonObject()
				.get("board").getAsJsonObject().get("totalColumn").getAsInt();

		System.out.println(String.format("It's your turn %s, please enter column (1-%s): ", playerNameToNextTurn, totalColumn));
		int inputColumn = sc.nextInt();

		int playerIdToNextTurn = response.get("payload").getAsJsonObject()
				.get("playerToNextTurn").getAsJsonObject()
				.get("id").getAsInt();
		this.client.nextMove(playerIdToNextTurn, inputColumn);
	}

	private void printBoard() {
		JsonObject response = new JsonParser()
				.parse(client.getGameStatus()).getAsJsonObject();
		JsonArray jsonBoardTable = response.get("payload").getAsJsonObject()
				.get("board").getAsJsonObject().get("table").getAsJsonArray();
		System.out.println("=============Game Board============");
		for(JsonElement row: jsonBoardTable) {
			for(int i = 1; i < row.getAsJsonArray().size(); i++) {
				System.out.print(String.format("[%s]", row.getAsJsonArray().get(i)));
			}
			System.out.println();
		}
	}

	private boolean isGameRunning() {
		JsonObject response = new JsonParser()
				.parse(client.getGameStatus()).getAsJsonObject();
		String gameStatus = response.get("payload")
				.getAsJsonObject().get("gameStatus").getAsString();
		if(gameStatus.equals("INITIALISED")) {
			return true;
		}
		return false;
	}
}
