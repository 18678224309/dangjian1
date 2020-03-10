package com.jtfu.controller;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.define.FileType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtfu.config.MyAuthRealm;
import com.jtfu.entity.Menu;
import com.jtfu.entity.Role;
import com.jtfu.entity.User;
import com.jtfu.entity.UserRole;
import com.jtfu.mapper.UserMapper;
import com.jtfu.service.IUserRoleService;
import com.jtfu.service.IUserService;
import com.jtfu.util.Doc2Pdf;
import com.jtfu.util.R;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
@RequestMapping("/user")
public class UserController {

    public static IUserService userService;

    public UserController(IUserService userService){
        this.userService=userService;
    }
    @Autowired
    IUserRoleService userRoleService;


    private String prefix="/user";

    @GetMapping("/index.html")
    @RequiresPermissions({"ADMIN"})
    public String index(){
        return prefix+"/index";
    }
    @GetMapping("/Home.html")
    public String Home(){
        return prefix+"/Home";
    }
    @GetMapping("/login.html")
    public String login(){
        return prefix+"/login";
    }
    @GetMapping("/unauthorized.html")
    public String unauthorized(){
        return prefix+"/unauthorized";
    }
    @GetMapping("/userList.html")
    public String userList(){return prefix+"/userList";}
    @GetMapping("/userEdit.html")
    public String userEdit(){return prefix+"/userEdit";}
    @GetMapping("/userCenter.html")
    public String userCenter(){
        return prefix+"/userCenter";
    }

    @GetMapping("/edit")
    public void edit(){
        System.out.println("访问了edit接口,,....");
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
    }

    @GetMapping("/userList")
    @ResponseBody
    public R userListData(int page,int limit,User user){
        QueryWrapper queryWrapper=new QueryWrapper();
        if(!StringUtils.isEmpty(user.getName())){
            queryWrapper.like("name",user.getName());
        }
        Page page1=new Page();
        page1.setCurrent(page);
        page1.setSize(limit);
        IPage iPage = userService.page(page1,queryWrapper);
        return R.success("查询成功",iPage);
    }


    @PostMapping("/updateUser")
    @ResponseBody
    public R updateUser(@RequestBody User user){
        if(user.getId()!=null){
            User byId = userService.getById(user.getId());
            if(byId!=null){
                byId.setUsername(user.getIdcard());
                byId.setAddress(user.getAddress());
                byId.setAge(user.getAge());
                byId.setName(user.getName());
                byId.setPhone(user.getPhone());
                byId.setSex(user.getSex());
                byId.setRoleid(user.getRoleid());
                byId.setIdcard(user.getIdcard());
                UserRole userRoleByUserId = userRoleService.getUserRoleByUserId(user.getId());
                if(userRoleByUserId==null){
                    UserRole userRole=new UserRole();
                    userRole.setRoleid(user.getRoleid());
                    userRole.setUserid(user.getId());
                    userRoleService.save(userRole);
                }else{
                    userRoleByUserId.setRoleid(byId.getRoleid());
                    userRoleService.updateById(userRoleByUserId);
                }
                userService.updateById(byId);
            }
        }else{
            QueryWrapper query=new QueryWrapper();
            query.eq("username",user.getIdcard());
            query.select("id");
            User yan = userService.getOne(query);
            if(yan!=null){
                return R.error("当前用户名已存在，添加失败！");
            }
            String idcard=user.getIdcard();
            user.setUsername(idcard);
            user.setPassword(idcard.substring(idcard.length()-6));
            user.setImgurl("");
            user.setDelflag(0);
            UserRole userRole=new UserRole();
            userRole.setRoleid(user.getRoleid());
            userService.save(user);
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("username",user.getUsername());
            queryWrapper.eq("password",user.getPassword());
            queryWrapper.select("id");
            User one = userService.getOne(queryWrapper);
            userRole.setUserid(one.getId());
            userRoleService.save(userRole);
        }
        return R.success();
    }


    @PostMapping("/deleteUser")
    @ResponseBody
    public R deleteUser(Integer id){
        User user=userService.getById(id);
        if(user!=null){
            UserRole userRoleByUserId = userRoleService.getUserRoleByUserId(id);
            if(userRoleByUserId!=null){
                userRoleService.removeById(userRoleByUserId);
            }
            userService.removeById(user);
            return R.success();
        }
        return R.error();
    }

    @PostMapping("/loginUser")
    @ResponseBody
    public R loginUser(String username, String password){
        User byUsernameAndPassword = userService.findByUsernameAndPassword(username, password);
        if(byUsernameAndPassword!=null){
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(token);
                User user = (User) subject.getPrincipal();
                subject.getSession().setAttribute("userInfo",user);
                List<Role> roleSet = user.getRoles();
                List<String> permissionList=new ArrayList<String>();
                if (CollectionUtils.isNotEmpty(roleSet)) {
                    for(Role role : roleSet) {
                        List<Menu> menuSet = role.getMenus();
                        if (CollectionUtils.isNotEmpty(menuSet)) {
                            MyAuthRealm.setPermissions(permissionList,menuSet);
                        }
                    }
                }
                subject.getSession().setAttribute("permissionList", JSONObject.toJSONString(permissionList));
                return R.success();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return R.error("用户名或密码错误！");
    }



    @PostMapping("/pdfUpload")
    @ResponseBody
    public R pdfUpload(MultipartFile file,Integer id,HttpServletRequest request) throws Exception{
        if(id!=null){
            User byId = userService.getById(id);
            if(byId!=null){
                String staticPath=request.getRealPath("static");
                String pdfPath="/pdfjs/web/pdf/";
                String suffix = FileType.getSuffixByFilename(file.getOriginalFilename());
                String pdfName= byId.getUsername()+suffix;
                File dirPath=new File(staticPath+pdfPath);
                if(!dirPath.exists()){
                    dirPath.mkdirs();
                }
                FileOutputStream outputStream=new FileOutputStream(new File(staticPath+pdfPath+pdfName));
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                byId.setPdfurl("/static"+pdfPath+pdfName);
                userService.updateById(byId);
                return R.success("上传成功");
            }
        }
        return R.error("上传失败");
    }

    @PostMapping("/imgUpload")
    @ResponseBody
    public R imgUpload(MultipartFile file,Integer id,HttpServletRequest request)throws Exception{
        if(id!=null){
            User byId = userService.getById(id);
            if(byId!=null){
                String staticPath=request.getRealPath("static");
                String imgPath="/head/photo/";
                String suffix = FileType.getSuffixByFilename(file.getOriginalFilename());
                String imgName= byId.getUsername()+suffix;
                File dirPath=new File(staticPath+imgPath);
                if(!dirPath.exists()){
                    dirPath.mkdirs();
                }
                FileOutputStream outputStream=new FileOutputStream(new File(staticPath+imgPath+imgName));
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                byId.setImgurl("/static"+imgPath+imgName);
                User user= (User) SecurityUtils.getSubject().getPrincipal();
                user.setImgurl(byId.getImgurl());
                userService.updateById(byId);
                return R.success("上传成功");
            }
        }
        return R.error("上传失败");
    }


    @PostMapping("/wordUpload")
    @ResponseBody
    public R wordUpload(MultipartFile file,Integer id,HttpServletRequest request)throws Exception{
        if(id!=null){
            User byId = userService.getById(id);
            if(byId!=null){
                String staticPath=request.getRealPath("static");
                String pdfPath="/pdfjs/web/word/";
                String suffix = replaceDocx(FileType.getSuffixByFilename(file.getOriginalFilename()));
                String pdfName= byId.getUsername()+suffix;
                File dirPath=new File(staticPath+pdfPath);
                if(!dirPath.exists()){
                    dirPath.mkdirs();
                }
                Doc2Pdf.doc2pdf(file.getInputStream(),staticPath+pdfPath+pdfName);
                byId.setWordurl("/static"+pdfPath+pdfName);
                userService.updateById(byId);
                User user= (User) SecurityUtils.getSubject().getPrincipal();
                user.setImgurl(byId.getImgurl());
                return R.success("上传成功");
            }
        }
        return R.error("上传失败");
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public R changePssword(String oldPassword,String newPassword){
        Subject subject = SecurityUtils.getSubject();
        User user= (User) subject.getPrincipal();
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            userService.updateById(user);
            return R.success();
        }
       return R.error();
    }


    static String replaceDocx(String docx){
        docx=docx.replaceAll("docx","pdf");
        return docx;
    }

}
