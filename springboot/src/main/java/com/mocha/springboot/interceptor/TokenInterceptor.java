package com.mocha.springboot.interceptor;

import com.mocha.springboot.exception.CustomException;
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

        // 1. 获取请求路径 (如果后续有需要使用请求路径的场景)
        String requestURI = request.getRequestURI();
        log.info("请求路径: {}", requestURI);

        // 2. 从请求头中获取 Authorization
        String authorizationHeader = request.getHeader("Authorization");

        // 确保格式为 "Bearer <token>"
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException("非法 Authorization header 格式", 401);
        }

        // 3. 获取 access token
        String oldAccessToken = authorizationHeader.substring(7);
        if (oldAccessToken.isEmpty()) {
            throw new CustomException("accessToken为空", 401);
        }

        // 4. 解析 access token
        if (!isTokenValid(oldAccessToken)) {
            throw new CustomException("accessToken非法", 401);
        }

        // 5. 校验通过，返回 true
        return true;
    }

    // 提取验证 token 的方法
    private boolean isTokenValid(String token) {
        try {
            // 验证 token 是否有效 (具体的验证逻辑根据 jwtUtils 来)
            jwtUtils.parseToken(token);
            return true;
        } catch (Exception e) {
            // 验证失败时记录异常日志
            log.error("Token 验证失败: {}", e.getMessage());
            return false;
        }
    }
}
