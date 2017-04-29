package sms.com.utils;

import android.util.Log;

/**
 * Author   : luweicheng on 2017/4/27 0027 10:50
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:
 */

public class L {

    public static final Boolean debug = true;


    public static void i(Object obj,String message){
        if(debug){
            Log.i(obj.getClass().getSimpleName(), message);
        }
    }
}
