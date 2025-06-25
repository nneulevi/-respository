package org.example.project2.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
@Service
public class AIService {
    private final OpenAiChatModel model;

    public AIService(OpenAiChatModel model) {
        this.model = model;
    }

    public String chat(String input) {
        Prompt prompt = new Prompt(new UserMessage(input));

        AssistantMessage reply = (AssistantMessage) model.call(prompt)
                .getResults()
                .get(0)
                .getOutput();

        return reply.getText();
    }
}
