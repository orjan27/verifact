package com.ai.agent.verifact.service;

import java.util.Date;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.agent.verifact.tool.GoogleSearch;

@Service
public class AiService {
    @Autowired
    OpenAiChatModel aiClient;

    @Autowired
    GoogleSearch googleSearch;
    
    public String isFakeNews(String news) {
		if (news == null || news.trim().isEmpty()) {
			throw new IllegalArgumentException("News cannot be null or empty");
		}

		String searchResults = googleSearch.searchWeb(news);
		System.out.println("Searching the Web...\n" + searchResults);
		
		Date today = new Date();
		
		final PromptTemplate promptTemplate = new PromptTemplate("""
				You are a fact-checking assistant. Today is {today}. Based on the following web search results, determine if the following statement is real or fake. 
                Provide a short justification.

                Statement: "{input}"

                Web Search Results:
                {searchResults}

                Is the statement likely fake news? Reply with "Likely Fake", "Likely Real", or "Uncertain", followed by your reasoning.
			   """);
		promptTemplate.add("today", today);
		promptTemplate.add("input", news);
		promptTemplate.add("searchResults", searchResults);

		System.out.println("Prompting the AI...\n" + promptTemplate.create().getContents());
		final ChatResponse response = this.aiClient.call(promptTemplate.create());
		
		System.out.println("AI Response:\n" + response.getResult().getOutput().getText());	
		return response.getResult().getOutput().getText();
    }
    
  
}
