package com.apollo.medgift.views.gifter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.gifters.GiftAdapter;

import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityGiftBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.views.models.GiftVModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GiftActivity extends BaseActivity{

    private ActivityGiftBinding giftBinding;

    private final List<Gift> gift = new ArrayList<>();
    private GiftAdapter giftAdapter;


    private DatabaseReference db;
    private ValueEventListener giftsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        giftBinding = ActivityGiftBinding.inflate(getLayoutInflater());
        setContentView(giftBinding.getRoot());
        // Setup tool bar and title
        setupToolbar(giftBinding.homeAppBar.getRoot(), getString(R.string.giftTitle), true); //new toolbar
        applyWindowInsetsListenerTo(this,giftBinding.giftActivity);

        setPageUp();
    }

    private void setPageUp()
    {
        giftBinding.btnAddGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftActivity.this, AddGiftActivity.class);
                intent.putExtra(Gift.STORE, new Gift());
                startActivity(intent);
            }
        });


        this.db = Firebase.database(Gift.STORE);
        RecyclerView recyclerView = giftBinding.giftList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        giftAdapter = new GiftAdapter(gift, this);
        recyclerView.setAdapter(giftAdapter);
        fetchAndListenOnGifts();
    }

    // Unregister listeners on db
    private void unRegisterValueListener() {
        if (giftsListener != null) {
            db.removeEventListener(giftsListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    // Fetch Gifts and begin to
    // listen to changes on the list
    private void fetchAndListenOnGifts() {
        if (db != null) {
            GiftVModel giftVModel = new ViewModelProvider(this).get(GiftVModel.class);
            ValueEvents<Gift> valueEvents = new ValueEvents<Gift>();
            Util.startProgress(giftBinding.progress, "Fetching gifts...");
            giftsListener = valueEvents.registerListener(db, this, giftAdapter, giftVModel, gift, Gift.class, (list) -> {
                Util.stopProgress(giftBinding.progress);
                giftBinding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("gifts") : "");
                giftBinding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });

        }
    }


}