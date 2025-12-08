package com.ms3.ms3.OpenAI;

import java.util.List;
import lombok.Data;

@Data
public class OpenAIChatReq {
    private String model;
    private List<OpenAIMessage> messages;

    public OpenAIChatReq(String model, List<OpenAIMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
}
