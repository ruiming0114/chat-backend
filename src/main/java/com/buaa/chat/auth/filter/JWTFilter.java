package com.buaa.chat.auth.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.buaa.chat.auth.token.JWTToken;
import org.apache.http.HttpStatus;
import com.buaa.chat.util.JWTUtil;
import com.buaa.chat.util.RedisUtil;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("all")
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private RedisUtil redisUtil;

    public JWTFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    private static int token_expire_time = 3600;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                String msg = e.getMessage();
                Throwable cause = e.getCause();
                if (cause != null && cause instanceof TokenExpiredException) {
                    String result = refreshToken(request, response);
                    if (result.equals("success")) {
                        return true;
                    }
                    msg = result;
                }
                responseError(response, msg);
            }
        }
        return true;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        JWTToken jwt = new JWTToken(token);
        getSubject(request, response).login(jwt);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Authorization", token);
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.SC_OK);
            return false;
        }
        return super.preHandle(request, response);
    }

    private void responseError(ServletResponse response, String msg) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.sendRedirect("/unauthorized");
            return;
        } catch (IOException ignored) {
        }
    }

    public <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }

    private String refreshToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(accessToken);
        String tokenKey = "token_" + mongoId;
        if (redisUtil.hasKey(tokenKey)) {
            long current = (long) redisUtil.get(tokenKey);
            if (current == JWTUtil.getExpire(accessToken)) {
                long currentTimeMillis = System.currentTimeMillis();
                String token = JWTUtil.createToken(mongoId, currentTimeMillis);
                redisUtil.set(tokenKey, currentTimeMillis, token_expire_time);
                JWTToken jwtToken = new JWTToken(token);
                try {
                    getSubject(request, response).login(jwtToken);
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader("Authorization", token);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                    return "success";
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
        }
        return "out of time";
    }
}
