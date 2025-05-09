package com.mocha.springboot.mapper;

import com.mocha.springboot.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    @Select("select * from admin where auth_id = #{authId}")
    Admin selectById(Integer authId);


    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("delete from admin where auth_id = #{authId}")
    void deleteById(Integer authId);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
}
