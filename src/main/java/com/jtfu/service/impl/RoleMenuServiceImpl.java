package com.jtfu.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jtfu.entity.RoleMenu;
import com.jtfu.mapper.RoleMenuMapper;
import com.jtfu.service.IRoleMenuService;
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
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Autowired
    RoleMenuMapper roleMenuMapper;
    public void deleteByRoleId(int roleid) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("roleid",roleid);
        roleMenuMapper.delete(updateWrapper);
    }
}
