package com.example.fiveinarowclient.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class Client {

    @Value("${game.client.apiBaseUrl:http://localhost:8080}")
    private String apiBaseURL;

    private RestTemplate restTemplate;

    @Autowired
    public Client(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     *
     * @return
     */
    public String getGameStatus() {
        String endpoint = String.format("%s/game/board/state", apiBaseURL);
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(endpoint, String.class);
        return responseEntity.getBody();
    }

    /**
     *
     * @param name
     * @return
     * @throws RestClientException
     */
    public String registerPlayer(String name) throws RestClientException {
        String endpoint = String.format("%s/game/register/player", apiBaseURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"name\":\""+ name + "\"}";
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(endpoint, request, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
            throw new RestClientException("problem in registering player.");
        }
        return responseEntity.getBody();
    }

    /**
     *
     * @param playerId
     * @throws RestClientException
     */
    public void disconnectPlayer(int playerId) throws RestClientException {
        String endpoint = String.format("%s/game/player/%s/disconnect", apiBaseURL, playerId);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("playerId", playerId);
        restTemplate.delete(endpoint, params);
    }

    /**
     *
     * @param playerId
     * @param column
     * @return
     */
    public String nextMove(int playerId, int column) {
        String endpoint = String.format("%s/game/player/%s/next-move/%s",
                apiBaseURL, playerId, column);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("playerId", playerId);
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(endpoint, String.class, params);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RestClientException("problem in next move.");
        }
        return responseEntity.getBody();
    }
}
