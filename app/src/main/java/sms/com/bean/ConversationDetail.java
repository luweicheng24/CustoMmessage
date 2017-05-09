package sms.com.bean;

import android.database.Cursor;

/**
 * Author   : luweicheng on 2017/5/8 0008 15:27
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 短信详情bean
 */

public class ConversationDetail {
    private String body;//短信内容
    private long date;//短信事件
    private int type;//短信种类，是发出的还是收到的
    private int _id;//

    //数据封装成对象后，本类中提供一个静态方法，直接获取本类对象。
    public static ConversationDetail createFromCursor(Cursor cursor){
        ConversationDetail sms = new ConversationDetail();
        sms.setBody(cursor.getString(cursor.getColumnIndex("body")));
        sms.setDate(cursor.getLong(cursor.getColumnIndex("date")));
        sms.setType(cursor.getInt(cursor.getColumnIndex("type")));
        sms.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        return sms;

    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

}
