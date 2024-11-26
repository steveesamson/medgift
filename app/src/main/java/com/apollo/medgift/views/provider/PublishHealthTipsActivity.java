package com.apollo.medgift.views.provider;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.ActivityPublishhealthtipsBinding;
///////////DELETE THIS CLASS///////////////

public class PublishHealthTipsActivity extends AppCompatActivity {
    ///////////DELETE THIS CLASS///////////////
    ActivityPublishhealthtipsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPublishhealthtipsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar homeAppBar = binding.homeAppBar.getRoot();
        setSupportActionBar(homeAppBar);

        // Set Appbar Title and enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Publish Health Tips");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}