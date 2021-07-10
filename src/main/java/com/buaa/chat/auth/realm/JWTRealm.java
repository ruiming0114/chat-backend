package com.buaa.chat.auth.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.auth.token.JWTToken;
import com.buaa.chat.util.JWTUtil;
import com.buaa.chat.util.RedisUtil;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("all")
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    UserDao userDao;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getCredentials();
        String mongoId = null;
        try {
            mongoId = JWTUtil.getMongoId(jwt);
        }catch (Exception e){
            throw new AuthenticationException("illegal token");
        }
        if (mongoId == null){
            throw new AuthenticationException("empty mongoId");
        }
        User user = userDao.findUserByMongoId(mongoId);
        if (user == null){
            throw new AuthenticationException("user doesn't exists");
        }
        String tokenKey = "token_"+mongoId;
        if(redisUtil.hasKey(tokenKey)){
            if (!JWTUtil.verify(jwt)){
                throw new TokenExpiredException("token expired");
            }
            else {
                long current = (long) redisUtil.get(tokenKey);
                if (current == JWTUtil.getExpire(jwt)){
                    return new SimpleAuthenticationInfo(jwt,jwt,"JWTRealm");
                }
                else {
                    throw new AuthenticationException("token out of time");
                }
            }
        }
        else {
            throw new AuthenticationException("failed");
        }
    }
}
