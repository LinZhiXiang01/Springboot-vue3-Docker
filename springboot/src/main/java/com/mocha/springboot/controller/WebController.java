package com.mocha.springboot.controller;

import com.mocha.springboot.common.Result;
import com.mocha.springboot.entity.Account;
import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.service.AdminService;
import com.mocha.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;


    //管理员 或者 员工 登录
    @PostMapping("/login")
    public Result login(@RequestBody Account account){

        Account result = null;
        if("ADMIN".equals(account.getRole())){
            result =adminService.   login(account);

        }else if("EMP".equals(account.getRole())){
            result =employeeService.login(account);
        }
         return Result.success(result);

    }

    @PostMapping("/register")
    public Result register(@RequestBody Account registerRequest){
        employeeService.register(registerRequest);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account updatePasswordRequest){
        if("ADMIN".equals( updatePasswordRequest.getRole())){
            adminService.updatePassword(updatePasswordRequest);
        }else if("EMP".equals( updatePasswordRequest.getRole())){
            employeeService.updatePassword(updatePasswordRequest);
        }else{
            throw new CustomException("非法输入",500);
        }
        return Result.success();
    }
}