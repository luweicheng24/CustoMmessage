package sms.com.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import sms.com.R;
import sms.com.adapter.ConversationAdapter;
import sms.com.base.BaseFragment;
import sms.com.bean.Conversation;
import sms.com.dao.SimpleQueryHandler;
import sms.com.globle.Constant;
import sms.com.ui.activity.SmsDetailActivity;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Author   : luweicheng on 2017/4/26 0026 19:43
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:  搜索Fragment
 */

public class SearchFragment extends BaseFragment implements TextWatcher, View.OnTouchListener, AdapterView.OnItemClickListener {
    private View view;
    private ListView lv;
    private EditText et_search;
    private ConversationAdapter adapter;

    @Override
    protected View bindView(LayoutInflater inflater, @Nullable ViewGroup conatiner, @Nullable Bundle saveInstanceState) {
        view = inflater.inflate(R.layout.fragement_search, null);
        return view;
    }

    @Override
    protected void initView() {
        et_search = (EditText) view.findViewById(R.id.et_fragment_search);
        lv = (ListView) view.findViewById(R.id.lv_search);
        lv.setOnTouchListener(this);
        lv.setOnItemClickListener(this);
    }

    @Override
    protected void initListener() {
       et_search.addTextChangedListener(this);
    }

    @Override
    protected void processListener(View v) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    private String[] params = {
            "sms.body AS snippet",
            "sms.thread_id AS _id",//必须含有_id字段
            "groups.msg_count AS msg_count",
            "address AS address",
            "date AS date"

    };

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        adapter = new ConversationAdapter(getActivity(), null, true);
        lv.setAdapter(adapter);
        SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
        String selection = "body like '%"+s+"%'";
        queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, params, selection, null, "date desc");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm  = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);
        }

        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //跳转到短信详情页面
        Cursor cursor = adapter.getCursor();
        Conversation conversation = Conversation.createObjfromCurson(cursor);
        Intent intent = new Intent(getActivity(), SmsDetailActivity.class);
        intent.putExtra("address", conversation.getAddress());
        intent.putExtra("thread_id", conversation.getThread_id());
        startActivity(intent);
    }
}
