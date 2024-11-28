package com.apollo.medgift.views.gifter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityCheckoutBinding;
import com.apollo.medgift.models.HealthcareService;

import android.text.TextWatcher;
import android.text.Editable;

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

        setupToolbar(checkoutBinding.homeAppBar.getRoot(), getString(R.string.checkout), true);

        setup();

        checkoutBinding.btnConfirmNow.setOnClickListener(this);

        // TextWatcher to edtRecipientEmail
        checkoutBinding.edtRecipientEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update text
                checkoutBinding.serviceRecipient.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setup() {
        // Set service title and type
        checkoutBinding.serviceTitle.setText(healthcareService.getServiceName());
        checkoutBinding.serviceType.setText(" " + healthcareService.getServiceType());

        // Display the total before tax
        checkoutBinding.totalBeforeTax.setText(String.format("$ %.2f", healthcareService.getPrice()));

        // Calculate tax and display it with two decimals
        double tax = (healthcareService.getPrice() * 13) / 100;
        checkoutBinding.estimatedTax.setText(String.format("$ %.2f", tax));

        // Calculate order total and display it with two decimals
        double orderTotal = healthcareService.getPrice() + tax;
        checkoutBinding.orderTotal.setText(String.format("$ %.2f", orderTotal));
    }

    @Override
    public void onClick(View v) {
        if (v == checkoutBinding.btnConfirmNow) {
            //GO TO PAYMENT DETAILS PAGE
        }
    }
}