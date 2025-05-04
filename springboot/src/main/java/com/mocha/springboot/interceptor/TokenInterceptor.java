package com.mocha.springboot.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.mocha.springboot.utils.JwtUtils;


/**
 * Token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    private final JwtUtils jwtUtils;

    @Autowired
    public TokenInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.获取请求路径
        String requestURI = request.getRequestURI();

        //2.判断是否包含login

        if(requestURI.contains("/login")) {
            return true;
        }

        //3.获取请求头中的令牌（token）
        String jwt = request.getHeader("Token");

        //4.判断令牌是否存在，如果不存在，返回401
        if(jwt==null || jwt.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //5.解析令牌，如果解析失败，返回401
        try{
            jwtUtils.parseToken(jwt);
        }catch(Exception e){
            log.info("令牌非法");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //6.校验通过，返回true
        return true;
    }
}
