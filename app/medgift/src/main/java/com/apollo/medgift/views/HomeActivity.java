package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityMainBinding;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.notificationsButton.setOnClickListener(this);
        binding.healthtipsButton.setOnClickListener(this);
        binding.logoutButton.setOnClickListener(this);
        binding.aboutUsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.notificationsButton) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        }
        if (v == binding.healthtipsButton) {
            Intent intent = new Intent(this, HealthTipActivity.class);
            startActivity(intent);
        }
        if (v == binding.logoutButton) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
        if (v == binding.aboutUsButton) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }
    }
}