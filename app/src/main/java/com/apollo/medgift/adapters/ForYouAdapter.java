package com.apollo.medgift.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.ActivityForyoulistviewBinding;
import com.apollo.medgift.databinding.ActivityHealthtipslistviewBinding;

public class ForYouAdapter extends BaseAdapter {

    Context context;
    String[] titleList;
    String[] providerList;
    String[] locationList;
    String[] descriptionList;
    String[] priceList;
    LayoutInflater inflater;
    private final int[] images;

    public ForYouAdapter(Context context,int[] images, String[] titleList, String[] providerList, String[] locationList, String[] descriptionList, String[] priceList) {
        this.context = context;
        this.images = images;
        this.titleList = titleList;
        this.providerList = providerList;
        this.locationList = locationList;
        this.descriptionList = descriptionList;
        this.priceList = priceList;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityForyoulistviewBinding binding;

        if (convertView == null) {
            binding = ActivityForyoulistviewBinding.inflate(inflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (ActivityForyoulistviewBinding) convertView.getTag();
        }

        binding.giftTitle.setText(titleList[position]);
        binding.giftProvider.setText(providerList[position]);
        binding.giftLocation.setText(locationList[position]);
        binding.giftDescription.setText(descriptionList[position]);
        binding.giftPrice.setText(priceList[position]);
        binding.giftImage.setImageResource(images[position]);

        return convertView;
    }
}
