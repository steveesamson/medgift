package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.databinding.ActivityAddrecipientBinding;
import com.apollo.medgift.models.Recipient;

public class AddRecipientActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddrecipientBinding addrecipientBinding;
    private Recipient recipient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        addrecipientBinding = ActivityAddrecipientBinding.inflate(getLayoutInflater());
        setContentView(addrecipientBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(addrecipientBinding.addRecipient, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        recipient = (Recipient) intent.getSerializableExtra("recipient");
        setup();
    }

    private  void setup(){
       addrecipientBinding.edtAddress.setText(recipient.getAddress());
       addrecipientBinding.edtDOB.setText(recipient.getDateOfBirth());
       addrecipientBinding.edtEmail.setText(recipient.getEmail());
       addrecipientBinding.edtPhone.setText(recipient.getPhone());
       addrecipientBinding.edtFirstName.setText(recipient.getFirstName());
       addrecipientBinding.edtLastName.setText(recipient.getLastName());
       addrecipientBinding.btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}