package sms.com.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sms.com.R;
import sms.com.adapter.NewSmsAdapter;
import sms.com.base.BaseActivity;
import sms.com.dao.SmsDao;
import sms.com.utils.L;

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
    private NewSmsAdapter adapter;
    private ImageView back;
    private TextView title;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_newsms_layout);
        tv_phone = (AutoCompleteTextView) findViewById(R.id.tv_newsms_phone);
        iv_contact = (ImageView) findViewById(R.id.iv_newsms_relative);
        but_send = (Button) findViewById(R.id.but_newsms_send);
        et_content = (EditText) findViewById(R.id.et_newsms_content);
        initTitleBar();
    }

    private void initTitleBar() {
        back = (ImageView) findViewById(R.id.but_title_back);
        title = (TextView) findViewById(R.id.tv_titlebar_title);
        title.setText("发送短信");
    }

    @Override
    protected void initData() {
        adapter = new NewSmsAdapter(this, null);
        //给下拉框设置adapter用来展示下拉框
        tv_phone.setAdapter(adapter);
        tv_phone.setDropDownVerticalOffset(10);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            //每次输入一个数字就会执行查询
            @Override
            public Cursor runQuery(CharSequence constraint) {
                L.i(this, "s输入的数据：" + constraint);
                //模糊查询联系人数据库
                String[] projects = {
                        "_id",//只要使用CursorAdapter适配数据必须查询字段含有_id
                        "data1",//电话号码
                        "display_name"
                };
                String selection = "data1 like '%" + constraint + "%'";
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projects, selection, null, null);
//                CursorUtils.printCursor(cursor);

                return cursor;
            }
        });

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        iv_contact.setOnClickListener(this);
        but_send.setOnClickListener(this);
    }

    @Override
    protected void processListener(View v) {
        switch (v.getId()) {
            case R.id.iv_newsms_relative:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("vnd.android.cursor.dir/contact");
                startActivityForResult(intent, 0);
                break;
            case R.id.but_newsms_send:
                if (!TextUtils.isEmpty(et_content.getText()) || !TextUtils.isEmpty(tv_phone.getText().toString())) {
                    SmsDao.sendSms(this, tv_phone.getText().toString(), et_content.getText().toString());
                    et_content.setText("");
                    tv_phone.setText("");
                    finish();
                } else {
                    Toast.makeText(this, "号码或者内容为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but_title_back:
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String[] projects = {
                "_id",
                "has_phone_number"
        };
        if (data == null) {
            return;
        }
//                不用关闭Cursor 查源码cursor在查询完成后自动关闭
        Cursor cursor = getContentResolver().query(data.getData(), projects, null, null, null);
        cursor.moveToFirst();//不需要判断是否查到，指针必须移动到顶端
        String _id = cursor.getString(0);//联系人id
        String has_phone_number = cursor.getString(1);
        if (has_phone_number.equals("0")) {
            Toast.makeText(this, "该联系人未保存号码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String[] parm = {
                    "data1"
            };
            //                根据联系人id查询联系人号码
            Cursor cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, parm, "contact_id=" + _id, null, null);
            cursor1.moveToFirst();//必须移动指针到顶端
            String phone = cursor1.getString(0);
            tv_phone.setText(phone);
            et_content.requestFocus();//短信内容获取焦点
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
