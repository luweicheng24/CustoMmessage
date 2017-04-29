package sms.com.utils;

import android.database.Cursor;

/**
 * Author   : luweicheng on 2017/4/27 0027 10:40
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:
 */

public class CursorUtils {

    public static void printCursor(Cursor cursor) {

        int counts = cursor.getCount();
        L.i(cursor, "一共有： " + counts + "行数据");
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String name= cursor.getColumnName(i);
                String content = cursor.getString(i);
                L.i(cursor, "字段名: ======"+name+"=======内容："+content );
            }
         L.i(cursor,"----------------------------------------------------");
        }
        cursor.close();
    }

}
