package com.mocha.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mocha.springboot.entity.EmployeeProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testRedisString() {
        // 写入 key-value
        redisTemplate.opsForValue().set("name","Mocha");

        redisTemplate.opsForValue().get("name");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

    @Test
    void testRedisObject() {
        // 用 Jackson 序列化对象
        ObjectMapper mapper = new ObjectMapper();

        try {
            EmployeeProfile profile = new EmployeeProfile();
            profile.setProfileId(1);
            profile.setName("ZhangSan");

            String json = mapper.writeValueAsString(profile);
            redisTemplate.opsForValue().set("emp:1", json);

            String jsonFromRedis = redisTemplate.opsForValue().get("emp:1");
            EmployeeProfile fromRedis = mapper.readValue(jsonFromRedis, EmployeeProfile.class);

            System.out.println("读取到的对象: " + fromRedis.getName());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}