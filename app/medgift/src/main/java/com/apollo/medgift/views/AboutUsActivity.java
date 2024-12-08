package com.apollo.medgift.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.apollo.medgift.databinding.ActivityAboutusBinding;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setupToolbar(binding.homeAppBar.getRoot(), "About Us", false);
    }
}