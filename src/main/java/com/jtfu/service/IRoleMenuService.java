package com.jtfu.service;

import com.jtfu.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface IRoleMenuService extends IService<RoleMenu> {
    void deleteByRoleId(int roleid);
}
