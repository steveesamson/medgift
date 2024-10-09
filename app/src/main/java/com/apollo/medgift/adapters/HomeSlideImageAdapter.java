package com.apollo.medgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.SlideimagecontainerBinding;
import com.apollo.medgift.models.HomeSlideImageItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HomeSlideImageAdapter extends RecyclerView.Adapter<HomeSlideImageAdapter.SlideViewHolder> {

    private List<HomeSlideImageItem> homeSlideImageItems;
    private ViewPager2 viewPager2;

    public HomeSlideImageAdapter(List<HomeSlideImageItem> homeSlideImageItems, ViewPager2 viewPager2) {
        this.homeSlideImageItems = homeSlideImageItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SlideimagecontainerBinding binding = SlideimagecontainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new SlideViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImage(homeSlideImageItems.get(position));
    }

    @Override
    public int getItemCount() {
        return homeSlideImageItems.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {
        SlideimagecontainerBinding binding;

        public SlideViewHolder(@NonNull SlideimagecontainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setImage(HomeSlideImageItem homeSlideImageItem) {
            binding.homeImageSlider.setImageResource(homeSlideImageItem.getImage());
        }
    }
}
