package com.apollo.medgift.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.ActivityLandingpageBinding;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_FIRST_TIME = "FirstTime";

    ActivityLandingpageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if this is the first time the app is launched
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true);

        if (!isFirstTime) {
            // If not the first time, redirect to LogInActivity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Otherwise, show the landing page
        binding = ActivityLandingpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set click listener
        binding.btnGetStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnGetStarted) {
            // Save in SharedPreferences that the user has seen the landing page
            SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_FIRST_TIME, false); // Mark as not first time
            editor.apply();

            // Navigate to LogInActivity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}