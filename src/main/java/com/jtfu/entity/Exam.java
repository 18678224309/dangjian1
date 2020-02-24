package com.jtfu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

public class Exam implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String examtitle;

    private String resulta;

    private String resultb;

    private String resultc;

    private String resultd;

    private String result;

    private Date createtime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamtitle() {
        return examtitle;
    }

    public void setExamtitle(String examtitle) {
        this.examtitle = examtitle;
    }

    public String getResulta() {
        return resulta;
    }

    public void setResulta(String resulta) {
        this.resulta = resulta;
    }

    public String getResultb() {
        return resultb;
    }

    public void setResultb(String resultb) {
        this.resultb = resultb;
    }

    public String getResultc() {
        return resultc;
    }

    public void setResultc(String resultc) {
        this.resultc = resultc;
    }

    public String getResultd() {
        return resultd;
    }

    public void setResultd(String resultd) {
        this.resultd = resultd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", examtitle='" + examtitle + '\'' +
                ", resulta='" + resulta + '\'' +
                ", resultb='" + resultb + '\'' +
                ", resultc='" + resultc + '\'' +
                ", resultd='" + resultd + '\'' +
                ", result='" + result + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
