package sms.com.globle;

import android.net.Uri;

import sms.com.provider.GroupsProvider;

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
    public interface PROVIDE{
        Uri URI_INSERT_GROUP = Uri.parse("content://"+ GroupsProvider.AUTHORITES+"/groups/insert");
        Uri URI_DELETE_GROUP = Uri.parse("content://"+ GroupsProvider.AUTHORITES+"/groups/delete");
        Uri URI_QUERY_GROUP = Uri.parse("content://"+ GroupsProvider.AUTHORITES+"/groups/query");
        Uri URI_UPDATE_GROUP = Uri.parse("content://"+ GroupsProvider.AUTHORITES+"/groups/update");

        Uri URI_INSERT_THREAD = Uri.parse("content://" + GroupsProvider.AUTHORITES + "/thread/insert");
        Uri URI_DELETE_THREAD = Uri.parse("content://" + GroupsProvider.AUTHORITES + "/thread/delete");
        Uri URI_QUERY_THREAD = Uri.parse("content://" + GroupsProvider.AUTHORITES + "/thread/query");
        Uri URI_UPDATE_THREAD = Uri.parse("content://" + GroupsProvider.AUTHORITES + "/thread/update");

    }
}
