package com.mocha.springboot.utils;

import com.mocha.springboot.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 生成短期JWT令牌
     */
    public String generateAccessToken(Map<String,Object> claims){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret())
                .addClaims(claims)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getAccessTokenExpiration()))
                .compact();//生成token
    }

    /**
     * 生成长期JWT令牌
     */
    public String generateRefreshToken(Map<String,Object> claims){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret())
                .addClaims(claims)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getRefreshTokenExpiration()))
                .compact();//生成token
    }

    /**
     * 解析JWT令牌
     */

    public Claims parseToken(String token) throws Exception{
       return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())//指定密钥
                .parseClaimsJws(token)//解析令牌
                .getBody();//获取自定义信息
    }
}
