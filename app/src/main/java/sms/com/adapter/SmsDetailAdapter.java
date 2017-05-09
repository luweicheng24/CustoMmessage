package sms.com.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import sms.com.R;
import sms.com.bean.ConversationDetail;

/**
 * Author   : luweicheng on 2017/5/8 0008 15:47
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:
 */

public class SmsDetailAdapter extends CursorAdapter {
    static final int DURATION = 3 * 60 * 1000;
    private ListView lv ;
    public SmsDetailAdapter(Context context, Cursor c, ListView lv) {
        super(context, c);
        this.lv=lv;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.sms_detail_ltem_layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getHolder(view);
        ConversationDetail cd = ConversationDetail.createFromCursor(cursor);
        long date = cd.getDate();
        int type = cd.getType();
        String body = cd.getBody();
        String time = "";
        if(DateUtils.isToday(date)){
             time = DateFormat.getTimeFormat(context).format(date);
        }else{
            time =DateFormat.getDateFormat(context).format(date);
        }
        if (timeIsGone(cursor)) {
            holder.tv_time.setText(time);
        }else{
            holder.tv_time.setVisibility(View.GONE);
        }
        switch (type){
            case 2:
                holder.tv_receive.setVisibility(View.GONE);
                holder.tv_send.setVisibility(View.VISIBLE);
                holder.tv_send.setText(body);
                break;
            case 1:
                holder.tv_receive.setVisibility(View.VISIBLE);
                holder.tv_send.setVisibility(View.GONE);
                holder.tv_receive.setText(body);
                break;
        }




      }

    private ViewHolder getHolder(View view) {
        ViewHolder holder;
        if (view.getTag() == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
          holder = (ViewHolder) view.getTag();
        }
        return holder;

    }

    class ViewHolder {
        private TextView tv_time;
        private TextView tv_send;
        private TextView tv_receive;

        public ViewHolder(View view) {
            tv_time = (TextView) view.findViewById(R.id.tv_conversation_detail_date);
            tv_receive = (TextView) view.findViewById(R.id.tv_conversation_detail_receive);
            tv_send = (TextView) view.findViewById(R.id.tv_conversation_detail_send);
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        lv.setSelection(getCount());
    }

    /**
     * 判断是否显示时间：三分钟以内的只显示第一个短信的时间
     * @param cursor
     * @return
     */
    private boolean timeIsGone(Cursor cursor){

        if(cursor.getPosition()==0){
            return true;
        }else {
            ConversationDetail c1 =ConversationDetail.createFromCursor((Cursor)getItem(cursor.getPosition()));
            ConversationDetail c2 =ConversationDetail.createFromCursor((Cursor)getItem(cursor.getPosition()-1));
            if(c1.getDate() - c2.getDate()>DURATION){
                return true;
            }
            return false;
        }
    }
}
