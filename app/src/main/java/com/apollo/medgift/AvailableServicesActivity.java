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

import com.apollo.medgift.adapters.ForYouRecyclerViewAdapter;
import com.apollo.medgift.core.MainMenuActivity;
import com.apollo.medgift.databinding.ActivityAvailableServicesBinding;
import com.apollo.medgift.databinding.ActivityHomepageBinding;

public class AvailableServicesActivity extends MainMenuActivity {

    ActivityAvailableServicesBinding binding;


    String gift_title[] = {"Telemedicine Consultations", "Telemedicine Consultations"};
    String gift_provider[] = {"Provider - 1", "Provider - 2"};
    String gift_location[] = {"Canada", "Canada"};
    String gift_description[] = {"Connect with a doctor remotely for a virtual check-up or to address health concerns from the comfort of your home.", "Connect with a doctor remotely for a virtual check-up or to address health concerns from the comfort of your home."};
    String gift_price[] = {"$ 423.00", "$ 565.00"};
    int[] images = {R.drawable.sample_image1, R.drawable.sample_image2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAvailableServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar availableServiceAppBar = binding.availableServiceAppBar.getRoot();
        setSupportActionBar(availableServiceAppBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Available Services");
        }

        // Available Services list
        RecyclerView forYouRecyclerView = binding.availableServicesRecyclerView;
        ForYouRecyclerViewAdapter forYouAdapter = new ForYouRecyclerViewAdapter(this, images, gift_title, gift_provider, gift_location, gift_description, gift_price);
        forYouRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        forYouRecyclerView.setAdapter(forYouAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }
}