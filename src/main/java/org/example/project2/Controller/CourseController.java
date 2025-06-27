package org.example.project2.Controller;

import org.example.project2.Mapper.courseMapper;
import org.example.project2.Service.CourseService;
import org.example.project2.entity.Course;
import org.example.project2.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CourseController {

    @Autowired
    courseMapper courseMapper;
    @Autowired
    CourseService courseService;
    @RequestMapping("/getCoursesByFilter")//展示所有已经通过审核的课程
    public ResponseEntity<PageResult<Course>> getCourses(@RequestParam(required = false) String title,
                                                         @RequestParam(required = false) Integer duration,
                                                         @RequestParam(required = false) String author,
                                                         @RequestParam(required = false) String summary,
                                                         @RequestParam(required = false) String content,
                                                         @RequestParam(required = false) String categoryName,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        PageResult<Course> result = courseService.getCourses(title, duration, author, summary, content, categoryName, page, size);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/unpassed_courses")//对管理员可见，展示待审核的课程
    public ResponseEntity<PageResult<Course>> getCoursesReady(@RequestParam(required = false) String title,
                                                              @RequestParam(required = false) Integer duration,
                                                              @RequestParam(required = false) String author,
                                                              @RequestParam(required = false) String summary,
                                                              @RequestParam(required = false) String content,
                                                              @RequestParam(required = false) String categoryName,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size){
        PageResult<Course> result = courseService.getCoursesReady(title, duration, author, summary, content, categoryName, page, size);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/getCourse")
    public ResponseEntity<Course> getCourse(@RequestParam long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @RequestMapping("/submit_course")//普通用户调用
    public ResponseEntity<Integer> submit(@RequestBody Course course) {
        course.setStatus(0);
        int result = courseService.addCourse(course);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/commit_course")//管理员调用
    public ResponseEntity<Integer> commit(@RequestBody Course course) {
        course.setStatus(1);
        int result = courseService.addCourse(course);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/passCourse")// 对于展示出来的course，传id把该项改为1，意为通过审核
    public ResponseEntity<Integer> pass(@RequestParam(required = false) long id){
        int result = courseMapper.updateStatus(id);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/updateCourse")//用户或者管理员修改课程信息
    public ResponseEntity<Integer> update(@RequestBody Course course){
        int ans = courseService.updateCourse(course);
        return ResponseEntity.ok(ans);
    }

    @RequestMapping("/refuseCourse")//管理员使课程不通过或用户删除课程都可以使用
    public ResponseEntity<Integer> refuse(@RequestParam(required = false) long id){
        int result = courseService.deleteCourse(id);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }
}
