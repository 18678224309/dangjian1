package com.jtfu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtfu.entity.Exam;
import com.jtfu.service.IExamService;
import com.jtfu.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/exam")
public class ExamController {


    @Autowired
    IExamService examService;

    private String prefix="/exam";

    @GetMapping("/examList.html")
    public String examList(){
        return prefix+"/examList";
    };

    @GetMapping("/examEdit.html")
    public String examEdit(){
        return prefix+"/examEdit";
    }

    @GetMapping("/examPage.html")
    public String examPage(){
        //获得当前登录用户的id，查询考试记录本季度是否进行过考试。
        //没有考试才允许继续；
        //随机从题库拿10个题组织试卷，进行考试.
        return prefix+"/examPage";
    }



    @GetMapping("/examList")
    @ResponseBody
    public R examListData(int page, int limit){
        Page page1=new Page();
        page1.setCurrent(page);
        page1.setSize(limit);
        IPage iPage = examService.page(page1);
        return R.success("查询成功",iPage);
    }


    @PostMapping("/updateExam")
    @ResponseBody
    public R updateExam(@RequestBody Exam exam){
        if(exam.getId()!=null){
            //更新操作
            Exam byId = examService.getById(exam.getId());
            if(byId!=null){
                byId.setExamtitle(exam.getExamtitle());
                byId.setResulta(exam.getResulta());
                byId.setResultb(exam.getResultb());
                byId.setResultc(exam.getResultc());
                byId.setResultd(exam.getResultd());
                byId.setResult(exam.getResult());
                byId.setCreatetime(new Date());
                examService.updateById(byId);
                return R.success();
            }
            return R.error();
        }else{
            //添加操作
            Exam examCu=new Exam();
            examCu.setExamtitle(exam.getExamtitle());
            examCu.setResulta(exam.getResulta());
            examCu.setResultb(exam.getResultb());
            examCu.setResultc(exam.getResultc());
            examCu.setResultd(exam.getResultd());
            examCu.setResult(exam.getResult());
            examCu.setCreatetime(new Date());
            examService.save(examCu);
            return R.success();
        }
    }

    @PostMapping("/deleteExam")
    @ResponseBody
    public R deleteExam(Integer id){
        if(id!=null){
            Exam byId = examService.getById(id);
            if(byId!=null){
                examService.removeById(byId);
            }
            return R.success();
        }
        return R.error();
    }
}
