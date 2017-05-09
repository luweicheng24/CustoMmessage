package sms.com.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sms.com.utils.L;

/**
 * Author   : luweicheng on 2017/5/9 0009 09:41
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 短信是否被接受成功的广播
 */

public class SmsReceiverBroadCast extends BroadcastReceiver {

    public static final String SMS_RECEIVER_ACTION ="com.luweicheng.smsreceiver.action";
    @Override
    public void onReceive(Context context, Intent intent) {
        int code  = getResultCode();
        if(code == Activity.RESULT_OK){
            L.i(this,"短信接受成功");
        }else{
            L.i(this,"短信接受失败");
        }
    }
}
