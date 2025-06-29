package org.example.project2.Kingbase;

import org.example.project2.Controller.meetingController;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.agendaMapper;
import org.example.project2.Mapper.meetMapper;
import org.example.project2.Service.AIService;
import org.example.project2.Service.meetingService;
import org.example.project2.entity.Agenda;
import org.example.project2.entity.Meeting;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.User_d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class meetingControllerTest {
    
    private meetingController meetingController;
    
    @Mock
    private ChatModel chatModel;
    
    @Mock
    private agendaMapper agendaMapper;
    
    @Mock
    private meetMapper meetMapper;
    
    @Mock
    private meetingService meetService;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private AIService aiService;
    
    @BeforeEach
    void setUp() {
        chatModel = mock(ChatModel.class);
        agendaMapper = mock(agendaMapper.class);
        meetMapper = mock(meetMapper.class);
        meetService = mock(meetingService.class);
        userMapper = mock(UserMapper.class);
        aiService = mock(AIService.class);
        
        meetingController = new meetingController();
        // 使用反射注入依赖
        try {
            var chatModelField = meetingController.class.getDeclaredField("chatModel");
            chatModelField.setAccessible(true);
            chatModelField.set(meetingController, chatModel);
            
            var agendaMapperField = meetingController.class.getDeclaredField("agendaMapper");
            agendaMapperField.setAccessible(true);
            agendaMapperField.set(meetingController, agendaMapper);
            
            var meetMapperField = meetingController.class.getDeclaredField("meetMapper");
            meetMapperField.setAccessible(true);
            meetMapperField.set(meetingController, meetMapper);
            
            var meetServiceField = meetingController.class.getDeclaredField("meetService");
            meetServiceField.setAccessible(true);
            meetServiceField.set(meetingController, meetService);
            
            var userMapperField = meetingController.class.getDeclaredField("userMapper");
            userMapperField.setAccessible(true);
            userMapperField.set(meetingController, userMapper);
            
            var aiServiceField = meetingController.class.getDeclaredField("AiService");
            aiServiceField.setAccessible(true);
            aiServiceField.set(meetingController, aiService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testSubmit_AddAgenda() {
        // 准备测试数据
        Agenda agenda = new Agenda(1L, 1L, "议程标题", "演讲者", LocalDateTime.now(), 60, "议程内容");
        
        // 模拟Mapper返回
        when(agendaMapper.insert(any(Agenda.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = meetingController.submit(agenda);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(agendaMapper).insert(agenda);
    }
    
    @Test
    void testGetAgenda_ByTime() {
        // 准备测试数据
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 60, "内容1"),
            new Agenda(2L, 1L, "议程2", "演讲者2", LocalDateTime.now().plusHours(1), 90, "内容2")
        );
        PageResult<Agenda> pageResult = new PageResult<>(1, 10, 2L, agendaList);
        
        // 模拟Service返回
        when(meetService.getSubAgendaByTime(1L, 1, 10)).thenReturn(pageResult);
        
        // 执行测试
        ResponseEntity<PageResult<Agenda>> response = meetingController.getAgenda(1L, 1, 10);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getList().size());
        assertEquals(2L, response.getBody().getTotal());
    }
    
    @Test
    void testGetPassedMeetings() {
        // 准备测试数据
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1),
            new Meeting(2L, "会议2", "cover2.jpg", "地址2", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2), "内容2", "创建者2", 1)
        );
        PageResult<Meeting> pageResult = new PageResult<>(1, 10, 2L, meetingList);
        
        // 模拟Service返回
        when(meetService.getPassedMeetingsByStartTime(anyLong(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(pageResult);
        
        // 执行测试
        ResponseEntity<PageResult<Meeting>> response = meetingController.getPassedMeetings(1L, "会议", "内容", "创建者", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getList().size());
        assertEquals(2L, response.getBody().getTotal());
    }
    
    @Test
    void testGetPassedMeetings_InvalidTimeRange() {
        // 执行测试 - 结束时间早于开始时间
        ResponseEntity<PageResult<Meeting>> response = meetingController.getPassedMeetings(
            null, null, null, null, 
            LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1, 10);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
    
    @Test
    void testDeleteMeeting_Success_Admin() {
        // 准备测试数据
        User_d user = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L);
        
        // 模拟Mapper返回
        when(meetMapper.getCreator(1L)).thenReturn("user1");
        when(userMapper.findByUsername("admin")).thenReturn(user);
        when(meetMapper.deleteById(1L)).thenReturn(1);
        when(agendaMapper.deleteByMeetingId(1L)).thenReturn(2);
        
        // 执行测试
        ResponseEntity<Integer> response = meetingController.deleteMeeting(1L, "admin");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(meetMapper).deleteById(1L);
        verify(agendaMapper).deleteByMeetingId(1L);
    }
    
    @Test
    void testDeleteMeeting_PermissionDenied() {
        // 准备测试数据
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        
        // 模拟Mapper返回
        when(meetMapper.getCreator(1L)).thenReturn("user2");
        when(userMapper.findByUsername("user1")).thenReturn(user);
        
        // 执行测试
        ResponseEntity<Integer> response = meetingController.deleteMeeting(1L, "user1");
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
        verify(meetMapper, never()).deleteById(anyLong());
    }
    
    @Test
    void testPass_PassMeeting() {
        // 模拟Mapper返回
        when(meetMapper.updateStatus(1L)).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = meetingController.pass(1L);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(meetMapper).updateStatus(1L);
    }
    
    @Test
    void testAddMeeting() {
        // 准备测试数据
        Meeting meeting = new Meeting(1L, "会议标题", "cover.jpg", "会议地址", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "会议内容", "user1", 0);
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("user1")).thenReturn(user);
        when(meetMapper.insert(any(Meeting.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = meetingController.addMeeting(meeting, "user1");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(meetMapper).insert(argThat(m -> m.getStatus() == 0));
    }
    
    /*@Test
    void testAsk_AISummary() {
        // 模拟AI服务返回
        when(aiService.chat("测试问题")).thenReturn("AI回答");
        
        // 执行测试
        ResponseEntity<String> response = meetingController.ask("测试问题");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AI回答", response.getBody());
        verify(aiService).chat("测试问题");
    }*/
} 