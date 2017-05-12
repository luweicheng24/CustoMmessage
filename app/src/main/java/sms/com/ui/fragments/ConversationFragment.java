package sms.com.ui.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import sms.com.R;
import sms.com.adapter.ConversationAdapter;
import sms.com.base.BaseFragment;
import sms.com.bean.Conversation;
import sms.com.bean.GroupsBean;
import sms.com.dao.SimpleQueryHandler;
import sms.com.globle.Constant;
import sms.com.ui.activity.NewSmsActivity;
import sms.com.ui.activity.SmsDetailActivity;
import sms.com.ui.dialog.ConfirmDialog;
import sms.com.ui.dialog.DeleteDialog;
import sms.com.utils.L;

/**
 * Author   : luweicheng on 2017/4/26 0026 17:53
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 会话Fragment
 */

public class ConversationFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private LinearLayout ll_bottom, ll_edit,ll_edit_globle;
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
                    delDialog.updateprogressBar(msg.arg1 + 1);
                    break;
            }
        }
    };
    private int index; //点击的群组索引

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
        ll_edit_globle = (LinearLayout) view.findViewById(R.id.ll_edit_globle);
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showSelectDialog();
                return true;
            }
        });

    }

    private void showSelectDialog() {

        final List<GroupsBean> list = new ArrayList<GroupsBean>();
        Cursor cursor = getContext().getContentResolver().query(Constant.PROVIDE.URI_QUERY_GROUP, null, null, null, null);

        if (cursor.getCount()==0) {
            Toast.makeText(getContext(), "未创建数组", Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()) {
            GroupsBean bean = GroupsBean.createBeanFromCursor(cursor);
            list.add(bean);
        }
        String[] groups = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            groups[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setSingleChoiceItems(groups, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;
            }
        });
        builder.setTitle("请选择加入的群组");
        builder.setPositiveButton("加入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Conversation c = Conversation.createObjfromCurson(adapter.getCursor());
                int thread_id = c.getThread_id();
                ContentValues values = new ContentValues();
                values.put("thread_id", thread_id);
                values.put("group_id", list.get(index).get_id());//将群组表的主键作为会话表的群组id
                //查询该会话是否已经插入该群组
                Cursor cursor = getContext().getContentResolver().query(Constant.PROVIDE.URI_QUERY_THREAD, new String[]{"thread_id"}, "group_id = " + list.get(index).get_id(), null, null);
                //CursorUtils.printCursor(cursor);
                if(cursor.getCount()!=0){
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("thread_id"));
                        if (id == thread_id) {
                            Toast.makeText(getContext(), "该会话已经属于该群组", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                //将该会话插入会话表
                Uri uri = getContext().getContentResolver().insert(Constant.PROVIDE.URI_INSERT_THREAD, values);
                L.i(this, "插入数据成功：" + uri);
                if (uri != null) {
                    int count = list.get(index).getThread_count();
                    count = count + 1;
                    ContentValues value = new ContentValues();
                    value.put("thread_count", count);
                    //根据群组id跟新群组的会话数
                    int row_id = getContext().getContentResolver().update(Constant.PROVIDE.URI_UPDATE_GROUP, value, "_id = " + list.get(index).get_id(), null);
                    L.i(this, "跟新群组中的会话数结果row_id == " + row_id);
                    Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();

                    List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                    for (int i = 0; i < fragments.size(); i++) {
                        if(fragments.get(i) instanceof GroupFragment){
                           ((GroupFragment) fragments.get(i)).initData();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
     private float editHeight ;

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
                startActivity(new Intent(getActivity(), NewSmsActivity.class));
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
       int w =  View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
       int h =  View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ll_edit_globle.measure(w,h);
        editHeight = ll_edit_globle.getMeasuredHeight();
        L.i(this,"编辑框的高度=="+editHeight);
        adapter = new ConversationAdapter(getActivity(), null, true);
        listView.setAdapter(adapter);

        SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
        queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, params, null, null, "date desc");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getEdit()) {
            adapter.addSelected(position);
        } else {
            //跳转到短信详情页面
            Cursor cursor = adapter.getCursor();
            Conversation conversation = Conversation.createObjfromCurson(cursor);
            Intent intent = new Intent(getActivity(), SmsDetailActivity.class);
            intent.putExtra("address", conversation.getAddress());
            intent.putExtra("thread_id", conversation.getThread_id());
            startActivity(intent);
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


    public void showEditMenu() {
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
