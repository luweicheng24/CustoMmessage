package sms.com.dao;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import sms.com.adapter.ConversationAdapter;

/**
 * Author   : luweicheng on 2017/4/27 0027 11:08
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 异步查询Handler
 */

public class SimpleQueryHandler extends AsyncQueryHandler {


    public SimpleQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        /*super.onQueryComplete(token, cookie, cursor);
        CursorUtils.printCursor(cursor);*/
        if(cookie!=null&&cookie instanceof ConversationAdapter){
            /*数据以改变马上对ListView重新布局*/
           ((ConversationAdapter)cookie).changeCursor(cursor);
        }
    }
}
