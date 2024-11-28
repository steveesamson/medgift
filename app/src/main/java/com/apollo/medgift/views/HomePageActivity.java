package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.HomeSlideImageAdapter;
import com.apollo.medgift.adapters.provider.HealthTipAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.NotificationUtil;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityHomepageBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.HomeSlideImageItem;
import com.apollo.medgift.views.gifter.RecipientActivity;
import com.apollo.medgift.views.provider.HealthTipActivity;
import com.apollo.medgift.views.gifter.GiftActivity;
import com.apollo.medgift.views.provider.MyServiceActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends BaseActivity implements View.OnClickListener {

    ActivityHomepageBinding binding;

//    String[] gift_title = {"Telemedicine Consultations", "Telemedicine Consultations"};
//    String[] gift_provider = {"Provider - 1", "Provider - 2"};
//    String[] gift_location = {"Canada", "Canada"};
//    String[] gift_description = {"Connect with a doctor remotely for a virtual check-up or to address health concerns from the comfort of your home.", "Connect with a doctor remotely for a virtual check-up or to address health concerns from the comfort of your home."};
//    String[] gift_price = {"$ 423.00", "$ 565.00"};
//    int[] images = {R.drawable.sample_image1, R.drawable.sample_image2};

    private final List<HealthTip> healthTips = new ArrayList<>();
    private List<HealthTip> healthTipsSubList = new ArrayList<>();
    private HealthTipAdapter healthTipAdapter;
    private DatabaseReference db;
    private ValueEventListener healthTipListener;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Util.applyWindowInsetsListenerTo(this, binding.main);
        // Setup tool bar and title
        setupToolbar(binding.homeAppBar.getRoot(), "MedGift", false);
        // Image Slider
        ViewPager2 viewPager2 = binding.imageSlider;


        //https://www.youtube.com/watch?v=U7bqZkmVps8
        List<HomeSlideImageItem> homeSliderItem = new ArrayList<>();
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_1));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_2));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_3));

        viewPager2.setAdapter(new HomeSlideImageAdapter(homeSliderItem, viewPager2));

        // For you list
        RecyclerView forYouRecyclerView = binding.forYouRecyclerView;
//        ForYouRecyclerViewAdapter forYouAdapter = new ForYouRecyclerViewAdapter(this, images, gift_title, gift_provider, gift_location, gift_description, gift_price);
        forYouRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        forYouRecyclerView.setAdapter(forYouAdapter);
        registerListeners();
    }

    private void registerListeners() {
        NotificationUtil.createNotificationChannel(this, getString(R.string.channel_name), getString(R.string.channel_description));
        binding.quickBtnAvailableServices.setOnClickListener(this);
        binding.quickBtnRecipient.setOnClickListener(this);
        binding.quickBtnHealthTips.setOnClickListener(this);
        binding.quickBtnGift.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent, giftIntent;
        // Handle Recipient navigation
        if (view == binding.quickBtnAvailableServices) {
            intent = new Intent(getApplicationContext(), MyServiceActivity.class);
            startActivity(intent);
        }
        if (view == binding.quickBtnRecipient) {
            intent = new Intent(getApplicationContext(), RecipientActivity.class);
            startActivity(intent);
        }
        if (view == binding.quickBtnHealthTips) {
            intent = new Intent(getApplicationContext(), HealthTipActivity.class);
            startActivity(intent);
        }
        if (view == binding.quickBtnGift) {
            giftIntent = new Intent(getApplicationContext(), GiftActivity.class);
            startActivity(giftIntent);
        }


    }

}
