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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class meetingControllerTest {
    private meetingController meetingController;
    @Mock private ChatModel chatModel;
    @Mock private agendaMapper agendaMapper;
    @Mock private meetMapper meetMapper;
    @Mock private meetingService meetService;
    @Mock private UserMapper userMapper;
    @Mock private AIService aiService;

    @BeforeEach
    void setUp() throws Exception {
        chatModel = mock(ChatModel.class);
        agendaMapper = mock(agendaMapper.class);
        meetMapper = mock(meetMapper.class);
        meetService = mock(meetingService.class);
        userMapper = mock(UserMapper.class);
        aiService = mock(AIService.class);
        meetingController = new meetingController();
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
    }

    // 1. submit
    @Test
    void testSubmit_AddAgenda() {
        Agenda agenda = new Agenda(1L, 1L, "议程标题", "演讲者", LocalDateTime.now(), 60, "议程内容");
        when(agendaMapper.insert(any(Agenda.class))).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.submit(agenda);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    // 2. getAgenda (by time)
    @Test
    void testGetAgenda_ByTime() {
        List<Agenda> agendaList = Collections.singletonList(new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 60, "内容1"));
        PageResult<Agenda> pageResult = new PageResult<>(1, 10, 1L, agendaList);
        when(meetService.getSubAgendaByTime(1L, 1, 10)).thenReturn(pageResult);
        ResponseEntity<PageResult<Agenda>> response = meetingController.getAgenda(1L, 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getList().size());
    }

    // 3. getAgendaByDuration
    @Test
    void testGetAgendaByDuration() {
        List<Agenda> agendaList = Collections.singletonList(new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 60, "内容1"));
        PageResult<Agenda> pageResult = new PageResult<>(1, 10, 1L, agendaList);
        when(meetService.getSubAgendaByDuration(1L, 1, 10)).thenReturn(pageResult);
        ResponseEntity<PageResult<Agenda>> response = meetingController.getAgendaByDuration(1L, 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getList().size());
    }

    // 4. getAgenda (by id)
    @Test
    void testGetAgenda_ById() {
        Agenda agenda = new Agenda(1L, 1L, "议程1", "演讲者1", LocalDateTime.now(), 60, "内容1");
        when(meetService.getAgenda(1L)).thenReturn(agenda);
        ResponseEntity<Agenda> response = meetingController.getAgenda(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(agenda, response.getBody());
    }

    // 5. update (Agenda)
    @Test
    void testUpdateAgenda_Success() {
        Agenda old = new Agenda(1L, 1L, "old", "old", LocalDateTime.now(), 10, "old");
        Agenda update = new Agenda(1L, 1L, "new", "new", LocalDateTime.now(), 20, "new");
        when(agendaMapper.findById(1L)).thenReturn(old);
        when(agendaMapper.updateAgenda(eq(1L), any(Agenda.class))).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.update(update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    @Test
    void testUpdateAgenda_NotFound() {
        Agenda update = new Agenda(2L, 1L, "new", "new", LocalDateTime.now(), 20, "new");
        when(agendaMapper.findById(2L)).thenReturn(null);
        ResponseEntity<Integer> response = meetingController.update(update);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }

    // 6. delete (Agenda)
    @Test
    void testDeleteAgenda_Success() {
        when(agendaMapper.deleteById(1L)).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.delete(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    @Test
    void testDeleteAgenda_Fail() {
        when(agendaMapper.deleteById(2L)).thenReturn(0);
        ResponseEntity<Integer> response = meetingController.delete(2L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    // 7. getPassedMeetings
    @Test
    void testGetPassedMeetings() {
        List<Meeting> meetingList = Arrays.asList(new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1));
        PageResult<Meeting> pageResult = new PageResult<>(1, 10, 1L, meetingList);
        when(meetService.getPassedMeetingsByStartTime(any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(pageResult);
        ResponseEntity<PageResult<Meeting>> response = meetingController.getPassedMeetings(1L, "会议", "内容", "创建者", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getList().size());
    }
    @Test
    void testGetPassedMeetings_InvalidTimeRange() {
        ResponseEntity<PageResult<Meeting>> response = meetingController.getPassedMeetings(null, null, null, null, LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1, 10);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // 8. getNotPassedMeetings
    @Test
    void testGetNotPassedMeetings() {
        List<Meeting> meetingList = Arrays.asList(new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 0));
        PageResult<Meeting> pageResult = new PageResult<>(1, 10, 1L, meetingList);
        when(meetService.getNotPassedMeeting(any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(pageResult);
        ResponseEntity<PageResult<Meeting>> response = meetingController.getNotPassedMeetings(1L, "会议", "内容", "创建者", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getList().size());
    }
    @Test
    void testGetNotPassedMeetings_InvalidTimeRange() {
        ResponseEntity<PageResult<Meeting>> response = meetingController.getNotPassedMeetings(null, null, null, null, LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1, 10);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // 9. getPersonalMeetings
    @Test
    void testGetPersonalMeetings() {
        List<Meeting> meetingList = Arrays.asList(new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1));
        PageResult<Meeting> pageResult = new PageResult<>(1, 10, 1L, meetingList);
        when(meetService.getPersonalMeeting(any(), any(), anyInt(), anyInt())).thenReturn(pageResult);
        ResponseEntity<PageResult<Meeting>> response = meetingController.getPersonalMeetings(1, "创建者1", 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getList().size());
    }

    // 10. getMeeting
    @Test
    void testGetMeeting() {
        Meeting meeting = new Meeting(1L, "会议1", "cover1.jpg", "地址1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "内容1", "创建者1", 1);
        when(meetMapper.findById(1L)).thenReturn(meeting);
        ResponseEntity<Meeting> response = meetingController.getMeeting(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meeting, response.getBody());
    }

    // 11. deleteMeeting
    @Test
    void testDeleteMeeting_Success_Admin() {
        User_d user = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L);
        when(meetMapper.getCreator(1L)).thenReturn("user1");
        when(userMapper.findByUsername("admin")).thenReturn(user);
        when(meetMapper.deleteById(1L)).thenReturn(1);
        when(agendaMapper.deleteByMeetingId(1L)).thenReturn(2);
        ResponseEntity<Integer> response = meetingController.deleteMeeting(1L, "admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    @Test
    void testDeleteMeeting_PermissionDenied() {
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        when(meetMapper.getCreator(1L)).thenReturn("user2");
        when(userMapper.findByUsername("user1")).thenReturn(user);
        ResponseEntity<Integer> response = meetingController.deleteMeeting(1L, "user1");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }

    // 12. pass
    @Test
    void testPassMeeting() {
        when(meetMapper.updateStatus(1L)).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.pass(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    // 13. refuse
    @Test
    void testRefuseMeeting() {
        when(meetMapper.refuseStatus(1L)).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.refuse(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    // 14. update (Meeting)
    @Test
    void testUpdateMeeting_Success() {
        Meeting old = new Meeting(1L, "old", "old", "old", LocalDateTime.now(), LocalDateTime.now(), "old", "user1", 0);
        Meeting update = new Meeting(1L, "new", "new", "new", LocalDateTime.now(), LocalDateTime.now(), "new", "user1", 0);
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        when(meetMapper.findById(1L)).thenReturn(old);
        when(userMapper.findByUsername("user1")).thenReturn(user);
        when(meetMapper.modify(any(Meeting.class))).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.update(update, "user1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    @Test
    void testUpdateMeeting_NotFound() {
        Meeting update = new Meeting(2L, "new", "new", "new", LocalDateTime.now(), LocalDateTime.now(), "new", "user1", 0);
        when(meetMapper.findById(2L)).thenReturn(null);
        ResponseEntity<Integer> response = meetingController.update(update, "user1");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }
    @Test
    void testUpdateMeeting_PermissionDenied() {
        Meeting old = new Meeting(1L, "old", "old", "old", LocalDateTime.now(), LocalDateTime.now(), "old", "user2", 0);
        Meeting update = new Meeting(1L, "new", "new", "new", LocalDateTime.now(), LocalDateTime.now(), "new", "user1", 0);
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        when(meetMapper.findById(1L)).thenReturn(old);
        when(userMapper.findByUsername("user1")).thenReturn(user);
        ResponseEntity<Integer> response = meetingController.update(update, "user1");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }

    // 15. addMeeting
    @Test
    void testAddMeeting() {
        Meeting meeting = new Meeting(1L, "会议标题", "cover.jpg", "会议地址", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "会议内容", "user1", 0);
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        when(userMapper.findByUsername("user1")).thenReturn(user);
        when(meetMapper.insert(any(Meeting.class))).thenReturn(1);
        ResponseEntity<Integer> response = meetingController.addMeeting(meeting, "user1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    // 16. ask
    @Test
    void testAsk_AISummary() {
        when(aiService.chat("测试问题")).thenReturn("AI回答");
        ResponseEntity<String> response = meetingController.ask("测试问题");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AI回答", response.getBody());
    }
} 