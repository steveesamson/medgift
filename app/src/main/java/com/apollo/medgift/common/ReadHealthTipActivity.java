package com.apollo.medgift.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.databinding.ActivityHealthtipBinding;
import com.apollo.medgift.databinding.ActivityReadhealthtipBinding;
import com.apollo.medgift.models.HealthTip;
import com.google.firebase.database.DatabaseReference;

public class ReadHealthTipActivity extends BaseActivity {

    private ActivityReadhealthtipBinding binding ;
    private DatabaseReference db;
    private HealthTip healthTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityReadhealthtipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.healthTipTitle), true);
        applyWindowInsetsListenerTo(this, binding.readHealthTipActivity);

        Intent intent = getIntent();
        healthTip = (HealthTip) intent.getSerializableExtra(HealthTip.STORE);

        boolean exists = Util.exists(healthTip);

        if (!exists) {

            healthTip.setCreatedBy(Firebase.currentUser().getUserId());
            healthTip.setCreatedByName(Firebase.currentUser().getEmail());
        }
        setup();
    }
    private  void setup(){
        binding.titleTip.setText(healthTip.getTitle());
        binding.titlecreatedBy.setText(healthTip.getCreatedByName());
        binding.tipDescription.setText(healthTip.getContent());
    }

}