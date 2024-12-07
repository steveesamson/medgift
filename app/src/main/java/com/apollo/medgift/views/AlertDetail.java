package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAlertdetailBinding;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Message;

public class AlertDetail extends BaseActivity {

    private ActivityAlertdetailBinding binding;
    private Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlertdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        Intent intent = getIntent();
        message = (Message)intent.getSerializableExtra(Message.STORE);
        assert message != null;

       setupActivity();

    }

    private void setupActivity() {

        binding.alertTitle.setText(message.getTitle());
        binding.alertContent.setText(message.getBody());
//        binding.alterButton.setText(message.getButtonLabel());

        binding.alterButton.setOnClickListener((v) ->{
            Intent outIntent;
            switch (message.getNotificationType()){
                case GiftService:
                    outIntent = new Intent(this, ServiceInfoActivity.class);
                    GiftService giftService = (GiftService)message.getPayLoad();
                    outIntent.putExtra(GiftService.STORE, giftService);
                    startActivity(outIntent);
                    break;
                case GiftInvite:
                     outIntent = new Intent(this,InviteInfoActivity.class);
                    GiftInvite giftInvite = (GiftInvite) message.getPayLoad();
                        outIntent.putExtra(GiftInvite.STORE, giftInvite);
                        startActivity(outIntent);
                    break;
            }
        });
    }
}