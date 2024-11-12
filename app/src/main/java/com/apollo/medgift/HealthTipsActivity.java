package com.apollo.medgift;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.adapters.HealthTipsRecyclerViewAdapter;
import com.apollo.medgift.core.MainMenuActivity;
import com.apollo.medgift.databinding.ActivityAvailableServicesBinding;
import com.apollo.medgift.databinding.ActivityHealthTipsBinding;

public class HealthTipsActivity extends MainMenuActivity {

ActivityHealthTipsBinding binding;

    String titleList[] = {"Stay Hydrated", "Stay Hydrated"};
    String contentList[] = {"Drinking enough water is crucial for maintaining overall health. Aim for 8 glasses of water a day.", "Drinking enough water is crucial for maintaining overall health. Aim for 8 glasses of water a day."};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHealthTipsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar availableServiceAppBar = binding.healthTipsAppBar.getRoot();
        setSupportActionBar(availableServiceAppBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Health Tips");
        }

        // Health Tips List
        RecyclerView healthTipsRecyclerView = binding.healthTipsRecyclerView;
        HealthTipsRecyclerViewAdapter healthTipsAdapter = new HealthTipsRecyclerViewAdapter(this, titleList, contentList);
        healthTipsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthTipsRecyclerView.setAdapter(healthTipsAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }
}