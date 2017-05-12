package sms.com.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import sms.com.R;

/**
 * Author   : luweicheng on 2017/5/10 0010 15:49
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:  自动搜索手机号
 */

public class NewSmsAdapter extends CursorAdapter {


    public NewSmsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.search_phone_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getHolder(view);
        String phone = cursor.getString(cursor.getColumnIndex("data1"));
        String name = cursor.getString(cursor.getColumnIndex("display_name"));
        holder.name.setText(name);
        holder.phone.setText(phone);
    }

    /**
     * 获取ViewHolder
     *
     * @param v
     * @return
     */
    private ViewHolder getHolder(View v) {
        ViewHolder holder;
        if (v.getTag() == null) {
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        return holder;
    }

    /**
     * 缓存视图
     */
    class ViewHolder {
        private TextView name;
        private TextView phone;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.tv_search_name);
            phone = (TextView) view.findViewById(R.id.tv_search_phone);
        }
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex("data1"));
    }
}
