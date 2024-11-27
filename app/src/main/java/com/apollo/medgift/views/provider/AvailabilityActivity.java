package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAvailabilityBinding;
import com.apollo.medgift.models.Availability;

import java.util.ArrayList;

public class AvailabilityActivity extends BaseActivity {
    private ActivityAvailabilityBinding availabilityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        availabilityBinding = ActivityAvailabilityBinding.inflate(getLayoutInflater());
        setContentView(availabilityBinding.getRoot());


        setupToolbar(availabilityBinding.homeAppBar.getRoot(), getString(R.string.availability_title), true);


        setupRecyclerView();


        Intent intent = getIntent();
        Availability newAvailability = (Availability) intent.getSerializableExtra("new_availability");
        if (newAvailability != null) {

        }


        setPageUp();
    }

    private void setupRecyclerView() {


    }

    private void setPageUp() {

        availabilityBinding.btnAddAvailability.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
            startActivity(intent);
        });
    }
}