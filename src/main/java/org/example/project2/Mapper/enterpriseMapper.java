package org.example.project2.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.project2.entity.Enterprise;

import java.util.List;

@Mapper
public interface enterpriseMapper {
    @Select("SELECT * FROM enterprise WHERE id = #{id}")
    Enterprise findByid(@Param("id") Long id);

    //传参时直接把audisStatus设为1即可,公司的注册部分
    @Insert("""
        INSERT INTO enterprise(id, name, licenseNumber, contactPerson, contactPhone, audiStatus)
        VALUES (#{id}, #{name}, #{licenseNumber}, #{contactPerson}, #{contactPhone}, #{audiStatus})
    """)
    int insert(Enterprise enterprise);

    @Select({
            "<script>",
            "SELECT * FROM enterprise",
            "<where>",
            "  <if test='id != null'>",
            "    AND CAST(id AS VARCHAR) LIKE CONCAT('%', #{id}, '%')",
            "  </if>",
            "  <if test='name != null and name != \"\"'>",
            "    AND name LIKE CONCAT('%', #{name}, '%')",
            "  </if>",
            "  <if test='phone != null and phone != \"\"'>",
            "    AND phone LIKE CONCAT('%', #{phone}, '%')",
            "  </if>",
            "  <if test='licenseNumber != null and licenseNumber != \"\"'>",
            "    AND licenseNumber LIKE CONCAT('%', #{licenseNumber}, '%')",
            "  </if>",
            "  <if test='contactPerson != null and contactPerson != \"\"'>",
            "    AND contactPerson LIKE CONCAT('%', #{contactPerson}, '%')",
            "  </if>",
            "  <if test='contactPhone != null and contactPhone != \"\"'>",
            "    AND contactPhone LIKE CONCAT('%', #{contactPhone}, '%')",
            "  </if>",
            "</where>",
            "ORDER BY id",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<Enterprise> getAllEnterprise(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("licenseNumber") String licenseNumber,
            @Param("contactPerson") String contactPerson,
            @Param("contactPhone") String contactPhone,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM enterprise",
            "<where>",
            "  <if test='id != null'>",
            "    AND CAST(id AS VARCHAR) LIKE CONCAT('%', #{id}, '%')",
            "  </if>",
            "  <if test='name != null and name != \"\"'>",
            "    AND name LIKE CONCAT('%', #{name}, '%')",
            "  </if>",
            "  <if test='phone != null and phone != \"\"'>",
            "    AND phone LIKE CONCAT('%', #{phone}, '%')",
            "  </if>",
            "  <if test='licenseNumber != null and licenseNumber != \"\"'>",
            "    AND lisenceNumber LIKE CONCAT('%', #{lisenceNumber}, '%')",
            "  </if>",
            "  <if test='contactPerson != null and contactPerson != \"\"'>",
            "    AND contactPerson LIKE CONCAT('%', #{contactPerson}, '%')",
            "  </if>",
            "  <if test='contactPhone != null and contactPhone != \"\"'>",
            "    AND contactPhone LIKE CONCAT('%', #{contactPhone}, '%')",
            "  </if>",
            "</where>",
            "ORDER BY id",
            "</script>"
    })
    Long count(@Param("id") Long id,
               @Param("name") String name,
               @Param("phone") String phone,
               @Param("licenseNumber") String licenseNumber,
               @Param("contactPerson") String contactPerson,
               @Param("contactPhone") String contactPhone);
}
