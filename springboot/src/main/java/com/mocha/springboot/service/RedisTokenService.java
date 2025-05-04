package com.mocha.springboot.service;

import com.mocha.springboot.config.JwtProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties  jwtProperties;
    public RedisTokenService(StringRedisTemplate redisTemplate, JwtProperties jwtProperties) {
        this.redisTemplate = redisTemplate;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 保存 Token 和设备信息
     * @param refreshToken: 又JWT生成的刷新令牌
     * @param deviceInfo: 用户登录设备的信息
     * Redis结构：
     *      KEY: login:token:{userId}         # Redis Hash 类型
     *      FIELDS:
     *          {deviceInfoUUID} -> {refreshToken}
      */
    public void saveRefreshToken(Integer userId,String refreshToken, String deviceInfo) {

        String key = "login:token:" + userId;
        redisTemplate.opsForHash().put(key,  deviceInfo, refreshToken);

        //DONE
        redisTemplate.expire(key, Duration.ofSeconds(jwtProperties.getRefreshTokenExpiration()));

    }


    public void deleteRefreshToken(Integer userId,String deviceInfo){
        String key = "login:token:" + userId;
        redisTemplate.opsForHash().delete(key,deviceInfo);
    }


    public String getRefreshToken(Integer userId,String deviceInfo){
        String key = "login:token:" + userId;
        Object refreshToken = redisTemplate.opsForHash().get(key, deviceInfo);
        return (refreshToken != null) ? refreshToken.toString() : null;
    }

}
