package com.apollo.medgift.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apollo.medgift.R;

public class HealthTipsAdapter extends BaseAdapter {

//    https://www.youtube.com/watch?v=aUFdgLSEl0g&t=434s

    Context context;
    String titleList[];
    String contentList[];
    LayoutInflater inflater;

    public HealthTipsAdapter(Context context, String[] titleList, String[] contentList) {
        this.context = context;
        this.titleList = titleList;
        this.contentList = contentList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_health_tips_list_view, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);

        title.setText(titleList[position]);
        content.setText(contentList[position]);

        return convertView;
    }
}
