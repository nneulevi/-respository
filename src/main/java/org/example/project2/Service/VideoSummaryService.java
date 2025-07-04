package org.example.project2.Service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class VideoSummaryService {
    @Autowired
    private ChatModel chatModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public File downloadVideo(String videoUrl) throws IOException {
        File temp = File.createTempFile("video", ".mp4");
        try (InputStream in = new URL(videoUrl).openStream();
             OutputStream out = new FileOutputStream(temp)) {
            FileCopyUtils.copy(in, out);
        }
        return temp;
    }

    public File extractAudio(File videoFile) throws IOException, InterruptedException {
        File audioFile = new File(videoFile.getParent(), "audio.mp3");
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-i", videoFile.getAbsolutePath(),
                "-vn", "-acodec", "libmp3lame", audioFile.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        if (process.waitFor() != 0) {
            throw new RuntimeException("ffmpeg 提取音频失败");
        }
        return audioFile;
    }

    public String transcribeAudio(File audioFile) {
        String url = "https://api.openai.com/v1/audio/transcriptions";

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(audioFile));
        body.add("model", "whisper-1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth("sk-..."); // 替换为你的 OpenAI Key

        HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        return (String) response.getBody().get("text");
    }

     public String summarizeText(String transcript) {
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("你是一个精通中文的视频总结助手，请根据用户提供的文字记录输出简洁、准确的中文总结。"),
                new UserMessage("请总结以下视频内容：\n\n" + transcript)
        ));

        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }
}
