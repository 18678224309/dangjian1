package com.jtfu.util;

import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.Base64Uploader;
import com.baidu.ueditor.upload.BinaryUploader;
import com.baidu.ueditor.upload.Uploader;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyUploader {
    private HttpServletRequest request = null;
    private Map<String, Object> conf = null;
    private MultipartFile upfile=null;
    public MyUploader(HttpServletRequest request, Map<String, Object> conf,MultipartFile upfile) {
        this.request = request;
        this.conf = conf;
        this.upfile=upfile;
    }

    public State doExec() {
        String filedName = (String)this.conf.get("fieldName");
        State state = null;
        if ("true".equals(this.conf.get("isBase64"))) {
            state = Base64Uploader.save(this.request.getParameter(filedName), this.conf);
        } else {
            state = MyBinaryUploader.save(this.request, this.conf,upfile);
        }

        return state;
    }
}
