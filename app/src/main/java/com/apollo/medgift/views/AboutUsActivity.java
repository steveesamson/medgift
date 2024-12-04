package com.apollo.medgift.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAboutusBinding;
import com.apollo.medgift.databinding.ActivityHomepageBinding;

public class AboutUsActivity extends BaseActivity {

    ActivityAboutusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.homeAppBar.getRoot(), "About Us", false);
    }
}