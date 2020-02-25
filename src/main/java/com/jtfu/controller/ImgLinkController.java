package com.jtfu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtfu.entity.Imglink;
import com.jtfu.service.IImgLinkService;
import com.jtfu.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/imgLink")
public class ImgLinkController {

    @Autowired
    IImgLinkService imgLinkService;



    @GetMapping("/insertImgLink")
    @ResponseBody
    public R insertImgLink(Imglink imgLink){
        if(imgLink.getImgurl()!=null||!imgLink.getImgurl().equals("")){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("imgurl",imgLink.getImgurl());
            Imglink one = imgLinkService.getOne(wrapper);
            if(one!=null){
                one.setJourid(imgLink.getJourid());
                one.setType(imgLink.getType());
            }
            imgLinkService.updateById(one);
            return R.success();
        }
        return R.error();
    }

    @GetMapping("/getImgLink")
    @ResponseBody
    public R getImgLink(Integer type){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("type",type);
        wrapper.orderByDesc("id");
        Page page=new Page();
        page.setCurrent(0);
        if(type==-1){
            page.setSize(5);
        }else{
            page.setSize(1);
        }
        IPage data0 = imgLinkService.page(page, wrapper);

        return R.success("page",data0);
    };
}
