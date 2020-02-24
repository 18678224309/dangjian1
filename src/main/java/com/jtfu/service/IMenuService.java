package com.jtfu.service;

import com.jtfu.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
public interface IMenuService extends IService<Menu> {
    List<Menu> getMenusByParentId(List<Menu> menus);
    List<Menu> getAll();
    List<Menu> getMenusByRoleIdAndParentId(List<Menu> menus,int roleid);
}
