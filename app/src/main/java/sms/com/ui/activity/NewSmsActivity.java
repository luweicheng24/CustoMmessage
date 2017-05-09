package sms.com.ui.activity;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import sms.com.R;
import sms.com.base.BaseActivity;

/**
 * Author   : luweicheng on 2017/5/9 0009 11:11
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:  新建新短信的Activity
 */

public class NewSmsActivity extends BaseActivity {
    private AutoCompleteTextView tv_phone;
    private ImageView iv_contact;
    private Button but_send;
    private EditText et_content;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_newsms_layout);
        tv_phone = (AutoCompleteTextView) findViewById(R.id.tv_newsms_phone);
        iv_contact = (ImageView) findViewById(R.id.iv_newsms_relative);
        but_send = (Button) findViewById(R.id.but_newsms_send);
        et_content = (EditText) findViewById(R.id.et_newsms_content);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
         iv_contact.setOnClickListener(this);
         but_send.setOnClickListener(this);
    }

    @Override
    protected void processListener(View v) {
            switch (v.getId()){
                case R.id.iv_newsms_relative:

                    break;
                case R.id.but_newsms_send:

                    break;
            }
    }
}
