package sms.com.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sms.com.R;
import sms.com.base.BaseFragment;

/**
 * Author   : luweicheng on 2017/4/26 0026 19:43
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:  群组Fragment
 */

public class GroupFragment extends BaseFragment {
    private View view;

    @Override
    protected View bindView(LayoutInflater inflater, @Nullable ViewGroup conatiner, @Nullable Bundle saveInstanceState) {
        view = inflater.inflate(R.layout.fragement_group,null);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void processListener(View v) {

    }

    @Override
    protected void initData() {

    }
}
