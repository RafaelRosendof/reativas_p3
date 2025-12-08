package com.ms3.ms3.OpenAI;

import org.springframework.stereotype.Service;

@Service
public class prompt {
    
    public String gerarPrompt(String data) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are a financial analyst AI. Analyze the following stock market data and provide insights:\n\n");
        
        
        if(data == null || data.isEmpty()){
            prompt.append("No stock data provided.\n");
            return prompt.toString();
        }

        prompt.append(data);
        prompt.append("\n\n");
        
        prompt.append("Please provide:\n");
        prompt.append("1. Market sentiment analysis\n");
        prompt.append("2. Key trends and patterns\n");
        prompt.append("3. Risk assessment\n");
        prompt.append("4. Investment recommendations\n");
        
        return prompt.toString();
    }


}