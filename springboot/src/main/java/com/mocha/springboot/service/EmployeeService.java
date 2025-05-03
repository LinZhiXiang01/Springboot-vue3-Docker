package com.mocha.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.mocha.springboot.dto.LoginDTO;
import com.mocha.springboot.dto.RegisterDTO;
import com.mocha.springboot.dto.UpdatePasswordDTO;
import com.mocha.springboot.entity.EmployeeAuth;
import com.mocha.springboot.entity.EmployeeProfile;
import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.mapper.EmployeeAuthMapper;
import com.mocha.springboot.mapper.EmployeeProfileMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service//创建 Service 并且标注为 Springboot 里面的一个 bean
public class EmployeeService {

    @Resource
    private EmployeeAuthMapper employeeAuthMapper;
    @Autowired
    private BCryptPasswordEncoder  passwordEncoder;
    @Autowired
    private EmployeeProfileMapper employeeProfileMapper;

    //增
    public void add(EmployeeAuth employeeAuth) {
        String username = employeeAuth.getUsername();

        //查询账号是否已存在
        EmployeeAuth dbEmployeeAuth = employeeAuthMapper.selectByUsername(username);
        if (dbEmployeeAuth != null){
            throw new CustomException("账号已存在，请更换",500);
        }

        // 插入认证表
        if (StrUtil.isBlank(employeeAuth.getPassword())){ //密码没填，设置默认密码
            employeeAuth.setPassword(passwordEncoder.encode("123456"));
        }
        employeeAuth.setRole("EMP"); //员工角色
        employeeAuth.setActive(true);//账号有效性
        employeeAuth.setCreatedAt(LocalDateTime.now());//创建时间
        employeeAuthMapper.insert(employeeAuth);

        // 插入个人信息表
        EmployeeProfile employeeProfile = new EmployeeProfile();
        if (StrUtil.isBlank(employeeProfile.getName())){ //名字nickName为空，设置默认名字
            employeeProfile.setName(username);
        }
        employeeProfile.setCreatedAt(LocalDateTime.now());
        employeeProfile.setAuthId(employeeAuth.getId());
        employeeProfileMapper.insert(employeeProfile);
    }

    //删
    public void deleteById(Integer id) {
        employeeAuthMapper.deleteById(id);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids) {
            this.deleteById(id);
        }
    }

//    //改
//    public void update(Employee employee) {
//        employeeMapper.updateById(employee);
//    }

//    public List<EmployeeAuth> selectAll(EmployeeAuth employee) {
//        return employeeMapper.selectAll(employee);
//    }

    public EmployeeAuth selectById(Integer id) {
        return employeeAuthMapper.selectById(id);
    }
//
//    public PageInfo<EmployeeAuth> selectPage(EmployeeAuth employee, Integer pageNum, Integer pageSize){
//        PageHelper.startPage(pageNum,pageSize);
//        List<EmployeeAuth> list = employeeMapper.selectAll(employee);
//        return PageInfo.of(list);
//    }

    //员工登录
    public EmployeeAuth login(LoginDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        EmployeeAuth dbEmployeeAuth = employeeAuthMapper.selectByUsername(username);
        if(dbEmployeeAuth == null){
            throw new CustomException("用户名不存在",500);
        }
        if(!dbEmployeeAuth.getPassword().equals(password)){
            throw new CustomException("用户名或密码错误",500);
        }

        //生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", dbEmployeeAuth.getId());
        claims.put("username", dbEmployeeAuth.getUsername());
        String jwt = JwtUtils.generateToken(claims);

        dbEmployeeAuth.setToken(jwt);
        return dbEmployeeAuth;
    }

    //员工注册
    public void register(RegisterDTO registerRequest) {
        String username = registerRequest.getUsername();
        String rawPassword = registerRequest.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);

        EmployeeAuth employeeAuth = new EmployeeAuth();
        employeeAuth.setUsername(username);
        employeeAuth.setPassword(hashedPassword);

        this.add(employeeAuth);
    }

    public void updatePassword(UpdatePasswordDTO dto) {

        Integer id = dto.getAuth_id();
        EmployeeAuth employeeAuth = this.selectById(id);
        String hashedPassword = dto.getOldPassword();

        //校验原密码，错误报错
        if(!passwordEncoder.matches(hashedPassword, employeeAuth.getPassword())){
            throw new CustomException("对不起，原密码错误",500);
        }

        employeeAuth.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        employeeAuthMapper.updatePassword(employeeAuth);

    }
}
