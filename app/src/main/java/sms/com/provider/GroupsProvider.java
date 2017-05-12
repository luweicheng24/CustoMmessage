package sms.com.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import sms.com.db.GroupOpenHelper;

/**
 * Author   : luweicheng on 2017/5/10 0010 18:02
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 本地内容提供者
 */

public class GroupsProvider extends ContentProvider {


    private GroupOpenHelper dbHelper;
    private SQLiteDatabase db;
    public static final String AUTHORITES = "com.gsww.sms";//主机名

    public final int CODE_GROUPS_INSERT = 0X00;
    public final int CODE_GROUPS_DELETE = 0X01;
    public final int CODE_GROUPS_QUERY = 0X02;
    public final int CODE_GROUPS_UPDATE = 0X04;

    public final int CODE_THREAD_INSERT = 0X05;
    public final int CODE_THREAD_DELETE = 0X06;
    public final int CODE_THREAD_QUERY = 0X07;
    public final int CODE_THREAD_UPDATE = 0X07;
    //群组匹配uri
    public final String Group_INSERT_URI = "groups/insert";
    public final String Group_DELETE_URI = "groups/delete";
    public final String Group_QUERY_URI = "groups/query";
    public final String Group_UPDATE_URI = "groups/update";
    // 会话匹配uri
    public final String THREAD_INSERT_URI = "thread/insert";
    public final String THREAD_DELETE_URI = "thread/delete";
    public final String THREAD_QUERY_URI = "thread/query";
    public final String THREAD_UPDATE_URI = "thread/update";
    // 表名
    public static String GROUP_TABLE = "groups";
    public static String THREAD_TABLE = "thread";

    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);//表示不匹配任何uri

    //      添加匹配规格
    {
    //       插入一个群组
            uriMatcher.addURI(AUTHORITES, Group_INSERT_URI, CODE_GROUPS_INSERT);
    //         删除一个群组
            uriMatcher.addURI(AUTHORITES, Group_DELETE_URI, CODE_GROUPS_DELETE);
    //        查询数组
            uriMatcher.addURI(AUTHORITES, Group_QUERY_URI, CODE_GROUPS_QUERY);
            uriMatcher.addURI(AUTHORITES, Group_UPDATE_URI, CODE_GROUPS_UPDATE);

     //       插入一个会话
            uriMatcher.addURI(AUTHORITES, THREAD_INSERT_URI, CODE_THREAD_INSERT);
    //           删除一个会话
            uriMatcher.addURI(AUTHORITES, THREAD_DELETE_URI, CODE_THREAD_DELETE);
    //        查询一个会话
        uriMatcher.addURI(AUTHORITES, THREAD_QUERY_URI, CODE_THREAD_QUERY);
        uriMatcher.addURI(AUTHORITES, THREAD_UPDATE_URI, CODE_THREAD_UPDATE);
    }

    /**
     * 当第一次调用该内容提供者初始化
     *
     * @return true 表示完成初始化 false 未完成初始化工作
     */
    @Override
    public boolean onCreate() {
        dbHelper = GroupOpenHelper.getInstance(getContext());
        db = dbHelper.getWritableDatabase();
        return db == null ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case CODE_GROUPS_QUERY:
                return db.query(GROUP_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_THREAD_QUERY:
                return db.query(THREAD_TABLE,projection,selection,selectionArgs,null,null,sortOrder);
            default:
                throw new IllegalArgumentException("uri查询匹配失败" + uri);
        }

    }

    //获取MIMI类型
   //   vnd.android.cursor.item(单条)|dir(多条)/自定义标记 google固定的item表示单条数据
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        /*int code = uriMatcher.match(uri);
        switch (code) {
            case CODE_GROUPS_DELETE:
                return "vnd.android.cursor.item/group/delete";

            case CODE_GROUPS_INSERT:
                return "vnd.android.cursor.item/group/insert";
            default:
                throw new IllegalArgumentException("未知url" + uri);
        }*/
        return "";

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case CODE_GROUPS_INSERT:
                long rowId = db.insert(GROUP_TABLE, null, values);
                if (rowId == -1) {
                    return null;
                } else {
                    return ContentUris.withAppendedId(uri, rowId);//将行id拼接在Uri后面
                }
            case CODE_THREAD_INSERT:
                long rowId1 = db.insert(THREAD_TABLE, null, values);
                if (rowId1 == -1) {
                    return null;
                } else {
                    return ContentUris.withAppendedId(uri, rowId1);//将行id拼接在Uri后面
                }
            default:
                throw new IllegalArgumentException("uri插入匹配失败" + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case CODE_GROUPS_DELETE:
                return db.delete(GROUP_TABLE, selection, selectionArgs);
            case CODE_THREAD_DELETE:
                return db.delete(THREAD_TABLE, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("uri删除uri不匹配" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case CODE_GROUPS_UPDATE:
                return db.update(GROUP_TABLE, values, selection,selectionArgs);
            case CODE_THREAD_UPDATE:
                return db.update(THREAD_TABLE,values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("uri删除uri不匹配" + uri);
        }

    }
}
