package com.apollo.medgift.views.gifter;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityGiftBinding;

public class GiftActivity extends BaseActivity implements View.OnClickListener {
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

    @Override
    public void onClick(View v) {

    }
}