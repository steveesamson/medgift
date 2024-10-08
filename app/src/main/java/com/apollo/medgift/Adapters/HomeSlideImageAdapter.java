package com.apollo.medgift.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo.medgift.Models.HomeSlideImageItem;
import com.apollo.medgift.R;
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
        return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_image_container,
                        parent,
                        false
                )
        );
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
        private RoundedImageView imageView;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.homeImageSlider);
        }

        void setImage(HomeSlideImageItem homeSlideImageItem) {
            imageView.setImageResource(homeSlideImageItem.getImage());
        }
    }
}
