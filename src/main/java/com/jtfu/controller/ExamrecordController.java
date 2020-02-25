package com.jtfu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jtfu.entity.Exam;
import com.jtfu.entity.Examrecord;
import com.jtfu.entity.User;
import com.jtfu.service.IExamService;
import com.jtfu.service.IExamrecordService;
import com.jtfu.service.IUserService;
import com.jtfu.util.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/examrecord")
public class ExamrecordController {

    @Autowired
    IExamService examService;

    @Autowired
    IExamrecordService examrecordService;

    @Autowired
    IUserService userService;


    @PostMapping("/addExamrecord")
    @ResponseBody
    public R addExamrecord(@RequestBody Exam[] exams){
        int num=100/exams.length;
        int index=0;
        Subject subject = SecurityUtils.getSubject();
        User user= (User) subject.getPrincipal();
        for (Exam exam : exams) {
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("id",exam.getId());
            wrapper.eq("result",exam.getResult());
            Exam one = examService.getOne(wrapper);
            if(one!=null){
                index++;
            }
        }
        Examrecord examrecord=new Examrecord();

        int examNum=num*index;
        examrecord.setUserid(user.getId());
        examrecord.setUsername(user.getName());
        examrecord.setNum(examNum);
        examrecord.setTime(new Date());
        examrecordService.save(examrecord);
        user.setExamnum(examNum);
        userService.updateById(user);
        return R.success("提交成功！考试得分："+examNum);
    }
}
