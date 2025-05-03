package com.mocha.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mocha.springboot.entity.Admin;
import com.mocha.springboot.entity.LoginDTO;
import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.mapper.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import utils.JwtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service//创建 Service 并且标注为 Springboot 里面的一个 bean
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    //增
    public void add(Admin admin) {
        String username = admin.getUsername();

        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null){
            throw new CustomException("账号已存在，请更换",500);
        }

        if (StrUtil.isBlank(admin.getName())){ //名字没填，自动
            admin.setName(username);
        }
        if (StrUtil.isBlank(admin.getPassword())){ //密码没填，自动
            admin.setPassword("ADMIN");
        }

        admin.setRole("ADMIN"); //员工角色
        adminMapper.insert(admin);
    }

    //删
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids) {
            this.deleteById(id);
        }
    }

    //改
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public PageInfo<Admin> selectPage(Admin admin,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    //员工登录
    public Admin login(LoginDTO account) {
        String username = account.getUsername();
        String password = account.getPassword();

        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin == null){
            throw new CustomException("用户名不存在",500);
        }
        if(!dbAdmin.getPassword().equals(password)){
            throw new CustomException("用户名或密码错误",500);
        }

        //生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", dbAdmin.getId());
        claims.put("username", dbAdmin.getUsername());
        String jwt = JwtUtils.generateToken(claims);

        dbAdmin.setToken(jwt);

        return dbAdmin;
    }

    public void updatePassword(LoginDTO updatePasswordRequest) {
        Integer id = updatePasswordRequest.getId();
        Admin admin = this.selectById(id);
        if(!admin.getPassword().equals(updatePasswordRequest.getPassword())){ //原密码错误，报错
            throw new CustomException("对不起，原密码错误",500);
        }
        admin.setPassword(updatePasswordRequest.getNewPassword());
        this.update(admin);
    }
}
