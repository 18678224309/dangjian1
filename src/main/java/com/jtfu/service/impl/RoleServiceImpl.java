package com.jtfu.service.impl;

import com.jtfu.entity.Role;
import com.jtfu.mapper.RoleMapper;
import com.jtfu.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    RoleMapper roleMapper;

    public Role getRoleByRoleId(int id) {
        return roleMapper.getRoleByRoleId(id);
    }
}
