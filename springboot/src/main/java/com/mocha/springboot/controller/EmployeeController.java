package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.dto.EmployeeAddInfoDTO;
import com.mocha.springboot.dto.EmployeeProfileUpdateDTO;
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
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public ResultCode add(@RequestBody EmployeeAddInfoDTO dto){
        employeeService.add(dto);
        return ResultCode.success();
    }

    @PutMapping("/update")
    public ResultCode updateProfile(@RequestBody EmployeeProfileUpdateDTO dto){
        employeeService.updateProfile(dto);
        return ResultCode.success();
    }

    @DeleteMapping("/deleteById/{authId}")
    public ResultCode delete(@PathVariable Integer authId){
        employeeService.deleteById(authId);
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

    @GetMapping("/selectProfileById/{authId}")
    public ResultCode selectById(@PathVariable Integer authId){
        EmployeeInfoVO employeeInfoVO = employeeService.selectProfileById(authId);
        return ResultCode.success(employeeInfoVO);
    }

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
