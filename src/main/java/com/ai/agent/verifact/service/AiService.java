package com.ai.agent.verifact.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.agent.verifact.tool.DateTimeTool;
import com.ai.agent.verifact.tool.GoogleSearchTool;
import com.ai.agent.verifact.tool.UriContentTool;

@Service
public class AiService {
	private final ChatClient chatClient;

	private final GoogleSearchTool googleSearchTool;

	private final DateTimeTool dateTimeTool;
	
	private final UriContentTool uriContentTool;

	@Autowired
	public AiService(ChatClient.Builder chatClientBuilder, GoogleSearchTool googleSearchTool,
			DateTimeTool dateTimeTool, UriContentTool uriContentTool) {
		this.chatClient = chatClientBuilder.build();
		this.googleSearchTool = googleSearchTool;
		this.dateTimeTool = dateTimeTool;
		this.uriContentTool = uriContentTool;
	}

	public String isFakeNews(String input) {
		if (input == null || input.trim().isEmpty()) {
			throw new IllegalArgumentException("News cannot be null or empty");
		}

		final PromptTemplate promptTemplate = new PromptTemplate("""
			You are a fact-checking assistant. Use dateTimeTool for today’s date.
			
			1. If the input is a sentence and it is jumbled or ungrammatical, rewrite it in proper English.
			2. Use the corrected statement and search the web to decide if it's real or fake.
			
			Respond with one of the following:
			- "Likely Real"
			- "Likely Fake"
			- "Uncertain"
			
			Include a short reason for your answer.
			
			Original Statement: "{input}"
		""");
		promptTemplate.add("input", input);

		final CallResponseSpec responseSpec = chatClient.prompt(promptTemplate.create())
				.tools(dateTimeTool, uriContentTool, googleSearchTool).call();

		return responseSpec.content();
	}
}
