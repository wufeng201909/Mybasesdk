package com.sdk.mysdklibrary.activity.Adaper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdk.mysdklibrary.Tools.ResourceUtil;

import java.util.ArrayList;

public class MyGridViewAdapter extends BaseAdapter {
    private Activity con = null;
    private ArrayList<String[]> list_ = null;
    public MyGridViewAdapter(Activity activity, ArrayList<String[]> data) {
        con = activity;
        list_ = data;
    }

    @Override
    public int getCount() {
        return list_.size();
    }

    @Override
    public Object getItem(int position) {
        return list_.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        final String[] item = list_.get(position);
        if (convertView == null) {
            mHolder = new ViewHolder();
            int paylistitem_id = ResourceUtil.getLayoutId(con,"myths_pay_griditem");
            convertView = LayoutInflater.from(con).inflate(paylistitem_id, null,true);
            mHolder.item_log = (TextView) convertView.findViewById(ResourceUtil.getId(con,"item_log"));
            mHolder.item_title = (TextView) convertView.findViewById(ResourceUtil.getId(con,"item_title"));
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.item_log.setBackgroundResource(ResourceUtil.getDrawableId(con,item[0]));
        mHolder.item_title.setText(item[2]);
        return convertView;
    }

    class ViewHolder {
        private TextView item_log;
        private TextView item_title;
    }
}
