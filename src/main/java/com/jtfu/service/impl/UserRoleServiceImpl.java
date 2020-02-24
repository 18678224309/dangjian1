package com.jtfu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jtfu.entity.UserRole;
import com.jtfu.mapper.UserRoleMapper;
import com.jtfu.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {


    @Autowired
    UserRoleMapper userRoleMapper;
    public void deleteByRoleId(int roleid) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("roleid",roleid);
        userRoleMapper.delete(updateWrapper);
    }

    public UserRole getUserRoleByUserId(int userId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("userid",userId);
        return userRoleMapper.selectOne(queryWrapper);
    }
}
