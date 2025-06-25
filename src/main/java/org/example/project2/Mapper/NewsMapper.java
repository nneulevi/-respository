package org.example.project2.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.project2.entity.News;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Select("SELECT * FROM news WHERE author=#{author}")
    List<News> selectByauthor(@Param("author") String author);
}
