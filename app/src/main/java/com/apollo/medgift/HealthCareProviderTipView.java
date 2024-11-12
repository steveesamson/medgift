package com.apollo.medgift;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.core.MainMenuActivity;
import com.apollo.medgift.databinding.ActivityHealthCareProviderTipViewBinding;
import com.apollo.medgift.databinding.ActivityHealthTipsBinding;

public class HealthCareProviderTipView extends MainMenuActivity {
    ActivityHealthCareProviderTipViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHealthCareProviderTipViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar availableServiceAppBar = binding.healthTipAppBar.getRoot();
        setSupportActionBar(availableServiceAppBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Health Tip");
        }
// not sure about this might need to change
        binding.tipEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String tipAlredyPosted = binding.tipDesciption.getText().toString();
//                if(!tipAlredyPosted.equals(null)){
//                    binding.tipDesciption.setSelection(tipAlredyPosted.length());
//                    String editedTip = binding.tipDesciption.getText().toString();
//                    binding.tipDesciption.setText(editedTip);
//                }
            }
        });

    }
}