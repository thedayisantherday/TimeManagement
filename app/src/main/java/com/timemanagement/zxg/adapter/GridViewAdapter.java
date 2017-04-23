package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timemanagement.zxg.timemanagement.R;

import java.util.List;

/**
 * Created by zxg on 17/2/23.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<List<String>> strs;

    public GridViewAdapter(Context context, List<List<String>> strings){
        mContext = context;
        strs = strings;
    }

    @Override
    public int getCount() {
        return strs.size();
    }

    @Override
    public Object getItem(int position) {
        return strs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.gridview_item, null);
        TextView tv1 = (TextView) convertView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.tv2);
        TextView tv3 = (TextView) convertView.findViewById(R.id.tv3);
        TextView tv4 = (TextView) convertView.findViewById(R.id.tv4);
        TextView tv5 = (TextView) convertView.findViewById(R.id.tv5);
        TextView tv6 = (TextView) convertView.findViewById(R.id.tv6);
        TextView tv7 = (TextView) convertView.findViewById(R.id.tv7);
        TextView tv8 = (TextView) convertView.findViewById(R.id.tv8);
        TextView tv9 = (TextView) convertView.findViewById(R.id.tv9);
        TextView tv10 = (TextView) convertView.findViewById(R.id.tv10);
        TextView tv11 = (TextView) convertView.findViewById(R.id.tv11);
        TextView tv12 = (TextView) convertView.findViewById(R.id.tv12);
        TextView tv13 = (TextView) convertView.findViewById(R.id.tv13);
        TextView tv14 = (TextView) convertView.findViewById(R.id.tv14);

        List<String> strings = strs.get(position);
        tv1.setText(strings.get(0));
        tv2.setText(strings.get(1));
        tv3.setText(strings.get(2));
        tv4.setText(strings.get(3));
        tv5.setText(strings.get(4));
        tv6.setText(strings.get(5));
        tv7.setText(strings.get(6));
        tv8.setText(strings.get(7));
        tv9.setText(strings.get(8));
        tv10.setText(strings.get(9));
        tv11.setText(strings.get(10));
        tv12.setText(strings.get(11));
        tv13.setText(strings.get(12));
        tv14.setText(strings.get(13));

        return convertView;
    }
}
