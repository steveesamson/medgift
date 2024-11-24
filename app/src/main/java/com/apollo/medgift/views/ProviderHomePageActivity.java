package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityProviderhomepageBinding;
import com.apollo.medgift.views.provider.AddHealthTipActivity;
import com.apollo.medgift.views.provider.CreateServiceActivity;
import com.apollo.medgift.views.provider.HealthTipActivity;
import com.apollo.medgift.views.provider.PublishHealthTipsActivity;

public class ProviderHomePageActivity extends BaseActivity {

    ActivityProviderhomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  ActivityProviderhomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Util.applyWindowInsetsListenerTo(this, binding.main);
        // Setup tool bar and title
        setupToolbar(binding.homeAppBar.getRoot(), "MedGift", false);

        // Set click listener for category_card_1 to open CreateServiceActivity
        binding.categoryCard1.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderHomePageActivity.this, CreateServiceActivity.class);
            startActivity(intent);
        });

        binding.categoryCard2.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderHomePageActivity.this, PublishHealthTipsActivity.class);
            startActivity(intent);
        });

        binding.categoryCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePageActivity.this, HealthTipActivity.class);
                startActivity(intent);
            }
        });
    }
}