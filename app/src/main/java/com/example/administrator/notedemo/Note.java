package com.example.administrator.notedemo;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/5/3.
 */
@Entity
public class Note {
    //商品名称 (unique 表示该属性必须在数据库中是唯一的值)
    @Unique
    private String title;
    //内容(可以自定义字段名，注意外键不能使用该属性)
    private String content;
    //图片url
    private String image_url;
    @Generated(hash = 1754236140)
    public Note(String title, String content, String image_url) {
        this.title = title;
        this.content = content;
        this.image_url = image_url;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImage_url() {
        return this.image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
