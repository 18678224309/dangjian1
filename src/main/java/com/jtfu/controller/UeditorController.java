package com.jtfu.controller;

import com.baidu.ueditor.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping("/ueditor")
public class UeditorController {

    @RequestMapping("/config")
    public void config(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        response.setHeader("Content-Type" , "text/html");
        String rootPath = request.getSession()
                .getServletContext().getRealPath("/");
        try {
            System.err.println(rootPath);
            PrintWriter writer = response.getWriter();
            String exec = new ActionEnter(request, rootPath).exec();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

