package com.jtfu.config;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sssr
 * @version 1.0
 * @Description:
 * @date 2019/2/17
 */

public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //bean.setFilters();
        bean.setSecurityManager(manager);
        //登录接口
        bean.setLoginUrl("/user/Home.html");
        //登录成功跳转页面
        bean.setSuccessUrl("/user/index.html");
        //没有权限跳转的页面
        //bean.setUnauthorizedUrl("/user/unauthorized.html");


        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap();
        filterChainDefinitionMap.put("/user/index.html", "authc");
        filterChainDefinitionMap.put("/user/Home.html", "anon");
        filterChainDefinitionMap.put("/user/login.html", "anon");
        filterChainDefinitionMap.put("/journalism/updateHot", "authc");
        filterChainDefinitionMap.put("/journalism/deleteJournalism", "authc");
        filterChainDefinitionMap.put("/journalism/uploadImage", "authc");
        filterChainDefinitionMap.put("/journalism/config", "authc");
        filterChainDefinitionMap.put("/journalism/addJournalism", "authc");
        filterChainDefinitionMap.put("/imgLink/getImgLink", "anon");
        filterChainDefinitionMap.put("/user/loginUser", "anon");
        filterChainDefinitionMap.put("/static/pdfjs/web/**","perms[lookPdf]");
  /*      filterChainDefinitionMap.put("/static/layui-v2.5.6/**", "anon");
        filterChainDefinitionMap.put("/static/djimages/**", "anon");*/
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/ueditor/**", "anon");
        filterChainDefinitionMap.put("/journalism/**", "anon");
        filterChainDefinitionMap.put("/user/logout", "logout");
/*        filterChainDefinitionMap.put("/user/admin", "roles[admin]");
        filterChainDefinitionMap.put("/user/edit", "perms[userEdit]");*/
        filterChainDefinitionMap.put("/**", "user");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return bean;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") MyAuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    @Bean("authRealm")
    public MyAuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher matcher) {
        MyAuthRealm authRealm = new MyAuthRealm();
        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher() {
        return new CredentialMatcher();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}