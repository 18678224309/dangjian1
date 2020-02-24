package com.jtfu.mapper;

import com.jtfu.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface RoleMapper extends BaseMapper<Role> {

    Role getRoleByRoleId(@Param("id")int id);
}
