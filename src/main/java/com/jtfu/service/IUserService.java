package com.jtfu.service;

import com.jtfu.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface IUserService extends IService<User> {
    User findByUsernameAndPassword(String username,String password);
    User findByUsername(String username);
}
