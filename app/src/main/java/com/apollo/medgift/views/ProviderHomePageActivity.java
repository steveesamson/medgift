package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityProviderhomepageBinding;
import com.apollo.medgift.views.provider.CreateServiceActivity;
import com.apollo.medgift.views.provider.PublishHealthTipsActivity;

public class ProviderHomePageActivity extends BaseActivity {

    ActivityProviderhomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  ActivityProviderhomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup tool bar and title
        setupToolbar(binding.homeAppBar.getRoot(), "MedGift", false);

        // Set click listener for category_card_1 to open CreateServiceActivity
        binding.categoryCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePageActivity.this, CreateServiceActivity.class);
                startActivity(intent);
            }
        });

        binding.categoryCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePageActivity.this, PublishHealthTipsActivity.class);
                startActivity(intent);
            }
        });
    }
}