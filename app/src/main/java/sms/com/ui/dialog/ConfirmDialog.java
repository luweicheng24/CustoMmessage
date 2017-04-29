package sms.com.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import sms.com.R;
import sms.com.base.BaseDialog;

/**
 * Author   : luweicheng on 2017/4/29 0029 13:03
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 确定取消的dialog
 */

public class ConfirmDialog extends BaseDialog {


    private TextView title;
    private TextView cancel;
    private TextView confirm;
    private TextView content;
    private String str_title;
    private String str_content;

    /**
     *  显示Dialog
     * @param context
     * @param title
     * @param content
     * @param listener
     */
    public static void showConfirmDialog(Context context, String title, String content, ConfirmDialog.ConfirmOnClickListener listener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setStr_title(title);
        dialog.setStr_content(content);
        dialog.setConfirmOnClickListener(listener);
        dialog.show();

    }

    protected ConfirmDialog(@NonNull Context context) {
        super(context);
    }


    public void setStr_title(String str_title) {
        this.str_title = str_title;
    }

    public void setStr_content(String str_content) {
        this.str_content = str_content;
    }


    @Override
    public void processListener(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (confirmOnClickListener != null) {
                    confirmOnClickListener.onCancel();
                }
                break;
            case R.id.tv_confirm:
                if (confirmOnClickListener != null) {
                    confirmOnClickListener.onConfirm();
                }
        }
        dismiss();

    }

    @Override
    public void initListener() {
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

    }

    /**
     * 填充数据
     */
    @Override
    public void initData() {
        title.setText(str_title);
        content.setText(str_content);
    }

    /**
     * 初始化布局
     */
    @Override
    public void initView() {
        setContentView(R.layout.dialog_layout);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        cancel = (TextView) findViewById(R.id.tv_cancel);
        confirm = (TextView) findViewById(R.id.tv_confirm);

    }


    /**
     * 确定取消接口回调
     */
    public interface ConfirmOnClickListener {
        void onCancel();

        void onConfirm();
    }

    public void setConfirmOnClickListener(ConfirmOnClickListener confirmOnClickListener) {
        this.confirmOnClickListener = confirmOnClickListener;
    }

    private ConfirmOnClickListener confirmOnClickListener;
}
