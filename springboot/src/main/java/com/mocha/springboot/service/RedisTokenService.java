package com.mocha.springboot.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;
    public RedisTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存 Token 和设备信息
     * @param refreshToken: 又JWT生成的刷新令牌
     * @param deviceInfo: 用户登录设备的信息
     * @param expireSeconds: 过期时间
      */
    public void saveRefreshToken(Integer userId,String refreshToken, String deviceInfo, long expireSeconds) {
        redisTemplate.opsForValue().set(refreshToken, deviceInfo);

        String key = "login:token:" + userId;

        /**
         * key = login:token:12345
         * fields:
         *   token  -> xxxxxxxx（JWT 或 refreshToken）
         *   device -> iPhone 15 / Chrome on Mac 等
         */
        redisTemplate.opsForHash().put(key, "refreshToken",refreshToken);
        redisTemplate.opsForHash().put(key,"device",deviceInfo);
        redisTemplate.expire(key, Duration.ofSeconds(expireSeconds));

    }

    public String getRefreshToken(Integer userId,  String deviceInfo){
        String key = "login:token:" + userId;
        Object refreshToken = redisTemplate.opsForHash().get(key,"refreshToken");
        return( refreshToken!= null) ? refreshToken.toString():null;
    }

}
