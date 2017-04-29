package sms.com.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.List;

import sms.com.R;
import sms.com.adapter.ConversationAdapter;
import sms.com.base.BaseFragment;
import sms.com.dao.SimpleQueryHandler;
import sms.com.globle.Constant;
import sms.com.ui.dialog.ConfirmDialog;
import sms.com.ui.dialog.DeleteDialog;

/**
 * Author   : luweicheng on 2017/4/26 0026 17:53
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 会话Fragment
 */

public class ConversationFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private LinearLayout ll_bottom, ll_edit;
    private ListView listView;
    private View view;
    private Button but_edit, but_newSms, but_allSelect, but_cancel, but_del;
    private String[] params = {
            "sms.body AS snippet",
            "sms.thread_id AS _id",//必须含有_id字段
            "groups.msg_count AS msg_count",
            "address AS address",
            "date AS date"

    };
    private ConversationAdapter adapter;//游标适配器
    private List<Integer> mList;
    private DeleteDialog delDialog;
    private Boolean isStopDelete = false;
    public final int WHAT_DELETE_COMPLETE = 0x01;
    public final int WHAT_DELETE_UPDATE = 0x02;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_DELETE_COMPLETE:
                    delDialog.dismiss();
                    adapter.setEdit(false);
                    ll_bottom.setVisibility(View.VISIBLE);
                    ll_edit.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    break;
                case WHAT_DELETE_UPDATE:
                    delDialog.updateprogressBar(msg.arg1+1);
                    break;
            }
        }
    };

    @Override
    protected View bindView(LayoutInflater inflater, @Nullable ViewGroup conatiner, @Nullable Bundle saveInstanceState) {
        view = inflater.inflate(R.layout.fragment_conversation_layout, null);
        return view;
    }


    @Override
    protected void initView() {
        listView = (ListView) view.findViewById(R.id.lv_conversation);
        but_edit = (Button) view.findViewById(R.id.but_edit);
        but_newSms = (Button) view.findViewById(R.id.but_newSms);
        ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        ll_edit = (LinearLayout) view.findViewById(R.id.ll_edit);
        but_allSelect = (Button) view.findViewById(R.id.but_allSelect);
        but_cancel = (Button) view.findViewById(R.id.but_cancel);
        but_del = (Button) view.findViewById(R.id.but_del);
    }

    @Override
    protected void initListener() {
        but_edit.setOnClickListener(this);
        but_newSms.setOnClickListener(this);
        but_del.setOnClickListener(this);
        but_cancel.setOnClickListener(this);
        but_allSelect.setOnClickListener(this);
        listView.setOnItemClickListener(this);

    }

    @Override
    protected void processListener(View v) {
        switch (v.getId()) {
            case R.id.but_edit:
                ll_bottom.setVisibility(View.GONE);
                ll_edit.setVisibility(View.VISIBLE);
                adapter.setEdit(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.but_newSms:
                break;
            case R.id.but_allSelect:
                adapter.selectAll();
                break;
            case R.id.but_del:
                mList = adapter.getConverstaionList();
                if (mList.size() == 0) {
                    return;
                }

                ConfirmDialog.showConfirmDialog(getActivity(), "提示", "确定删除该信息吗？", new ConfirmDialog.ConfirmOnClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm() {
                        deleteSms();
                    }
                });

                break;
            case R.id.but_cancel:
                ll_bottom.setVisibility(View.VISIBLE);
                ll_edit.setVisibility(View.GONE);
                adapter.setEdit(false);
                adapter.getConverstaionList().clear();
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void initData() {
        adapter = new ConversationAdapter(getActivity(), null, true);
        listView.setAdapter(adapter);
        SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
        queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, params, null, null, "date desc");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getEdit()) {
            adapter.addSelected(position);
        }
    }

    /**
     * 删除短信
     */
    public void deleteSms() {
        delDialog = DeleteDialog.showDeleteDialog(getActivity(), mList.size(), new DeleteDialog.OnDeleteCancelListener() {
            @Override
            public void cancel() {
                isStopDelete = true;
            }
        });

        /**
         * 开启线程进行删除短信(完成利用Handler进行通知主线程)
         */
        new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < mList.size(); i++) {
                    if (isStopDelete) {
                        isStopDelete = false;
                        break;//跳出循环
                    }
                    try {
                        sleep(1000);//每1秒删除一个短信
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String where = "thread_id =" + mList.get(i);
                    getActivity().getContentResolver().delete(Constant.URI.URI_SMS_DELETE, where, null);
                    Message msg = handler.obtainMessage();
                    msg.what = WHAT_DELETE_UPDATE;
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                }
                mList.clear();//清理选择删除的短信id集合
                handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
            }
        }.start();


    }


public void showEditMenu(){
    ViewPropertyAnimator.animate(ll_edit).translationY(ll_edit.getHeight()).setDuration(200);
    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            ViewPropertyAnimator.animate(ll_bottom).translationY(0).setDuration(200);
        }
    }, 200);

}

    private void showSelectMenu() {
        ViewPropertyAnimator.animate(ll_bottom).translationY(ll_bottom.getHeight()).setDuration(200);
        //延时200毫秒执行run方法的代码
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(ll_bottom).translationY(0).setDuration(200);
            }
        }, 200);

    }

}
