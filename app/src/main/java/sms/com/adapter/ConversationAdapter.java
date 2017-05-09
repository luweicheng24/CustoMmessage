package sms.com.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sms.com.R;
import sms.com.bean.Conversation;
import sms.com.dao.ContactDao;

import static sms.com.bean.Conversation.createObjfromCurson;

/**
 * Author   : luweicheng on 2017/4/27 0027 11:27
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 会话listView的适配器
 */

public class ConversationAdapter extends CursorAdapter {

    private Boolean isEdit = false;  //是否处于编辑状态

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    public List<Integer> getConverstaionList() {
        return converstaionList;
    }

    public void setConverstaionList(List<Integer> converstaionList) {
        this.converstaionList = converstaionList;
    }

    /*已经选择的短信的Thread_id集合*/
    private List<Integer> converstaionList;

    public ConversationAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        converstaionList = new ArrayList<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.lv_conversation_item, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getHolder(view);
        /*根据Cursor内容创建会话对象*/
        Conversation conversation = createObjfromCurson(cursor);
        //设置短信内容
        holder.tv_content.setText(conversation.getSnipped());
        //根据手机号查询联系人
        String address = ContactDao.getNameByAddress(context.getContentResolver(), conversation.getAddress());
        holder.tv_address.setText(address + "(" + conversation.getMsg_count() + ")");
        //判断日期（如果当天只显示时间如果以前的显示日期）
        Bitmap bmp = ContactDao.getAvatorByAddress(context.getContentResolver(), conversation.getAddress());
        if (bmp != null) {
            holder.iv_avator.setBackground(new BitmapDrawable(bmp));
        } else {
            holder.iv_avator.setBackgroundResource(R.drawable.img_default_avatar);
        }
        if (DateUtils.isToday(conversation.getDate())) {
            holder.tv_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
        } else {
            holder.tv_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
        }
        if (isEdit) {
            holder.iv_check.setVisibility(View.VISIBLE);
            //不强转成Integer对象，就会查找是否包含该int类型的索引
            if (converstaionList.contains((Integer) conversation.getThread_id())) {
                holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_checked);
            } else {
                holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_normal);
            }
        } else {
            holder.iv_check.setVisibility(View.GONE);
        }

    }

    /**
     * 创建ViewHolder
     *
     * @param view
     * @return
     */
    private ViewHolder getHolder(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
        } else {
            view.setTag(holder);
        }
        return holder;
    }


    class ViewHolder {

        private TextView tv_address;
        private TextView tv_date;
        private TextView tv_content;
        private ImageView iv_avator;
        private ImageView iv_check;

        public ViewHolder(View v) {
            iv_check = (ImageView) v.findViewById(R.id.iv_check);
            tv_address = (TextView) v.findViewById(R.id.tv_address);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            iv_avator = (ImageView) v.findViewById(R.id.iv_avatar);
        }

    }

    public void addSelected(int position) {
        Cursor cursor = (Cursor) getItem(position);//游标在该位置的Item
        Conversation c = Conversation.createObjfromCurson(cursor);
        //converstaionList.add(c.getThread_id());


        if (converstaionList.contains(c.getThread_id())) {
            converstaionList.remove((Integer) c.getThread_id());
        } else {
            converstaionList.add(c.getThread_id());
        }
        notifyDataSetChanged();//跟新数据
    }

    public void selectAll() {
        Cursor cursor = getCursor();
        cursor.moveToPosition(-1);
        converstaionList.clear();//清楚以前单选的
        while (cursor.moveToNext()) {
            Conversation conversation = Conversation.createObjfromCurson(cursor);
            converstaionList.add(conversation.getThread_id());
        }
        notifyDataSetChanged();//跟新数据
    }
}
