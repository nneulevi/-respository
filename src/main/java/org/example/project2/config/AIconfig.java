package org.example.project2.config;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Map;


@Configuration
public class AIconfig {
    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;
}
