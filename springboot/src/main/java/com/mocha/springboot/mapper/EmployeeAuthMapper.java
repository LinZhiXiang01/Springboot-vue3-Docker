package com.mocha.springboot.mapper;

import com.mocha.springboot.dto.EmployeeQueryDTO;
import com.mocha.springboot.entity.EmployeeAuth;
import com.mocha.springboot.vo.EmployeeInfoVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeAuthMapper {

    List<EmployeeAuth> selectAll(EmployeeAuth employee);

    List<EmployeeInfoVO> selectAllWithProfile(EmployeeQueryDTO employeeQueryDTO);

    @Select("select * from employee_auth where id = #{id}")
    EmployeeAuth selectById(Integer id);

    void insert(EmployeeAuth employee);

    void updateById(EmployeeAuth employee);

    @Delete("delete from employee_auth where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from employee_auth where username = #{username}")
    EmployeeAuth selectByUsername(String username);

    void updatePassword(EmployeeAuth employeeAuth);
}
