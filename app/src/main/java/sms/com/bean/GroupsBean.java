package sms.com.bean;

import android.database.Cursor;

/**
 * Author   : luweicheng on 2017/5/11 0011 11:46
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:  群组的实体bean
 */

public class GroupsBean {


    public static GroupsBean createBeanFromCursor(Cursor cursor) {
        GroupsBean bean = new GroupsBean();
        bean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        bean.setName(cursor.getString(cursor.getColumnIndex("name")));
        bean.setDate(cursor.getInt(cursor.getColumnIndex("date")));
        bean.setThread_count(cursor.getInt(cursor.getColumnIndex("thread_count")));
        return bean;
    }

    private int _id;
    private String name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getThread_count() {
        return thread_count;
    }

    public void setThread_count(int thread_count) {
        this.thread_count = thread_count;
    }

    private int date;
    private int thread_count;

}
