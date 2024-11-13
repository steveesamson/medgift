package com.apollo.medgift;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo.medgift.adapters.ForYouRecyclerViewAdapter;
import com.apollo.medgift.adapters.HealthTipsRecyclerViewAdapter;
import com.apollo.medgift.adapters.HomeSlideImageAdapter;
import com.apollo.medgift.core.MainMenuActivity;
import com.apollo.medgift.databinding.ActivityHomepageBinding;
import com.apollo.medgift.models.HomeSlideImageItem;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends MainMenuActivity implements View.OnClickListener {

    ActivityHomepageBinding binding;

    String titleList[] = {"Stay Hydrated", "Stay Hydrated"};
    String contentList[] = {"Drinking enough water is crucial for maintaining overall health. Aim for 8 glasses of water a day.", "Drinking enough water is crucial for maintaining overall health. Aim for 8 glasses of water a day."};

    String gift_title[] = {"Telemedicine Consultations", "Telemedicine Consultations"};
    String gift_provider[] = {"Provider - 1", "Provider - 2"};
    String gift_location[] = {"Canada", "Canada"};
    String gift_description[] = {"Connect with a doctor remotely for a virtual check-up or to address health concerns from the comfort of your home.", "Connect with a doctor remotely for a virtual check-up or to address health concerns from the comfort of your home."};
    String gift_price[] = {"$ 423.00", "$ 565.00"};
    int[] images = {R.drawable.sample_image1, R.drawable.sample_image2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar homeAppBar = binding.homeAppBar.getRoot();
        setSupportActionBar(homeAppBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MedGift");
        }

        // Image Slider
        ViewPager2 viewPager2 = binding.imageSlider;

        //https://www.youtube.com/watch?v=U7bqZkmVps8
        List<HomeSlideImageItem> homeSliderItem = new ArrayList<>();
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_1));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_2));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_3));

        viewPager2.setAdapter(new HomeSlideImageAdapter(homeSliderItem, viewPager2));

        // Health Tips List
        RecyclerView healthTipsRecyclerView = binding.healthTipsRecyclerView;
        HealthTipsRecyclerViewAdapter healthTipsAdapter = new HealthTipsRecyclerViewAdapter(this, titleList, contentList);
        healthTipsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthTipsRecyclerView.setAdapter(healthTipsAdapter);

        // For you list
        RecyclerView forYouRecyclerView = binding.forYouRecyclerView;
        ForYouRecyclerViewAdapter forYouAdapter = new ForYouRecyclerViewAdapter(this, images, gift_title, gift_provider, gift_location, gift_description, gift_price);
        forYouRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        forYouRecyclerView.setAdapter(forYouAdapter);

        bindEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }
    private void bindEvents()
    {
        this.binding.categoryCard3.setOnClickListener(this);

    }
    private void gift(){
        Intent intent = new Intent(this, GiftActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == binding.categoryCard3.getId())
        {
            gift();
        }
    }
}