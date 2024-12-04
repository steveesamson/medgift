package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Closeable;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.databinding.ActivityAlertdetailBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.views.gifter.ServiceDetailActivity;

public class AlertDetail extends BaseActivity {

    private ActivityAlertdetailBinding binding;
    private Message message;
    private Closeable closeable;
    private Gift gift;
    private HealthcareService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlertdetailBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();
        message = (Message)intent.getSerializableExtra(Message.STORE);
        assert message != null;
        switch (message.getNotificationType()){
            case  GiftService:
                GiftService gs = (GiftService)message.getPayLoad();
                closeable = Firebase.getModelBy(HealthcareService.STORE,"key", gs.getServiceId(),HealthcareService.class,(svc) ->{
                    closeable.release();
                    service = svc;
                    setupActivity();
                });
                break;
            case GiftInvite:
                GiftInvite gi = (GiftInvite)message.getPayLoad();
                closeable = Firebase.getModelBy(Gift.STORE,"key", gi.getGiftId(),Gift.class,(gft) ->{
                    closeable.release();
                    gift = gft;
                    setupActivity();
                });
                break;
        }



    }

    private void setupActivity() {
        binding = ActivityAlertdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        binding.alertTitle.setText(message.getTitle());
        binding.alertContent.setText(message.getBody());
        binding.alterButton.setText(message.getButtonLabel());

        binding.alterButton.setOnClickListener((v) ->{
            Intent outIntent;
            switch (message.getNotificationType()){
                case  GiftService:
                    outIntent = new Intent(this, ServiceDetailActivity.class);
                    outIntent.putExtra(HealthcareService.STORE, service);
                    startActivity(outIntent);
                    break;
                case GiftInvite:
                     outIntent = new Intent(this,HealthcareService.class);
                        outIntent.putExtra(Gift.STORE, gift);
                        startActivity(outIntent);
                    break;
            }
        });
    }
}