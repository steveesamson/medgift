package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.provider.ServiceAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityViewserviceBinding;
import com.apollo.medgift.models.HealthcareService;

public class ViewServiceActivity extends BaseActivity implements View.OnClickListener {

    ActivityViewserviceBinding viewserviceBinding;
    private HealthcareService healthcareService;
    private ActivityResultLauncher<Intent> serviceImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewserviceBinding = ActivityViewserviceBinding.inflate(getLayoutInflater());
        setContentView(viewserviceBinding.getRoot());
        applyWindowInsetsListenerTo(this, viewserviceBinding.main);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);

        setupToolbar(viewserviceBinding.homeAppBar.getRoot(), "", true);

        viewserviceBinding.scrollView.setVisibility(View.INVISIBLE);
        Util.startProgress(viewserviceBinding.progress, "Loading Service...");

        setup();

        viewserviceBinding.btnAddToGift.setOnClickListener(this);
        viewserviceBinding.btnCheckoutService.setOnClickListener(this);
    }

    private void setup() {
        viewserviceBinding.serviceTitle.setText(healthcareService.getServiceName());
        viewserviceBinding.provider.setText(healthcareService.getCreatedBy());
        viewserviceBinding.serviceType.setText(healthcareService.getServiceType());
        viewserviceBinding.price.setText("$ " + healthcareService.getPrice());
        viewserviceBinding.description.setText(healthcareService.getDescription());

        if (healthcareService.getBannerUrl() != null && !healthcareService.getBannerUrl().isEmpty()) {
            Util.loadImageUri(viewserviceBinding.serviceImage, healthcareService.getBannerUrl(), this);
        } else {
            viewserviceBinding.serviceImage.setImageResource(R.drawable.default_service_image);
        }

        viewserviceBinding.scrollView.setVisibility(View.VISIBLE);
        Util.stopProgress(viewserviceBinding.progress);
    }

    @Override
    public void onClick(View v) {
        if (v == viewserviceBinding.btnAddToGift) {
            // DO ADD TO GIFT
        }
        if (v == viewserviceBinding.btnCheckoutService) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(HealthcareService.STORE, healthcareService);
            this.startActivity(intent);
        }
    }
}