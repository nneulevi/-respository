package org.example.project2.Kingbase;

import org.example.project2.Mapper.courseMapper;
import org.example.project2.Service.CourseService;
import org.example.project2.entity.Course;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.courseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {
    
    private CourseService courseService;
    
    @Mock
    private courseMapper courseMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseService();
        // 使用反射注入依赖
        try {
            var courseMapperField = CourseService.class.getDeclaredField("courseMapper");
            courseMapperField.setAccessible(true);
            courseMapperField.set(courseService, courseMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testGetCourses_Success() {
        // 准备测试数据
        List<Course> courseList = Arrays.asList(
            new Course(1L, "Java Course", "video1", null, 60, "author1", "summary1", "content1", 10, null, 1),
            new Course(2L, "Python Course", "video2", null, 90, "author2", "summary2", "content2", 20, null, 1)
        );
        List<courseCategory> categories = Arrays.asList(
            new courseCategory(1L, "Programming"),
            new courseCategory(2L, "Web Development")
        );
        
        // 模拟Mapper返回
        when(courseMapper.selectByFilter(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(courseList);
        when(courseMapper.selectByFiltercount(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(2L);
        when(courseMapper.selectCategoriesByCourseId(1L)).thenReturn(categories);
        when(courseMapper.selectCategoriesByCourseId(2L)).thenReturn(categories);
        
        // 执行测试
        PageResult<Course> result = courseService.getCourses("Java", 60, "author1", "summary1", "content1", "Programming", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
        assertEquals("Java Course", result.getList().get(0).getTitle());
        assertEquals(2, result.getList().get(0).getCategories().size());
        
        // 验证方法调用
        verify(courseMapper).selectByFilter("Java", 60, "author1", "summary1", "content1", "Programming", 10, 0);
        verify(courseMapper).selectByFiltercount("Java", 60, "author1", "summary1", "content1", "Programming");
        verify(courseMapper).selectCategoriesByCourseId(1L);
        verify(courseMapper).selectCategoriesByCourseId(2L);
    }
    
    @Test
    void testGetCoursesReady_Success() {
        // 准备测试数据
        List<Course> courseList = Arrays.asList(
            new Course(1L, "Java Course", "video1", null, 60, "author1", "summary1", "content1", 10, null, 0)
        );
        List<courseCategory> categories = Arrays.asList(new courseCategory(1L, "Programming"));
        
        // 模拟Mapper返回
        when(courseMapper.selectByFilter2(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(courseList);
        when(courseMapper.selectByFiltercount2(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(1L);
        when(courseMapper.selectCategoriesByCourseId(1L)).thenReturn(categories);
        
        // 执行测试
        PageResult<Course> result = courseService.getCoursesReady("Java", 60, "author1", "summary1", "content1", "Programming", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(0, result.getList().get(0).getStatus());
        
        // 验证方法调用
        verify(courseMapper).selectByFilter2("Java", 60, "author1", "summary1", "content1", "Programming", 10, 0);
        verify(courseMapper).selectByFiltercount2("Java", 60, "author1", "summary1", "content1", "Programming");
    }
    
    @Test
    void testGetCourseByAuthor_Success() {
        // 准备测试数据
        List<Course> courseList = Arrays.asList(
            new Course(1L, "My Course", "video1", null, 60, "user", "summary1", "content1", 10, null, 0)
        );
        List<courseCategory> categories = Arrays.asList(new courseCategory(1L, "Programming"));
        
        // 模拟Mapper返回
        when(courseMapper.selectByStatusAndAuthor(0, "user", 10, 0)).thenReturn(courseList);
        when(courseMapper.selectByStatusAndAuthorCount(0, "user")).thenReturn(1L);
        when(courseMapper.selectCategoriesByCourseId(1L)).thenReturn(categories);
        
        // 执行测试
        PageResult<Course> result = courseService.getCourseByAuthor(0, "user", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals("user", result.getList().get(0).getAuthor());
        assertEquals(0, result.getList().get(0).getStatus());
        
        // 验证方法调用
        verify(courseMapper).selectByStatusAndAuthor(0, "user", 10, 0);
        verify(courseMapper).selectByStatusAndAuthorCount(0, "user");
    }
    
    @Test
    void testGetCourseById_Success() {
        // 准备测试数据
        Course course = new Course(1L, "Java Course", "video1", null, 60, "author1", "summary1", "content1", 10, null, 1);
        List<courseCategory> categories = Arrays.asList(new courseCategory(1L, "Programming"));
        
        // 模拟Mapper返回
        when(courseMapper.selectCourseById(1L)).thenReturn(course);
        when(courseMapper.selectCategoriesByCourseId(1L)).thenReturn(categories);
        
        // 执行测试
        Course result = courseService.getCourseById(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("Java Course", result.getTitle());
        assertEquals(1, result.getCategories().size());
        
        // 验证方法调用
        verify(courseMapper).selectCourseById(1L);
        verify(courseMapper).selectCategoriesByCourseId(1L);
    }
    
    @Test
    void testGetCourseById_NotFound() {
        // 模拟Mapper返回null
        when(courseMapper.selectCourseById(999L)).thenReturn(null);
        
        // 执行测试
        Course result = courseService.getCourseById(999L);
        
        // 验证结果
        assertNull(result);
        
        // 验证方法调用
        verify(courseMapper).selectCourseById(999L);
        verify(courseMapper, never()).selectCategoriesByCourseId(anyLong());
    }
    
    @Test
    void testAddCourse_Success() {
        // 准备测试数据
        Course course = new Course(null, "New Course", "video1", null, 60, "author1", "summary1", "content1", 0, null, 0);
        course.setId(1L);
        List<courseCategory> categories = Arrays.asList(new courseCategory(1L, "Programming"));
        course.setCategories(categories);
        
        // 模拟Mapper返回
        when(courseMapper.insertCourse(any(Course.class))).thenReturn(1);
        when(courseMapper.insertCourseCategoryMapping(anyLong(), anyLong())).thenReturn(1);
        
        // 执行测试
        int result = courseService.addCourse(course);
        
        // 验证结果
        assertEquals(1, result);
        
        // 验证方法调用
        verify(courseMapper).insertCourse(course);
        verify(courseMapper).insertCourseCategoryMapping(1L, 1L);
    }
    
    @Test
    void testAddCourse_WithoutCategories() {
        // 准备测试数据
        Course course = new Course(null, "New Course", "video1", null, 60, "author1", "summary1", "content1", 0, null, 0);
        course.setId(1L);
        
        // 模拟Mapper返回
        when(courseMapper.insertCourse(any(Course.class))).thenReturn(1);
        
        // 执行测试
        int result = courseService.addCourse(course);
        
        // 验证结果 - 根据CourseService逻辑，当categories为null时应该返回0
        assertEquals(0, result);
        
        // 验证方法调用
        verify(courseMapper).insertCourse(course);
        verify(courseMapper, never()).insertCourseCategoryMapping(anyLong(), anyLong());
    }
    
    @Test
    void testAddCourse_Failure() {
        // 准备测试数据
        Course course = new Course(null, "New Course", "video1", null, 60, "author1", "summary1", "content1", 0, null, 0);
        course.setId(1L);
        
        // 模拟Mapper返回失败
        when(courseMapper.insertCourse(any(Course.class))).thenReturn(0);
        
        // 执行测试
        int result = courseService.addCourse(course);
        
        // 验证结果
        assertEquals(0, result);
        
        // 验证方法调用
        verify(courseMapper).insertCourse(course);
        verify(courseMapper, never()).insertCourseCategoryMapping(anyLong(), anyLong());
    }
    
    @Test
    void testDeleteCourse_Success() {
        // 模拟Mapper返回
        when(courseMapper.deleteCourseCategoryMappings(1L)).thenReturn(2);
        when(courseMapper.deleteCourseById(1L)).thenReturn(1);
        
        // 执行测试
        int result = courseService.deleteCourse(1L);
        
        // 验证结果
        assertEquals(1, result);
        
        // 验证方法调用
        verify(courseMapper).deleteCourseCategoryMappings(1L);
        verify(courseMapper).deleteCourseById(1L);
    }
    
    @Test
    void testUpdateCourse_Success() {
        // 准备测试数据
        Course course = new Course(1L, "Updated Course", "video1", null, 60, "author1", "summary1", "content1", 10, null, 1);
        List<courseCategory> categories = Arrays.asList(new courseCategory(1L, "Programming"));
        course.setCategories(categories);
        
        // 模拟Mapper返回
        when(courseMapper.deleteCourseCategoryMappings(1L)).thenReturn(1);
        when(courseMapper.insertCourseCategoryMapping(anyLong(), anyLong())).thenReturn(1);
        when(courseMapper.updateCourse(any(Course.class))).thenReturn(1);
        
        // 执行测试
        int result = courseService.updateCourse(course);
        
        // 验证结果
        assertEquals(1, result);
        
        // 验证方法调用
        verify(courseMapper).deleteCourseCategoryMappings(1L);
        verify(courseMapper).insertCourseCategoryMapping(1L, 1L);
        verify(courseMapper).updateCourse(course);
    }
    
    @Test
    void testUpdateCourse_WithoutCategories() {
        // 准备测试数据
        Course course = new Course(1L, "Updated Course", "video1", null, 60, "author1", "summary1", "content1", 10, null, 1);
        
        // 模拟Mapper返回
        when(courseMapper.deleteCourseCategoryMappings(1L)).thenReturn(1);
        when(courseMapper.updateCourse(any(Course.class))).thenReturn(1);
        
        // 执行测试
        int result = courseService.updateCourse(course);
        
        // 验证结果
        assertEquals(1, result);
        
        // 验证方法调用
        verify(courseMapper).deleteCourseCategoryMappings(1L);
        verify(courseMapper, never()).insertCourseCategoryMapping(anyLong(), anyLong());
        verify(courseMapper).updateCourse(course);
    }
} 