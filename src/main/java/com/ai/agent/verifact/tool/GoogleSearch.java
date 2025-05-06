package com.ai.agent.verifact.tool;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GoogleSearch {

    private static final String API_KEY = "AIzaSyDetJj72sisY-cvTP-3TXEDD_L8GEJmi58";
    private static final String SEARCH_ENGINE_ID = "017330425e0414c5c";


    public String searchWeb(String query) {
        String url = UriComponentsBuilder.fromUriString("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", API_KEY)
                .queryParam("cx", SEARCH_ENGINE_ID)
                .queryParam("q", query)
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        StringBuilder snippets = new StringBuilder();
        try {
            JsonNode root = new ObjectMapper().readTree(response);
            for (JsonNode item : root.path("items")) {
                snippets.append("- ").append(item.path("snippet").asText()).append("\n");
            }
        } catch (Exception e) {
            return "Error parsing search response: " + e.getMessage();
        }

        return snippets.toString();
    }
}
