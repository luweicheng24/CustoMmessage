package sms.com.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import sms.com.R;
import sms.com.adapter.GroupsAdapter;
import sms.com.base.BaseFragment;
import sms.com.bean.GroupsBean;
import sms.com.dao.SimpleQueryHandler;
import sms.com.globle.Constant;
import sms.com.ui.activity.GroupThreadActivity;
import sms.com.ui.dialog.InputGroupsDialog;
import sms.com.utils.L;

/**
 * Author   : luweicheng on 2017/4/26 0026 19:43
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:  群组Fragment
 */

public class GroupFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private View view;
    private ListView lv;
    private Button but_create;
    private String name;
    private int group_id;
    private Dialog dialog;
    private GroupsAdapter adapter;
    private AlertDialog.Builder builder;

    @Override
    protected View bindView(LayoutInflater inflater, @Nullable ViewGroup conatiner, @Nullable Bundle saveInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_layout, null);
        return view;
    }

    @Override
    protected void initView() {
        lv = (ListView) view.findViewById(R.id.lv_fragment_groups);
        but_create = (Button) view.findViewById(R.id.but_group_create);

    }

    @Override
    protected void initListener() {
        but_create.setOnClickListener(this);
        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);
    }

    @Override
    protected void processListener(View v) {
        switch (v.getId()) {
            case R.id.but_group_create:
                InputGroupsDialog.showDialog(getActivity(), new InputGroupsDialog.UpdateGroups() {
                    @Override
                    public void update() {
                        initData();
                    }
                });
                break;
        }

    }

    // TODO: 2017/5/11 0011
    @Override
    protected void initData() {
        builder = initDialog();
        adapter = new GroupsAdapter(getContext(), null);
        lv.setAdapter(adapter);
        SimpleQueryHandler handler = new SimpleQueryHandler(getContext().getContentResolver());
        handler.startQuery(0, adapter, Constant.PROVIDE.URI_QUERY_GROUP, null, null, null, null);

    }


    private AlertDialog.Builder initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除群组：");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除群组
                int rows = getActivity().getContentResolver().delete(Constant.PROVIDE.URI_DELETE_GROUP, "_id= " + group_id, null);
                if (rows < 1) {
                    Toast.makeText(getContext(), "群组删除异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                initData();
                Toast.makeText(getContext(), "群组删除成功", Toast.LENGTH_SHORT).show();
                L.i(this, "群组删除了" + rows + "行");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //跳转到会话群组页面
        GroupsBean groupsBean = GroupsBean.createBeanFromCursor(adapter.getCursor());
        Intent intent = new Intent(getActivity(), GroupThreadActivity.class);
        intent.putExtra("group_id", groupsBean.get_id());
        intent.putExtra("title", groupsBean.getName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //跳转到会话群组页面
        GroupsBean groupsBean = GroupsBean.createBeanFromCursor(adapter.getCursor());
        this.name = groupsBean.getName();
        this.group_id = groupsBean.get_id();
        builder.setMessage("确定删除群组" + name + "吗？");
        dialog = builder.create();
        dialog.show();
        return true;
    }
}
