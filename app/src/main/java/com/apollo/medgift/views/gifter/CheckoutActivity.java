package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityCheckoutBinding;
import com.apollo.medgift.models.HealthcareService;

public class CheckoutActivity extends BaseActivity implements View.OnClickListener {

    ActivityCheckoutBinding checkoutBinding;
    private HealthcareService healthcareService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        checkoutBinding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(checkoutBinding.getRoot());
        applyWindowInsetsListenerTo(this, checkoutBinding.main);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);

        setupToolbar(checkoutBinding.homeAppBar.getRoot(), "", true);

        setup();
    }

    private void setup() {

    }

    @Override
    public void onClick(View v) {

    }
}