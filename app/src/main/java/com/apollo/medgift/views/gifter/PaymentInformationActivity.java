package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityPaymentinformationBinding;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Recipient;

public class PaymentInformationActivity extends BaseActivity {

    private ActivityPaymentinformationBinding binding;
    private HealthcareService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentinformationBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        Intent intent = getIntent();
        service = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);

        setupToolbar(binding.homeAppBar.getRoot(), "Service Details", true);
        setupActivity();
    }

    private void setupActivity() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}