package sms.com.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import sms.com.R;
import sms.com.adapter.SmsAdapter;
import sms.com.base.BaseActivity;
import sms.com.ui.fragments.ConversationFragment;
import sms.com.ui.fragments.GroupFragment;
import sms.com.ui.fragments.SearchFragment;

/**
 * 首页Activity
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private LinearLayout ll_conversation, ll_group, ll_search;
    private TextView tv_conversation, tv_group, tv_search;
    private SmsAdapter adapter;
    private List<Fragment> mList;
    private View dividerLine;
    private int divideLineLength;
    private long time = 0;



    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        dividerLine = findViewById(R.id.v_divider);
        ll_conversation = (LinearLayout) findViewById(R.id.ll_conversation);
        ll_group = (LinearLayout) findViewById(R.id.ll_group);
        ll_search= (LinearLayout) findViewById(R.id.ll_search);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        tv_conversation = (TextView) findViewById(R.id.tv_conversation);
        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_search = (TextView) findViewById(R.id.tv_search);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        divideLineLength = dm.widthPixels/3;
        dividerLine.getLayoutParams().width = divideLineLength;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {
        mList = new ArrayList<>();
        ConversationFragment cFragment = new ConversationFragment();
        GroupFragment gFragment = new GroupFragment();
        SearchFragment sFragment = new SearchFragment();
        mList.add(cFragment);
        mList.add(gFragment);
        mList.add(sFragment);
        adapter = new SmsAdapter(getSupportFragmentManager(),mList);
        mViewPager.setAdapter(adapter);
        /*字体的缩放*/
        textScale();

    }

    private void textScale() {
        int item = mViewPager.getCurrentItem();
        tv_conversation.setTextColor(item==0? Color.BLUE:Color.WHITE);
        tv_group.setTextColor(item==1? Color.BLUE:Color.WHITE);
        tv_search.setTextColor(item==2? Color.BLUE:Color.WHITE);


        ViewPropertyAnimator.animate(tv_conversation).scaleX(item==0?1.3f:1f).setDuration(200);
        ViewPropertyAnimator.animate(tv_conversation).scaleY(item==0?1.3f:1f).setDuration(200);

        ViewPropertyAnimator.animate(tv_group).scaleX(item==1?1.3f:1f).setDuration(200);
        ViewPropertyAnimator.animate(tv_group).scaleY(item==1?1.3f:1f).setDuration(200);

        ViewPropertyAnimator.animate(tv_search).scaleX(item==2?1.3f:1f).setDuration(200);
        ViewPropertyAnimator.animate(tv_search).scaleY(item==2?1.3f:1f).setDuration(200);

    }

    @Override
    protected void initListener() {
        mViewPager.setOnPageChangeListener(this);
        ll_conversation.setOnClickListener(this);
        ll_group.setOnClickListener(this);
        ll_search.setOnClickListener(this);

    }

    @Override
    protected void processListener(View v) {
        switch (v.getId()) {

            case R.id.ll_conversation:
//                会话
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_group:
//                群组
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ll_search:
//                搜索
                mViewPager.setCurrentItem(2);
                break;
        }

    }

    /**
     * 滑动时监听
     *
     * @param position 移动前的Fragment索引,当滑动到下一个界面马上改变，和偏移的像素同时改变（0-1）
     * @param positionOffset 偏移像素(百分比)
     * @param positionOffsetPixels 偏移的像素(具体值)（0 -1-2-3.... fragment页面的宽度 -- 0）
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int distance = positionOffsetPixels/3+position*divideLineLength;
        Log.e(TAG, "onPageScrolled: distance"+distance +"position"+position);
      ViewPropertyAnimator.animate(dividerLine).translationX(distance).setDuration(0);

    }

    /**
     * 滑动完成时监听方法
     *
     * @param position 滑动完成时Fragment的索引
     */
    @Override
    public void onPageSelected(int position) {
            textScale();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        /**
         * 隐藏软键盘
         */
        InputMethodManager imm  = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }


    }

    /**
     * 三秒内按两次返回键直接退出
     */
    @Override
    public void onBackPressed() {
        if(time == 0){
            time = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            return;
        }else{

            if(System.currentTimeMillis() - time <  2000){
                finish();
            }else{
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            }
        }

    }
}
