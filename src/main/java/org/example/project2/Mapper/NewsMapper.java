package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.News;
import org.example.project2.entity.User_d;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface NewsMapper {

    @Select("SELECT * FROM news WHERE author=#{author}")
    List<News> selectByauthor(@Param("author") String author);

    @Select("SELECT * FROM news WHERE title=#{title}")
    News selectBytitle(@Param("title") String title);

    @Select("select * from news where id=#{id}")
    News selectById(@Param("id") Long id);

    @Insert("""
         INSERT INTO ready_news(id,title,summary,content,coverImage,author,viewCount,publishTime,tags)
         VALUES (#{id},#{title},#{summary},#{content},#{coverImage},#{author},#{viewCount},#{publishtTime},#{tags})
""")
    int submit(News news);

    @Insert("""
         INSERT INTO news(id,title,summary,content,coverImage,author,viewCount,publishTime,tags)
         VALUES (#{id},#{title},#{summary},#{content},#{coverImage},#{author},#{viewCount},#{publishtTime},#{tags})
""")
    int insert(News news);

    @Select("SELECT * FROM ready_news ORDER BY publishTime DESC LIMIT #{limit} OFFSET #{offset}")
    List<News> selectAll(@Param("offset") int offset,
                         @Param("limit") int limit);

    @Select("select COUNT(*) from ready_news")
    Long count2();

    @Update("update news set id=#{id},title=#{title},summary=#{summary},content=#{content},coverImage=#{coverImage},author=#{author},tags=#{tags} where id=#{old_id}")
    int modify(News news,@Param("old_id") Long old_id);

    @Update("update news set viewCount=#{viewCount} where title=#{title}")
    int update(@Param("viewCount") int viewCount,@Param("title") String title);

    @Select({
            "<script>",
            "SELECT * FROM news",
            "<where>",
            "  <if test='id != null'>",
            "    AND id = #{id}",
            "  </if>",
            "  <if test='title != null and title != \"\"'>",
            "    AND title LIKE CONCAT('%', #{title}, '%')",
            "  </if>",
            "  <if test='summary != null and summary != \"\"'>",
            "    AND summary LIKE CONCAT('%', #{summary}, '%')",
            "  </if>",
            "  <if test='content != null and content != \"\"'>",
            "    AND content LIKE CONCAT('%', #{content}, '%')",
            "  </if>",
            "  <if test='coverImage != null and coverImage != \"\"'>",
            "    AND coverImage LIKE CONCAT('%', #{coverImage}, '%')",
            "  </if>",
            "  <if test='author != null and author != \"\"'>",
            "    AND author LIKE CONCAT('%', #{author}, '%')",
            "  </if>",
            "  <if test='tags != null and tags != \"\"'>",
            "    AND tags LIKE CONCAT('%', #{tags}, '%')",
            "  </if>",
            "  <if test='startDate != null and endDate != null'>",
            "    AND publishTime BETWEEN #{startDate} AND #{endDate}",
            "  </if>",
            "</where>",
            "ORDER BY publishTime DESC",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<News> findNewsByKeyword(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("summary") String summary,
            @Param("content") String content,
            @Param("coverImage") String coverImage,
            @Param("author") String author,
            @Param("tags") String tags,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Select({
            "<script>",
            "SELECT * FROM news",
            "<where>",
            "  <if test='id != null'>",
            "    AND id = #{id}",
            "  </if>",
            "  <if test='title != null and title != \"\"'>",
            "    AND title LIKE CONCAT('%', #{title}, '%')",
            "  </if>",
            "  <if test='summary != null and summary != \"\"'>",
            "    AND summary LIKE CONCAT('%', #{summary}, '%')",
            "  </if>",
            "  <if test='content != null and content != \"\"'>",
            "    AND content LIKE CONCAT('%', #{content}, '%')",
            "  </if>",
            "  <if test='coverImage != null and coverImage != \"\"'>",
            "    AND coverImage LIKE CONCAT('%', #{coverImage}, '%')",
            "  </if>",
            "  <if test='author != null and author != \"\"'>",
            "    AND author LIKE CONCAT('%', #{author}, '%')",
            "  </if>",
            "  <if test='tags != null and tags != \"\"'>",
            "    AND tags LIKE CONCAT('%', #{tags}, '%')",
            "  </if>",
            "  <if test='startDate != null and endDate != null'>",
            "    AND publishTime BETWEEN #{startDate} AND #{endDate}",
            "  </if>",
            "</where>",
            "ORDER BY publishTime DESC",
            "</script>"
    })
    Long count(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("summary") String summary,
            @Param("content") String content,
            @Param("coverImage") String coverImage,
            @Param("author") String author,
            @Param("tags") String tags,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Delete("delete from news where id=#{id}")
    int deleteById(@Param("id") long id);
}
