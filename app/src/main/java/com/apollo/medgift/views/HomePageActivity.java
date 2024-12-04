package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.HomeSlideImageAdapter;
import com.apollo.medgift.adapters.provider.HealthTipAdapter;
import com.apollo.medgift.adapters.provider.ServiceAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.NotificationUtil;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityConfirmpaymentBinding;
import com.apollo.medgift.databinding.ActivityHomepageBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.HomeSlideImageItem;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.views.gifter.ConfirmPaymentActivity;
import com.apollo.medgift.views.gifter.RecipientActivity;
import com.apollo.medgift.views.models.ServiceVModel;
import com.apollo.medgift.views.provider.AvailabilityActivity;
import com.apollo.medgift.views.provider.HealthTipActivity;
import com.apollo.medgift.views.gifter.GiftActivity;
import com.apollo.medgift.views.provider.ServiceActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends BaseActivity implements View.OnClickListener {

    ActivityHomepageBinding binding;

    private final List<HealthcareService> healthcareServices = new ArrayList<>();

    private ServiceAdapter serviceAdapter;

    private Query query;
    private Gift gift;

    private ValueEventListener serviceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.homeAppBar.getRoot(), "MedGift", false);
        applyWindowInsetsListenerTo(this, binding.main);

        Intent intent = getIntent();
        gift = (Gift) intent.getSerializableExtra(Gift.STORE);

        setForYouUp();

        // Image Slider
        ViewPager2 viewPager2 = binding.imageSlider;

        //https://www.youtube.com/watch?v=U7bqZkmVps8
        List<HomeSlideImageItem> homeSliderItem = new ArrayList<>();
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_1));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_2));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_3));

        viewPager2.setAdapter(new HomeSlideImageAdapter(homeSliderItem, viewPager2));

        // Set up auto-slide with a 2-second delay
        setupAutoSlide(viewPager2, 2500);

        registerListeners();
    }

    private void setupAutoSlide(ViewPager2 viewPager2, int slideInterval) {
        final int itemCount = viewPager2.getAdapter().getItemCount();

        // Runnable to update the current item in ViewPager2
        Runnable autoSlideRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                int nextItem = (currentItem + 1) % itemCount; // Loop back to the first item after the last
                viewPager2.setCurrentItem(nextItem, true); // Smooth scroll to the next item
                viewPager2.postDelayed(this, slideInterval); // Post the runnable again after delay
            }
        };

        // Start the auto-slide
        viewPager2.postDelayed(autoSlideRunnable, slideInterval);

        // Stop auto-slide on manual interaction
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Reset the delay when the user interacts
                viewPager2.removeCallbacks(autoSlideRunnable);
                viewPager2.postDelayed(autoSlideRunnable, slideInterval);
            }
        });
    }

    private void setForYouUp() {
        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;

        if (sessionUser.getUserRole().equals(Role.GIFTER)) {
            this.query = Firebase.database(HealthcareService.STORE);
        } else {
            this.query = Firebase.database(HealthcareService.STORE).orderByChild("createdBy").equalTo(sessionUser.getUserId());
        }

        RecyclerView recyclerView = binding.serviceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(healthcareServices, this.gift, this, "home");
        recyclerView.setAdapter(serviceAdapter);
        fetchAndListenOnRecipients();
    }

    private void fetchAndListenOnRecipients() {
        if (query != null) {
            ServiceVModel serviceVModel = new ViewModelProvider(this).get(ServiceVModel.class);
            ValueEvents<HealthcareService> valueEvents = new ValueEvents<>();
            Util.startProgress(binding.progress, "Fetching Services...");
            serviceListener = valueEvents.registerListener(query, this, serviceAdapter, serviceVModel, healthcareServices, HealthcareService.class, (list) -> {
                Util.stopProgress(binding.progress);
            });
        }
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
            intent = new Intent(getApplicationContext(), ServiceActivity.class);
            startActivity(intent);
        } else if (view == binding.quickBtnRecipient) {
            intent = new Intent(getApplicationContext(), RecipientActivity.class);
            startActivity(intent);
        } else if (view == binding.quickBtnHealthTips) {
            intent = new Intent(getApplicationContext(), HealthTipActivity.class);
            startActivity(intent);
        } else if (view == binding.quickBtnGift) {
            intent = new Intent(getApplicationContext(), GiftActivity.class);
            startActivity(intent);
        }
    }
}

