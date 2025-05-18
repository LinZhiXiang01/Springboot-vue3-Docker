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

    @Select("select * from employee_profile where profile_id = #{profileId}")
    EmployeeProfile selectById(Integer profileId);


    void insert(EmployeeProfile employee);

    void updateByAuthId(EmployeeProfileUpdateDTO dto);

    @Delete("delete from employee_profile where profile_id = #{profileId}")
    void deleteById(Integer profileId);

    @Select("select * from employee_profile where name = #{name}")
    EmployeeProfile selectByUsername(String username);

    @Select("select auth_id,profile_id, name, sex, no, age, description, department_id, avatar from employee_profile where auth_id = #{authId}")
    EmployeeProfile selectByAuthId(Integer authId);
}
