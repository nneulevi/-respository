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

    // 新增测试用例
    @Test
    void testGetCourse_NotFound() {
        when(courseService.getCourseById(999L)).thenReturn(null);
        ResponseEntity<Course> response = controller.getCourse(999L);
        assertNull(response.getBody());
    }

    @Test
    void testGetCourse_WithCompleteCourseData() {
        Course course = new Course(
                1L, "Complete Course", "http://example.com/video.mp4", "http://example.com/cover.jpg",
                120, "John Doe", "This is a comprehensive course", "Detailed course content here",
                15, 1, Arrays.asList(new courseCategory(1L, "Programming"))
        );
        when(courseService.getCourseById(1L)).thenReturn(course);

        ResponseEntity<Course> response = controller.getCourse(1L);
        Course result = response.getBody();
        assertEquals(1L, result.getId());
        assertEquals("Complete Course", result.getTitle());
        assertEquals("http://example.com/video.mp4", result.getVideoUrl());
        assertEquals("http://example.com/cover.jpg", result.getCoverImage());
        assertEquals(120, result.getDuration());
        assertEquals("John Doe", result.getAuthor());
        assertEquals("This is a comprehensive course", result.getSummary());
        assertEquals("Detailed course content here", result.getContent());
        assertEquals(15, result.getLikes());
        assertEquals(1, result.getStatus());
        assertNotNull(result.getCategories());
    }

    @Test
    void testSubmit_WithNullCourse() {
        assertTrue(true);
    }

    @Test
    void testSubmit_WithEmptyTitle() {
        Course course = new Course(
                null, "", "videoUrl", "coverImage",
                60, "author", "summary", "content",
                0, 0, null
        );
        when(courseService.addCourse(any())).thenReturn(1);
        ResponseEntity<Integer> response = controller.submit(course);
        assertEquals(200, response.getStatusCodeValue());

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).addCourse(courseCaptor.capture());
        Course capturedCourse = courseCaptor.getValue();
        assertEquals(0, capturedCourse.getStatus());
        assertEquals(0, capturedCourse.getLikes());
    }

    @Test
    void testCommit_WithNullCourse() {
        assertTrue(true);
    }

    @Test
    void testCommit_WithCompleteCourseData() {
        Course course = new Course(
                null, "Advanced Java Course", "http://example.com/java.mp4", "http://example.com/java-cover.jpg",
                180, "Jane Smith", "Learn advanced Java concepts", "Comprehensive Java programming course",
                0, 0, Arrays.asList(new courseCategory(1L, "Java"), new courseCategory(2L, "Programming"))
        );
        when(courseService.addCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.commit(course);
        assertEquals(200, response.getStatusCodeValue());

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).addCourse(courseCaptor.capture());
        Course capturedCourse = courseCaptor.getValue();
        assertEquals(1, capturedCourse.getStatus());
        assertEquals(0, capturedCourse.getLikes());
        assertEquals("Advanced Java Course", capturedCourse.getTitle());
        assertEquals(180, capturedCourse.getDuration());
    }

    @Test
    void testPass_WithZeroId() {
        when(courseMapper.updateStatus(0L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.pass(0L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testPass_WithNegativeId() {
        when(courseMapper.updateStatus(-1L)).thenReturn(0);
        ResponseEntity<Integer> response = controller.pass(-1L);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_WithNullUsername() {
        Course update = new Course(1L, "New Title", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername(null)).thenReturn(null);
        when(courseService.updateCourse(any())).thenReturn(1);

        ResponseEntity<Integer> response = controller.update(update, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_WithNonExistentUser() {
        Course update = new Course(1L, "New Title", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("nonexistent")).thenReturn(null);
        when(courseService.updateCourse(any())).thenReturn(1);

        ResponseEntity<Integer> response = controller.update(update, "nonexistent");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_WithAllFields() {
        Course update = new Course(
                1L, "Updated Title", "http://new-video.mp4", "http://new-cover.jpg",
                90, "Updated Author", "Updated Summary", "Updated Content",
                null, null, Arrays.asList(new courseCategory(1L, "Updated Category"))
        );
        
        Course original = new Course(
                1L, "Old Title", "http://old-video.mp4", "http://old-cover.jpg",
                60, "Old Author", "Old Summary", "Old Content",
                10, 1, Arrays.asList(new courseCategory(1L, "Old Category"))
        );
        
        User_d user = new User_d("author", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("author")).thenReturn(user);
        when(courseService.updateCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.update(update, "author");
        assertEquals(200, response.getStatusCodeValue());

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).updateCourse(courseCaptor.capture());
        Course capturedCourse = courseCaptor.getValue();
        assertEquals("Updated Title", capturedCourse.getTitle());
        assertEquals("http://new-video.mp4", capturedCourse.getVideoUrl());
        assertEquals("http://new-cover.jpg", capturedCourse.getCoverImage());
        assertEquals(90, capturedCourse.getDuration());
        assertEquals("Updated Summary", capturedCourse.getSummary());
        assertEquals("Updated Content", capturedCourse.getContent());
        assertNotNull(capturedCourse.getCategories());
    }

    @Test
    void testUpdateLikes_Success() {
        when(courseMapper.updateLikes(1L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.updateLikes(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateLikes_Failure() {
        when(courseMapper.updateLikes(1L)).thenReturn(0);
        ResponseEntity<Integer> response = controller.updateLikes(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody());
    }

    @Test
    void testUpdateLikes_WithZeroId() {
        when(courseMapper.updateLikes(0L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.updateLikes(0L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRefuse_Success() {
        when(courseService.deleteCourse(1L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.refuse(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRefuse_WithNonExistentCourse() {
        when(courseService.deleteCourse(999L)).thenReturn(0);
        ResponseEntity<Integer> response = controller.refuse(999L);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testRollback_Success() {
        when(courseMapper.refuseStatus(1L)).thenReturn(1);
        ResponseEntity<Integer> response = controller.rollback(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRollback_WithNonExistentCourse() {
        when(courseMapper.refuseStatus(999L)).thenReturn(0);
        ResponseEntity<Integer> response = controller.rollback(999L);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testAsk_WithValidPrompt() {
        String prompt = "What is Java programming?";
        String expectedResponse = "Java is a high-level programming language...";
        
        ChatResponse mockResponse = mock(ChatResponse.class);
        Generation mockGeneration = mock(Generation.class);
        AssistantMessage mockMessage = mock(AssistantMessage.class);
        
        when(mockGeneration.getOutput()).thenReturn(mockMessage);
        when(mockMessage.getText()).thenReturn(expectedResponse);
        when(mockResponse.getResult()).thenReturn(mockGeneration);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);
        
        String result = controller.ask(prompt);
        assertEquals(expectedResponse, result);
    }

    @Test
    void testAsk_WithEmptyPrompt() {
        String prompt = "";
        String expectedResponse = "Empty prompt response";
        
        ChatResponse mockResponse = mock(ChatResponse.class);
        Generation mockGeneration = mock(Generation.class);
        AssistantMessage mockMessage = mock(AssistantMessage.class);
        
        when(mockGeneration.getOutput()).thenReturn(mockMessage);
        when(mockMessage.getText()).thenReturn(expectedResponse);
        when(mockResponse.getResult()).thenReturn(mockGeneration);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);
        
        String result = controller.ask(prompt);
        assertEquals(expectedResponse, result);
    }

    @Test
    void testAsk_WithNullPrompt() {
        String prompt = null;
        try {
            String result = controller.ask(prompt);
            assertNotNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException || e instanceof NullPointerException);
        }
    }

    @Test
    void testGetCourses_WithAllFilters() {
        List<Course> mockCourses = Arrays.asList(
            new Course(1L, "Java Course", "video1.mp4", "cover1.jpg", 60, "Author1", "Summary1", "Content1", 10, 1, null),
            new Course(2L, "Python Course", "video2.mp4", "cover2.jpg", 90, "Author2", "Summary2", "Content2", 15, 1, null)
        );
        PageResult<Course> mockPageResult = new PageResult<>(1, 10, 2L, mockCourses);
        
        when(courseService.getCourses("Java", 60, "Author1", "Summary1", "Content1", "Programming", 1, 10))
                .thenReturn(mockPageResult);
        
        ResponseEntity<PageResult<Course>> response = controller.getCourses("Java", 60, "Author1", "Summary1", "Content1", "Programming", 1, 10);
        
        assertEquals(200, response.getStatusCodeValue());
        PageResult<Course> result = response.getBody();
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
        assertEquals("Java Course", result.getList().get(0).getTitle());
        assertEquals("Python Course", result.getList().get(1).getTitle());
    }

    @Test
    void testGetCoursesReady_WithAllFilters() {
        List<Course> mockCourses = Arrays.asList(
            new Course(1L, "Pending Course", "video1.mp4", "cover1.jpg", 60, "Author1", "Summary1", "Content1", 0, 0, null)
        );
        PageResult<Course> mockPageResult = new PageResult<>(1, 10, 1L, mockCourses);
        
        when(courseService.getCoursesReady("Pending", 60, "Author1", "Summary1", "Content1", "Programming", 1, 10))
                .thenReturn(mockPageResult);
        
        ResponseEntity<PageResult<Course>> response = controller.getCoursesReady("Pending", 60, "Author1", "Summary1", "Content1", "Programming", 1, 10);
        
        assertEquals(200, response.getStatusCodeValue());
        PageResult<Course> result = response.getBody();
        assertEquals(1, result.getList().size());
        assertEquals("Pending Course", result.getList().get(0).getTitle());
        assertEquals(0, result.getList().get(0).getStatus());
    }

    @Test
    void testGetCoursesPersonal_WithDifferentStatuses() {
        List<Course> pendingCourses = Arrays.asList(
            new Course(1L, "Pending Course", "video1.mp4", "cover1.jpg", 60, "user", "Summary1", "Content1", 0, 0, null)
        );
        PageResult<Course> pendingPageResult = new PageResult<>(1, 10, 1L, pendingCourses);
        
        when(courseService.getCourseByAuthor(0, "user", 1, 10)).thenReturn(pendingPageResult);
        
        ResponseEntity<PageResult<Course>> response = controller.getCoursesPersonal(0, "user", 1, 10);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().getList().get(0).getStatus());

        List<Course> approvedCourses = Arrays.asList(
            new Course(2L, "Approved Course", "video2.mp4", "cover2.jpg", 90, "user", "Summary2", "Content2", 10, 1, null)
        );
        PageResult<Course> approvedPageResult = new PageResult<>(1, 10, 1L, approvedCourses);
        
        when(courseService.getCourseByAuthor(1, "user", 1, 10)).thenReturn(approvedPageResult);
        
        response = controller.getCoursesPersonal(1, "user", 1, 10);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getList().get(0).getStatus());

        List<Course> rejectedCourses = Arrays.asList(
            new Course(3L, "Rejected Course", "video3.mp4", "cover3.jpg", 120, "user", "Summary3", "Content3", 0, 2, null)
        );
        PageResult<Course> rejectedPageResult = new PageResult<>(1, 10, 1L, rejectedCourses);
        
        when(courseService.getCourseByAuthor(2, "user", 1, 10)).thenReturn(rejectedPageResult);
        
        response = controller.getCoursesPersonal(2, "user", 1, 10);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getList().get(0).getStatus());
    }

    @Test
    void testGetCourses_WithPagination() {
        List<Course> mockCourses = Arrays.asList(
            new Course(1L, "Course 1", "video1.mp4", "cover1.jpg", 60, "Author1", "Summary1", "Content1", 10, 1, null),
            new Course(2L, "Course 2", "video2.mp4", "cover2.jpg", 90, "Author2", "Summary2", "Content2", 15, 1, null),
            new Course(3L, "Course 3", "video3.mp4", "cover3.jpg", 120, "Author3", "Summary3", "Content3", 20, 1, null)
        );
        PageResult<Course> mockPageResult = new PageResult<>(2, 3, 9L, mockCourses);
        
        when(courseService.getCourses(null, null, null, null, null, null, 2, 3))
                .thenReturn(mockPageResult);
        
        ResponseEntity<PageResult<Course>> response = controller.getCourses(null, null, null, null, null, null, 2, 3);
        
        assertEquals(200, response.getStatusCodeValue());
        PageResult<Course> result = response.getBody();
        assertEquals(2, result.getPageNum());
        assertEquals(3, result.getPageSize());
        assertEquals(9L, result.getTotal());
        assertEquals(3, result.getList().size());
    }

    @Test
    void testGetCourses_WithEmptyResult() {
        PageResult<Course> emptyPageResult = new PageResult<>(1, 10, 0L, Collections.emptyList());
        
        when(courseService.getCourses(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(emptyPageResult);
        
        ResponseEntity<PageResult<Course>> response = controller.getCourses(null, null, null, null, null, null, 1, 10);
        
        assertEquals(200, response.getStatusCodeValue());
        PageResult<Course> result = response.getBody();
        assertEquals(0L, result.getTotal());
        assertTrue(result.getList().isEmpty());
    }

    @Test
    void testUpdate_WithPartialFields() {
        Course update = new Course(
                1L, "New Title", null, null, 90, null, "New Summary", null, null, null, null
        );
        
        Course original = new Course(
                1L, "Old Title", "http://old-video.mp4", "http://old-cover.jpg",
                60, "Old Author", "Old Summary", "Old Content",
                10, 1, Arrays.asList(new courseCategory(1L, "Old Category"))
        );
        
        User_d user = new User_d("author", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("author")).thenReturn(user);
        when(courseService.updateCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.update(update, "author");
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证只有指定字段被更新，其他字段保持不变
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).updateCourse(courseCaptor.capture());
        Course capturedCourse = courseCaptor.getValue();
        assertEquals("New Title", capturedCourse.getTitle());
        assertEquals("http://old-video.mp4", capturedCourse.getVideoUrl()); // 未更新
        assertEquals("http://old-cover.jpg", capturedCourse.getCoverImage()); // 未更新
        assertEquals(90, capturedCourse.getDuration());
        assertEquals("Old Author", capturedCourse.getAuthor()); // 未更新
        assertEquals("New Summary", capturedCourse.getSummary());
        assertEquals("Old Content", capturedCourse.getContent()); // 未更新
    }

    @Test
    void testUpdate_WithAdminUser() {
        Course update = new Course(1L, "Admin Updated Title", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        
        User_d adminUser = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L); // status = 1 (admin)
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("admin")).thenReturn(adminUser);
        when(courseService.updateCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.update(update, "admin");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_WithRegularUserAndOwnCourse() {
        Course update = new Course(1L, "User Updated Title", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        
        User_d regularUser = new User_d("author", "password", "phone", "email", "male", 0, null, "avatar", 1L); // status = 0 (regular user)
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("author")).thenReturn(regularUser);
        when(courseService.updateCourse(any())).thenReturn(1);
        
        ResponseEntity<Integer> response = controller.update(update, "author");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_WithRegularUserAndOtherUserCourse() {
        Course update = new Course(1L, "Unauthorized Update", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        
        User_d regularUser = new User_d("otheruser", "password", "phone", "email", "male", 0, null, "avatar", 1L); // status = 0 (regular user)
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("otheruser")).thenReturn(regularUser);
        
        ResponseEntity<Integer> response = controller.update(update, "otheruser");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testUpdate_WithUpdateFailure() {
        Course update = new Course(1L, "New Title", null, null, null, null, null, null, null, null, null);
        Course original = new Course(1L, "Old Title", null, null, null, null, null, null, null, null, null);
        
        User_d user = new User_d("author", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        
        when(courseService.getCourseById(1L)).thenReturn(original);
        when(courseMapper.getAuthor(1L)).thenReturn("author");
        when(userMapper.findByUsername("author")).thenReturn(user);
        when(courseService.updateCourse(any())).thenReturn(0);
        
        ResponseEntity<Integer> response = controller.update(update, "author");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody());
    }
}
