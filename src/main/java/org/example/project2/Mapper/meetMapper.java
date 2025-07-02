package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.Meeting;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface meetMapper {
    @Select({
            "<script>",
            "SELECT * FROM meeting",
            "<where>",
            "  status = 1",
            "  <if test='id != null'>",
            "    AND id = #{id}",
            "  </if>",
            "  <if test='title != null and title != \"\"'>",
            "    AND title LIKE CONCAT('%', #{title}, '%')",
            "  </if>",
            "  <if test='content != null and content != \"\"'>",
            "    AND content LIKE CONCAT('%', #{content}, '%')",
            "  </if>",
            "  <if test='creator != null and creator != \"\"'>",
            "    AND creator LIKE CONCAT('%', #{creator}, '%')",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND startTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND endTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "</where>",
            "ORDER BY startTime DESC",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<Meeting> findMeetingsByKeyword(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("content") String content,
            @Param("creator") String creator,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM meeting",
            "<where>",
            "  status = 1",
            "  <if test='id != null'>",
            "    AND id = #{id}",
            "  </if>",
            "  <if test='title != null and title != \"\"'>",
            "    AND title LIKE CONCAT('%', #{title}, '%')",
            "  </if>",
            "  <if test='content != null and content != \"\"'>",
            "    AND content LIKE CONCAT('%', #{content}, '%')",
            "  </if>",
            "  <if test='creator != null and creator != \"\"'>",
            "    AND creator LIKE CONCAT('%', #{creator}, '%')",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND startTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND endTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "</where>",
            "ORDER BY startTime DESC",
            "</script>"
    })
    Long findMeetingsByKeywordCount(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("content") String content,
            @Param("creator") String creator,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Select({
            "<script>",
            "SELECT * FROM meeting",
            "<where>",
            "  status = 0",
            "  <if test='id != null'>",
            "    AND id = #{id}",
            "  </if>",
            "  <if test='title != null and title != \"\"'>",
            "    AND title LIKE CONCAT('%', #{title}, '%')",
            "  </if>",
            "  <if test='content != null and content != \"\"'>",
            "    AND content LIKE CONCAT('%', #{content}, '%')",
            "  </if>",
            "  <if test='creator != null and creator != \"\"'>",
            "    AND creator LIKE CONCAT('%', #{creator}, '%')",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND startTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND endTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "</where>",
            "ORDER BY startTime DESC",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<Meeting> findMeetingsNotPassed(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("content") String content,
            @Param("creator") String creator,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM meeting",
            "<where>",
            "  status = 0",
            "  <if test='id != null'>",
            "    AND id = #{id}",
            "  </if>",
            "  <if test='title != null and title != \"\"'>",
            "    AND title LIKE CONCAT('%', #{title}, '%')",
            "  </if>",
            "  <if test='content != null and content != \"\"'>",
            "    AND content LIKE CONCAT('%', #{content}, '%')",
            "  </if>",
            "  <if test='creator != null and creator != \"\"'>",
            "    AND creator LIKE CONCAT('%', #{creator}, '%')",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND startTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "  <if test='startTime != null and endTime != null'>",
            "    AND endTime BETWEEN #{startTime} AND #{endTime}",
            "  </if>",
            "</where>",
            "ORDER BY startTime DESC",
            "</script>"
    })
    Long findMeetingsNotPassedCount(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("content") String content,
            @Param("creator") String creator,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Select("select * from meeting where id=#{id}")
    Meeting findById(@Param("id") long id);

    @Insert("insert into meeting(title, coverImage, address, startTime, endTime, content, creator,status)" +
            "values (#{title},#{coverImage},#{address},#{startTime},#{endTime},#{content},#{creator},#{status})")
    int insert(Meeting meeting);

    @Update({
            "<script>",
            "UPDATE meeting",
            "<set>",
            "  <if test='meeting.title != null'> title = #{meeting.title},</if>",
            "  <if test='meeting.coverImage != null'> coverImage = #{meeting.coverImage},</if>",
            "  <if test='meeting.address != null'> address = #{meeting.address},</if>",
            "  <if test='meeting.startTime != null'> startTime = #{meeting.startTime},</if>",
            "  <if test='meeting.endTime != null'> endTime = #{meeting.endTime},</if>",
            "  <if test='meeting.content != null'> content = #{meeting.content},</if>",
            "  <if test='meeting.creator != null'> creator = #{meeting.creator},</if>",
            "</set>",
            "WHERE id = #{meeting.id}",
            "</script>"
    })
    int modify(@Param("meeting") Meeting meeting);

    @Delete("delete from meeting where id=#{id}")
    int deleteById(@Param("id") long id);

    @Select({
            "<script>",
            "SELECT * FROM meeting",
            "<where>",
            "  <if test='status != null'>AND status = #{status}</if>",
            "  <if test='creator != null and creator != \"\"'>AND creator= #{creator}</if>",
            "</where>",
            "ORDER BY startTime DESC",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<Meeting> selectByStatusAndAuthor(@Param("status") Integer status,
                                          @Param("creator") String creator,@Param("limit") int limit,
                                          @Param("offset") int offset);

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM meeting",
            "<where>",
            "  <if test='status != null'>AND status = #{status}</if>",
            "  <if test='creator != null and creator != \"\"'>AND creator= #{creator}</if>",
            "</where>",
            "ORDER BY startTime DESC",
            "</script>"
    })
    Long selectByStatusAndAuthorCount(@Param("status") Integer status,
                                          @Param("creator") String creator);

    @Select("select creator from meeting where id=#{id}")
    String getCreator(@Param("id") long id);

    @Update("update meeting set status = 1 where id=#{id}")
    int updateStatus(@Param("id") long id);

    @Update("update meeting set status = 2 where id=#{id}")
    int refuseStatus(@Param("id") long id);
}
