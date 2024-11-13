package com.apollo.medgift.views.provider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.ActivityCreateserviceBinding;

public class CreateServiceActivity extends AppCompatActivity {

    ActivityCreateserviceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateserviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar homeAppBar = binding.homeAppBar.getRoot();
        setSupportActionBar(homeAppBar);

        // Set Appbar Title and enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Services");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerCategory.setAdapter(adapter);
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