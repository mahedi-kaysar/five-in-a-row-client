package com.example.fiveinarowclient;

import com.example.fiveinarowclient.rest.Client;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Log4j2
@SpringBootApplication
public class FiveInARowClientApplication implements CommandLineRunner {

	@Autowired
	Client client;

	public static void main(String[] args) {
		new SpringApplicationBuilder(FiveInARowClientApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Override
	public void run(String... args) throws Exception {

		try {
			String connectionInfo1 = client.registerPlayer("Mahedi");
			log.info(connectionInfo1);
			String connectionInfo2 = client.registerPlayer("Kaysar");
			log.info(connectionInfo2);
			//client.disconnectPlayer(1);
			String nextMove = client.nextMove(1, 5);
			log.info(nextMove);
			String status = client.getGameStatus();
			log.info(status);
		} catch (Exception e) {
			log.error("error in rest client", e);
		}
	}
}
