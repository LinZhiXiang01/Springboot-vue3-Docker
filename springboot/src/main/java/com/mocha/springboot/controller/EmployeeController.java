package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.dto.EmployeeQueryDTO;
import com.mocha.springboot.entity.EmployeeAuth;
import com.mocha.springboot.service.EmployeeService;
import com.mocha.springboot.vo.EmployeeInfoVO;
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
    public ResultCode add(@RequestBody EmployeeAuth employee){
        employeeService.add(employee);
        return ResultCode.success();
    }

//    @PutMapping("/update")
//    public ResultCode update(@RequestBody Employee employee){
//        employeeService.update(employee);
//        return ResultCode.success();
//    }

    @DeleteMapping("/deleteById/{id}")
    public ResultCode delete(@PathVariable Integer id){
        employeeService.deleteById(id);
        return ResultCode.success();
    }

    //批量删除
    @DeleteMapping("/deleteBatch")
    public ResultCode deleteBatch(@RequestBody List<Integer> ids){
        employeeService.deleteBatch(ids);
        return ResultCode.success();
    }

    //带name查询的selectAll
    @GetMapping("/selectAll")
    public ResultCode selectAll(EmployeeQueryDTO employeeQueryDTO){
        List<EmployeeInfoVO> list = employeeService.selectAllWithProfile(employeeQueryDTO);
        return ResultCode.success(list);
    }

//    @GetMapping("/selectById/{id}")
//    public ResultCode selectById(@PathVariable Integer id){
//        EmployeeAuth employee = employeeService.selectById(id);
//        return ResultCode.success(employee);
//    }

    /*
    * 分页查询数据
    *pageNum 当前页码
    * pageSize 每页显示条数
    */
   @GetMapping("/selectPage")
    public ResultCode selectPage(EmployeeQueryDTO employeeQueryDTO,
                                 @RequestParam (defaultValue = "1") Integer pageNum,
                                 @RequestParam (defaultValue = "10") Integer pageSize){
       PageInfo<EmployeeInfoVO> employeePageInfo = employeeService.selectPage(employeeQueryDTO,pageNum, pageSize);
       return ResultCode.success(employeePageInfo);
    }
}
