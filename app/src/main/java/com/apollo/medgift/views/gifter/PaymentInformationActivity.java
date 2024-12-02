package com.apollo.medgift.views.gifter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityPaymentinformationBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Payment;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.Schedule;
import com.apollo.medgift.models.ServiceStatus;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.views.HomePageActivity;

import java.util.Locale;

public class PaymentInformationActivity extends BaseActivity {

    private ActivityPaymentinformationBinding binding;
    private HealthcareService service;
    private Gift gift;
    private Schedule schedule;
    private Recipient recipient;
    private Payment payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentinformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.paymentDetail), true);
        Intent intent = getIntent();
        service = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);
        recipient = (Recipient) intent.getSerializableExtra(Recipient.STORE);
        gift = (Gift) intent.getSerializableExtra(Gift.STORE);
        schedule = (Schedule) intent.getSerializableExtra(Schedule.STORE);
        payment = (Payment) intent.getSerializableExtra(Payment.STORE);
        if (service == null || gift == null || schedule == null || recipient == null || payment == null) {
            finish();
            return;
        }
        setupActivity();
    }

    private void clearErrors() {
        binding.lytCardHolderName.setError("");
        binding.lytCardNumber.setError("");
        binding.lytCVV.setError("");
        binding.lytExpireDate.setError("");
    }
    private void setupActivity() {
        binding.txtServiceName.setText(service.getServiceName());
        binding.txtRecipent.setText(String.format("%s %s", this.recipient.getFirstName(), this.recipient.getLastName()));
        binding.totalAmountbeforeTax.setText(String.format(Locale.getDefault(),"$ %.2f", service.getPrice()));
        binding.estimatedTax.setText(String.format(Locale.getDefault(),"$ %.2f", payment.getTax()));
        binding.orderTotal.setText(String.format(Locale.getDefault(),"$ %.2f", payment.getOrderTotal()));
        binding.btnPayNow.setOnClickListener((v) ->{
            clearErrors();

            boolean formIsValid = true;
            String cardHolder = Util.valueOf(binding.edtCardHolderName);
            String cardNo = Util.valueOf(binding.edtCardNumber);
            String expiryDate = Util.valueOf(binding.edtExpireDate);
            String cvv = Util.valueOf(binding.edtCVV);

            if (cardHolder.isEmpty()) {
                binding.lytCardHolderName.setError("Card holder name is required.");
                formIsValid = false;
            }
            if (cardNo.isEmpty()) {
                binding.lytCardNumber.setError("Card number is required.");
                formIsValid = false;
            }
            if (expiryDate.isEmpty()) {
                binding.lytExpireDate.setError("Card expiry is required.");
                formIsValid = false;
            }
            if (cvv.isEmpty()) {
                binding.lytCVV.setError("Card CVV is required.");
                formIsValid = false;
            }
            if(formIsValid){
                GiftService giftService = new GiftService();
                giftService.setGiftId(gift.getKey());
                giftService.setServiceId(service.getKey());
                giftService.setServicePrice(payment.getOrderTotal());
                giftService.setServiceName(service.getServiceName());
                giftService.setServiceDescription(service.getDescription());
                SessionUser sessionUser = Firebase.currentUser();
                assert sessionUser != null;
                giftService.setGifterEmail(sessionUser.getEmail());
                giftService.setGifterName(sessionUser.getUserName());
                giftService.setDeliveryDate(schedule.getSchedule());
                giftService.setStatus(ServiceStatus.SCHEDULED);
                giftService.setContributionDate(Util.today());
                saveGiftService(giftService);
            }
        });
    }

    private void saveGiftService(GiftService giftService){
        boolean exists = Util.exists(giftService);
        Util.startProgress(binding.progress, "Adding service to gift...");
        Firebase.save(giftService, GiftService.STORE, (task, key) -> {
            Util.stopProgress(binding.progress);
            if (task.isSuccessful()) {

                Util.notify(PaymentInformationActivity.this, Util.success("GiftService", exists));
                finish();
                navigateTo(HomePageActivity.class);

            } else {
                Util.notify(PaymentInformationActivity.this, Util.fail("GiftService", exists));
            }
        });
    }

}