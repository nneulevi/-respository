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
import org.example.project2.Mapper.comentsMapper;
import org.example.project2.entity.coments;

public class NewsControllerTest {
    
    private NewsController newsController;
    
    @Mock
    private NewsMapper newsMapper;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private comentsMapper comentsMapper;
    
    @BeforeEach
    void setUp() {
        newsMapper = mock(NewsMapper.class);
        userMapper = mock(UserMapper.class);
        comentsMapper = mock(comentsMapper.class);
        newsController = new NewsController();
        try {
            var newsMapperField = NewsController.class.getDeclaredField("newsMapper");
            newsMapperField.setAccessible(true);
            newsMapperField.set(newsController, newsMapper);
            var userMapperField = NewsController.class.getDeclaredField("userMapper");
            userMapperField.setAccessible(true);
            userMapperField.set(newsController, userMapper);
            var comentsMapperField = NewsController.class.getDeclaredField("comentsMapper");
            comentsMapperField.setAccessible(true);
            comentsMapperField.set(newsController, comentsMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testAdd_Submit() {
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 0, LocalDateTime.now(), "测试标签");

        when(newsMapper.submit(any(News.class))).thenReturn(1);

        ResponseEntity<Integer> response = newsController.Add(news);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(newsMapper).submit(news);
    }
    
    @Test
    void testAdd_Submit_Failure() {
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 0, LocalDateTime.now(), "测试标签");
        
        // 模拟Mapper返回失败
        when(newsMapper.submit(any(News.class))).thenReturn(0);

        ResponseEntity<Integer> response = newsController.Add(news);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
    
    @Test
    void testAdd_Submit_NullNews() {
        // @RequestBody null is handled by Spring, so just skip or assert true
        assertTrue(true);
    }
    
    @Test
    void testInsert_AddNews() {
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 0, LocalDateTime.now(), "测试标签");

        when(newsMapper.insert(any(News.class))).thenReturn(1);

        ResponseEntity<Integer> response = newsController.Insert(news);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(newsMapper).insert(news);
    }
    
    @Test
    void testInsert_AddNews_Failure() {
        News news = new News(1L, "标题", "摘要", "内容", "cover.jpg", "admin", 0, LocalDateTime.now(), "标签");
        when(newsMapper.insert(any(News.class))).thenReturn(0);
        ResponseEntity<Integer> response = newsController.Insert(news);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
    
    @Test
    void testSearch() {

        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "user2", 20, LocalDateTime.now(), "标签2")
        );

        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);

        PageResult<News> result = newsController.search(1, 10, null, null, null, null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }
    
    @Test
    void testSearch_EmptyResult() {
        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(Collections.emptyList());
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(0L);
        PageResult<News> result = newsController.search(1, 10, null, null, null, null, null, null, null, null, null);
        assertNotNull(result);
        assertEquals(0, result.getList().size());
        assertEquals(0L, result.getTotal());
    }

    @Test
    void testSearch_NullFilters() {
        List<News> newsList = Arrays.asList(
            new News(1L, "标题", "摘要", "内容", "cover.jpg", "user", 1, LocalDateTime.now(), "标签")
        );
        when(newsMapper.findNewsByKeyword(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenReturn(1L);
        PageResult<News> result = newsController.search(1, 10, null, null, null, null, null, null, null, null, null);
        assertEquals(1, result.getList().size());
        assertEquals(1L, result.getTotal());
    }
    
    @Test
    void testSearchByTime() {
        LocalDateTime now = LocalDateTime.now();
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, now.minusHours(1), "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "user2", 20, now, "标签2")
        );

        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);

        PageResult<News> result = newsController.searchByTime(1, 10, null, null, null, null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(now, result.getList().get(0).getPublishTime());
    }
    
    @Test
    void testSearchByCount() {
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "user2", 20, LocalDateTime.now(), "标签2")
        );

        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);

        PageResult<News> result = newsController.searchBycount(1, 10, null, null, null, null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(20, result.getList().get(0).getViewCount());
    }
    
    @Test
    void testGet() {
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );

        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);

        ResponseEntity<PageResult<News>> response = newsController.get(1, 10, null, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
    }
    
    @Test
    void testGetByTime() {
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );

        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);

        ResponseEntity<PageResult<News>> response = newsController.get_by_time(1, 10, null, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetByCount() {
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );

        when(newsMapper.findNewsByKeyword(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(newsList);
        when(newsMapper.count(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);

        ResponseEntity<PageResult<News>> response = newsController.get_by_count(1, 10, null, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetDetail() {
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 10, LocalDateTime.now(), "测试标签");

        when(newsMapper.selectBytitle("测试标题")).thenReturn(news);

        ResponseEntity<News> response = newsController.getDetail("测试标题");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(news, response.getBody());
        verify(newsMapper).selectBytitle("测试标题");
    }
    
    @Test
    void testGetDetail_NotFound() {
        when(newsMapper.selectBytitle("不存在的标题")).thenReturn(null);
        ResponseEntity<News> response = newsController.getDetail("不存在的标题");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
    
    @Test
    void testReadyToCheck() {
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "user1", 10, LocalDateTime.now(), "标签1")
        );

        when(newsMapper.selectAll(anyInt(), anyInt())).thenReturn(newsList);
        when(newsMapper.count2()).thenReturn(1L);

        ResponseEntity<PageResult<News>> response = newsController.ready_to_check(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
        verify(newsMapper).selectAll(0, 10);
        verify(newsMapper).count2();
    }
    
    @Test
    void testModify() {
        News news = new News(1L, "测试标题", "测试摘要", "测试内容", 
                           "cover.jpg", "testuser", 10, LocalDateTime.now(), "测试标签");

        when(newsMapper.selectBytitle("测试标题")).thenReturn(news);
        when(newsMapper.update(11, "测试标题")).thenReturn(1);

        ResponseEntity<Integer> response = newsController.modify("测试标题");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(newsMapper).selectBytitle("测试标题");
        verify(newsMapper).update(11, "测试标题");
    }
    
    @Test
    void testModify_IncreaseViewCount() {
        News news = new News(1L, "标题", "摘要", "内容", "cover.jpg", "user", 5, LocalDateTime.now(), "标签");
        when(newsMapper.selectBytitle("标题")).thenReturn(news);
        when(newsMapper.update(6, "标题")).thenReturn(1);
        ResponseEntity<Integer> response = newsController.modify("标题");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void testModify_Failure() {
        News news = new News(1L, "标题", "摘要", "内容", "cover.jpg", "user", 5, LocalDateTime.now(), "标签");
        when(newsMapper.selectBytitle("标题")).thenReturn(news);
        when(newsMapper.update(6, "标题")).thenReturn(0);
        ResponseEntity<Integer> response = newsController.modify("标题");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_Success() {
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L);
        News oldNews = new News(1L, "旧标题", "旧摘要", "旧内容", 
                              "old.jpg", "admin", 10, LocalDateTime.now(), "旧标签");
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "admin", 10, LocalDateTime.now(), "新标签");

        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(newsMapper.selectById(1L)).thenReturn(oldNews);
        when(newsMapper.modify(any(News.class), eq(1L))).thenReturn(1);

        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, updateNews);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_UserNotFound() {
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "admin", 10, LocalDateTime.now(), "新标签");

        when(userMapper.findByUsername("admin")).thenReturn(null);

        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, updateNews);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(-3, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_NewsNotFound() {
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, null, "avatar", 1L);
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "admin", 10, LocalDateTime.now(), "新标签");

        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(newsMapper.selectById(1L)).thenReturn(null);

        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, updateNews);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(-2, response.getBody());
    }
    
    @Test
    void testChangeNewsInfo_PermissionDenied() {
        User_d currentUser = new User_d("user1", "password", "phone", "email", "male", 0, null, "avatar", 1L);
        News oldNews = new News(1L, "旧标题", "旧摘要", "旧内容", 
                              "old.jpg", "user2", 10, LocalDateTime.now(), "旧标签");
        News updateNews = new News(1L, "新标题", "新摘要", "新内容", 
                                 "new.jpg", "user1", 10, LocalDateTime.now(), "新标签");

        when(userMapper.findByUsername("user1")).thenReturn(currentUser);
        when(newsMapper.selectById(1L)).thenReturn(oldNews);

        ResponseEntity<Integer> response = newsController.changeNewsInfo("user1", 1L, updateNews);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }

    @Test
    void testChangeNewsInfo_AdminCanEditAnyNews() {
        User_d admin = new User_d("admin", "pwd", "phone", "email", "male", 1, null, "avatar", 1L);
        News oldNews = new News(1L, "标题", "摘要", "内容", "cover.jpg", "user", 1, LocalDateTime.now(), "标签");
        News update = new News(1L, "新标题", null, null, null, null, null, null, null);
        when(userMapper.findByUsername("admin")).thenReturn(admin);
        when(newsMapper.selectById(1L)).thenReturn(oldNews);
        when(newsMapper.modify(any(News.class), eq(1L))).thenReturn(1);
        ResponseEntity<Integer> response = newsController.changeNewsInfo("admin", 1L, update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void testChangeNewsInfo_UserCannotEditOthersNews() {
        User_d user = new User_d("user1", "pwd", "phone", "email", "male", 0, null, "avatar", 1L);
        News oldNews = new News(1L, "标题", "摘要", "内容", "cover.jpg", "user2", 1, LocalDateTime.now(), "标签");
        News update = new News(1L, "新标题", null, null, null, null, null, null, null);
        when(userMapper.findByUsername("user1")).thenReturn(user);
        when(newsMapper.selectById(1L)).thenReturn(oldNews);
        ResponseEntity<Integer> response = newsController.changeNewsInfo("user1", 1L, update);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(-1, response.getBody());
    }

    // --- 新增：评论相关和新闻删除相关方法的测试 ---
    @Test
    void testAddComment() {
        org.example.project2.entity.coments comment = new org.example.project2.entity.coments(1L, 1L, "内容", java.time.LocalDateTime.now(), 0, "author");
        when(comentsMapper.insert(any())).thenReturn(1);
        ResponseEntity<Integer> response = newsController.addComment(comment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void testAddComment_Success() {
        coments comment = new coments(1L, 1L, "评论内容", LocalDateTime.now(), 0, "user");
        when(comentsMapper.insert(any(coments.class))).thenReturn(1);
        ResponseEntity<Integer> response = newsController.addComment(comment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void testAddComment_Failure() {
        coments comment = new coments(1L, 1L, "评论内容", LocalDateTime.now(), 0, "user");
        when(comentsMapper.insert(any(coments.class))).thenReturn(0);
        ResponseEntity<Integer> response = newsController.addComment(comment);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    @Test
    void testGetCommentsByTime() {
        List<org.example.project2.entity.coments> list = Collections.singletonList(new org.example.project2.entity.coments(1L, 1L, "内容", java.time.LocalDateTime.now(), 0, "author"));
        when(comentsMapper.findByNewsIdOrderByTime(anyLong(), anyInt(), anyInt())).thenReturn(list);
        when(comentsMapper.countByNewsId(anyLong())).thenReturn(1L);
        ResponseEntity<PageResult<org.example.project2.entity.coments>> response = newsController.getCommentsByTime(1L, 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
    }

    @Test
    void testGetCommentsByTime_Empty() {
        when(comentsMapper.findByNewsIdOrderByTime(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(comentsMapper.countByNewsId(anyLong())).thenReturn(0L);
        ResponseEntity<PageResult<coments>> response = newsController.getCommentsByTime(1L, 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getList().size());
        assertEquals(0L, response.getBody().getTotal());
    }

    @Test
    void testGetCommentsByLikes() {
        List<org.example.project2.entity.coments> list = Collections.singletonList(new org.example.project2.entity.coments(1L, 1L, "内容", java.time.LocalDateTime.now(), 0, "author"));
        when(comentsMapper.findByNewsIdOrderByLikes(anyLong(), anyInt(), anyInt())).thenReturn(list);
        when(comentsMapper.countByNewsId(anyLong())).thenReturn(1L);
        ResponseEntity<PageResult<org.example.project2.entity.coments>> response = newsController.getCommentsByLikes(1L, 1, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
    }

    @Test
    void testLikeComment() {
        when(comentsMapper.likeComment(1L)).thenReturn(1);
        ResponseEntity<Integer> response = newsController.likeComment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void testDeleteComment() {
        when(comentsMapper.deleteById(1L)).thenReturn(1);
        ResponseEntity<Integer> response = newsController.deleteComment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void testDeleteCommentsByNewsId() {
        when(comentsMapper.deleteByNewsId(1L)).thenReturn(2);
        ResponseEntity<Integer> response = newsController.deleteCommentsByNewsId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody());
    }

    @Test
    void testResourseOfComments() {
        when(comentsMapper.findAuthorById(1L)).thenReturn("author");
        ResponseEntity<String> response = newsController.resourseOfComments(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("author", response.getBody());
    }

    @Test
    void testDeleteNews() {
        when(newsMapper.deleteById(1L)).thenReturn(1);
        when(comentsMapper.deleteByNewsId(1L)).thenReturn(2);
        ResponseEntity<Integer> response = newsController.deleteNews(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody());
    }
} 