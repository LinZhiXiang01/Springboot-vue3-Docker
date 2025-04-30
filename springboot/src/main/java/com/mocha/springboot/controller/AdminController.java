package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.Result;
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
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.update(admin);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(@RequestBody Admin admin){
        List<Admin> admins = adminService.selectAll(admin);
        return Result.success(admins);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam (defaultValue = "1") Integer pageNum,
                             @RequestParam (defaultValue = "5") Integer pageSize){
        PageInfo<Admin> adminPageInfo = adminService.selectPage(admin,pageNum, pageSize);
        return Result.success(adminPageInfo);
    }

}
