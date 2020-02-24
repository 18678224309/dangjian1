package com.jtfu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtfu.entity.*;
import com.jtfu.service.*;
import com.jtfu.util.R;
import org.apache.ibatis.annotations.Update;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    IRoleService roleService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IRoleMenuService roleMenuService;

    @Autowired
    IUserRoleService userRoleService;

    private static List<Role> ROLELIST=new ArrayList<Role>();

    public static List<Role> getROLELIST() {
        return ROLELIST;
    }

    @PostConstruct
    public void init(){
        ROLELIST.addAll(roleService.list()) ;
    }

    @GetMapping("/roleList.html")
    public String roleList(){
        return "roleList";
    }



    @GetMapping("/roleConfig.html")
    public String roleConfig(Model model){
        model.addAttribute("roles",ROLELIST);
        return "roleConfig";
    }

    @PostMapping("/getMenusTreeByRoleId")
    public R getMenusTreeByRoleId(int roleid){
        Role roleByRoleId = roleService.getRoleByRoleId(roleid);
        List<Integer> checked=new ArrayList<Integer>();
        if(roleByRoleId!=null){
            List<Menu> menus = roleByRoleId.getMenus();
            for (Menu menu : menus) {
                if(menu.getParentid()!=0){
                    checked.add(menu.getId());
                }
            }
        }
        List<Menu> all = menuService.getAll();
        List<MenuTree> menuTrees=new ArrayList<MenuTree>();
        menuTransitionMenuTree(all,menuTrees);
        return R.success("查询成功",menuTrees).set("Checked",checked);
    }

    public void menuTransitionMenuTree(List<Menu> menus,List<MenuTree> menuTrees){
        for (Menu menu : menus) {
            MenuTree menuTree=new MenuTree();
            menuTree.setTitle(menu.getMenuname());
            menuTree.setId(menu.getId());
            menuTree.setField("menuname");
            if(menu.getChildren()!=null&&menu.getChildren().size()>0){
               menuTransitionMenuTree(menu.getChildren(),menuTree.getChildren());
            }
            menuTrees.add(menuTree);
        }
    }


    @GetMapping("/getRoleAll")
    @ResponseBody
    public List<Role> getRoleAll(){
        return getROLELIST();
    }
    @GetMapping("/roleList")
    public R roleListData(int page,int limit){
        Page page1=new Page();
        page1.setCurrent(page);
        page1.setSize(limit);
        IPage iPage = roleService.page(page1);
        return R.success("查询成功",iPage);
    }

    @PostMapping("/updateRole")
    public R updateRole(int id,String field,String fieldValue){
        Role role=roleService.getById(id);
        if(role!=null){
            if(field.equals("rolename")){
                role.setRolename(fieldValue);
            }else{
                role.setRolenamecn(fieldValue);
            }
            roleService.updateById(role);
            ROLELIST=roleService.list();
            return R.success("更新成功！");
        }
        return R.error("更新错误！");
    }

    @PostMapping("/roleConfig/{roleid}")
    @Transactional
    public R roleConfig(@RequestBody List<MenuTree> checkData, @PathVariable("roleid") int roleid){
        roleMenuService.deleteByRoleId(roleid);
        List<RoleMenu> list=new ArrayList<RoleMenu>();
        getMenuTreeForRoleMenu(checkData,roleid,list);
        roleMenuService.saveBatch(list);
        return R.success("配置成功！请退出登陆后重试！");
    }

    public  void getMenuTreeForRoleMenu(List<MenuTree> checkData,int roleid,List<RoleMenu> list){
        for (MenuTree checkDatum : checkData) {
            RoleMenu roleMenu=new RoleMenu();
            roleMenu.setRoleid(roleid);
            roleMenu.setMenuid(checkDatum.getId());
            if(checkDatum.getChildren()!=null&&checkDatum.getChildren().size()>0){
                getMenuTreeForRoleMenu(checkDatum.getChildren(),roleid,list);
            }
            list.add(roleMenu);
        }
    }
    @PostMapping("/deleteRole")
    public R deleteRole(int id){
            boolean b = roleService.removeById(id);
            if(b){
                roleMenuService.deleteByRoleId(id);
                userRoleService.deleteByRoleId(id);
                ROLELIST=roleService.list();
                return R.success("删除成功！");
            }else{
                return R.error("删除失败！");
            }
    }

    @PostMapping("/addRole")
    public R addRole(Integer roleId,String  rolename,String rolenamecn){
        Role role=null;
        if(roleId!=null){
            role=roleService.getById(roleId);
        }else{
            role=new Role();
        }
        role.setRolename(rolename);
        role.setRolenamecn(rolenamecn);
        roleService.saveOrUpdate(role);
        ROLELIST=roleService.list();
        return R.success("更新成功!");
    }
}
