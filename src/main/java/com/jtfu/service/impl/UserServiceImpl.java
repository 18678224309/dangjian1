package com.jtfu.service.impl;

import com.jtfu.entity.Role;
import com.jtfu.entity.User;
import com.jtfu.mapper.UserMapper;
import com.jtfu.service.IMenuService;
import com.jtfu.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
@Service
@Lazy
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    UserMapper userMapper;
    @Autowired
    IMenuService menuService;


    public User findByUsernameAndPassword(String username,String password){
        User byUsername = userMapper.findByUsername(username,password);
        return byUsername;
    }

    public User findByUsername(String username) {
        User byUsername = userMapper.findByUsername(username,null);
        if(byUsername!=null){
            for(int i=0;i<byUsername.getRoles().size();i++){
                Role role=byUsername.getRoles().get(i);
                role.setMenus(menuService.getMenusByRoleIdAndParentId(role.getMenus(),role.getId()));
                byUsername.getRoles().set(i,role);
            }
        }
        return byUsername;
    }
}
