package sms.com.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author   : luweicheng on 2017/4/26 0026 17:54
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 基类Fragment
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
        initData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return bindView(inflater,container,savedInstanceState);
    }

    protected  abstract View bindView(LayoutInflater inflater,ViewGroup conatiner,Bundle saveInstanceState);
    protected  abstract void initView();
    protected abstract void initListener();
    protected abstract void processListener(View v);
    protected abstract void initData();

    @Override
    public void onClick(View v) {
        processListener(v);
    }
}
