package com.mocha.springboot.controller;

import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.dto.RegisterDTO;
import com.mocha.springboot.dto.UpdatePasswordDTO;
import com.mocha.springboot.entity.LoginDTO;
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
    public ResultCode login(@RequestBody LoginDTO dto){

        LoginDTO result = null;
        if("ADMIN".equals(dto.getRole())){
            result =adminService.login(dto);

        }else if("EMP".equals(dto.getRole())){
            result =employeeService.login(dto);
        }
         return ResultCode.success(result);

    }

    @PostMapping("/register")
    public ResultCode register(@RequestBody RegisterDTO registerRequest){
        employeeService.register(registerRequest);
        return ResultCode.success();
    }

    @PutMapping("/updatePassword")
    public ResultCode updatePassword(@RequestBody UpdatePasswordDTO dto){
        if("ADMIN".equals( dto.getRole())){
//            adminService.updatePassword(dto);
        }else if("EMP".equals( dto.getRole())){
            employeeService.updatePassword(dto);
        }else{
            throw new CustomException("非法输入",500);
        }
        return ResultCode.success();
    }
}