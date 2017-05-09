package sms.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import sms.com.R;
import sms.com.adapter.SmsDetailAdapter;
import sms.com.base.BaseActivity;
import sms.com.dao.ContactDao;
import sms.com.dao.SimpleQueryHandler;
import sms.com.dao.SmsDao;
import sms.com.globle.Constant;
import sms.com.utils.L;

/**
 * Author   : luweicheng on 2017/5/8 0008 13:59
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 短信详情页面
 */

public class SmsDetailActivity extends BaseActivity {
    private ListView lv_smsDetail;
    private EditText et_content;
    private Button but_send;
    private ImageView iv_back;
    private TextView tv_title;
    private String address;
    private int thread_id;
    private SmsDetailAdapter adapter;
    private SimpleQueryHandler queryHandler;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_smsdetail);
        lv_smsDetail = (ListView) findViewById(R.id.lv_conversation_detail);
        et_content = (EditText) findViewById(R.id.et_conversation_detail);
        but_send = (Button) findViewById(R.id.bt_conversation_detail_send);
        initTitleBar();
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar() {
        iv_back = (ImageView) findViewById(R.id.but_title_back);
        tv_title = (TextView) findViewById(R.id.tv_titlebar_title);

    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        thread_id = intent.getIntExtra("thread_id", 0);
        String name = ContactDao.getNameByAddress(getContentResolver(), address);
        if (name != null) {
            tv_title.setText(name);
        } else {
            tv_title.setText(address);
        }
        L.i(this, "地址：" + address + "会话id==" + thread_id);


        adapter = new SmsDetailAdapter(this, null,lv_smsDetail);
        lv_smsDetail.setAdapter(adapter);
        //按照会话id查询属于该会话的所有短信
        String[] projection = {
                "_id",
                "body",
                "type",
                "date"
        };
        String selection = "thread_id = " + thread_id;

        //异步查询短信
        queryHandler = new SimpleQueryHandler(getContentResolver());
        queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_DELETE, projection, selection, null, "date");

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        but_send.setOnClickListener(this);
    }

    @Override
    protected void processListener(View v) {
        switch (v.getId()) {
            case R.id.but_title_back:
                finish();
                break;
            case R.id.bt_conversation_detail_send:
               String content = et_content.getText().toString().trim();
                if(!TextUtils.isEmpty(content)) {
//                   发送短信
                    SmsDao.sendSms(this,address,content);
                    et_content.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
        }
    }
    }

