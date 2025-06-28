package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.User;
import org.example.project2.entity.User_d;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {//由于List的原因，使用User_d与数据库匹配，列表展示也以User_d的形式，在User个人主页中查询News表拼成List展示

    @Insert("""
        INSERT INTO users(username, password, phone, email, gender, status, creatTime, avatar_url, enterprise)
        VALUES (#{username}, #{password}, #{phone}, #{email}, #{gender}, #{status}, #{creatTime}, #{avatar_url}, #{enterprise})
    """)
    int insert(User_d user);

    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    User_d findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User_d findByUsername(@Param("username") String username);

    @Select({
            "<script>",
            "SELECT * FROM users",
            "<where>",
            "  <if test='username != null and username != \"\"'>",
            "    AND username LIKE CONCAT('%', #{username}, '%')",
            "  </if>",
            "  <if test='phone != null and phone != \"\"'>",
            "    AND phone LIKE CONCAT('%', #{phone}, '%')",
            "  </if>",
            "  <if test='email != null and email != \"\"'>",
            "    AND email LIKE CONCAT('%', #{email}, '%')",
            "  </if>",
            "  <if test='gender != null and gender != \"\"'>",
            "    AND gender LIKE CONCAT('%', #{gender}, '%')",
            "  </if>",
            "  <if test='startDate != null and endDate != null'>",
            "    AND creatTime BETWEEN #{startDate} AND #{endDate}",
            "  </if>",
            "</where>",
            "ORDER BY username",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<User_d> findByKeyword(
            @Param("username") String username,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("gender") String gender,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("offset") int offset,
            @Param("limit") int limit);

    @Update("update users set username=#{username},password=#{password},email=#{email},phone=#{phone},status=#{status},avatar_url=#{avatar_url},enterprise=#{enterprise} where username=#{old_username}")
    int change_info(User_d user,@Param("old_username") String old_username);

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM users",
            "<where>",
            "  <if test='username != null and username != \"\"'>",
            "    AND username LIKE CONCAT('%', #{username}, '%')",
            "  </if>",
            "  <if test='phone != null and phone != \"\"'>",
            "    AND phone LIKE CONCAT('%', #{phone}, '%')",
            "  </if>",
            "  <if test='email != null and email != \"\"'>",
            "    AND email LIKE CONCAT('%', #{email}, '%')",
            "  </if>",
            "  <if test='gender != null and gender != \"\"'>",
            "    AND gender LIKE CONCAT('%', #{gender}, '%')",
            "  </if>",
            "  <if test='startDate != null and endDate != null'>",
            "    AND creatTime BETWEEN #{startDate} AND #{endDate}",
            "  </if>",
            "</where>",
            "</script>"
    })
    Long count(@Param("username") String username,
               @Param("phone") String phone,
               @Param("email") String email,
               @Param("gender") String gender,
               @Param("startDate") LocalDate startDate,
               @Param("endDate") LocalDate endDate);

    //User个人主页待补充
}
