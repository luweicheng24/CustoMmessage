package sms.com.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sms.com.R;
import sms.com.adapter.ConversationAdapter;
import sms.com.base.BaseActivity;
import sms.com.bean.Conversation;
import sms.com.dao.SimpleQueryHandler;
import sms.com.globle.Constant;
import sms.com.utils.L;

/**
 * Author   : luweicheng on 2017/5/11 0011 16:41
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 群组会话Activity
 */

public class GroupThreadActivity extends BaseActivity {
    private ListView lv;
    private ImageView back;
    private TextView tv_title;
    private ConversationAdapter adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_groups_thread);
        lv = (ListView) findViewById(R.id.lv_groups_thread);
        initTitleBar();
    }

    private void initTitleBar() {
        back = (ImageView) findViewById(R.id.but_title_back);
        tv_title = (TextView) findViewById(R.id.tv_titlebar_title);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        int group_id = intent.getIntExtra("group_id", 0);
        String title = intent.getStringExtra("title");
        L.i(this, "group_id=" + group_id + " title == " + title);
        tv_title.setText(title);
        //查询该群组的所有会话id

        String[] projection  = {
                "thread_id"
        };
        String selection = "group_id = "+group_id;
        Cursor cursor  = getContentResolver().query(Constant.PROVIDE.URI_QUERY_THREAD,projection,selection,null,null);
        int count = cursor.getCount();
        if(count == 0){
            return;
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("(");
        List<Integer> thread_ids = new ArrayList(count);
        while (cursor.moveToNext()){
             int id = cursor.getInt(cursor.getColumnIndex("thread_id"));
//             thread_ids.add(id);
            strBuilder.append(id+",");

         }
        L.i(this,"********************"+strBuilder.toString());
        strBuilder.deleteCharAt(strBuilder.length()-1);
        L.i(this,"********************"+strBuilder.toString());

        strBuilder.append(")");
        L.i(this,"********************"+strBuilder.toString());

         adapter = new ConversationAdapter(this,null,true);
        lv.setAdapter(adapter);

        String[] params = {
                "sms.body AS snippet",
                "sms.thread_id AS _id",//必须含有_id字段
                "groups.msg_count AS msg_count",
                "address AS address",
                "date AS date"

        };
        SimpleQueryHandler handler = new SimpleQueryHandler(getContentResolver());
        // 根据群组中的所有会话id查询短信内容
        handler.startQuery(0,adapter, Constant.URI.URI_SMS_CONVERSATION,params,"thread_id in "+strBuilder.toString(),null,"date");
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到短信详情页面
                Cursor cursor = adapter.getCursor();//获取当前cursor不用移动
                Conversation conversation = Conversation.createObjfromCurson(cursor);
                Intent intent = new Intent(GroupThreadActivity.this, SmsDetailActivity.class);
                intent.putExtra("address", conversation.getAddress());
                intent.putExtra("thread_id", conversation.getThread_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processListener(View v) {
        switch (v.getId()) {
            case R.id.but_title_back:
                finish();
                break;
        }
    }
}
