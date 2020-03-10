package com.jtfu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.FileType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtfu.entity.Imglink;
import com.jtfu.entity.Journalism;
import com.jtfu.entity.User;
import com.jtfu.service.IImgLinkService;
import com.jtfu.service.IJournalismService;

import com.jtfu.util.R;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/journalism")
public class JournalismController {

    @Autowired
    IJournalismService journalismService;

    @Autowired
    IImgLinkService imgLinkService;

    private String prefix="/journalism";

    @GetMapping("/journalismList.html")
    public String JournalismList(){
        return prefix+"/journalismList";
    }

    @GetMapping("/journalismAdd.html")
    public String JournalismEdit(){
        return prefix+"/journalismAdd";
    }

    @GetMapping("/modelPage.html")
    public String modelPage(Integer type,Model model){
        model.addAttribute("type",type);
        return prefix+"/modelPage";
    }

    @GetMapping("/detail.html")
    public String detailHtml(Integer id, Model model){
        Journalism byId = journalismService.getById(id);
        model.addAttribute("model",byId);
        return prefix+"/detail";
    }

    @GetMapping("/imgLinkJour.html")
    public  String imgLinkJour(){return prefix+"/imgLinkJour";}

    @GetMapping("/journalismList")
    @ResponseBody
    public R journalismListData(int page, int limit,Journalism journalism){
        QueryWrapper queryWrapper=new QueryWrapper();
        if(journalism.getTitle()!=null&&!journalism.getTitle().equals("")){
            queryWrapper.like("title",journalism.getTitle());
        }
        if(journalism.getType()!=null){
            queryWrapper.eq("type",journalism.getType());
        }
        Page page1=new Page();
        page1.setCurrent(page);
        page1.setSize(limit);
        IPage iPage = journalismService.page(page1,queryWrapper);
        return R.success("查询成功",iPage);
    }

    @PostMapping("/updateHot")
    @ResponseBody
    public R updateHot(Integer id){
        if(id!=null){
            Journalism byId = journalismService.getById(id);
            if(byId!=null){
                if(byId.getType()==7){
                    return R.success("此文章已是热点文章！");
                }
                QueryWrapper wrapper=new QueryWrapper();
                wrapper.eq("type",7);
                wrapper.orderByAsc("createtime");
                List<Journalism> list = journalismService.list(wrapper);
                //如果热点文章超出10个，则根据创建时间将最后一个剔除热点。
                if(list.size()==10){
                    Journalism journalism = list.get(0);
                    journalism.setType(7);
                    journalismService.updateById(journalism);
                }else{
                    byId.setType(7);
                    journalismService.updateById(byId);
                }
                return R.success();
            }
        }
        return R.error();
    }

    @PostMapping("/deleteJournalism")
    @ResponseBody
    public R deleteJournalism(Integer id){
        if(id!=null){
             Journalism byId = journalismService.getById(id);
            if(byId!=null){
                journalismService.removeById(byId);
            }
            return R.success();
        }
        return R.error();
    }


    @PostMapping("/uploadImage")
    public R uploadImage(MultipartFile file,HttpServletRequest request) throws Exception {
        String rootPath = request.getSession()
                .getServletContext().getRealPath("/");
        String s = uploadImg(rootPath, file);
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String url = (String) parse.get("url");
        Imglink imgLink=new Imglink();
        imgLink.setImgurl(url);
        imgLinkService.save(imgLink);
        return R.success("url",url);
    }


    @RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "upfile",required = false) MultipartFile upfile)
            throws Exception {
        response.setContentType("application/json");
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getSession()
                .getServletContext().getRealPath("/");
        //该参数表示请求的功能，如值为 uploadimage 表示上传图片
        String action=request.getParameter("action");
        try {
            PrintWriter writer = response.getWriter();
            if(action.equals("uploadimage")){
                writer.write(uploadImg(rootPath,upfile));
                writer.flush();
                writer.close();
            }
            String exec = new ActionEnter(request, rootPath).exec();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String uploadImg(String rootPath,MultipartFile medias)throws Exception{
        //按照规定格式返回json数据即可
        Map<String, Object> map = new HashMap();
        map.put("size", medias.getSize());
        map.put("type", medias.getContentType());
        List<String> images = new ArrayList<String>();
        String suffix = FileType.getSuffixByFilename(medias.getOriginalFilename());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        String ueditorPath="/ueditor/jsp/upload/image/"+dateFormat.format(new Date());
        String savePath = "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}"+suffix;
        savePath=PathFormat.parse(savePath, "");
        File file=new File(rootPath+ueditorPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String url = rootPath+savePath;
        FileOutputStream outputStream=new FileOutputStream(new File(url));
        outputStream.write(medias.getBytes());
        outputStream.flush();
        outputStream.close();
        images.add(url);
        map.put("original",url.substring(url.lastIndexOf("/")+1));
        map.put("state", "SUCCESS");
        map.put("url", savePath);
        System.err.println(map);
        return JSON.toJSONString(map);
    }


    @PostMapping("/addJournalism")
    @ResponseBody
    public R addJournalism(@RequestBody Journalism journalism){
        if(journalism.getId()!=null){
            Journalism byId = journalismService.getById(journalism.getId());
            if(byId!=null){
                byId.setTitle(journalism.getTitle());
                byId.setAuth(journalism.getAuth());
                byId.setAuthid(journalism.getAuthid());
                byId.setContent(journalism.getContent());
                byId.setType(journalism.getType());
                byId.setCreatetime(journalism.getCreatetime());
                journalismService.updateById(byId);
                return R.success();
            }
            return R.error();
        }else{
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            journalism.setCreatetime(journalism.getCreatetime());
            journalism.setAuthid(user.getId());
            journalism.setAuth(user.getName());
            journalismService.save(journalism);
        }
        System.out.println(journalism);
        return R.success();
    }

/*    public String uploadBase64Img(String doodling)throws Exception{
        //将字符串转换为byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(doodling.trim());
        //转化为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        //这里我是上传到阿里云进行管理，大家吧这一步改为自己的上传逻辑即可，url图片的全路径
        String imagePath = OssUtil.uploadFileImage(inputStream,".jpg");
        Map<String, Object> map = new HashMap();
        map.put("state", "SUCCESS");
        map.put("url", OssUtil.URL_PATH+imagePath);
        return JSON.toJSONString(map);
    }

    public String uploadVideo(MultipartFile video)throws Exception{
        String name = video.getOriginalFilename();
        String suffix = name.substring(name.lastIndexOf("."));
        //这里我是上传到阿里云进行管理，大家吧这一步改为自己的上传逻辑即可，url图片的全路径
        String imagePath = OssUtil.uploadFileImage(video.getInputStream(),suffix);
        Map<String, Object> map = new HashMap();
        map.put("state", "SUCCESS");
        map.put("url", OssUtil.URL_PATH+imagePath);
        return JSON.toJSONString(map);
    }

    public String uploadNetworkImage(String[] list){
        if(null == list || list.length == 0){
            return null;
        }
        JSONObject result = new JSONObject();
        List resultList = new ArrayList();
        for(String urlStr : list){
            HttpURLConnection connection = null;
            URL url = null;
            String suffix = null;
            try {
                url = new URL(urlStr);
                connection = (HttpURLConnection)url.openConnection();
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(true);
                suffix = MIMEType.getSuffix(connection.getContentType());
                //这里我是上传到阿里云进行管理，大家吧这一步改为自己的上传逻辑即可，url图片的全路径
                String imagePath = OssUtil.uploadFileImage(connection.getInputStream(),suffix);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("state","SUCCESS");
                jsonObject.put("source",urlStr);
                jsonObject.put("url",OssUtil.URL_PATH+imagePath);
                resultList.add(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        result.put("state","SUCCESS");
        result.put("list",resultList);
        return JSON.toJSONString(result);
    }*/
}
