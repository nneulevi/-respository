package org.example.project2;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.ai.openai")
public class OpenAiConfig {
    private String apiKey;

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    @PostConstruct
    public void printKey() {
        System.out.println("OpenAI API Key: " + apiKey);
    }
}
