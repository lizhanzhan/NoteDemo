package com.example.administrator.notedemo;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ContentDao {
    /**
     * 添加数据，如果有重复则覆盖
     */
    public static void insertNote(Note note){
        BaseApplication.getDaoInstant().getNoteDao().insertOrReplace(note);
    }
    /**
     * 删除数据
     */
    public static void deleteNote(Note note) {
        BaseApplication.getDaoInstant().getNoteDao().delete(note);
    }

    /**
     * 查询数据
     * @return
     */
    public static List<Note> queryAll(){
        return BaseApplication.getDaoInstant().getNoteDao().loadAll();
    }
}
