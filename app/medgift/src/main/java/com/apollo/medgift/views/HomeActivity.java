package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityMainBinding;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    //binding obj for homeactivity
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //attach click listeners for each button
        binding.notificationsButton.setOnClickListener(this);
        binding.healthtipsButton.setOnClickListener(this);
        binding.logoutButton.setOnClickListener(this);
        binding.aboutUsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.notificationsButton) {
            //navigate to notifications
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        }
        if (v == binding.healthtipsButton) {
            //navigate to health tips
            Intent intent = new Intent(this, HealthTipActivity.class);
            startActivity(intent);
        }
        if (v == binding.logoutButton) {
            logout();

        }
        if (v == binding.aboutUsButton) {
            //navigate to about us
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }
    }
}