package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityConfirmpaymentBinding;
import com.apollo.medgift.views.HomePageActivity;

public class ConfirmPaymentActivity extends BaseActivity implements View.OnClickListener {

    private ActivityConfirmpaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmpaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.paymentDetail), false);

        binding.btnGoHome.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnGoHome) {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }
}