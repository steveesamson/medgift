package com.apollo.medgift;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.databinding.ActivityInviteGroupGiftBinding;


public class InviteGroupGiftActivity extends AppCompatActivity {
    private ActivityInviteGroupGiftBinding InviteGroupGiftBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        InviteGroupGiftBinding = ActivityInviteGroupGiftBinding.inflate(getLayoutInflater());
        setContentView(InviteGroupGiftBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}