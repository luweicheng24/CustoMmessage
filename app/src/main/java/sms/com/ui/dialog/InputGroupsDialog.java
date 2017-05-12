package sms.com.ui.dialog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import sms.com.R;
import sms.com.base.BaseDialog;
import sms.com.db.GroupOpenHelper;
import sms.com.globle.Constant;
import sms.com.utils.L;

/**
 * Author   : luweicheng on 2017/5/11 0011 09:51
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 创建群组Dialog
 */

public class InputGroupsDialog extends BaseDialog {

    public static EditText groupName;
    private TextView cancel, confirm;
    private Context context;

    public static void showDialog(Activity context, UpdateGroups updateGroups) {
        InputGroupsDialog dialog = new InputGroupsDialog(context);
        dialog.setUpdateGroups(updateGroups);
        dialog.setView(new EditText(context));
        dialog.show();
    }

    private InputGroupsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void processListener(View view) {
        switch (view.getId()) {
            case R.id.tv_groups_cancel:
                dismiss();
                break;
            case R.id.tv_groups_confirm:
                String name = groupName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, "群组名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String date = formatter.format(curDate);
                values.put(GroupOpenHelper.Groups_NAME, name);
                values.put(GroupOpenHelper.CREATE_DATE,date);
                values.put(GroupOpenHelper.THREAD_COUNT, 0);
                Uri uri = getContext().getContentResolver().insert(Constant.PROVIDE.URI_INSERT_GROUP, values);
                L.i(this, "创建群组结果uri==：" + uri);
                if (updateGroups != null) {
                    updateGroups.update();
                }
                dismiss();
                break;
        }
    }

    @Override
    public void initListener() {
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.dialog_groups__input_layout);
        groupName = (EditText) findViewById(R.id.et_groups_name);
        cancel = (TextView) findViewById(R.id.tv_groups_cancel);
        confirm = (TextView) findViewById(R.id.tv_groups_confirm);
    }

    public interface UpdateGroups {
        void update();
    }

    private UpdateGroups updateGroups;

    public void setUpdateGroups(UpdateGroups updateGroups) {
        this.updateGroups = updateGroups;
    }
}
