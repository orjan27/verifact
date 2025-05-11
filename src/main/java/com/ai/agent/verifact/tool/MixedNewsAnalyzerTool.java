package com.ai.agent.verifact.tool;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MixedNewsAnalyzerTool {

    public List<String> splitIntoClaims(String input) {
        // Very basic sentence splitting; can be improved with NLP tools
        String[] sentences = input.split("(?<=[.!?])\\s+");
        List<String> claims = new ArrayList<>();
        for (String s : sentences) {
            if (!s.trim().isEmpty()) {
                claims.add(s.trim());
            }
        }
        return claims;
    }
}
