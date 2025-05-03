package com.mocha.springboot.controller;

import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.dto.RegisterDTO;
import com.mocha.springboot.dto.UpdatePasswordDTO;
import com.mocha.springboot.dto.LoginDTO;
import com.mocha.springboot.entity.EmployeeAuth;
import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.service.AdminService;
import com.mocha.springboot.service.EmployeeService;
import com.mocha.springboot.vo.LoginVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;

    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");


    //管理员 或者 员工 登录
    @PostMapping("/login")
    public ResultCode login(@RequestBody LoginDTO dto, HttpServletRequest request){

        LoginVO result = new LoginVO();
        if("ADMIN".equals(dto.getRole())){
//            result =adminService.login(dto);

        }else if("EMP".equals(dto.getRole())){
            result = employeeService.login(dto,request);
        }else{
            throw new CustomException("请输入role",500);
        }
         return ResultCode.success(result);
    }

    @PostMapping("/register")
    public ResultCode register(@RequestBody RegisterDTO registerRequest){
        //密码强度校验
        String rawPassword = registerRequest.getPassword();
        if (!STRONG_PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new CustomException("密码强度不足：需包含大小写字母、数字和特殊字符，且不少于8位", 400);
        }

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