package org.example.project2.Kingbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project2.Controller.NewsController;
import org.example.project2.Mapper.NewsMapper;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.entity.News;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.User_d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class NewsControllerTest {
    
    private NewsController newsController;
    
    @Mock
    private NewsMapper newsMapper;
    
    @Mock
    private UserMapper userMapper;
    
    @BeforeEach
    void setUp() {
        newsMapper = mock(NewsMapper.class);
        userMapper = mock(UserMapper.class);
        
        newsController = new NewsController();
        // 使用反射注入依赖
        try {
            var newsMapperField = NewsController.class.getDeclaredField("newsMapper");
            newsMapperField.setAccessible(true);
            newsMapperField.set(newsController, newsMapper);
            
            var userMapperField = NewsController.class.getDeclaredField("userMapper");
            userMapperField.setAccessible(true);
            userMapperField.set(newsController, userMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testAdd_Submit() {
        // 准备测试数据
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 0, LocalDateTime.now(), "测试标签");
        
        // 模拟Mapper返回
        when(newsMapper.submit(any(News.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.Add(news);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(newsMapper).submit(news);
    }
    
    @Test
    void testAdd_Submit_Failure() {
        // 准备测试数据
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 0, LocalDateTime.now(), "测试标签");
        
        // 模拟Mapper返回失败
        when(newsMapper.submit(any(News.class))).thenReturn(0);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.Add(news);
        
        // 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
    
    @Test
    void testInsert_AddNews() {
        // 准备测试数据
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 0, LocalDateTime.now(), "测试标签");
        
        // 模拟Mapper返回
        when(newsMapper.insert(any(News.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.Insert(news);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(newsMapper).insert(news);
    }
    
    @Test
    void testSearch() {
        // 准备测试数据
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "user2", 20, LocalDateTime.now(), "标签2")
        );
        
        // 模拟Mapper返回
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<News> result = newsController.search(1, 10, null, null, null, null, null, null, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }
    
    @Test
    void testSearchByTime() {
        // 准备测试数据
        LocalDateTime now = LocalDateTime.now();
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, now.minusHours(1), "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "user2", 20, now, "标签2")
        );
        
        // 模拟Mapper返回
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<News> result = newsController.searchByTime(1, 10, null, null, null, null, null, null, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        // 验证按时间排序（最新的在前）
        assertEquals(now, result.getList().get(0).getPublishTime());
    }
    
    @Test
    void testSearchByCount() {
        // 准备测试数据
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "user2", 20, LocalDateTime.now(), "标签2")
        );
        
        // 模拟Mapper返回
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<News> result = newsController.searchBycount(1, 10, null, null, null, null, null, null, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        // 验证按浏览量排序（高的在前）
        assertEquals(20, result.getList().get(0).getViewCount());
    }
    
    @Test
    void testGet() {
        // 准备测试数据
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );
        
        // 模拟Mapper返回
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);
        
        // 执行测试
        ResponseEntity<PageResult<News>> response = newsController.get(1, 10, null, null, null, null, null, null, null, null, null);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
    }
    
    @Test
    void testGetByTime() {
        // 准备测试数据
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );
        
        // 模拟Mapper返回
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);
        
        // 执行测试
        ResponseEntity<PageResult<News>> response = newsController.get_by_time(1, 10, null, null, null, null, null, null, null, null, null);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetByCount() {
        // 准备测试数据
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );
        
        // 模拟Mapper返回
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);
        
        // 执行测试
        ResponseEntity<PageResult<News>> response = newsController.get_by_count(1, 10, null, null, null, null, null, null, null, null, null);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetDetail() {
        // 准备测试数据
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 10, LocalDateTime.now(), "测试标签");
        
        // 模拟Mapper返回
        when(newsMapper.selectBytitle("测试标题")).thenReturn(news);
        
        // 执行测试
        ResponseEntity<News> response = newsController.getDetail("测试标题");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(news, response.getBody());
        verify(newsMapper).selectBytitle("测试标题");
    }
    
    @Test
    void testReadyToCheck() {
        // 准备测试数据
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );
        
        // 模拟Mapper返回
        when(newsMapper.selectAll(anyInt(), anyInt())).thenReturn(newsList);
        when(newsMapper.count2()).thenReturn(1L);
        
        // 执行测试
        ResponseEntity<PageResult<News>> response = newsController.ready_to_check(1, 10);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
        verify(newsMapper).selectAll(0, 10);
        verify(newsMapper).count2();
    }
    
    @Test
    void testModify() {
        // 准备测试数据
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 10, LocalDateTime.now(), "测试标签");
        
        // 模拟Mapper返回
        when(newsMapper.selectBytitle("测试标题")).thenReturn(news);
        when(newsMapper.update(11, "测试标题")).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.modify("测试标题");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(newsMapper).selectBytitle("测试标题");
        verify(newsMapper).update(11, "测试标题");
    }
    
    @Test
    void testChangeNewsInfo_Success() {
        // 准备测试数据
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L);
        News oldNews = new News(1L, "旧标题", "旧摘要", "旧内容", 
                              "old.jpg", "admin", 10, LocalDateTime.now(), "旧标签");
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "admin", 10, LocalDateTime.now(), "新标签");
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(newsMapper.selectById(1L)).thenReturn(oldNews);
        when(newsMapper.modify(any(News.class), eq(1L))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, updateNews);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_UserNotFound() {
        // 准备测试数据
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "admin", 10, LocalDateTime.now(), "新标签");
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("admin")).thenReturn(null);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, updateNews);
        
        // 验证结果
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(-3, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_NewsNotFound() {
        // 准备测试数据
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L);
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "admin", 10, LocalDateTime.now(), "新标签");
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(newsMapper.selectById(1L)).thenReturn(null);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, updateNews);
        
        // 验证结果
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(-2, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_PermissionDenied() {
        // 准备测试数据
        User_d currentUser = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        News oldNews = new News(1L, "旧标题", "旧摘要", "旧内容", 
                              "old.jpg", "user2", 10, LocalDateTime.now(), "旧标签");
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "user1", 10, LocalDateTime.now(), "新标签");
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("user1")).thenReturn(currentUser);
        when(newsMapper.selectById(1L)).thenReturn(oldNews);
        
        // 执行测试
        ResponseEntity<Integer> response = newsController.changeNewsInfo("user1", 1L, updateNews);
        
        // 验证结果
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }
} 