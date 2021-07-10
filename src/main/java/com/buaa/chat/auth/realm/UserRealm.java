package com.buaa.chat.auth.realm;

import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.module.user.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userId = usernamePasswordToken.getUsername();
        User user = userDao.findUserByUserId(userId);
        if (user == null){
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(user.getUserId(), user.getPassword(),"UserRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
