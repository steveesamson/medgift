package com.apollo.medgift.views.gifter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAddrecipientBinding;
import com.apollo.medgift.databinding.ActivityRecipientBinding;

public class RecipientActivity extends BaseActivity {
    private ActivityRecipientBinding recipientBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        recipientBinding = ActivityRecipientBinding.inflate(getLayoutInflater());
        setContentView(recipientBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(recipientBinding.recipientActivity, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}