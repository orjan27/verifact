package com.ai.agent.verifact.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.agent.verifact.tool.DateTimeTool;
import com.ai.agent.verifact.tool.GoogleSearchTool;


@Service
public class AiService {
    private final ChatClient chatClient;
    
    private final GoogleSearchTool googleSearchTool;
    
    private final DateTimeTool dateTimeTool;;

    @Autowired
    public AiService(ChatClient.Builder chatClientBuilder, GoogleSearchTool googleSearchTool, DateTimeTool dateTimeTool) {
		this.chatClient = chatClientBuilder.build();
		this.googleSearchTool = googleSearchTool;
		this.dateTimeTool = dateTimeTool;
	}
    
    public String isFakeNews(String news) {
		if (news == null || news.trim().isEmpty()) {
			throw new IllegalArgumentException("News cannot be null or empty");
		}
				
		final PromptTemplate promptTemplate = new PromptTemplate("""
				You are a fact-checking assistant. Today's date is provided by dateTodayTool. Based on the following web search results, determine if the following statement is real or fake. 
                Provide a short justification.

                Statement: "{input}"

                Is the statement likely fake news? Reply with "Likely Fake", "Likely Real", or "Uncertain", followed by your reasoning.
			   """);
		promptTemplate.add("input", news);

		final CallResponseSpec responseSpec = chatClient.prompt(promptTemplate.create())
													.tools(dateTimeTool, googleSearchTool)
													.call();
		
		return responseSpec.content();
    }
    
  
}
