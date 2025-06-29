package org.example.project2.Service;

import org.example.project2.Mapper.courseMapper;
import org.example.project2.entity.Course;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.courseCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    courseMapper courseMapper;

    public PageResult<Course> getCourses(String title, Integer duration, String author,
                                         String summary, String content, String categoryName,
                                         int page, int size) {
        int offset = (page - 1) * size;
        List<Course> courses = courseMapper.selectByFilter(title, duration, author, summary, content, categoryName, size, offset);
        for (Course course : courses) {
            List<courseCategory> categories = courseMapper.selectCategoriesByCourseId(course.getId());
            course.setCategories(categories);
        }
        Long total = courseMapper.selectByFiltercount(title, duration, author, summary, content, categoryName);
        return new PageResult<>(page,size,total,courses);
    }

    public PageResult<Course> getCoursesReady(String title, Integer duration, String author,
                                         String summary, String content, String categoryName,
                                         int page, int size) {
        int offset = (page - 1) * size;
        List<Course> courses = courseMapper.selectByFilter2(title, duration, author, summary, content, categoryName, size, offset);
        for (Course course : courses) {
            List<courseCategory> categories = courseMapper.selectCategoriesByCourseId(course.getId());
            course.setCategories(categories);
        }
        Long total = courseMapper.selectByFiltercount2(title, duration, author, summary, content, categoryName);
        return new PageResult<>(page,size,total,courses);
    }

    public PageResult<Course> getCourseByAuthor(Integer status,String author,int page,int size){
        int offset = (page - 1) * size;
        List<Course> courses = courseMapper.selectByStatusAndAuthor(status,author,size,offset);
        for (Course course : courses) {
            List<courseCategory> categories = courseMapper.selectCategoriesByCourseId(course.getId());
            course.setCategories(categories);
        }
        Long total = courseMapper.selectByStatusAndAuthorCount(status,author);
        return new PageResult<>(page,size,total,courses);
    }

    public Course getCourseById(Long id) {
        Course course = courseMapper.selectCourseById(id);
        if (course != null) {
            List<courseCategory> categories = courseMapper.selectCategoriesByCourseId(id);
            course.setCategories(categories);
        }
        return course;
    }

    @Transactional
    public int addCourse(Course course) {
        int rows = courseMapper.insertCourse(course);
        if (rows > 0 && course.getCategories() != null) {
            for (courseCategory cat : course.getCategories()) {
                courseMapper.insertCourseCategoryMapping(course.getId(), cat.getId());
            }
            return 1;
        }
        return 0;
    }

    @Transactional
    public int deleteCourse(Long id) {
        courseMapper.deleteCourseCategoryMappings(id);
        int rows = courseMapper.deleteCourseById(id);
        return rows;
    }

    @Transactional
    public int updateCourse(Course course) {
        // 先删除原分类映射
        courseMapper.deleteCourseCategoryMappings(course.getId());
        // 插入新分类映射
        if (course.getCategories() != null && !course.getCategories().isEmpty()) {
            for (courseCategory cat : course.getCategories()) {
                courseMapper.insertCourseCategoryMapping(course.getId(), cat.getId());
            }
        }
        int rows = courseMapper.updateCourse(course);
        return rows;
    }
}
