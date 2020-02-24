package com.jtfu.mapper;

import com.jtfu.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select menu.* from role inner join role_menu on role.id=role_menu.roleid inner join menu on role_menu.menuid=menu.id\n" +
            " where role.id=#{roleid} and menu.parentid=#{parentid}")
    public List<Menu> getMenusByRoleIdAndParentId(@Param("roleid")int roleid,@Param("parentid")int parentid);
}
