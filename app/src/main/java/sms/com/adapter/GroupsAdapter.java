package sms.com.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import sms.com.R;

/**
 * Author   : luweicheng on 2017/5/11 0011 10:43
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 群组适配器
 */

public class GroupsAdapter extends CursorAdapter {


    public GroupsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.fragment_group_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getHolder(view);
//        该id在群组中是主键在会话中作为group_id
        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
         String name = cursor.getString(cursor.getColumnIndex("name"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        int threadCount = cursor.getInt(cursor.getColumnIndex("thread_count"));
        holder.groupsName.setText(name + "(" + threadCount + ")");
        holder.groupsDate.setText(date);


    }




    private ViewHolder getHolder(View view) {
        ViewHolder holder;
        if (view.getTag() == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return holder;
    }

    class ViewHolder {

        private TextView groupsName;
        private TextView groupsDate;
        private LinearLayout ll_groups;

        public ViewHolder(View view) {
            ll_groups = (LinearLayout) view.findViewById(R.id.ll_group_item);
            groupsName = (TextView) view.findViewById(R.id.tv_groups_name);
            groupsDate = (TextView) view.findViewById(R.id.tv_groups_date);
        }

    }


}
