package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.Course;
import org.example.project2.entity.courseCategory;

import java.util.List;

@Mapper
public interface courseMapper {
    @Select({
            "<script>",
            "SELECT DISTINCT c.id, c.title, c.videoUrl, c.coverImage, c.duration, c.author, c.summary, c.content, c.likes",
            "FROM course c",
            "LEFT JOIN course_category_mapping m ON c.id = m.courseId",
            "LEFT JOIN course_category cat ON m.categoryId = cat.id",
            "<where>",
            "  c.status = 1",
            "  <if test='title != null and title != \"\"'> AND c.title LIKE CONCAT('%', #{title}, '%')</if>",
            "  <if test='duration != null'> AND c.duration = #{duration}</if>",
            "  <if test='author != null and author != \"\"'> AND c.author LIKE CONCAT('%', #{author}, '%')</if>",
            "  <if test='summary != null and summary != \"\"'> AND c.summary LIKE CONCAT('%', #{summary}, '%')</if>",
            "  <if test='content != null and content != \"\"'> AND c.content LIKE CONCAT('%', #{content}, '%')</if>",
            "  <if test='categoryName != null and categoryName != \"\"'> AND cat.name LIKE CONCAT('%', #{categoryName}, '%')</if>",
            "</where>",
            "ORDER BY c.likes DESC",
            "LIMIT #{size} OFFSET #{offset}",
            "</script>"
    })
    List<Course> selectByFilter(@Param("title") String title,
                                @Param("duration") Integer duration,
                                @Param("author") String author,
                                @Param("summary") String summary,
                                @Param("content") String content,
                                @Param("categoryName") String categoryName,
                                @Param("size") int size,
                                @Param("offset") int offset);

    @Select({
            "<script>",
            "SELECT DISTINCT c.id, c.title, c.videoUrl, c.coverImage, c.duration, c.author, c.summary, c.content, c.likes",
            "FROM course c",
            "LEFT JOIN course_category_mapping m ON c.id = m.courseId",
            "LEFT JOIN course_category cat ON m.categoryId = cat.id",
            "<where>",
            "  c.status = 1",
            "  <if test='title != null and title != \"\"'> AND c.title LIKE CONCAT('%', #{title}, '%')</if>",
            "  <if test='duration != null'> AND c.duration = #{duration}</if>",
            "  <if test='author != null and author != \"\"'> AND c.author LIKE CONCAT('%', #{author}, '%')</if>",
            "  <if test='summary != null and summary != \"\"'> AND c.summary LIKE CONCAT('%', #{summary}, '%')</if>",
            "  <if test='content != null and content != \"\"'> AND c.content LIKE CONCAT('%', #{content}, '%')</if>",
            "  <if test='categoryName != null and categoryName != \"\"'> AND cat.name LIKE CONCAT('%', #{categoryName}, '%')</if>",
            "</where>",
            "ORDER BY c.likes DESC",
            "</script>"
    })
    Long selectByFiltercount(@Param("title") String title,
                             @Param("duration") Integer duration,
                             @Param("author") String author,
                             @Param("summary") String summary,
                             @Param("content") String content,
                             @Param("categoryName") String categoryName);

    @Select({
            "<script>",
            "SELECT DISTINCT c.id, c.title, c.videoUrl, c.coverImage, c.duration, c.author, c.summary, c.content, c.likes",
            "FROM course c",
            "LEFT JOIN course_category_mapping m ON c.id = m.courseId",
            "LEFT JOIN course_category cat ON m.categoryId = cat.id",
            "<where>",
            "  c.status = 0",
            "  <if test='title != null and title != \"\"'> AND c.title LIKE CONCAT('%', #{title}, '%')</if>",
            "  <if test='duration != null'> AND c.duration = #{duration}</if>",
            "  <if test='author != null and author != \"\"'> AND c.author LIKE CONCAT('%', #{author}, '%')</if>",
            "  <if test='summary != null and summary != \"\"'> AND c.summary LIKE CONCAT('%', #{summary}, '%')</if>",
            "  <if test='content != null and content != \"\"'> AND c.content LIKE CONCAT('%', #{content}, '%')</if>",
            "  <if test='categoryName != null and categoryName != \"\"'> AND cat.name LIKE CONCAT('%', #{categoryName}, '%')</if>",
            "</where>",
            "ORDER BY c.likes DESC",
            "LIMIT #{size} OFFSET #{offset}",
            "</script>"
    })
    List<Course> selectByFilter2(@Param("title") String title,
                                 @Param("duration") Integer duration,
                                 @Param("author") String author,
                                 @Param("summary") String summary,
                                 @Param("content") String content,
                                 @Param("categoryName") String categoryName,
                                 @Param("size") int size,
                                 @Param("offset") int offset);

    @Select({
            "<script>",
            "SELECT DISTINCT c.id, c.title, c.videoUrl, c.coverImage, c.duration, c.author, c.summary, c.content, c.likes",
            "FROM course c",
            "LEFT JOIN course_category_mapping m ON c.id = m.courseId",
            "LEFT JOIN course_category cat ON m.categoryId = cat.id",
            "<where>",
            "  c.status = 0",
            "  <if test='title != null and title != \"\"'> AND c.title LIKE CONCAT('%', #{title}, '%')</if>",
            "  <if test='duration != null'> AND c.duration = #{duration}</if>",
            "  <if test='author != null and author != \"\"'> AND c.author LIKE CONCAT('%', #{author}, '%')</if>",
            "  <if test='summary != null and summary != \"\"'> AND c.summary LIKE CONCAT('%', #{summary}, '%')</if>",
            "  <if test='content != null and content != \"\"'> AND c.content LIKE CONCAT('%', #{content}, '%')</if>",
            "  <if test='categoryName != null and categoryName != \"\"'> AND cat.name LIKE CONCAT('%', #{categoryName}, '%')</if>",
            "</where>",
            "ORDER BY c.likes DESC",
            "</script>"
    })
    Long selectByFiltercount2(@Param("title") String title,
                             @Param("duration") Integer duration,
                             @Param("author") String author,
                             @Param("summary") String summary,
                             @Param("content") String content,
                             @Param("categoryName") String categoryName);

    @Select("SELECT cat.id, cat.name FROM course_category cat " +
            "JOIN course_category_mapping m ON cat.id = m.categoryId " +
            "WHERE m.courseId = #{courseId}")
    List<courseCategory> selectCategoriesByCourseId(@Param("courseId") Long courseId);

    @Select("SELECT * FROM course WHERE id = #{id}")
    Course selectCourseById(@Param("id") Long id);

    @Insert("INSERT INTO course (title, videoUrl, coverImage, duration, author, summary, content, likes,status) " +
            "VALUES (#{title}, #{videoUrl}, #{coverImage}, #{duration}, #{author}, #{summary}, #{content}, #{likes}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCourse(Course course);

    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteCourseById(@Param("id") Long id);

    @Delete("DELETE FROM course_category_mapping WHERE courseId = #{courseId}")
    int deleteCourseCategoryMappings(@Param("courseId") Long courseId);

    @Update("UPDATE course SET title=#{title}, videoUrl=#{videoUrl}, coverImage=#{coverImage}, duration=#{duration}, author=#{author}, summary=#{summary}, content=#{content} WHERE id=#{id}")
    int updateCourse(Course course);

    @Update("update course set likes = likes + 1 where id=#{id}")
    int updateLikes(@Param("id") long id);

    @Update("update course SET status = 1 where id=#{id}")
    int updateStatus(@Param("id") long id);

    @Update("update course set status = 2 where id=#{id}")
    int refuseStatus(@Param("id") long id);

    @Insert("INSERT INTO course_category_mapping(courseId, categoryId) VALUES(#{courseId}, #{categoryId})")
    int insertCourseCategoryMapping(@Param("courseId") Long courseId, @Param("categoryId") Long categoryId);

    @Insert("INSERT INTO ready_course (title, videoUrl, coverImage, duration, author, summary, content, likes) " +
            "VALUES (#{title}, #{videoUrl}, #{coverImage}, #{duration}, #{author}, #{summary}, #{content}, #{likes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertToReady_course(Course course);

    @Select({
            "<script>",
            "SELECT DISTINCT c.id, c.title, c.videoUrl, c.coverImage, c.duration, c.author, c.summary, c.content, c.likes",
            "FROM course c",
            "LEFT JOIN course_category_mapping m ON c.id = m.courseId",
            "LEFT JOIN course_category cat ON m.categoryId = cat.id",
            "<where>",
            "  <if test='status != null'>AND c.status = #{status}</if>",
            "  <if test='author != null and author != \"\"'>AND c.author = #{author}</if>",
            "</where>",
            "ORDER BY c.likes DESC",
            "LIMIT #{size} OFFSET #{offset}",
            "</script>"
    })
    List<Course> selectByStatusAndAuthor(@Param("status") Integer status,
                                         @Param("author") String author,@Param("size") int size,
                                         @Param("offset") int offset);

    @Select({
            "<script>",
            "SELECT DISTINCT c.id, c.title, c.videoUrl, c.coverImage, c.duration, c.author, c.summary, c.content, c.likes",
            "FROM course c",
            "LEFT JOIN course_category_mapping m ON c.id = m.courseId",
            "LEFT JOIN course_category cat ON m.categoryId = cat.id",
            "<where>",
            "  <if test='status != null'>AND c.status = #{status}</if>",
            "  <if test='author != null and author != \"\"'>AND c.author = #{author}</if>",
            "</where>",
            "ORDER BY c.likes DESC",
            "</script>"
    })
    Long selectByStatusAndAuthorCount(@Param("status") Integer status,
                                         @Param("author") String author);

    @Select("select author from course where id=#{id}")
    String getAuthor(@Param("id") long id);
}
