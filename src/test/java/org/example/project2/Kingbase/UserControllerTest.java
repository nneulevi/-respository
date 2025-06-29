package org.example.project2.Kingbase;

import org.example.project2.Controller.UserController;
import org.example.project2.Mapper.NewsMapper;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.enterpriseMapper;
import org.example.project2.entity.Enterprise;
import org.example.project2.entity.News;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.User;
import org.example.project2.entity.User_d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    
    private UserController userController;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private NewsMapper newsMapper;
    
    @Mock
    private enterpriseMapper enterpriseMapper;
    
    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        newsMapper = mock(NewsMapper.class);
        enterpriseMapper = mock(enterpriseMapper.class);
        
        userController = new UserController();
        // 使用反射注入依赖
        try {
            var userMapperField = UserController.class.getDeclaredField("userMapper");
            userMapperField.setAccessible(true);
            userMapperField.set(userController, userMapper);
            
            var newsMapperField = UserController.class.getDeclaredField("newsMapper");
            newsMapperField.setAccessible(true);
            newsMapperField.set(userController, newsMapper);
            
            var enterpriseMapperField = UserController.class.getDeclaredField("enterpriseMapper");
            enterpriseMapperField.setAccessible(true);
            enterpriseMapperField.set(userController, enterpriseMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testCheck_LoginSuccess_Admin() {
        // 准备测试数据
        User_d user = new User_d("admin", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsernameAndPassword("admin", "password")).thenReturn(user);
        
        // 执行测试
        boolean result = userController.check(user);
        
        // 验证结果
        assertTrue(result);
        verify(userMapper).findByUsernameAndPassword("admin", "password");
    }
    
    @Test
    void testCheck_LoginSuccess_EnterpriseUser_Approved() {
        // 准备测试数据
        User_d user = new User_d("user", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        Enterprise enterprise = new Enterprise(1L, "公司名", "许可证号", "联系人", "联系电话", 1);
        
        // 模拟Mapper返回
        when(enterpriseMapper.findByid(1L)).thenReturn(enterprise);
        when(userMapper.findByUsernameAndPassword("user", "password")).thenReturn(user);
        
        // 执行测试
        boolean result = userController.check(user);
        
        // 验证结果
        assertTrue(result);
        verify(enterpriseMapper).findByid(1L);
        verify(userMapper).findByUsernameAndPassword("user", "password");
    }
    
    @Test
    void testCheck_LoginFailure_EnterpriseUser_NotApproved() {
        // 准备测试数据
        User_d user = new User_d("user", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        Enterprise enterprise = new Enterprise(1L, "公司名", "许可证号", "联系人", "联系电话", 0);
        
        // 模拟Mapper返回
        when(enterpriseMapper.findByid(1L)).thenReturn(enterprise);
        
        // 执行测试
        boolean result = userController.check(user);
        
        // 验证结果
        assertFalse(result);
        verify(enterpriseMapper).findByid(1L);
        verify(userMapper, never()).findByUsernameAndPassword(any(), any());
    }
    
    @Test
    void testCheck_LoginFailure_EnterpriseUser_EnterpriseNotFound() {
        // 准备测试数据
        User_d user = new User_d("user", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        
        // 模拟Mapper返回
        when(enterpriseMapper.findByid(1L)).thenReturn(null);
        
        // 执行测试
        boolean result = userController.check(user);
        
        // 验证结果
        assertFalse(result);
        verify(enterpriseMapper).findByid(1L);
        verify(userMapper, never()).findByUsernameAndPassword(any(), any());
    }
    
    @Test
    void testCheck_LoginFailure_UserNotFound() {
        // 准备测试数据
        User_d user = new User_d("user", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsernameAndPassword("user", "password")).thenReturn(null);
        
        // 执行测试
        boolean result = userController.check(user);
        
        // 验证结果
        assertFalse(result);
        verify(userMapper).findByUsernameAndPassword("user", "password");
    }
    
    @Test
    void testAdd_AddUser_Success() {
        // 准备测试数据
        User_d user = new User_d("newuser", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        Enterprise enterprise = new Enterprise(1L, "公司名", "许可证号", "联系人", "联系电话", 1);
        
        // 模拟Mapper返回
        when(userMapper.insert(any(User_d.class))).thenReturn(1);
        when(enterpriseMapper.findByid(1L)).thenReturn(enterprise);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.Add(user);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(userMapper).insert(user);
        verify(enterpriseMapper).findByid(1L);
    }
    
    @Test
    void testAdd_AddUser_Failure() {
        // 准备测试数据
        User_d user = new User_d("newuser", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        Enterprise enterprise = new Enterprise(1L, "公司名", "许可证号", "联系人", "联系电话", 1);
        
        // 模拟Mapper返回失败
        when(userMapper.insert(any(User_d.class))).thenReturn(0);
        when(enterpriseMapper.findByid(1L)).thenReturn(enterprise);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.Add(user);
        
        // 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
        verify(userMapper).insert(user);
        verify(enterpriseMapper).findByid(1L);
    }
    
    @Test
    void testAdd_AddUser_Failure_EnterpriseNull() {
        // 准备测试数据 - enterprise为null
        User_d user = new User_d("newuser", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", null);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.Add(user);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
        verify(userMapper, never()).insert(any(User_d.class));
        verify(enterpriseMapper, never()).findByid(any());
    }
    
    @Test
    void testAdd_AddUser_Failure_EnterpriseNotFound() {
        // 准备测试数据
        User_d user = new User_d("newuser", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        
        // 模拟Mapper返回 - enterprise不存在
        when(enterpriseMapper.findByid(1L)).thenReturn(null);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.Add(user);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
        verify(userMapper, never()).insert(any(User_d.class));
        verify(enterpriseMapper).findByid(1L);
    }
    
    @Test
    void testGet_GetUserInfo() {
        // 准备测试数据
        User_d user = new User_d("testuser", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        List<News> newsList = Arrays.asList(
            new News(1L, "标题1", "摘要1", "内容1", "cover1.jpg", "testuser", 10, null, "标签1"),
            new News(2L, "标题2", "摘要2", "内容2", "cover2.jpg", "testuser", 20, null, "标签2")
        );
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("testuser")).thenReturn(user);
        when(newsMapper.selectByauthor("testuser")).thenReturn(newsList);
        
        // 执行测试
        User result = userController.get("testuser");
        
        // 验证结果
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals("phone", result.getPhone());
        assertEquals("email", result.getEmail());
        assertEquals("male", result.getGender());
        assertEquals(1, result.getStatus());
        assertEquals("avatar", result.getAvatar_url());
        assertEquals(1L, result.getEnterprise());
        assertEquals(2, result.getHistory().size());
        
        verify(userMapper).findByUsername("testuser");
        verify(newsMapper).selectByauthor("testuser");
    }
    
    @Test
    void testSearch() {
        // 准备测试数据
        List<User_d> userList = Arrays.asList(
            new User_d("user1", "password1", "phone1", "email1", "male", 1, LocalDate.now(), "avatar1", 1L),
            new User_d("user2", "password2", "phone2", "email2", "female", 0, LocalDate.now(), "avatar2", 2L)
        );
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        // 模拟Mapper返回
        when(userMapper.findByKeyword(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(userList);
        when(userMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<User_d> result = userController.search(1, 10, "user", "phone", "email", "male", startDate, endDate);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }
    
    @Test
    void testGet_GetUserList() {
        // 准备测试数据
        List<User_d> userList = Arrays.asList(
            new User_d("user1", "password1", "phone1", "email1", "male", 1, LocalDate.now(), "avatar1", 1L)
        );
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        // 模拟Mapper返回
        when(userMapper.findByKeyword(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(userList);
        when(userMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);
        
        // 执行测试
        ResponseEntity<PageResult<User_d>> response = userController.get(1, 10, "user", "phone", "email", "male", startDate, endDate);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
    }
    
    @Test
    void testChange_ChangeUserInfo_Success_Admin() {
        // 准备测试数据
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        User_d targetUser = new User_d("targetuser", "oldpassword", "oldphone", "oldemail", "male", 0, LocalDate.now(), "oldavatar", 1L);
        User_d updateUser = new User_d("newuser", "newpassword", "newphone", "newemail", "female", 1, LocalDate.now(), "newavatar", 2L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(userMapper.findByUsername("targetuser")).thenReturn(targetUser);
        when(userMapper.change_info(any(User_d.class), eq("targetuser"))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.change("admin", "targetuser", updateUser);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(userMapper).change_info(any(User_d.class), eq("targetuser"));
    }
    
    @Test
    void testChange_ChangeUserInfo_Success_UserSelf() {
        // 准备测试数据
        User_d currentUser = new User_d("user", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        User_d updateUser = new User_d("user", "newpassword", "newphone", "newemail", "female", 0, LocalDate.now(), "newavatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("user")).thenReturn(currentUser);
        when(userMapper.change_info(any(User_d.class), eq("user"))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.change("user", "user", updateUser);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }
    
    @Test
    void testChange_ChangeUserInfo_Failure_UserModifyOther() {
        // 准备测试数据
        User_d currentUser = new User_d("user1", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        User_d updateUser = new User_d("user2", "newpassword", "newphone", "newemail", "female", 0, LocalDate.now(), "newavatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("user1")).thenReturn(currentUser);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.change("user1", "user2", updateUser);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody());
        verify(userMapper, never()).change_info(any(), any());
    }
    
    @Test
    void testChange_ChangeUserInfo_PartialUpdate() {
        // 准备测试数据
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        User_d targetUser = new User_d("targetuser", "oldpassword", "oldphone", "oldemail", "male", 0, LocalDate.now(), "oldavatar", 1L);
        // 创建一个部分更新的用户对象
        User_d updateUser = new User_d("newuser", "newpassword", null, null, null, null, null, null, null);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(userMapper.findByUsername("targetuser")).thenReturn(targetUser);
        when(userMapper.change_info(any(User_d.class), eq("targetuser"))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.change("admin", "targetuser", updateUser);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        
        // 验证只有非空字段被更新
        verify(userMapper).change_info(argThat(user -> 
            "newuser".equals(user.getUsername()) && 
            "newpassword".equals(user.getPassword()) &&
            "oldphone".equals(user.getPhone()) &&
            "oldemail".equals(user.getEmail()) &&
            "oldavatar".equals(user.getAvatar_url())
        ), eq("targetuser"));
    }
    
    @Test
    void testChange_ChangeUserInfo_AdminCanChangeStatus() {
        // 准备测试数据
        User_d currentUser = new User_d("admin", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        User_d targetUser = new User_d("targetuser", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        User_d updateUser = new User_d("targetuser", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("admin")).thenReturn(currentUser);
        when(userMapper.findByUsername("targetuser")).thenReturn(targetUser);
        when(userMapper.change_info(any(User_d.class), eq("targetuser"))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.change("admin", "targetuser", updateUser);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        
        // 验证状态被更新
        verify(userMapper).change_info(argThat(user -> user.getStatus() == 1), eq("targetuser"));
    }
    
    @Test
    void testChange_ChangeUserInfo_UserCannotChangeStatus() {
        // 准备测试数据
        User_d currentUser = new User_d("user", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        User_d targetUser = new User_d("user", "password", "phone", "email", "male", 0, LocalDate.now(), "avatar", 1L);
        User_d updateUser = new User_d("user", "password", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        
        // 模拟Mapper返回
        when(userMapper.findByUsername("user")).thenReturn(currentUser);
        when(userMapper.change_info(any(User_d.class), eq("user"))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = userController.change("user", "user", updateUser);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        
        // 验证状态没有被更新（应该保持原状态）
        verify(userMapper).change_info(argThat(user -> user.getStatus() == 0), eq("user"));
    }
} 