package com.jtfu.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.jtfu.controller.UserController;
import com.jtfu.entity.Menu;
import com.jtfu.entity.Role;
import com.jtfu.entity.User;
import com.jtfu.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sssr
 * @version 1.0
 * @Description:
 * @date 2019/2/17
 */

public class MyAuthRealm extends AuthorizingRealm {
/*
    @Autowired
    @Lazy
    private IUserService userService;*/

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.fromRealm(this.getClass().getName()).iterator().next();
        List<String> permissionList = new ArrayList();
        List<String> roleNameList = new ArrayList();
        List<Role> roleSet = user.getRoles();
        if (CollectionUtils.isNotEmpty(roleSet)) {
            for(Role role : roleSet) {
                roleNameList.add(role.getRolename());
                List<Menu> menuSet = role.getMenus();
                if (CollectionUtils.isNotEmpty(menuSet)) {
                    setPermissions(permissionList,menuSet);
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        info.addRoles(roleNameList);
        return info;
    }

    public void setPermissions(List<String> permissionList,List<Menu> menus){
        for (Menu menu : menus) {
            permissionList.add(menu.getRes());
            if(menu.getChildren()!=null&&menu.getChildren().size()>0){
                setPermissions(permissionList,menu.getChildren());
            }
        }
    }
    /**
     * 认证登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        User user = UserController.userService.findByUsername(username);
        if(user!=null){
            return new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());
        }else{
            return null;
        }
    }
}