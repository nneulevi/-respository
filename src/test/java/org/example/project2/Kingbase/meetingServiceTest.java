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
        LocalDateTime startTime1 = LocalDateTime.now().plusDays(1); // 更晚的时间
        LocalDateTime endTime1 = startTime1.plusHours(2);
        LocalDateTime startTime2 = LocalDateTime.now(); // 更早的时间
        LocalDateTime endTime2 = startTime2.plusHours(2);
        
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "Team Meeting", null, "Conference Room A", startTime1, endTime1, "Weekly team discussion", "user1", 1),
            new Meeting(2L, "Project Review", null, "Conference Room B", startTime2, endTime2, "Project status review", "user2", 1)
        );

        when(meetMapper.findMeetingsByKeyword(eq(1L), eq("Team"), eq("discussion"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(eq(1L), eq("Team"), eq("discussion"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(2L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(1L, "Team", "discussion", "user1", startTime2, endTime1, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());

        assertEquals("Team Meeting", result.getList().get(0).getTitle());
        assertEquals("Project Review", result.getList().get(1).getTitle());

        verify(meetMapper).findMeetingsByKeyword(1L, "Team", "discussion", "user1", startTime2, endTime1, 0, 10);
        verify(meetMapper).findMeetingsByKeywordCount(1L, "Team", "discussion", "user1", startTime2, endTime1);
    }
    
    @Test
    void testGetNotPassedMeeting_Success() {

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "Pending Meeting", null, "Conference Room C", startTime, endTime, "Waiting for approval", "user1", 0)
        );

        when(meetMapper.findMeetingsNotPassed(eq(1L), eq("Pending"), eq("approval"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(eq(1L), eq("Pending"), eq("approval"), eq("user1"), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getNotPassedMeeting(1L, "Pending", "approval", "user1", startTime, endTime, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(0, result.getList().get(0).getStatus());
        assertEquals("Pending Meeting", result.getList().get(0).getTitle());

        verify(meetMapper).findMeetingsNotPassed(1L, "Pending", "approval", "user1", startTime, endTime, 0, 10);
        verify(meetMapper).findMeetingsNotPassedCount(1L, "Pending", "approval", "user1", startTime, endTime);
    }
    
    @Test
    void testGetPersonalMeeting_Success() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(2L, "My Pending Meeting", null, "Conference Room", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "Pending personal meeting", "user1", 0),
            new Meeting(1L, "My Meeting", null, "My Office", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Personal meeting", "user1", 1)
        );
        when(meetMapper.selectByStatusAndAuthor(eq(1), eq("user1"), eq(10), eq(0)))
            .thenReturn(meetingList);
        when(meetMapper.selectByStatusAndAuthorCount(eq(1), eq("user1")))
            .thenReturn(2L);
        PageResult<Meeting> result = meetingService.getPersonalMeeting(1, "user1", 1, 10);
        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertTrue(result.getList().stream().allMatch(m -> "user1".equals(m.getCreator())));
        verify(meetMapper).selectByStatusAndAuthor(1, "user1", 10, 0);
        verify(meetMapper).selectByStatusAndAuthorCount(1, "user1");
    }
    
    @Test
    void testGetPersonalMeeting_EmptyResult() {
        when(meetMapper.selectByStatusAndAuthor(eq(0), eq("user1"), eq(10), eq(0)))
            .thenReturn(Arrays.asList());
        when(meetMapper.selectByStatusAndAuthorCount(eq(0), eq("user1")))
            .thenReturn(0L);
        PageResult<Meeting> result = meetingService.getPersonalMeeting(0, "user1", 1, 10);
        assertNotNull(result);
        assertEquals(0, result.getList().size());
        assertEquals(0L, result.getTotal());
        verify(meetMapper).selectByStatusAndAuthor(0, "user1", 10, 0);
        verify(meetMapper).selectByStatusAndAuthorCount(0, "user1");
    }
    
    @Test
    void testGetSubAgendaByTime_Success() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "Agenda Item 1", "Speaker 1", LocalDateTime.now(), 30, "First agenda item"),
            new Agenda(2L, 1L, "Agenda Item 2", "Speaker 2", LocalDateTime.now().plusMinutes(30), 45, "Second agenda item")
        );
        when(agendaMapper.findBystartTime(eq(1L), eq(10), eq(0)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(2L);
        PageResult<Agenda> result = meetingService.getSubAgendaByTime(1L, 1, 10);
        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertEquals(2L, result.getTotal());
        verify(agendaMapper).findBystartTime(1L, 10, 0);
        verify(agendaMapper).countByMeetingsId(1L);
    }

    @Test
    void testGetSubAgendaByDuration_Success() {
        List<Agenda> agendaList = Arrays.asList(
                new Agenda(1L, 1L, "Short Agenda", "Speaker 1", LocalDateTime.now(), 15, "Short duration item"),
                new Agenda(2L, 1L, "Long Agenda", "Speaker 2", LocalDateTime.now().plusMinutes(15), 60, "Long duration item")
        );
        when(agendaMapper.findByDuration(eq(1L), eq(10), eq(0)))
                .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
                .thenReturn(2L);

        PageResult<Agenda> result = meetingService.getSubAgendaByDuration(1L, 1, 10);

        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertEquals("Short Agenda", result.getList().get(0).getTitle());
        assertEquals("Long Agenda", result.getList().get(1).getTitle());
        assertEquals(2L, result.getTotal());

        verify(agendaMapper).findByDuration(1L, 10, 0);
        verify(agendaMapper).countByMeetingsId(1L);
    }
    
    @Test
    void testGetAgenda_Success() {

        Agenda agenda = new Agenda(1L, 1L, "Test Agenda", "Test Speaker", LocalDateTime.now(), 30, "Test agenda content");

        when(agendaMapper.findById(1L)).thenReturn(agenda);

        Agenda result = meetingService.getAgenda(1L);

        assertNotNull(result);
        assertEquals("Test Agenda", result.getTitle());
        assertEquals("Test agenda content", result.getContent());
        assertEquals(30, result.getDuration());
        assertEquals("Test Speaker", result.getSpeaker());

        verify(agendaMapper).findById(1L);
    }
    
    @Test
    void testGetAgenda_NotFound() {
        when(agendaMapper.findById(999L)).thenReturn(null);

        Agenda result = meetingService.getAgenda(999L);

        assertNull(result);

        verify(agendaMapper).findById(999L);
    }

    // 新增测试用例
    @Test
    void testGetPassedMeetingsByStartTime_WithNullFilters() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1),
            new Meeting(2L, "会议2", "cover2.jpg", "地址2", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容2", "创建者2", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(2L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithPagination() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(3L, "会议3", "cover3.jpg", "地址3", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容3", "创建者3", 1),
            new Meeting(4L, "会议4", "cover4.jpg", "地址4", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容4", "创建者4", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(10), eq(5)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(4L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, 3, 5);

        assertNotNull(result);
        assertEquals(3, result.getPageNum());
        assertEquals(5, result.getPageSize());
        assertEquals(4L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithZeroPage() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(-10), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithNegativePage() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(-20), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, -1, 10);

        assertNotNull(result);
        assertEquals(-1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithZeroSize() {
        List<Meeting> emptyList = Arrays.asList();

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(0)))
            .thenReturn(emptyList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(0L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, 1, 0);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(0, result.getPageSize());
        assertEquals(0L, result.getTotal());
        assertEquals(0, result.getList().size());
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithTimeRange() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(7);
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "时间范围会议1", "cover1.jpg", "地址1", startTime.plusDays(1), startTime.plusDays(1).plusHours(2), "内容1", "创建者1", 1),
            new Meeting(2L, "时间范围会议2", "cover2.jpg", "地址2", startTime.plusDays(2), startTime.plusDays(2).plusHours(2), "内容2", "创建者2", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), eq(startTime), eq(endTime), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), eq(startTime), eq(endTime)))
            .thenReturn(2L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, startTime, endTime, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithAllFilters() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(7);
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "过滤会议1", "cover1.jpg", "地址1", startTime.plusDays(1), startTime.plusDays(1).plusHours(2), "过滤内容1", "过滤创建者1", 1)
        );

        when(meetMapper.findMeetingsByKeyword(eq(1L), eq("过滤"), eq("内容"), eq("创建者"), eq(startTime), eq(endTime), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(eq(1L), eq("过滤"), eq("内容"), eq("创建者"), eq(startTime), eq(endTime)))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(1L, "过滤", "内容", "创建者", startTime, endTime, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals("过滤会议1", result.getList().get(0).getTitle());
    }

    @Test
    void testGetNotPassedMeeting_WithNullFilters() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "待审核会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 0),
            new Meeting(2L, "待审核会议2", "cover2.jpg", "地址2", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容2", "创建者2", 0)
        );

        when(meetMapper.findMeetingsNotPassed(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(2L);

        PageResult<Meeting> result = meetingService.getNotPassedMeeting(null, null, null, null, null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
        assertTrue(result.getList().stream().allMatch(m -> m.getStatus() == 0));
    }

    @Test
    void testGetNotPassedMeeting_WithPagination() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(3L, "待审核会议3", "cover3.jpg", "地址3", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容3", "创建者3", 0),
            new Meeting(4L, "待审核会议4", "cover4.jpg", "地址4", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容4", "创建者4", 0)
        );

        when(meetMapper.findMeetingsNotPassed(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(10), eq(5)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(4L);

        PageResult<Meeting> result = meetingService.getNotPassedMeeting(null, null, null, null, null, null, 3, 5);

        assertNotNull(result);
        assertEquals(3, result.getPageNum());
        assertEquals(5, result.getPageSize());
        assertEquals(4L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetNotPassedMeeting_WithZeroPage() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "待审核会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 0)
        );

        when(meetMapper.findMeetingsNotPassed(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(-10), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getNotPassedMeeting(null, null, null, null, null, null, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetNotPassedMeeting_WithAllFilters() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(7);
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "过滤待审核会议1", "cover1.jpg", "地址1", startTime.plusDays(1), startTime.plusDays(1).plusHours(2), "过滤内容1", "过滤创建者1", 0)
        );

        when(meetMapper.findMeetingsNotPassed(eq(1L), eq("过滤"), eq("内容"), eq("创建者"), eq(startTime), eq(endTime), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(eq(1L), eq("过滤"), eq("内容"), eq("创建者"), eq(startTime), eq(endTime)))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getNotPassedMeeting(1L, "过滤", "内容", "创建者", startTime, endTime, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals("过滤待审核会议1", result.getList().get(0).getTitle());
        assertEquals(0, result.getList().get(0).getStatus());
    }

    @Test
    void testGetPersonalMeeting_WithNullFilters() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "个人会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "user1", 1),
            new Meeting(2L, "个人会议2", "cover2.jpg", "地址2", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容2", "user1", 0)
        );

        when(meetMapper.selectByStatusAndAuthor(isNull(), isNull(), eq(10), eq(0)))
            .thenReturn(meetingList);
        when(meetMapper.selectByStatusAndAuthorCount(isNull(), isNull()))
            .thenReturn(2L);

        PageResult<Meeting> result = meetingService.getPersonalMeeting(null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetPersonalMeeting_WithPagination() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(3L, "个人会议3", "cover3.jpg", "地址3", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容3", "user1", 1),
            new Meeting(4L, "个人会议4", "cover4.jpg", "地址4", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容4", "user1", 0)
        );

        when(meetMapper.selectByStatusAndAuthor(eq(1), eq("user1"), eq(5), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.selectByStatusAndAuthorCount(eq(1), eq("user1")))
            .thenReturn(4L);

        PageResult<Meeting> result = meetingService.getPersonalMeeting(1, "user1", 3, 5);

        assertNotNull(result);
        assertEquals(3, result.getPageNum());
        assertEquals(5, result.getPageSize());
        assertEquals(4L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetPersonalMeeting_WithZeroPage() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "个人会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "user1", 1)
        );

        when(meetMapper.selectByStatusAndAuthor(eq(1), eq("user1"), eq(10), eq(-10)))
            .thenReturn(meetingList);
        when(meetMapper.selectByStatusAndAuthorCount(eq(1), eq("user1")))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getPersonalMeeting(1, "user1", 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetPersonalMeeting_WithDifferentStatuses() {
        // 测试状态为0（待审核）的会议
        List<Meeting> pendingMeetings = Arrays.asList(
            new Meeting(1L, "待审核个人会议", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "user1", 0)
        );

        when(meetMapper.selectByStatusAndAuthor(eq(0), eq("user1"), eq(10), eq(0)))
            .thenReturn(pendingMeetings);
        when(meetMapper.selectByStatusAndAuthorCount(eq(0), eq("user1")))
            .thenReturn(1L);

        PageResult<Meeting> result = meetingService.getPersonalMeeting(0, "user1", 1, 10);
        assertEquals(0, result.getList().get(0).getStatus());

        // 测试状态为1（已通过）的会议
        List<Meeting> approvedMeetings = Arrays.asList(
            new Meeting(2L, "已通过个人会议", "cover2.jpg", "地址2", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容2", "user1", 1)
        );

        when(meetMapper.selectByStatusAndAuthor(eq(1), eq("user1"), eq(10), eq(0)))
            .thenReturn(approvedMeetings);
        when(meetMapper.selectByStatusAndAuthorCount(eq(1), eq("user1")))
            .thenReturn(1L);

        result = meetingService.getPersonalMeeting(1, "user1", 1, 10);
        assertEquals(1, result.getList().get(0).getStatus());

        // 测试状态为2（未通过）的会议
        List<Meeting> rejectedMeetings = Arrays.asList(
            new Meeting(3L, "未通过个人会议", "cover3.jpg", "地址3", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4), "内容3", "user1", 2)
        );

        when(meetMapper.selectByStatusAndAuthor(eq(2), eq("user1"), eq(10), eq(0)))
            .thenReturn(rejectedMeetings);
        when(meetMapper.selectByStatusAndAuthorCount(eq(2), eq("user1")))
            .thenReturn(1L);

        result = meetingService.getPersonalMeeting(2, "user1", 1, 10);
        assertEquals(2, result.getList().get(0).getStatus());
    }

    @Test
    void testGetSubAgendaByTime_WithPagination() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(3L, 1L, "议程3", "演讲者3", LocalDateTime.now().plusHours(2), 60, "内容3"),
            new Agenda(4L, 1L, "议程4", "演讲者4", LocalDateTime.now().plusHours(3), 90, "内容4")
        );

        when(agendaMapper.findBystartTime(eq(1L), eq(5), eq(10)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(4L);

        PageResult<Agenda> result = meetingService.getSubAgendaByTime(1L, 3, 5);

        assertNotNull(result);
        assertEquals(3, result.getPageNum());
        assertEquals(5, result.getPageSize());
        assertEquals(4L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetSubAgendaByTime_WithZeroPage() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 60, "内容1")
        );

        when(agendaMapper.findBystartTime(eq(1L), eq(10), eq(-10)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(1L);

        PageResult<Agenda> result = meetingService.getSubAgendaByTime(1L, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetSubAgendaByTime_WithNonExistentMeeting() {
        List<Agenda> emptyList = Arrays.asList();

        when(agendaMapper.findBystartTime(eq(999L), eq(10), eq(0)))
            .thenReturn(emptyList);
        when(agendaMapper.countByMeetingsId(eq(999L)))
            .thenReturn(0L);

        PageResult<Agenda> result = meetingService.getSubAgendaByTime(999L, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(0L, result.getTotal());
        assertEquals(0, result.getList().size());
    }

    @Test
    void testGetSubAgendaByDuration_WithPagination() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(3L, 1L, "短议程3", "演讲者3", LocalDateTime.now().plusHours(2), 30, "内容3"),
            new Agenda(4L, 1L, "长议程4", "演讲者4", LocalDateTime.now().plusHours(3), 120, "内容4")
        );

        when(agendaMapper.findByDuration(eq(1L), eq(5), eq(10)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(4L);

        PageResult<Agenda> result = meetingService.getSubAgendaByDuration(1L, 3, 5);

        assertNotNull(result);
        assertEquals(3, result.getPageNum());
        assertEquals(5, result.getPageSize());
        assertEquals(4L, result.getTotal());
        assertEquals(2, result.getList().size());
    }

    @Test
    void testGetSubAgendaByDuration_WithZeroPage() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 60, "内容1")
        );

        when(agendaMapper.findByDuration(eq(1L), eq(10), eq(-10)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(1L);

        PageResult<Agenda> result = meetingService.getSubAgendaByDuration(1L, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetSubAgendaByDuration_WithNonExistentMeeting() {
        List<Agenda> emptyList = Arrays.asList();

        when(agendaMapper.findByDuration(eq(999L), eq(10), eq(0)))
            .thenReturn(emptyList);
        when(agendaMapper.countByMeetingsId(eq(999L)))
            .thenReturn(0L);

        PageResult<Agenda> result = meetingService.getSubAgendaByDuration(999L, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(0L, result.getTotal());
        assertEquals(0, result.getList().size());
    }

    @Test
    void testGetAgenda_WithCompleteData() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(2);
        Agenda agenda = new Agenda(1L, 1L, "完整议程详情", "演讲者姓名", startTime, 120, "详细的议程内容描述");

        when(agendaMapper.findById(1L)).thenReturn(agenda);

        Agenda result = meetingService.getAgenda(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getMeeting_id());
        assertEquals("完整议程详情", result.getTitle());
        assertEquals("演讲者姓名", result.getSpeaker());
        assertEquals(startTime, result.getStartTime());
        assertEquals(120, result.getDuration());
        assertEquals("详细的议程内容描述", result.getContent());
    }

    @Test
    void testGetAgenda_WithZeroId() {
        when(agendaMapper.findById(0L)).thenReturn(null);

        Agenda result = meetingService.getAgenda(0L);

        assertNull(result);
        verify(agendaMapper).findById(0L);
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithSorting() {
        LocalDateTime now = LocalDateTime.now();
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "会议1", "cover1.jpg", "地址1", now.plusHours(1), now.plusHours(3), "内容1", "创建者1", 1),
            new Meeting(2L, "会议2", "cover2.jpg", "地址2", now, now.plusHours(2), "内容2", "创建者2", 1),
            new Meeting(3L, "会议3", "cover3.jpg", "地址3", now.plusHours(2), now.plusHours(4), "内容3", "创建者3", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(3L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, 1, 10);

        assertNotNull(result);
        assertEquals(3, result.getList().size());
        
        // 验证按开始时间降序排序（最新的在前）
        assertTrue(result.getList().get(0).getStartTime().isAfter(result.getList().get(1).getStartTime()));
        assertTrue(result.getList().get(1).getStartTime().isAfter(result.getList().get(2).getStartTime()));
    }

    @Test
    void testGetNotPassedMeeting_WithSorting() {
        LocalDateTime now = LocalDateTime.now();
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "待审核会议1", "cover1.jpg", "地址1", now.plusHours(1), now.plusHours(3), "内容1", "创建者1", 0),
            new Meeting(2L, "待审核会议2", "cover2.jpg", "地址2", now, now.plusHours(2), "内容2", "创建者2", 0),
            new Meeting(3L, "待审核会议3", "cover3.jpg", "地址3", now.plusHours(2), now.plusHours(4), "内容3", "创建者3", 0)
        );

        when(meetMapper.findMeetingsNotPassed(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(10)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsNotPassedCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(3L);

        PageResult<Meeting> result = meetingService.getNotPassedMeeting(null, null, null, null, null, null, 1, 10);

        assertNotNull(result);
        assertEquals(3, result.getList().size());
        
        // 验证按开始时间降序排序（最新的在前）
        assertTrue(result.getList().get(0).getStartTime().isAfter(result.getList().get(1).getStartTime()));
        assertTrue(result.getList().get(1).getStartTime().isAfter(result.getList().get(2).getStartTime()));
    }

    @Test
    void testGetPassedMeetingsByStartTime_WithLargePageSize() {
        List<Meeting> meetingList = Arrays.asList(
            new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1),
            new Meeting(2L, "会议2", "cover2.jpg", "地址2", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "内容2", "创建者2", 1),
            new Meeting(3L, "会议3", "cover3.jpg", "地址3", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4), "内容3", "创建者3", 1),
            new Meeting(4L, "会议4", "cover4.jpg", "地址4", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), "内容4", "创建者4", 1),
            new Meeting(5L, "会议5", "cover5.jpg", "地址5", LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(6), "内容5", "创建者5", 1)
        );

        when(meetMapper.findMeetingsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(100)))
            .thenReturn(meetingList);
        when(meetMapper.findMeetingsByKeywordCount(isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(5L);

        PageResult<Meeting> result = meetingService.getPassedMeetingsByStartTime(null, null, null, null, null, null, 1, 100);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(100, result.getPageSize());
        assertEquals(5L, result.getTotal());
        assertEquals(5, result.getList().size());
    }

    @Test
    void testGetSubAgendaByTime_WithLargePageSize() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 30, "内容1"),
            new Agenda(2L, 1L, "议程2", "演讲者2", LocalDateTime.now().plusMinutes(30), 45, "内容2"),
            new Agenda(3L, 1L, "议程3", "演讲者3", LocalDateTime.now().plusMinutes(75), 60, "内容3"),
            new Agenda(4L, 1L, "议程4", "演讲者4", LocalDateTime.now().plusMinutes(135), 90, "内容4"),
            new Agenda(5L, 1L, "议程5", "演讲者5", LocalDateTime.now().plusMinutes(225), 120, "内容5")
        );

        when(agendaMapper.findBystartTime(eq(1L), eq(100), eq(0)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(5L);

        PageResult<Agenda> result = meetingService.getSubAgendaByTime(1L, 1, 100);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(100, result.getPageSize());
        assertEquals(5L, result.getTotal());
        assertEquals(5, result.getList().size());
    }

    @Test
    void testGetSubAgendaByDuration_WithLargePageSize() {
        List<Agenda> agendaList = Arrays.asList(
            new Agenda(1L, 1L, "短议程1", "演讲者1", LocalDateTime.now(), 15, "内容1"),
            new Agenda(2L, 1L, "短议程2", "演讲者2", LocalDateTime.now().plusMinutes(15), 30, "内容2"),
            new Agenda(3L, 1L, "长议程3", "演讲者3", LocalDateTime.now().plusMinutes(45), 90, "内容3"),
            new Agenda(4L, 1L, "长议程4", "演讲者4", LocalDateTime.now().plusMinutes(135), 120, "内容4"),
            new Agenda(5L, 1L, "超长议程5", "演讲者5", LocalDateTime.now().plusMinutes(255), 180, "内容5")
        );

        when(agendaMapper.findByDuration(eq(1L), eq(100), eq(0)))
            .thenReturn(agendaList);
        when(agendaMapper.countByMeetingsId(eq(1L)))
            .thenReturn(5L);

        PageResult<Agenda> result = meetingService.getSubAgendaByDuration(1L, 1, 100);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(100, result.getPageSize());
        assertEquals(5L, result.getTotal());
        assertEquals(5, result.getList().size());
    }
} 