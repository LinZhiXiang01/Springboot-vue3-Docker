package com.mocha.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mocha.springboot.dto.*;
import com.mocha.springboot.entity.EmployeeAuth;
import com.mocha.springboot.entity.EmployeeProfile;
import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.mapper.EmployeeAuthMapper;
import com.mocha.springboot.mapper.EmployeeProfileMapper;
import com.mocha.springboot.vo.EmployeeInfoVO;
import com.mocha.springboot.vo.LoginVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.mocha.springboot.utils.JwtUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service//创建 Service 并且标注为 Springboot 里面的一个 bean
public class EmployeeService {

    private final EmployeeAuthMapper employeeAuthMapper;
    private final EmployeeProfileMapper employeeProfileMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;
    private final JwtUtils jwtUtils;

    @Autowired
    public EmployeeService(EmployeeAuthMapper employeeAuthMapper,
                           EmployeeProfileMapper employeeProfileMapper,
                           BCryptPasswordEncoder passwordEncoder,
                           RedisTokenService redisTokenService,
                           JwtUtils jwtUtils){
        this.employeeAuthMapper = employeeAuthMapper;
        this.employeeProfileMapper = employeeProfileMapper;
        this.passwordEncoder = passwordEncoder;
        this.redisTokenService = redisTokenService;
        this.jwtUtils = jwtUtils;
    }

    //增
    public void add(EmployeeAddInfoDTO dto) {
        String username = dto.getUsername();

        //查询账号是否已存在
        EmployeeAuth dbEmployeeAuth = employeeAuthMapper.selectByUsername(username);
        if (dbEmployeeAuth != null){
            throw new CustomException("账号已存在，请更换",500);
        }

        EmployeeAuth employeeAuth = new EmployeeAuth();

        // 插入认证表
        // 密码为空，设置默认密码,
        if (StrUtil.isBlank(dto.getPassword())){ //密码没填，设置默认密码
            employeeAuth.setPassword(passwordEncoder.encode("123456"));
        }else{
            employeeAuth.setPassword(dto.getPassword());
        }

        employeeAuth.setUsername(username);
        employeeAuth.setRole("EMP"); //员工角色
        employeeAuth.setActive(true);//账号有效性
        employeeAuth.setCreatedAt(LocalDateTime.now());//创建时间
        employeeAuthMapper.insert(employeeAuth);

        // 插入个人信息表
        EmployeeProfile employeeProfile = new EmployeeProfile();
        if (StrUtil.isBlank(employeeProfile.getName())){ //名字nickName为空，设置默认名字
            employeeProfile.setName(username);
        }

        employeeProfile.setAuthId(employeeAuth.getAuthId());
        employeeProfile.setAge(dto.getAge());
        employeeProfile.setAvatar(dto.getAvatar());
        employeeProfile.setDescription(dto.getDescription());
        employeeProfile.setDepartmentId(dto.getDepartmentId());
        employeeProfile.setNo(dto.getNo());
        employeeProfile.setSex(dto.getSex());

        employeeProfileMapper.insert(employeeProfile);
    }

    //删
    public void deleteById(Integer authId) {
        employeeAuthMapper.deleteById(authId);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer authId: ids) {
            this.deleteById(authId);
        }
    }

    //改
    public void updateProfile(EmployeeProfileUpdateDTO dto) {
        if(dto.getAuthId() == null){
            throw new CustomException("AuthId不能为空",400);
        }
        employeeProfileMapper.updateByAuthId(dto);
    }

    public List<EmployeeInfoVO> selectAllWithProfile(EmployeeQueryDTO employeeQueryDTO) {
        return employeeAuthMapper.selectAllWithProfile(employeeQueryDTO);
    }

    public EmployeeAuth selectById(Integer authId) {
        return employeeAuthMapper.selectById(authId);
    }

    public EmployeeInfoVO selectProfileById(Integer authId) {
        return employeeAuthMapper.selectProfileById(authId);
    }


    /**
     * 分页查询：
     *  selectPage->selectAll带name查询
     */
    public PageInfo<EmployeeInfoVO> selectPage(EmployeeQueryDTO employeeQueryDTO,
                                               Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<EmployeeInfoVO> list = employeeAuthMapper.selectAllWithProfile(employeeQueryDTO);
        return PageInfo.of(list);
    }

    //员工登录
    public LoginVO login(LoginDTO dto, HttpServletRequest request) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        EmployeeAuth dbEmployeeAuth = employeeAuthMapper.selectByUsername(username);

        if(dbEmployeeAuth == null){
            throw new CustomException("用户名不存在",401);
        }
        if(!dbEmployeeAuth.getActive()){
            throw new CustomException("账号已被禁用",401);
        }
        if(!passwordEncoder.matches(password, dbEmployeeAuth.getPassword())){
            throw new CustomException("用户名或密码错误",401);
        }

        Integer authId = dbEmployeeAuth.getAuthId();
        String authUsername = dbEmployeeAuth.getUsername();
        //生成JWT令牌
        String accessJWT = jwtUtils.generateAccessToken(authId,authUsername);
        String refreshJWT = jwtUtils.generateRefreshToken(authId,authUsername);

        // 解析 deviceInfo（从 UA + IP 模拟）
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        String deviceInfo = UUID.nameUUIDFromBytes((ip + ua).getBytes()).toString();

        //7天保存refreshToken
        redisTokenService.saveRefreshToken(authId,refreshJWT,deviceInfo);

        //返回前端 employeeProfile以及令牌
        LoginVO vo = new LoginVO();
        vo.setAccessToken(accessJWT);
        vo.setRefreshToken(refreshJWT);
        vo.setRole("EMP");
        vo.setProfile(employeeProfileMapper.selectByAuthId(authId));
        return vo;
    }

    //员工注册
    public void register(RegisterDTO registerRequest) {

        String username = registerRequest.getUsername();
        String rawPassword = registerRequest.getPassword();

        //防止非法输入
        if (StrUtil.isBlank(username) ||StrUtil.isBlank(rawPassword)) {
            throw new CustomException("用户名和密码不能为空", 400);
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        EmployeeAddInfoDTO dto = new EmployeeAddInfoDTO();
        dto.setUsername(username);
        dto.setPassword(hashedPassword);

        this.add(dto);
    }

    public void updatePassword(UpdatePasswordDTO dto) {

        Integer authId = dto.getAuthId();
        EmployeeAuth employeeAuth = this.selectById(authId);
        String hashedPassword = dto.getOldPassword();

        //校验原密码，错误报错
        if(!passwordEncoder.matches(hashedPassword, employeeAuth.getPassword())){
            throw new CustomException("对不起，原密码错误",500);
        }

        employeeAuth.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        employeeAuthMapper.updatePassword(employeeAuth);

    }
}
