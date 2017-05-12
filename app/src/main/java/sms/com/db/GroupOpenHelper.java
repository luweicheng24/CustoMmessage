package sms.com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author   : luweicheng on 2017/5/10 0010 17:41
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 群组数据库帮助类
 */

public class GroupOpenHelper extends SQLiteOpenHelper {
    public static GroupOpenHelper dbHelper;
    //公共字段
    public static String _ID = "_id";
    //群组字段
    public static String THREAD_COUNT = "thread_count";
    public static String Groups_NAME = "name";
    public static String CREATE_DATE = "date";
    //会话字段
    public static String GROUP_ID = "group_id";
    public static String THREAD_ID = "thread_id";
    //表名
    public static String GROUP_TABLE = "groups";
    public static String THREAD_TABLE = "thread";

    /**
     * 因为是客户端不存在多线程问题
     *
     * @param context
     * @return
     */
    public static GroupOpenHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new GroupOpenHelper(context);
        }
        return dbHelper;
    }

    private GroupOpenHelper(Context context) {
        this(context, "group.db", null, 1);
    }

    private GroupOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + GROUP_TABLE + " (" + _ID + " integer primary key autoincrement," +
                Groups_NAME + " varchar," +
                THREAD_COUNT + " integer," +
                CREATE_DATE + " varchar)");
        db.execSQL("create table " + THREAD_TABLE + "(" + _ID + " integer primary key autoincrement," +
                GROUP_ID + " varchar," +
                THREAD_ID + " integer)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //跟新数据库
        db.execSQL("drop table if exists "+GROUP_TABLE);
        db.execSQL("drop table if exists "+THREAD_TABLE);
        onCreate(db);
    }
}
