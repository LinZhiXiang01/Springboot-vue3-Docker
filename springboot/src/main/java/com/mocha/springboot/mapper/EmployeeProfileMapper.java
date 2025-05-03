package com.mocha.springboot.mapper;

import com.mocha.springboot.entity.EmployeeProfile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeProfileMapper {
    List<EmployeeProfile> selectAll(EmployeeProfile employee);

    @Select("select * from employee_profile where id = #{id}")
    EmployeeProfile selectById(Integer id);


    void insert(EmployeeProfile employee);

    void updateById(EmployeeProfile employee);

    @Delete("delete from employee_profile where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from employee_profile where name = #{name}")
    EmployeeProfile selectByUsername(String username);

    @Select("select * from employee_profile where auth_id = #{authId}")
    EmployeeProfile selectByAuthId(Integer id);
}
