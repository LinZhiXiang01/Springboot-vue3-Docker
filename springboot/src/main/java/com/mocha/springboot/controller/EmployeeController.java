package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.Result;
import com.mocha.springboot.entity.Employee;
import com.mocha.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    //查询所有员工
    @Resource
    private EmployeeService employeeService;

    /**
     * 新增数据
     * @param employee
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee){
        employeeService.add(employee);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id){
        employeeService.deleteById(id);
        return Result.success();
    }

    //批量删除
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        employeeService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /*
    * 分页查询数据
    *pageNum 当前页码
    * pageSize 每页显示条数
    */
   @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
                             @RequestParam (defaultValue = "1") Integer pageNum,
                             @RequestParam (defaultValue = "10") Integer pageSize){
       PageInfo<Employee> employeePageInfo = employeeService.selectPage(employee,pageNum, pageSize);
       return Result.success(employeePageInfo);
    }
}
