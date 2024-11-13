package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.ActivityProviderhomepageBinding;
import com.apollo.medgift.views.provider.CreateServiceActivity;
import com.apollo.medgift.views.provider.PublishHealthTipsActivity;

public class ProviderHomePageActivity extends AppCompatActivity {

    ActivityProviderhomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  ActivityProviderhomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar homeAppBar = binding.homeAppBar.getRoot();
        setSupportActionBar(homeAppBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MedGift");
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.providermenu, menu);
        return true;
    }
}