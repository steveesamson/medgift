package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Closeable;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityServiceinfoBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.models.DayTime;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Recipient;

public class ServiceInfoActivity extends BaseActivity {

    private ActivityServiceinfoBinding binding;
    private Recipient recipient;
    private HealthcareService service;
    private Gift gift;
    private GiftService giftService;
    private Closeable closeable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        // Setup tool bar and title
        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.serviceDetails), true);

        Intent intent = getIntent();
        giftService = (GiftService) intent.getSerializableExtra(GiftService.STORE);
        assert giftService != null;

        Util.startProgress(binding.progress, "Loading data...");

        closeable = Firebase.getModelBy(HealthcareService.STORE, "key", giftService.getServiceId(), HealthcareService.class, (srv) -> {
            service = srv;
            closeable.release();
            closeable = Firebase.getModelBy(Gift.STORE, "key", giftService.getGiftId(), Gift.class, (gft) -> {
                gift = gft;
                closeable.release();
                assert gift != null;
                closeable = Firebase.getModelBy(Recipient.STORE, "key", gift.getRecipientId(), Recipient.class, (recp) -> {
                    recipient = recp;
                    closeable.release();
                    setupActivity();
                });
            });
        });

    }

    private void setupActivity() {
        Util.stopProgress(binding.progress);
        binding.serviceTitle.setText(service.getServiceName());
        binding.provider.setText(" " + service.getCreatedBy());
        binding.serviceType.setText(" " + service.getServiceType());
        binding.price.setText("$ " + service.getPrice());
        binding.description.setText(service.getDescription());
        if (service.getBannerUrl() != null && !service.getBannerUrl().isEmpty()) {
            Util.loadImageUri(binding.serviceImage, service.getBannerUrl(), this);
        } else {
            binding.serviceImage.setImageResource(R.drawable.default_service_image);
        }
        binding.txtRecipient.setText(recipient.toString());
        binding.txtGiftName.setText(gift.getName());
        binding.txtSchedule.setText(Util.formatToReadableDate(Util.parseTime(giftService.getDeliveryDate())));
        binding.txtRecipientAddress.setText(recipient.getAddress());
        binding.txtStatus.setText(giftService.getStatus());
    }
}