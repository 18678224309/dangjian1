package com.jtfu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class Imglink {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String imgurl;

    private Integer jourid;

    private Integer type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getJourid() {
        return jourid;
    }

    public void setJourid(Integer jourid) {
        this.jourid = jourid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
