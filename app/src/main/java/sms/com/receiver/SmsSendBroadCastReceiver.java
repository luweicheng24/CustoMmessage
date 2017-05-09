package sms.com.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Author   : luweicheng on 2017/5/9 0009 09:00
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:发送短信接收类
 */

public class SmsSendBroadCastReceiver extends BroadcastReceiver {
    public static final String ACTION_SEND_SMS = "com.luweicheng.sendsms";

    @Override
    public void onReceive(Context context, Intent intent) {
        int code  = getResultCode();
        if(code == Activity.RESULT_OK)
        {
            Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
        }
    }
}
