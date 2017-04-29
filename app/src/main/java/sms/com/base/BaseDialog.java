package sms.com.base;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import sms.com.R;

/**
 * Author   : luweicheng on 2017/4/29 0029 12:31
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: Dialog的基类
 */

public abstract class BaseDialog  extends AlertDialog implements View.OnClickListener{
    protected BaseDialog(@NonNull Context context) {
        super(context, R.style.dialogStyle);

    }


    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initListener();
        initData();

    }

    public abstract void processListener(View view) ;

    public abstract void initListener() ;

    public abstract void initData() ;

    public  abstract void initView();

    @Override
    public void onClick(View v) {
        processListener(v);
    }

    /**
     * 按返回键dialog消失
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
