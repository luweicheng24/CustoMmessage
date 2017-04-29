package sms.com.globle;

import android.net.Uri;

/**
 * Author   : luweicheng on 2017/4/27 0027 10:26
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 全局变量
 */

public class Constant {
    public interface URI{
        Uri URI_SMS_CONVERSATION = Uri.parse("content://sms/conversations");//查询短信
        Uri URI_SMS_DELETE = Uri.parse("content://sms");//删除短信
    }
}
