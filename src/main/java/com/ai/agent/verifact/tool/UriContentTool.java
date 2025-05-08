package com.ai.agent.verifact.tool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class UriContentTool {
	
	@Tool(description = "Check if the input is a valid URL")
    public boolean isUrl(String input) {
        try {
            new URL(input);
            System.out.println("Valid URL: " + input);
            return true;
        } catch (MalformedURLException e) {
        	System.out.println("It is not a URL: " + input);
            return false;
        }
    }
    
    @Tool(name = "fetchContentFromUrl", description = "Fetches and extracts content from the given URL.")
    public Optional<String> fetchContentFromUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            String title = document.title();
            String bodyText = document.body().text();
            System.out.println("Ftching content from URL: " + url);
            return Optional.of(title + " " + bodyText);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
