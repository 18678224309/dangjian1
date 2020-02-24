package com.jtfu.controller;


import com.jtfu.entity.Menu;
import com.jtfu.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jtfu
 * @since 2020-01-27
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @GetMapping("/getAll")
    public List<Menu> getAll(){
            return menuService.getAll();
    }
}
