package sms.com.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import sms.com.R;
import sms.com.adapter.ConversationAdapter;
import sms.com.base.BaseFragment;
import sms.com.dao.SimpleQueryHandler;
import sms.com.globle.Constant;
import sms.com.ui.dialog.ConfirmDialog;

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
    private ConfirmDialog delDialog;


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
        for (int i = 0; i < mList.size(); i++) {
            String where = "thread_id =" + mList.get(i);
            getActivity().getContentResolver().delete(Constant.URI.URI_SMS_DELETE, where, null);
        }


    }


}
