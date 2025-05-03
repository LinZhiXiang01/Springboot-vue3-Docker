package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.entity.Admin;
import com.mocha.springboot.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource private AdminService adminService;

    @PostMapping("/add")
    public ResultCode add(@RequestBody Admin admin){
        adminService.add(admin);
        return ResultCode.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultCode delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return ResultCode.success();
    }

    @DeleteMapping("/deleteBatch")
    public ResultCode deleteBatch(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return ResultCode.success();
    }
    @PutMapping("/update")
    public ResultCode update(@RequestBody Admin admin){
        adminService.update(admin);
        return ResultCode.success();
    }


    @GetMapping("/selectAll")
    public ResultCode selectAll(@RequestBody Admin admin){
        List<Admin> admins = adminService.selectAll(admin);
        return ResultCode.success(admins);
    }

    @GetMapping("/selectPage")
    public ResultCode selectPage(Admin admin,
                                 @RequestParam (defaultValue = "1") Integer pageNum,
                                 @RequestParam (defaultValue = "5") Integer pageSize){
        PageInfo<Admin> adminPageInfo = adminService.selectPage(admin,pageNum, pageSize);
        return ResultCode.success(adminPageInfo);
    }

}
