package sms.com.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Author   : luweicheng on 2017/4/26 0026 17:35
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 基类Activity
 */

public  abstract  class BaseActivity extends AppCompatActivity implements View.OnClickListener {
   public String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();

    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();
    protected abstract void processListener(View v);
    @Override
    public void onClick(View v) {
        processListener(v);
    }

}
