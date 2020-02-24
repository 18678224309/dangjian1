package com.jtfu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jtfu.entity.Menu;
import com.jtfu.mapper.MenuMapper;
import com.jtfu.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;


    public List<Menu> getMenusByParentId(List<Menu> menus){
        for (int i=0;i<menus.size();i++) {
            Menu menu=menus.get(i);
            List<Menu> menusByParentId = findMenusByParentId(menu.getId());
            if(menusByParentId!=null&&menusByParentId.size()>0){
                menu.setChildren(menusByParentId);
                getMenusByParentId(menusByParentId);
            }
        }
        return menus;
    }

    public List<Menu> getAll() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("parentid",0);
        List list = menuMapper.selectList(queryWrapper);
        List menus = getMenusByParentId(list);
        return menus;
    }

    public List<Menu> getMenusByRoleIdAndParentId(List<Menu> menus,int roleid) {
        for (int i=0;i<menus.size();i++) {
            Menu menu=menus.get(i);
            //依据角色id与父id查询子菜单;
            List<Menu> menusByParentId = menuMapper.getMenusByRoleIdAndParentId(roleid,menu.getId());
            if(menusByParentId!=null&&menusByParentId.size()>0){
                menu.setChildren(menusByParentId);
                getMenusByRoleIdAndParentId(menusByParentId,roleid);
            }
        }
        return menus;
    }


    public List<Menu> findMenusByParentId(int parentId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("parentid",parentId);
        return menuMapper.selectList(queryWrapper);
    }
}
