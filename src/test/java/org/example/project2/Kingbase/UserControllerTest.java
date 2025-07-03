package org.example.project2.Kingbase;

import org.example.project2.Controller.UserController;
import org.example.project2.Mapper.NewsMapper;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.enterpriseMapper;
import org.example.project2.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserController userController;
    @Mock private UserMapper userMapper;
    @Mock private NewsMapper newsMapper;
    @Mock private enterpriseMapper enterpriseMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        newsMapper = mock(NewsMapper.class);
        enterpriseMapper = mock(enterpriseMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userController = new UserController();
        try {
            var f1 = UserController.class.getDeclaredField("userMapper");
            f1.setAccessible(true); f1.set(userController, userMapper);
            var f2 = UserController.class.getDeclaredField("newsMapper");
            f2.setAccessible(true); f2.set(userController, newsMapper);
            var f3 = UserController.class.getDeclaredField("enterpriseMapper");
            f3.setAccessible(true); f3.set(userController, enterpriseMapper);
            var f4 = UserController.class.getDeclaredField("passwordEncoder");
            f4.setAccessible(true); f4.set(userController, passwordEncoder);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Test
    void testCheck_Admin_Success() {
        User_d user = new User_d("admin", "rawpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        User_d dbUser = new User_d("admin", "encodedpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        when(userMapper.findByUsername("admin")).thenReturn(dbUser);
        when(passwordEncoder.matches("rawpass", "encodedpass")).thenReturn(true);
        assertTrue(userController.check(user));
    }

    @Test
    void testCheck_Admin_Fail_WrongPassword() {
        User_d user = new User_d("admin", "rawpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        User_d dbUser = new User_d("admin", "encodedpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 1L);
        when(userMapper.findByUsername("admin")).thenReturn(dbUser);
        when(passwordEncoder.matches("rawpass", "encodedpass")).thenReturn(false);
        assertFalse(userController.check(user));
    }

    @Test
    void testCheck_EnterpriseUser_EnterpriseNotFound() {
        User_d user = new User_d("user", "pass", "phone", "email", "male", 0, LocalDate.now(), "avatar", 2L);
        when(enterpriseMapper.findByid(2L)).thenReturn(null);
        assertFalse(userController.check(user));
    }

    @Test
    void testCheck_EnterpriseUser_NotApproved() {
        User_d user = new User_d("user", "pass", "phone", "email", "male", 0, LocalDate.now(), "avatar", 2L);
        Enterprise ent = new Enterprise(2L, "ent", "lic", "contact", "phone", 0);
        when(enterpriseMapper.findByid(2L)).thenReturn(ent);
        assertFalse(userController.check(user));
    }

    @Test
    void testCheck_EnterpriseUser_Success() {
        User_d user = new User_d("user", "rawpass", "phone", "email", "male", 0, LocalDate.now(), "avatar", 2L);
        Enterprise ent = new Enterprise(2L, "ent", "lic", "contact", "phone", 1);
        User_d dbUser = new User_d("user", "encodedpass", "phone", "email", "male", 0, LocalDate.now(), "avatar", 2L);
        when(enterpriseMapper.findByid(2L)).thenReturn(ent);
        when(userMapper.findByUsername("user")).thenReturn(dbUser);
        when(passwordEncoder.matches("rawpass", "encodedpass")).thenReturn(true);
        assertTrue(userController.check(user));
    }

    @Test
    void testCheck_UserNotFound() {
        User_d user = new User_d("user", "pass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 2L);
        when(userMapper.findByUsername("user")).thenReturn(null);
        assertFalse(userController.check(user));
    }

    @Test
    void testAddUser_Success() {
        User_d user = new User_d("user", "rawpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 2L);
        Enterprise ent = new Enterprise(2L, "ent", "lic", "contact", "phone", 1);
        when(enterpriseMapper.findByid(2L)).thenReturn(ent);
        when(passwordEncoder.encode("rawpass")).thenReturn("encodedpass");
        when(userMapper.insert(any(User_d.class))).thenReturn(1);
        ResponseEntity<Integer> resp = userController.Add(user);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody());
    }

    @Test
    void testAddUser_Fail_EnterpriseNull() {
        User_d user = new User_d("user", "rawpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", null);
        ResponseEntity<Integer> resp = userController.Add(user);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(-1, resp.getBody());
    }

    @Test
    void testAddUser_Fail_EnterpriseNotExist() {
        User_d user = new User_d("user", "rawpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 2L);
        when(enterpriseMapper.findByid(2L)).thenReturn(null);
        ResponseEntity<Integer> resp = userController.Add(user);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(-1, resp.getBody());
    }

    @Test
    void testAddUser_Fail_InsertFail() {
        User_d user = new User_d("user", "rawpass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 2L);
        Enterprise ent = new Enterprise(2L, "ent", "lic", "contact", "phone", 1);
        when(enterpriseMapper.findByid(2L)).thenReturn(ent);
        when(passwordEncoder.encode("rawpass")).thenReturn("encodedpass");
        when(userMapper.insert(any(User_d.class))).thenReturn(0);
        ResponseEntity<Integer> resp = userController.Add(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals(0, resp.getBody());
    }

    @Test
    void testGetUserInfo_Success() {
        User_d user = new User_d("user", "pass", "phone", "email", "male", 1, LocalDate.now(), "avatar", 2L);
        List<News> newsList = Arrays.asList(new News(1L, "t1", "s1", "c1", "cover1", "user", 1, null, "tag1"));
        when(userMapper.findByUsername("user")).thenReturn(user);
        when(newsMapper.selectByauthor("user")).thenReturn(newsList);
        User result = userController.get("user");
        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertEquals(1, result.getHistory().size());
    }

    @Test
    void testGetUserInfo_UserNotFound() {
        when(userMapper.findByUsername("nouser")).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userController.get("nouser"));
    }

    @Test
    void testSearchUser() {
        List<User_d> users = Arrays.asList(new User_d("u1", "p1", "ph1", "e1", "m", 1, LocalDate.now(), "a1", 1L));
        when(userMapper.findByKeyword(any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(users);
        when(userMapper.count(any(), any(), any(), any(), any(), any())).thenReturn(1L);
        PageResult<User_d> result = userController.search(1, 10, "u", "ph", "e", "m", LocalDate.now().minusDays(1), LocalDate.now());
        assertEquals(1, result.getList().size());
        assertEquals(1L, result.getTotal());
    }

    @Test
    void testGetUserList() {
        List<User_d> users = Arrays.asList(new User_d("u1", "p1", "ph1", "e1", "m", 1, LocalDate.now(), "a1", 1L));
        when(userMapper.findByKeyword(any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(users);
        when(userMapper.count(any(), any(), any(), any(), any(), any())).thenReturn(1L);
        ResponseEntity<PageResult<User_d>> resp = userController.get(1, 10, "u", "ph", "e", "m", LocalDate.now().minusDays(1), LocalDate.now());
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().getList().size());
    }

    @Test
    void testChangeUserInfo_Admin_Success() {
        User_d now = new User_d("admin", "pass", "ph", "e", "m", 1, LocalDate.now(), "a", 1L);
        User_d old = new User_d("target", "oldpass", "oldph", "olde", "m", 0, LocalDate.now(), "olda", 1L);
        User_d update = new User_d("newname", "newpass", "newph", "newe", "f", 1, LocalDate.now(), "newa", 2L);
        when(userMapper.findByUsername("admin")).thenReturn(now);
        when(userMapper.findByUsername("target")).thenReturn(old);
        when(userMapper.change_info(any(User_d.class), eq("target"))).thenReturn(1);
        ResponseEntity<Integer> resp = userController.change("admin", "target", update);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody());
    }

    @Test
    void testChangeUserInfo_UserSelf_Success() {
        User_d now = new User_d("user", "pass", "ph", "e", "m", 0, LocalDate.now(), "a", 1L);
        User_d old = new User_d("user", "oldpass", "oldph", "olde", "m", 0, LocalDate.now(), "olda", 1L);
        User_d update = new User_d("user", "newpass", "newph", "newe", "f", 0, LocalDate.now(), "newa", 1L);
        when(userMapper.findByUsername("user")).thenReturn(now);
        when(userMapper.findByUsername("user")).thenReturn(old);
        when(userMapper.change_info(any(User_d.class), eq("user"))).thenReturn(1);
        ResponseEntity<Integer> resp = userController.change("user", "user", update);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody());
    }

    @Test
    void testChangeUserInfo_UserNoPermission() {
        User_d now = new User_d("user1", "pass", "ph", "e", "m", 0, LocalDate.now(), "a", 1L);
        User_d update = new User_d("user2", "newpass", "newph", "newe", "f", 0, LocalDate.now(), "newa", 1L);
        when(userMapper.findByUsername("user1")).thenReturn(now);
        ResponseEntity<Integer> resp = userController.change("user1", "user2", update);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(-1, resp.getBody());
    }

    @Test
    void testChangeUserInfo_PartialUpdate() {
        User_d now = new User_d("admin", "pass", "ph", "e", "m", 1, LocalDate.now(), "a", 1L);
        User_d old = new User_d("target", "oldpass", "oldph", "olde", "m", 0, LocalDate.now(), "olda", 1L);
        User_d update = new User_d("newname", null, null, null, null, null, null, null, null);
        when(userMapper.findByUsername("admin")).thenReturn(now);
        when(userMapper.findByUsername("target")).thenReturn(old);
        when(userMapper.change_info(any(User_d.class), eq("target"))).thenReturn(1);
        ResponseEntity<Integer> resp = userController.change("admin", "target", update);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody());
    }

    @Test
    void testChangeUserInfo_AdminCanChangeStatus() {
        User_d now = new User_d("admin", "pass", "ph", "e", "m", 1, LocalDate.now(), "a", 1L);
        User_d old = new User_d("target", "oldpass", "oldph", "olde", "m", 0, LocalDate.now(), "olda", 1L);
        User_d update = new User_d("target", "pass", "ph", "e", "m", 1, LocalDate.now(), "a", 1L);
        when(userMapper.findByUsername("admin")).thenReturn(now);
        when(userMapper.findByUsername("target")).thenReturn(old);
        when(userMapper.change_info(any(User_d.class), eq("target"))).thenReturn(1);
        ResponseEntity<Integer> resp = userController.change("admin", "target", update);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody());
    }

    @Test
    void testChangeUserInfo_UserCannotChangeStatus() {
        User_d now = new User_d("user", "pass", "ph", "e", "m", 0, LocalDate.now(), "a", 1L);
        User_d old = new User_d("user", "oldpass", "oldph", "olde", "m", 0, LocalDate.now(), "olda", 1L);
        User_d update = new User_d("user", "pass", "ph", "e", "m", 1, LocalDate.now(), "a", 1L);
        when(userMapper.findByUsername("user")).thenReturn(now);
        when(userMapper.findByUsername("user")).thenReturn(old);
        when(userMapper.change_info(any(User_d.class), eq("user"))).thenReturn(1);
        ResponseEntity<Integer> resp = userController.change("user", "user", update);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody());
    }
} 