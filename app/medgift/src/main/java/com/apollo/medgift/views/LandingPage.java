package com.apollo.medgift.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityLandingpageBinding;

public class LandingPage extends BaseActivity implements View.OnClickListener {

    //shred preferences to store the app pref and to track the first time users on app launch
    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_FIRST_TIME = "FirstTime";

    //binding obj to access views
    ActivityLandingpageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if this is the first time the app is launched
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true);//set by default true

        if (!isFirstTime) {
            // If not the first time, redirect to LogInActivity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();// close current activity
            return;//end of onCreate
        }

        //inflate layout
        binding = ActivityLandingpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set the onclick for the launch/get started button
        binding.btnGetStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //if the get started button is clicked do
        if (v == binding.btnGetStarted) {
            // Save in SharedPreferences that the user has seen the landing page
            SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_FIRST_TIME, false); // Mark as not first time
            editor.apply();//save changes

            //navigate to login activity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();//close current activity
        }
    }
}