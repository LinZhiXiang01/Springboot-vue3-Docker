package com.mocha.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mocha.springboot.entity.Employee;
import com.mocha.springboot.entity.Account;
import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.mapper.EmployeeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import utils.JwtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service//创建 Service 并且标注为 Springboot 里面的一个 bean
public class EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    //增
    public void add(Employee employee) {
        String username = employee.getUsername();

        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee != null){
            throw new CustomException("账号已存在，请更换",500);
        }

        if (StrUtil.isBlank(employee.getName())){ //名字没填，自动
            employee.setName(username);
        }
        if (StrUtil.isBlank(employee.getPassword())){ //密码没填，自动
            employee.setPassword("123");
        }

        employee.setRole("EMP"); //员工角色
        employeeMapper.insert(employee);
    }

    //删
    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids) {
            this.deleteById(id);
        }
    }

    //改
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public PageInfo<Employee> selectPage(Employee employee,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    //员工登录
    public Employee login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee == null){
            throw new CustomException("用户名不存在",500);
        }
        if(!dbEmployee.getPassword().equals(password)){
            throw new CustomException("用户名或密码错误",500);
        }

        //生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", dbEmployee.getId());
        claims.put("username", dbEmployee.getUsername());
        String jwt = JwtUtils.generateToken(claims);

        dbEmployee.setToken(jwt);
        return dbEmployee;
    }

    //员工注册
    public void register(Account registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword(password);

        this.add(employee);

    }

    public void updatePassword(Account updatePasswordRequest) {

        Integer id = updatePasswordRequest.getId();
        Employee employee = this.selectById(id);
        if(!employee.getPassword().equals( updatePasswordRequest.getPassword())){ //原密码错误，报错
            throw new CustomException("对不起，原密码错误",500);
        }
        employee.setPassword(updatePasswordRequest.getNewPassword());
        this.update(employee);

    }
}
