package org.example.project2.Kingbase;

import org.example.project2.Mapper.agendaMapper;
import org.example.project2.Mapper.meetMapper;
import org.example.project2.Service.meetingService;
import org.example.project2.entity.Agenda;
import org.example.project2.entity.Meeting;
import org.example.project2.entity.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class meetingServiceTest {
    
    private meetingService meetingService;
    
    @Mock
    private meetMapper meetMapper;
    
    @Mock
    private agendaMapper agendaMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meetingService = new meetingService();
        // 使用反射注入依赖
        try {
            var meetMapperField = meetingService.class.getDeclaredField("meetMapper");
            meetMapperField.setAccessible(true);
            meetMapperField.set(meetingService, meetMapper);
            
            var agendaMapperField = meetingService.class.getDeclaredField("agendaMapper");
            agendaMapperField.setAccessible(true);
            agendaMapperField.set(meetingService, agendaMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testGetPassedMeetingsByStartTime_Success() {
        // 准备测试数据 - 注意时间顺序，排序后应该是降序
        LocalDateTime startTime1 = LocalDateTime.now().plusDays(1); // 更晚的时间
        LocalDateTime endTime1 = startTime1.plusHours(2);
        LocalDateTime startTime2 = LocalDateTime.now(); // 更早的时间
        LocalDateTime endTime2 = startTime2.plusHours(2);
        
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "Team Meeting", null, "Conference Room A", startTime1, endTime1, "Weekly team discussion", "user1", 1),
            new Meeting(2L, "Project Review", null, "Conference Room B", startTime2, endTime2, "Project status review", "user2", 1)
        );
        
        // 模拟Mapper返回
        when(meetMapper.findMeetingsByKeyword(eq(1L), eq("Team"), eq("discussion"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(eq(1L), eq("Team"), eq("discussion"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(1L, "Team", "discussion", "user1", startTime2, endTime1, 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
        
        // 验证排序后的顺序 - 第一个应该是更晚的时间（Team Meeting）
        assertEquals("Team Meeting", result.getList().get(0).getTitle());
        assertEquals("Project Review", result.getList().get(1).getTitle());
        
        // 验证方法调用
        verify(meetMapper).findMeetingsByKeyword(1L, "Team", "discussion", "user1", startTime2, endTime1, 0, 10);
        verify(meetMapper).findMeetingsByKeywordCount(1L, "Team", "discussion", "user1", startTime2, endTime1);
    }
    
    @Test
    void testGetNotPassedMeeting_Success() {
        // 准备测试数据
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "Pending Meeting", null, "Conference Room C", startTime, endTime, "Waiting for approval", "user1", 0)
        );
        
        // 模拟Mapper返回
        when(meetMapper.findMeetingsNotPassed(eq(1L), eq("Pending"), eq("approval"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(eq(1L), eq("Pending"), eq("approval"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(1L);
        
        // 执行测试
        PageResult<Meeting> result = meetingService.getNotPassedMeeting(1L, "Pending", "approval", "user1", startTime, endTime, 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(0, result.getList().get(0).getStatus());
        assertEquals("Pending Meeting", result.getList().get(0).getTitle());
        
        // 验证方法调用
        verify(meetMapper).findMeetingsNotPassed(1L, "Pending", "approval", "user1", startTime, endTime, 0, 10);
        verify(meetMapper).findMeetingsNotPassedCount(1L, "Pending", "approval", "user1", startTime, endTime);
    }
    
    @Test
    void testGetPersonalMeeting_Success() {
        // 准备测试数据
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "My Meeting", null, "My Office", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Personal meeting", "user1", 1),
            new Meeting(2L, "My Pending Meeting", null, "Conference Room", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "Pending personal meeting", "user1", 0)
        );
        
        // 模拟Mapper返回
        when(meetMapper.selectByStatusAndAuthor(anyInt(), anyString(), anyInt(), anyInt()))
            .thenReturn(meetingList);
        when(meetMapper.selectByStatusAndAuthorCount(anyInt(), anyString()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<Meeting> result = meetingService.getPersonalMeeting(1, "user1", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertEquals("user1", result.getList().get(0).getCreator());
        assertEquals("user1", result.getList().get(1).getCreator());
        
        // 验证方法调用
        verify(meetMapper).selectByStatusAndAuthor(1, "user1", 0, 10);
        verify(meetMapper).selectByStatusAndAuthorCount(1, "user1");
    }
    
    @Test
    void testGetPersonalMeeting_EmptyResult() {
        // 模拟Mapper返回空列表
        when(meetMapper.selectByStatusAndAuthor(anyInt(), anyString(), anyInt(), anyInt()))
            .thenReturn(Arrays.asList());
        when(meetMapper.selectByStatusAndAuthorCount(anyInt(), anyString()))
            .thenReturn(0L);
        
        // 执行测试
        PageResult<Meeting> result = meetingService.getPersonalMeeting(0, "user1", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getList().size());
        assertEquals(0L, result.getTotal());
        
        // 验证方法调用
        verify(meetMapper).selectByStatusAndAuthor(0, "user1", 0, 10);
        verify(meetMapper).selectByStatusAndAuthorCount(0, "user1");
    }
    
    @Test
    void testGetSubAgendaByTime_Success() {
        // 准备测试数据
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "Agenda Item 1", "Speaker 1", LocalDateTime.now(), 30, "First agenda item"),
            new Agenda(2L, 1L, "Agenda Item 2", "Speaker 2", LocalDateTime.now().plusMinutes(30), 45, "Second agenda item")
        );
        
        // 模拟Mapper返回
        when(agendaMapper.findBystartTime(anyLong(), anyInt(), anyInt()))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(anyLong()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<Agenda> result = meetingService.getSubAgendaByTime(1L, 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertEquals("Agenda Item 1", result.getList().get(0).getTitle());
        assertEquals("Agenda Item 2", result.getList().get(1).getTitle());
        assertEquals(2L, result.getTotal());
        
        // 验证方法调用
        verify(agendaMapper).findBystartTime(1L, 0, 10);
        verify(agendaMapper).countByMeetingsId(1L);
    }
    
    @Test
    void testGetSubAgendaByDuration_Success() {
        // 准备测试数据
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "Short Agenda", "Speaker 1", LocalDateTime.now(), 15, "Short duration item"),
            new Agenda(2L, 1L, "Long Agenda", "Speaker 2", LocalDateTime.now().plusMinutes(15), 60, "Long duration item")
        );
        
        // 模拟Mapper返回
        when(agendaMapper.findByDuration(anyLong(), anyInt(), anyInt()))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(anyLong()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<Agenda> result = meetingService.getSubAgendaByDuration(1L, 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertEquals("Short Agenda", result.getList().get(0).getTitle());
        assertEquals("Long Agenda", result.getList().get(1).getTitle());
        assertEquals(2L, result.getTotal());
        
        // 验证方法调用
        verify(agendaMapper).findByDuration(1L, 0, 10);
        verify(agendaMapper).countByMeetingsId(1L);
    }
    
    @Test
    void testGetAgenda_Success() {
        // 准备测试数据
        Agenda agenda = new Agenda(1L, 1L, "Test Agenda", "Test Speaker", LocalDateTime.now(), 30, "Test agenda content");
        
        // 模拟Mapper返回
        when(agendaMapper.findById(1L)).thenReturn(agenda);
        
        // 执行测试
        Agenda result = meetingService.getAgenda(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("Test Agenda", result.getTitle());
        assertEquals("Test agenda content", result.getContent());
        assertEquals(30, result.getDuration());
        assertEquals("Test Speaker", result.getSpeaker());
        
        // 验证方法调用
        verify(agendaMapper).findById(1L);
    }
    
    @Test
    void testGetAgenda_NotFound() {
        // 模拟Mapper返回null
        when(agendaMapper.findById(999L)).thenReturn(null);
        
        // 执行测试
        Agenda result = meetingService.getAgenda(999L);
        
        // 验证结果
        assertNull(result);
        
        // 验证方法调用
        verify(agendaMapper).findById(999L);
    }
    
    @Test
    void testGetPassedMeetingsByStartTime_EmptyResult() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        
        // 模拟Mapper返回空列表
        when(meetMapper.findMeetingsByKeyword(eq(1L), eq("Nonexistent"), eq("content"), eq("user1"), eq(startTime), eq(endTime), eq(0), eq(10)))
            .thenReturn(Arrays.asList());
        when(meetMapper.findMeetingsByKeywordCount(eq(1L), eq("Nonexistent"), eq("content"), eq("user1"), eq(startTime), eq(endTime)))
            .thenReturn(0L);
        
        // 执行测试
        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(1L, "Nonexistent", "content", "user1", startTime, endTime, 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getList().size());
        assertEquals(0L, result.getTotal());
        
        // 验证方法调用
        verify(meetMapper).findMeetingsByKeyword(1L, "Nonexistent", "content", "user1", startTime, endTime, 0, 10);
        verify(meetMapper).findMeetingsByKeywordCount(1L, "Nonexistent", "content", "user1", startTime, endTime);
    }
    
    @Test
    void testGetSubAgendaByTime_EmptyResult() {
        // 模拟Mapper返回空列表
        when(agendaMapper.findBystartTime(anyLong(), anyInt(), anyInt()))
            .thenReturn(Arrays.asList());
        when(agendaMapper.countByMeetingsId(anyLong()))
            .thenReturn(0L);
        
        // 执行测试
        PageResult<Agenda> result = meetingService.getSubAgendaByTime(999L, 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getList().size());
        assertEquals(0L, result.getTotal());
        
        // 验证方法调用
        verify(agendaMapper).findBystartTime(999L, 0, 10);
        verify(agendaMapper).countByMeetingsId(999L);
    }
} 