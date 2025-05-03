package com.mocha.springboot;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtTest {


    @Test
    public void testGenerateJwt(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("id",1);
        dataMap.put("username","admin");

        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256,"mySecretKey")
                .addClaims(dataMap)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis()+3600*1000)) //一个小时对应的毫秒时间
                .compact();//生成token
        System.out.println("jwt生成成功\n"+jwt);
    }

    /**
     * 解析JWT令牌
     */
    @Test
    public void testParseJWT(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc0NTY3MjExOH0.Rsyz7fIcpB19ACdys9J662R9a6QiybRYC6nTDcVxCR4";
        Claims claims =Jwts.parser()
                .setSigningKey("mySecretKey")//指定密钥
                .parseClaimsJws(token)//解析令牌
                .getBody();//获取自定义信息
        System.out.println(claims);
    }
}
