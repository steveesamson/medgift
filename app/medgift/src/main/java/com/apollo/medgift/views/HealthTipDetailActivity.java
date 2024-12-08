package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityHealthtipdetailBinding;
import com.apollo.medgift.models.HealthTip;

public class HealthTipDetailActivity extends BaseActivity {

    private ActivityHealthtipdetailBinding binding ;
    private HealthTip healthTip;//holds the health tip obj passed to this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //set content view
        binding = ActivityHealthtipdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.healthTipTitle), true);
        applyWindowInsetsListenerTo(this, binding.main);

        //retrieve health tip obj from previous activity
        Intent intent = getIntent();
        healthTip = (HealthTip) intent.getSerializableExtra(HealthTip.STORE, HealthTip.class);
        //get and display the health tip details
        setup();

    }
    private  void setup(){
        binding.titleTip.setText(healthTip.getTitle());
        binding.titlecreatedBy.setText(healthTip.getCreatedByName());
        binding.tipDescription.setText(healthTip.getContent());
    }

}