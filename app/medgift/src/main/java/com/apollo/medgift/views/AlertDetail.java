package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAlertdetailBinding;
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
        message = (Message)intent.getSerializableExtra(Message.STORE, Message.class);
        assert message != null;

       setupActivity();

    }

    private void setupActivity() {

        binding.alertTitle.setText(message.getTitle());
        binding.alertContent.setText(message.getBody());

        binding.alterButton.setOnClickListener((v) ->{
            message.setPayloadKey(message.getPayLoad().getKey());
            sendRemoteIntent(message);
        });
    }
}