package org.example.project2.Kingbase;

import org.example.project2.Controller.CourseController;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.courseMapper;
import org.example.project2.Service.CourseService;
import org.example.project2.entity.Course;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.User_d;
import org.example.project2.entity.courseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CourseControllerTest {
    
    private CourseController controller;
    private courseMapper courseMapper;
    private CourseService courseService;
    private UserMapper userMapper;
    private ChatModel chatModel;

    @BeforeEach
    void setUp() {
        courseMapper = mock(courseMapper.class);
        courseService = mock(CourseService.class);
        userMapper = mock(UserMapper.class);
        chatModel = mock(ChatModel.class);

        controller = new CourseController();
        controller.courseMapper = courseMapper;
        controller.courseService = courseService;
        controller.userMapper = userMapper;

        // chatModel是private需要反射注入或设置为public进行测试
        try {
            var field = CourseController.class.getDeclaredField("chatModel");
            field.setAccessible(true);
            field.set(controller, chatModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetCourses() {
        when(courseService.getCourses(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new PageResult<>(1, 10, 0L, Collections.emptyList()));

        ResponseEntity<PageResult<Course>> response = controller.getCourses(null, null, null, null, null, null, 1, 10);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetCoursesReady() {
        when(courseService.getCoursesReady(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new PageResult<>(1, 10, 0L, Collections.emptyList()));

        ResponseEntity<PageResult<Course>> response = controller.getCoursesReady(null, null, null, null, null, null, 1, 10);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetCoursesPersonal() {
        when(courseService.getCourseByAuthor(any(), any(), anyInt(), anyInt()))
                .thenReturn(new PageResult<>(1, 10, 0L, Collections.emptyList()));

        ResponseEntity<PageResult<Course>> response = controller.getCoursesPersonal(1, "user", 1, 10);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetCourse() {
        Course course = new Course(
                1L, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                10, 1, null
        );
        course.setId(1L);
        when(courseService.getCourseById(1L)).thenReturn(course);

        ResponseEntity<Course> response = controller.getCourse(1L);
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testSubmit() {
        Course course = new Course(
                null, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                0, 0, null
        );
        when(courseService.addCourse(any())).thenReturn(1);
        ResponseEntity<Integer> response = controller.submit(course);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testSubmit_Failure() {
        Course course = new Course(
                null, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                0, 0, null
        );
        when(courseService.addCourse(any())).thenReturn(0);
        ResponseEntity<Integer> response = controller.submit(course);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testCommit() {
        Course course = new Course(
                null, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                0, 0, null
        );
        when(courseService.addCourse(any())).thenReturn(1);
        ResponseEntity<Integer> response = controller.commit(course);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCommit_Failure() {
        Course course = new Course(
                null, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                0, 0, null
        );
        when(courseService.addCourse(any())).thenReturn(0);
        ResponseEntity<Integer> response = controller.commit(course);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testPass() {
        when(courseMapper.updateStatus(anyLong())).thenReturn(1);
        ResponseEntity<Integer> response = controller.pass(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testPass_Failure() {
        when(courseMapper.updateStatus(anyLong())).thenReturn(0);
        ResponseEntity<Integer> response = controller.pass(1L);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_Success() {
        Course update = new Course(
                1L, "New Title", null, null,
                null, null, null, null,
                null, null, null
        );
        update.setId(1L);
        update.setTitle("New");

        Course original = new Course(
                1L, "Old Title", null, null,
                null, null, null, null,
                null, null, null
        );
        original.setId(1L);
        original.setTitle("Old");

        User_d user = new User_d("zhang", "password", "phone", "email", "male", 0, null, "avatar", 1L);

        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("zhang");
        when(userMapper.findByUsername("zhang")).thenReturn(user);
        when(courseService.updateCourse(any())).thenReturn(1);

        ResponseEntity<Integer> response = controller.update(update, "zhang");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_CourseNotFound() {
        Course update = new Course(1L, "New Title", null, null, null, null, null, null, null, null, null);
        
        when(courseService.getCourseById(1L)).thenReturn(null);

        ResponseEntity<Integer> response = controller.update(update, "user");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(-1, response.getBody());
    }

    @Test
    void testUpdate_PermissionDenied() {
        Course update = new Course(1L, "New Title", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        User_d user = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);

        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("user2");
        when(userMapper.findByUsername("user1")).thenReturn(user);

        ResponseEntity<Integer> response = controller.update(update, "user1");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(-1, response.getBody());
    }

    @Test
    void testUpdate_PartialUpdate() {
        Course update = new Course(1L, "New Title", null, null, null, null, "New Summary", null, null, null, null);
        Course original = new Course(1L, "Old Title", "Old Video", null, null, null, "Old Summary", null, null, null, null);
        User_d user = new User_d("zhang", "password", "phone", "email", "male", 0, null, "avatar", 1L);

        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("zhang");
        when(userMapper.findByUsername("zhang")).thenReturn(user);
        when(courseService.updateCourse(any())).thenReturn(1);

        ResponseEntity<Integer> response = controller.update(update, "zhang");
        assertEquals(200, response.getStatusCodeValue());

        // 验证updateCourse被调用，并且传入的参数不为null
        verify(courseService).updateCourse(any(Course.class));
    }
    
    @Test
    void testUpdate_PartialUpdateWithCaptor() {
        Course update = new Course(1L, "New Title", null, null, null, null, "New Summary", null, null, null, null);
        Course original = new Course(1L, "Old Title", "Old Video", null, null, null, "Old Summary", null, null, null, null);
        User_d user = new User_d("zhang", "password", "phone", "email", "male", 0, null, "avatar", 1L);

        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("zhang");
        when(userMapper.findByUsername("zhang")).thenReturn(user);
        when(courseService.updateCourse(any())).thenReturn(1);

        ResponseEntity<Integer> response = controller.update(update, "zhang");
        assertEquals(200, response.getStatusCodeValue());

        // 使用ArgumentCaptor捕获传入的参数
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).updateCourse(courseCaptor.capture());
        
        Course capturedCourse = courseCaptor.getValue();
        assertNotNull(capturedCourse);
        assertEquals("New Title", capturedCourse.getTitle());
        assertEquals("New Summary", capturedCourse.getSummary());
        assertEquals("Old Video", capturedCourse.getVideoUrl()); // 应该保持原值
    }

    @Test
    void testUpdateLikes() {
        when(courseMapper.updateLikes(1L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.updateLikes(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRefuse() {
        when(courseService.deleteCourse(1L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.refuse(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRefuse_Failure() {
        when(courseService.deleteCourse(1L)).thenReturn(0);
        ResponseEntity<Integer> response = controller.refuse(1L);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testRollback() {
        when(courseMapper.refuseStatus(1L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.rollback(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRollback_Failure() {
        when(courseMapper.refuseStatus(1L)).thenReturn(0);
        ResponseEntity<Integer> response = controller.rollback(1L);
        assertEquals(500, response.getStatusCodeValue());
    }


    @Test
    void testAsk() {
        ChatResponse mockResponse = mock(ChatResponse.class);
        Generation mockGeneration = mock(Generation.class);
        AssistantMessage mockAssistantMessage = mock(AssistantMessage.class);

        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);
        when(mockResponse.getResult()).thenReturn(mockGeneration);
        when(mockGeneration.getOutput()).thenReturn(mockAssistantMessage);
        when(mockAssistantMessage.getText()).thenReturn("Test Answer");

        String result = controller.ask("Test Question");
        assertEquals("Test Answer", result);
    }

    @Test
    void testSubmit_VerifyStatusAndLikesSet() {
        Course course = new Course(
                null, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                10, 5, null
        );
        when(courseService.addCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.submit(course);
        
        assertEquals(200, response.getStatusCodeValue());
        verify(courseService).addCourse(argThat(c -> c.getStatus() == 0 && c.getLikes() == 0));
    }

    @Test
    void testCommit_VerifyStatusAndLikesSet() {
        Course course = new Course(
                null, "Title", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                10, 5, null
        );
        when(courseService.addCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.commit(course);
        
        assertEquals(200, response.getStatusCodeValue());
        verify(courseService).addCourse(argThat(c -> c.getStatus() == 1 && c.getLikes() == 0));
    }

    @Test
    void testGetCourses_WithFilters() {
        List<Course> courseList = Arrays.asList(
            new Course(1L, "Java Course", "video1", "cover1", 60, "author1", "summary1", "content1", 10, 1, null),
            new Course(2L, "Python Course", "video2", "cover2", 90, "author2", "summary2", "content2", 20, 1, null)
        );
        PageResult<Course> pageResult = new PageResult<>(1, 10, 2L, courseList);
        
        when(courseService.getCourses(eq("Java"), eq(60), eq("author1"), eq("summary1"), eq("content1"), eq("Programming"), eq(1), eq(10)))
            .thenReturn(pageResult);

        ResponseEntity<PageResult<Course>> response = controller.getCourses("Java", 60, "author1", "summary1", "content1", "Programming", 1, 10);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getList().size());
        assertEquals(2L, response.getBody().getTotal());
    }

    @Test
    void testGetCoursesPersonal_WithStatus() {
        List<Course> courseList = Arrays.asList(
            new Course(1L, "My Course", "video1", "cover1", 60, "user", "summary1", "content1", 10, 0, null)
        );
        PageResult<Course> pageResult = new PageResult<>(1, 10, 1L, courseList);
        
        when(courseService.getCourseByAuthor(eq(0), eq("user"), eq(1), eq(10)))
            .thenReturn(pageResult);

        ResponseEntity<PageResult<Course>> response = controller.getCoursesPersonal(0, "user", 1, 10);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getList().size());
        assertEquals(0, response.getBody().getList().get(0).getStatus());
    }
}
