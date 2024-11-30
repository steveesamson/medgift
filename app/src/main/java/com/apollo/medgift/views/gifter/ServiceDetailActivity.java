package com.apollo.medgift.views.gifter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityServicedetailBinding;
import com.apollo.medgift.models.HealthcareService;

public class ServiceDetailActivity extends BaseActivity implements View.OnClickListener {

    ActivityServicedetailBinding servicedetailBinding;
    private HealthcareService healthcareService;
    private ActivityResultLauncher<Intent> serviceImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicedetailBinding = ActivityServicedetailBinding.inflate(getLayoutInflater());
        setContentView(servicedetailBinding.getRoot());
        applyWindowInsetsListenerTo(this, servicedetailBinding.main);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);

        setupToolbar(servicedetailBinding.homeAppBar.getRoot(), "", true);

        servicedetailBinding.scrollView.setVisibility(View.INVISIBLE);
        Util.startProgress(servicedetailBinding.progress, "Loading Service...");

        setup();

        servicedetailBinding.btnAddToGift.setOnClickListener(this);
        servicedetailBinding.btnCheckoutService.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setup() {
        servicedetailBinding.serviceTitle.setText(healthcareService.getServiceName());
        servicedetailBinding.provider.setText(" " + healthcareService.getCreatedBy());
        servicedetailBinding.serviceType.setText(" " + healthcareService.getServiceType());
        servicedetailBinding.price.setText("$ " + healthcareService.getPrice());
        servicedetailBinding.description.setText(healthcareService.getDescription());

        if (healthcareService.getBannerUrl() != null && !healthcareService.getBannerUrl().isEmpty()) {
            Util.loadImageUri(servicedetailBinding.serviceImage, healthcareService.getBannerUrl(), this);
        } else {
            servicedetailBinding.serviceImage.setImageResource(R.drawable.default_service_image);
        }

        servicedetailBinding.scrollView.setVisibility(View.VISIBLE);
        Util.stopProgress(servicedetailBinding.progress);
    }

    @Override
    public void onClick(View v) {
        if (v == servicedetailBinding.btnAddToGift) {
            // DO ADD TO GIFT
        }
        if (v == servicedetailBinding.btnCheckoutService) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(HealthcareService.STORE, healthcareService);
            this.startActivity(intent);
        }
    }
}