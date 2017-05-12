package sms.com.dao;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

import sms.com.globle.Constant;

import static sms.com.receiver.SmsReceiverBroadCast.SMS_RECEIVER_ACTION;
import static sms.com.receiver.SmsSendBroadCastReceiver.ACTION_SEND_SMS;

/**
 * Author   : luweicheng on 2017/5/9 0009 08:53
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 短信管理
 */

public class SmsDao {
    public static void sendSms(Context context,String address,String msg){
        SmsManager smsManager = SmsManager.getDefault();
        Intent intent = new Intent(ACTION_SEND_SMS);//创建一个Intent广播发送者的Action
        PendingIntent pendIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Intent intent2 = new Intent(SMS_RECEIVER_ACTION);//创建一个广播接受者的广播
        PendingIntent pendIntent2 = PendingIntent.getBroadcast(context,0,intent2,PendingIntent.FLAG_ONE_SHOT);
        // 将短信内容分割成数组
        ArrayList<String> sms =  smsManager.divideMessage(msg);
        for (String  text:sms) {
            //只负责发送短信，不会将数据插入数据库
            smsManager.sendTextMessage(address,null,text,pendIntent,null);
            ContentValues value = new ContentValues();
            //插数据到数据库
            value.put("type",1);
            value.put("address",address);
            value.put("body",text);
            context.getContentResolver().insert(Constant.URI.URI_SMS_DELETE,value);
        }
    }
}
