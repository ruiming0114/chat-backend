package com.buaa.chat.config;


import com.buaa.chat.auth.filter.JWTFilter;
import com.buaa.chat.auth.realm.JWTRealm;
import com.buaa.chat.auth.realm.MyModularRealmAuthenticator;
import com.buaa.chat.auth.realm.UserRealm;
import com.buaa.chat.util.RedisUtil;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Resource
    private RedisUtil redisUtil;

    @Bean
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new MyModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(userRealm(),jwtRealm()));
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("jwtRealm") JWTRealm jwtRealm, @Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> list = new ArrayList<>();
        list.add(userRealm);
        list.add(jwtRealm);
        securityManager.setRealms(list);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setAuthenticator(authenticator());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //过滤器
        Map<String, Filter> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("jwt",new JWTFilter(redisUtil));
        shiroFilterFactoryBean.setFilters(filterChainDefinitionMap);
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        Map<String,String> filterRuleMap = new HashMap<>();
        filterRuleMap.put("/websocket/**","anon");
        filterRuleMap.put("/image/**","anon");
        filterRuleMap.put("/unauthorized","anon");
        filterRuleMap.put("/login","anon");
        filterRuleMap.put("/register","anon");
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/swagger-resources/**", "anon");
        filterRuleMap.put("/v2/**", "anon");
        filterRuleMap.put("/webjars/**", "anon");
        filterRuleMap.put("/websocketAPI.html","anon");
        filterRuleMap.put("/**","jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }

    @Bean("jwtRealm")
    public JWTRealm jwtRealm(){
        return new JWTRealm();
    }

    @Bean("userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
