package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.Agenda;

import java.util.List;

//子议程
@Mapper
public interface agendaMapper {
    @Insert("insert into agenda(meeting_id, title, speaker, startTime, duration, content)" +
    "values (#{meeting_id},#{title},#{speaker},#{startTime},#{duration},#{content})")
    int insert(Agenda agenda);

    @Select("SELECT * FROM agenda WHERE id = #{id}")
    Agenda findById(@Param("id") long id);

    @Select("SELECT * FROM agenda WHERE meeting_id = #{meeting_id} " +
            "ORDER BY startTime DESC LIMIT #{limit} OFFSET #{offset}")
    List<Agenda> findBystartTime(
            @Param("meeting_id") long id,
            @Param("limit") int limit,
            @Param("offset") int offset);

    @Select("SELECT * FROM agenda WHERE meeting_id = #{meeting_id} " +
            "ORDER BY duration DESC LIMIT #{limit} OFFSET #{offset}")
    List<Agenda> findByDuration(
            @Param("meeting_id") long id,
            @Param("limit") int limit,
            @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM agenda WHERE meeting_id = #{meeting_id}")
    Long countByMeetingsId(@Param("meeting_id") long meeting_id);

    @Delete("DELETE FROM agenda WHERE id = #{id}")
    int deleteById(@Param("id") long id);

    @Delete("delete from agenda where meeting_id=#{meeting_id}")
    int deleteByMeetingId(@Param("meeting_id") long meeting_id);

    @Select("SELECT speaker FROM agenda WHERE id = #{id}")
    String findAuthorById(@Param("id") long id);

    @Update("update agenda set title=#{agenda.title},speaker=#{agenda.speaker},startTime=#{agenda.startTime},duration=#{agenda.duration},content=#{agenda.content} where id=#{id}")
    int updateAgenda(@Param("id") long id,@Param("agenda") Agenda agenda);

}
