package com.apollo.medgift.views;

import android.os.Bundle;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAboutusBinding;

public class AboutUsActivity extends BaseActivity {

    ActivityAboutusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setupToolbar(binding.homeAppBar.getRoot(), "About Us", false);
    }
}