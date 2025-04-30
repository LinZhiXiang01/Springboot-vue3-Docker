package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "mySecretKey";
    private static final long EXPIRE_TIME = 12*60*1000;


    /**
     * 生成JWT令牌
     */
    public static String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .addClaims(claims)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .compact();//生成token
    }

    /**
     * 解析JWT令牌
     */

    public static Claims parseToken(String token) throws Exception{
       return Jwts.parser()
                .setSigningKey(SECRET_KEY)//指定密钥
                .parseClaimsJws(token)//解析令牌
                .getBody();//获取自定义信息
    }
}
