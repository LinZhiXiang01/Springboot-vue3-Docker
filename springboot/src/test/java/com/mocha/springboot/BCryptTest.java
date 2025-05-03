package com.mocha.springboot;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class BCryptTest {

    @Test
    public void testBCryptHashing() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "testPassword";
        String hashedPassword = encoder.encode(rawPassword);

        System.out.println("原始密码: " + rawPassword);
        System.out.println("hashed密码: " + hashedPassword);


        // 验证匹配
        boolean matches = encoder.matches("testPassword", hashedPassword);
        System.out.println("是否匹配: " + matches);
    }
}
