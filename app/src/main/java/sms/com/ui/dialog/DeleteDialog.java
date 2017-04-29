package sms.com.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import sms.com.R;
import sms.com.base.BaseDialog;

/**
 * Author   : luweicheng on 2017/4/29 0029 20:41
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 删除Dialog
 */

public class DeleteDialog extends BaseDialog{
    private TextView title;//标题
    private ProgressBar proBar;//进度条
    private TextView tv_stop;//中断删除

    public void setProgress(int progress) {
        this.maxProgress = progress;
    }

    private int maxProgress;//删除进度
    protected DeleteDialog(@NonNull Context context,int maxprogress,OnDeleteCancelListener listener) {
        super(context);
        this.maxProgress = maxprogress;
        this.onDeleteCancelListener = listener;
    }
    public static DeleteDialog showDeleteDialog(Context context,int maxProgress,OnDeleteCancelListener listener){
        DeleteDialog dialog = new DeleteDialog(context,maxProgress,listener);
        dialog.setOnDeleteCancelListener(listener);
        dialog.show();
        return dialog;
    }
    @Override
    public void processListener(View view) {
        switch (view.getId()){
            case R.id.tv_delete_cancel:
                //中断删除
                if(onDeleteCancelListener!=null){
                    onDeleteCancelListener.cancel();
                }
                break;
        }
    }

    @Override
    public void initListener() {
       tv_stop.setOnClickListener(this);
    }

    @Override
    public void initData() {
        title.setText("正在删除(0/"+maxProgress+")");
        proBar.setMax(maxProgress);
    }

    @Override
    public void initView() {
        setContentView(R.layout.dialog_delete_layout);
        title = (TextView) findViewById(R.id.tv_delete_title);
        proBar = (ProgressBar) findViewById(R.id.pg_delete);
        tv_stop = (TextView) findViewById(R.id.tv_delete_cancel);
    }
    
    public interface OnDeleteCancelListener{
        void cancel();
    }
    private OnDeleteCancelListener onDeleteCancelListener;

    public void setOnDeleteCancelListener(OnDeleteCancelListener onDeleteCancelListener) {
        this.onDeleteCancelListener = onDeleteCancelListener;
    }

    /**
     * 刷新进度条和修改标题
     * @param progress
     */
    public void updateprogressBar(int progress){
        proBar.setProgress(progress);
        title.setText("正在删除("+progress+"/"+maxProgress+")");
    }

}
