package com.apollo.medgift;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.core.MainMenuActivity;
import com.apollo.medgift.databinding.ActivityGiftBinding;

public class GiftActivity extends MainMenuActivity {
    ActivityGiftBinding giftBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_gift);
        giftBinding = ActivityGiftBinding.inflate(getLayoutInflater());
        View view = giftBinding.getRoot();
        setContentView(view);
        init();

    }
    public void init()
    {

    }
}