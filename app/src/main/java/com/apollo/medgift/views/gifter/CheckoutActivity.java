package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Closeable;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityCheckoutBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Payment;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.Schedule;

import java.util.Locale;

public class CheckoutActivity extends BaseActivity implements View.OnClickListener {

    private ActivityCheckoutBinding checkoutBinding;
    private HealthcareService healthcareService;
    private Gift gift;
    private Schedule schedule;
    private Recipient recipient;
    private Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        checkoutBinding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(checkoutBinding.getRoot());
        applyWindowInsetsListenerTo(this, checkoutBinding.main);
        // Setup tool bar and title
        setupToolbar(checkoutBinding.homeAppBar.getRoot(), getString(R.string.checkoutTitle), true);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);
        gift = (Gift) intent.getSerializableExtra(Gift.STORE);
        schedule = (Schedule) intent.getSerializableExtra(Schedule.STORE);
        if (healthcareService == null || gift == null || schedule == null) {
            finish();
            return;
        }

        Util.startProgress(checkoutBinding.progress, "Loading data...");
        Firebase.getModelBy(Recipient.STORE, "key", gift.getRecipientId(), Recipient.class, (rec) -> {
            this.recipient = rec;
            checkoutBinding.btnConfirmNow.setVisibility( rec != null? View.VISIBLE : View.GONE);
            if (this.recipient != null) {
                checkoutBinding.edtRecipient.setText(String.format("%s %s", this.recipient.getFirstName(), this.recipient.getLastName()));
                setup();
            }
        });



    }

    private void setup() {
        Util.stopProgress(checkoutBinding.progress);
        checkoutBinding.btnConfirmNow.setOnClickListener(this);
        // Set service title and type
        checkoutBinding.serviceTitle.setText(healthcareService.getServiceName());
        checkoutBinding.serviceType.setText(" " + healthcareService.getServiceType());

        // Display the total before tax
        checkoutBinding.totalBeforeTax.setText(String.format(Locale.getDefault(),"$ %.2f", healthcareService.getPrice()));

        // Calculate tax and display it with two decimals
        double tax = (healthcareService.getPrice() * 13) / 100;
        payment = new Payment();
        payment.setTax(tax);
        checkoutBinding.estimatedTax.setText(String.format(Locale.getDefault(), "$ %.2f", tax));

        // Calculate order total and display it with two decimals
        double orderTotal = healthcareService.getPrice() + tax;
        payment.setOrderTotal(orderTotal);
        checkoutBinding.orderTotal.setText(String.format(Locale.getDefault(),"$ %.2f", orderTotal));

    }

    @Override
    public void onClick(View v) {
        if (v == checkoutBinding.btnConfirmNow) {
            //GO TO PAYMENT DETAILS PAGE
            Intent intent = new Intent(this, PaymentInformationActivity.class);
            intent.putExtra(HealthcareService.STORE, healthcareService);
            intent.putExtra(Gift.STORE, this.gift);
            intent.putExtra(Recipient.STORE, this.recipient);
            intent.putExtra(Schedule.STORE, this.schedule);
            intent.putExtra(Payment.STORE, this.payment);
            startActivity(intent);
        }
    }
}