package com.jtfu.mapper;

import com.jtfu.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface UserMapper extends BaseMapper<User> {
    User findByUsername(@Param("username") String username,@Param("password") String password);
}
