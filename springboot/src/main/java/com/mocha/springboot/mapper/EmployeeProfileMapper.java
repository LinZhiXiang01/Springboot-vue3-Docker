package com.mocha.springboot.mapper;

import com.mocha.springboot.dto.EmployeeProfileUpdateDTO;
import com.mocha.springboot.entity.EmployeeProfile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeProfileMapper {

    List<EmployeeProfile> selectAll(EmployeeProfile employee);

    @Select("select * from employee_profile where id = #{id}")
    EmployeeProfile selectById(Integer id);


    void insert(EmployeeProfile employee);

    void updateById(EmployeeProfileUpdateDTO dto);

    @Delete("delete from employee_profile where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from employee_profile where name = #{name}")
    EmployeeProfile selectByUsername(String username);

    @Select("select id, auth_id, name, sex, no, age, description, department_id, avatar from employee_profile where auth_id = #{authId}")
    EmployeeProfile selectByAuthId(Integer authId);
}
