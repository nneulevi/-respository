package org.example.project2.ServiceTest;

import org.example.project2.Service.VideoSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VideoSummaryServiceTest {
    @Mock
    private ChatModel chatModel;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private VideoSummaryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 用反射注入mock的RestTemplate
        try {
            var field = VideoSummaryService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDownloadVideo_success() throws Exception {
        String url = new File("src/test/resources/test.mp4").toURI().toURL().toString();
        var method = service.getClass().getDeclaredMethod("downloadVideo", String.class);
        method.setAccessible(true);
        File file = (File) method.invoke(service, url);
        assertTrue(file.exists());
        assertTrue(file.getName().endsWith(".mp4"));
    }

    @Test
    void testDownloadVideo_fail() {
        assertThrows(Exception.class, () -> {
            service.getClass().getDeclaredMethod("downloadVideo", String.class)
                    .invoke(service, "http://not-exist-url/video.mp4");
        });
    }

    @Test
    void testExtractAudio_success() throws Exception {
        // 需要本地有ffmpeg和一个小的mp4文件
        File video = new File("src/test/resources/test.mp4");
        File audio = (File) service.getClass().getDeclaredMethod("extractAudio", File.class)
                .invoke(service, video);
        assertTrue(audio.exists());
        assertTrue(audio.getName().endsWith(".mp3"));
    }

    @Test
    void testExtractAudio_fail() throws Exception {
        File fake = new File("src/test/resources/not_a_video.txt");
        assertThrows(Exception.class, () -> {
            service.getClass().getDeclaredMethod("extractAudio", File.class)
                    .invoke(service, fake);
        });
    }

    @Test
    void testTranscribeAudio_success() throws Exception {
        File audio = new File("src/test/resources/test.mp3");
        Map<String, String> map = new HashMap<>();
        map.put("text", "转录内容");
        ResponseEntity<Map> response = new ResponseEntity<>(map, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(response);
        String result = (String) service.getClass().getDeclaredMethod("transcribeAudio", File.class)
                .invoke(service, audio);
        assertEquals("转录内容", result);
    }

    @Test
    void testTranscribeAudio_fail() throws Exception {
        File audio = new File("src/test/resources/test.mp3");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("API错误"));
        assertThrows(Exception.class, () -> {
            service.getClass().getDeclaredMethod("transcribeAudio", File.class)
                    .invoke(service, audio);
        });
    }

    @Test
    void testSummarizeText_success() throws Exception {
        String transcript = "视频内容很精彩。";
        ChatResponse mockResponse = mock(ChatResponse.class);
        Generation result = mock(Generation.class);
        AssistantMessage output = mock(AssistantMessage.class);
        when(output.getText()).thenReturn("总结：精彩");
        when(result.getOutput()).thenReturn(output);
        when(mockResponse.getResult()).thenReturn(result);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);

        var method = service.getClass().getDeclaredMethod("summarizeText", String.class);
        method.setAccessible(true); // 关键
        String summary = (String) method.invoke(service, transcript);

        assertEquals("总结：精彩", summary);
    }

    @Test
    void testSummarizeText_fail() throws Exception {
        String transcript = "视频内容很精彩。";
        when(chatModel.call(any(Prompt.class))).thenThrow(new RuntimeException("AI错误"));
        assertThrows(Exception.class, () -> {
            service.getClass().getDeclaredMethod("summarizeText", String.class)
                    .invoke(service, transcript);
        });
    }
} 