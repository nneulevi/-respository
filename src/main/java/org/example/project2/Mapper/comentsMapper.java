package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.coments;

import java.util.List;

@Mapper
public interface comentsMapper {
    @Insert("INSERT INTO comments(news_id, content, createTime, likes) " +
            "VALUES(#{news_id}, #{content}, #{createTime}, #{likes})")
    int insert(coments comment);

    // 按时间倒序查询某条新闻的评论（分页）
    @Select("SELECT * FROM comments WHERE news_id = #{news_id} " +
            "ORDER BY createTime DESC LIMIT #{limit} OFFSET #{offset}")
    List<coments> findByNewsIdOrderByTime(
            @Param("news_id") long news_id,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    // 按点赞数倒序查询某条新闻的评论（分页）
    @Select("SELECT * FROM comments WHERE news_id = #{news_id} " +
            "ORDER BY likes DESC, createTime DESC LIMIT #{limit} OFFSET #{offset}")
    List<coments> findByNewsIdOrderByLikes(
            @Param("news_id") long news_id,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Select("SELECT COUNT(*) FROM coments WHERE news_id = #{news_id}")
    long countByNewsId(@Param("news_id") long news_id);

    // 删除指定评论
    @Delete("DELETE FROM comments WHERE id = #{id}")
    int deleteById(@Param("id") long id);

    // 删除某篇新闻的所有评论
    @Delete("DELETE FROM comments WHERE news_id = #{news_id}")
    int deleteByNewsId(@Param("news_id") long news_id);

    @Update("UPDATE comments SET likes = likes + 1 WHERE id = #{id}")
    int likeComment(@Param("id") long id);

    @Select("SELECT author FROM coments WHERE id = #{id}")
    String findAuthorById(@Param("id") long id);
}
