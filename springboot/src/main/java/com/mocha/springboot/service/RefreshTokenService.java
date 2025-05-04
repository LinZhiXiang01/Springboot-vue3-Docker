package com.mocha.springboot.service;

import com.mocha.springboot.exception.CustomException;
import com.mocha.springboot.utils.JwtUtils;
import com.mocha.springboot.vo.JwtVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService {

    @Resource
    private RedisTokenService redisTokenService;
    @Resource
    private JwtUtils jwtUtils;

    public JwtVO refreshToken(Integer userId, HttpServletRequest request){

        //1. 解析 deviceId（从 UA + IP 模拟）

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        String deviceInfo = UUID.nameUUIDFromBytes((ip + ua).getBytes()).toString();

        //1.校验redis中是否存在refreshToken,是否已经过期
        String redisRefreshToken = redisTokenService.getRefreshToken(userId,deviceInfo);

        if("".equals(redisRefreshToken)|| redisRefreshToken==null){
            throw new CustomException("refreshToken已过期",401);
        }

        //2. 从请求头中获取Authorization
        String authorizationHeader = request.getHeader("Authorization");

        // 确保格式为 "Bearer <token>"
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException("非法 Authorization header 格式", 401);
        }

        String oldRefreshToken = authorizationHeader.substring(7);
        if(oldRefreshToken.isEmpty()){
            throw new CustomException("refreshToken为空",401);
        }

        //3.解析refreshToken
        try{
            jwtUtils.parseToken(oldRefreshToken);
        }catch(Exception e){
            throw new CustomException("refreshToken非法",401);
        }

        //4.新旧TOKEN匹配校验
        if(!redisRefreshToken.equals(oldRefreshToken)){
            throw new CustomException("refreshToken非法",401);
        }

        // 删除旧的 device 对应的 refreshToken
        redisTokenService.deleteRefreshToken(userId, deviceInfo);

        //5.生成新的accessToken和refreshToken
        String refreshToken = jwtUtils.generateRefreshToken(userId,deviceInfo);
        String accessToken = jwtUtils.generateAccessToken(userId,deviceInfo);

        //6.更新保存到redis中
        redisTokenService.saveRefreshToken(userId,refreshToken,deviceInfo);

        //7.返回新的accessToken和refreshToken
        JwtVO jwtVO = new JwtVO();
        jwtVO.setAccessToken(accessToken);
        jwtVO.setRefreshToken(refreshToken);
        return jwtVO;
    }

}
