package com.jtfu.service;

import com.jtfu.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface IUserRoleService extends IService<UserRole> {
    void deleteByRoleId(int roleid);
    UserRole getUserRoleByUserId(int userId);
}
