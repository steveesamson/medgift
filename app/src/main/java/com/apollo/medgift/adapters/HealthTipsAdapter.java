package com.apollo.medgift.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.apollo.medgift.databinding.ActivityHealthtipslistviewBinding;

public class HealthTipsAdapter extends BaseAdapter {

    //    https://www.youtube.com/watch?v=aUFdgLSEl0g&t=434s

    Context context;
    String[] titleList;
    String[] contentList;
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
        ActivityHealthtipslistviewBinding binding;

        if (convertView == null) {
            binding = ActivityHealthtipslistviewBinding.inflate(inflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ActivityHealthtipslistviewBinding) convertView.getTag();
        }

        binding.title.setText(titleList[position]);
        binding.content.setText(contentList[position]);

        return convertView;
    }
}
