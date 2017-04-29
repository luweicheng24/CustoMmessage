package sms.com.dao;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.InputStream;

/**
 * Author   : luweicheng on 2017/4/27 0027 15:47
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 查询联系人
 */

public class ContactDao {
    /**
     * 根据手机号码查询联系人
     *
     * @param address
     * @return
     */
    public static String getNameByAddress(ContentResolver resolver, String address) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) {//如果光标为空返回false
            String name = cursor.getString(cursor.getColumnIndex("display_name"));
            cursor.close();
            return name;
        } else {
            cursor.close();
            return address;
        }
    }

    /**
     * 查询联系人的头像
     * @param resolver
     * @param address
     * @return
     */
    public static Bitmap getAvatorByAddress(ContentResolver resolver,String address){
           Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,address);
           Cursor cursor =resolver.query(uri,new String[]{ContactsContract.PhoneLookup._ID},null,null,null);
        if(cursor.moveToFirst()){
            String _id = cursor.getString(0);
            cursor.close();
            InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(resolver,Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,_id));
            Bitmap bmp =  BitmapFactory.decodeStream(is);
            return bmp;
        }
        return null;
    }
}
