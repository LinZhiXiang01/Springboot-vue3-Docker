package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    //FIXME:.env全局环境配置,按GPT最后的建议写就好
    private static String SECRET_KEY;
    private static final long ACCESS_TOKEN_EXP = 15*60*1000; //15mins过期
    private static final long REFRESH_TOKEN_EXP = 7*24*60*60*1000; //7天过期

    @Value("${jwt.secret}")
    public void setSecretKey(String secretKey){
        SECRET_KEY = secretKey;
    }

    /**
     * 生成短期JWT令牌
     */
    public static String generateAccessToken(Map<String,Object> claims){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .addClaims(claims)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXP))
                .compact();//生成token
    }

    /**
     * 生成长期JWT令牌
     */
    public static String generateRefreshToken(Map<String,Object> claims){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .addClaims(claims)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXP))
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
