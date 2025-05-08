package com.ai.agent.verifact.tool;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GoogleSearchTool {

	@Value("${google.api.key}")
    private String API_KEY;
	
	@Value("${google.search.engine.id}")
    private String SEARCH_ENGINE_ID;


	@Tool(description = "Search the web using Google Custom Search API")
    public String searchWeb(String query) {
		System.out.println("Google Search query: " + query);
        final String url = UriComponentsBuilder.fromUriString("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", API_KEY)
                .queryParam("cx", SEARCH_ENGINE_ID)
                .queryParam("q", query)
                .build()
                .toUriString();

        final RestTemplate restTemplate = new RestTemplate();
        final String response = restTemplate.getForObject(url, String.class);

        final StringBuilder snippets = new StringBuilder();
        try {
            final JsonNode root = new ObjectMapper().readTree(response);
            for (final JsonNode item : root.path("items")) {
                snippets.append("- ").append(item.path("snippet").asText()).append("\n");
            }
        } catch (Exception e) {
            return "Error parsing search response: " + e.getMessage();
        }
        
        System.out.println("Google Search Response: " + snippets.toString());

        return snippets.toString();
    }
}
