package sms.com.bean;

import android.database.Cursor;

/**
 * Author   : luweicheng on 2017/4/27 0027 11:47
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 会话bean
 */

public class Conversation {
    public String getSnipped() {
        return snipped;
    }

    public void setSnipped(String snipped) {
        this.snipped = snipped;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public String getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(String msg_count) {
        this.msg_count = msg_count;
    }

    private String snipped;
    private int thread_id;
    private String msg_count;
    private Long date;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "snipped='" + snipped + '\'' +
                ", thread_id='" + thread_id + '\'' +
                ", msg_count='" + msg_count + '\'' +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    private String address;

    public static Conversation createObjfromCurson(Cursor cursor) {
        Conversation conversation = new Conversation();
        conversation.setSnipped(cursor.getString(cursor.getColumnIndex("snippet")));
        conversation.setThread_id(cursor.getInt(cursor.getColumnIndex("_id")));
        conversation.setMsg_count(cursor.getString(cursor.getColumnIndex("msg_count")));
        conversation.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        conversation.setDate(cursor.getLong(cursor.getColumnIndex("date")));
        return conversation;
    }
}