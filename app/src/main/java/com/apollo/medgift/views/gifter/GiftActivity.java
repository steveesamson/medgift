package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.GiftRecyclerViewAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityGiftBinding;
import com.apollo.medgift.models.Contributor;
import com.apollo.medgift.models.Gift;

import java.util.ArrayList;
import java.util.List;

public class GiftActivity extends BaseActivity implements View.OnClickListener {

    private static final int ADD_GIFT_REQUEST_CODE = 1;
    ActivityGiftBinding giftBinding;
    private List<Gift> giftList;
    private GiftRecyclerViewAdapter giftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giftBinding = ActivityGiftBinding.inflate(getLayoutInflater());
        View view = giftBinding.getRoot();
        setContentView(view);

        // Setup Toolbar
        Toolbar toolbar = giftBinding.homeAppBar.getRoot();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Gifts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Gift List
        initGiftList();

        // Set RecyclerView
        setupRecyclerView();

        // Add Gift Button Listener
        giftBinding.addGift.setOnClickListener(this);
    }

    private void initGiftList() {
        giftList = new ArrayList<>();

    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = giftBinding.giftRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        giftAdapter = new GiftRecyclerViewAdapter(this, giftList);
        recyclerView.setAdapter(giftAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == giftBinding.addGift.getId()) {
            Intent intent = new Intent(this, AddGiftActivity.class);
            startActivityForResult(intent, ADD_GIFT_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GIFT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String giftName = data.getStringExtra("gift_name");
            String giftDescription = data.getStringExtra("gift_description");
            boolean isGroupGift = data.getBooleanExtra("is_group_gift", false);
            ArrayList<Contributor> contributors = (ArrayList<Contributor>) data.getSerializableExtra("contributors");

            Log.d("GiftActivity", "Gift Received: " + giftName + ", " + giftDescription + ", Group Gift: " + isGroupGift);


            if (giftName != null && giftDescription != null) {
                Gift newGift = new Gift();
                newGift.setName(giftName);
                newGift.setDescription(giftDescription);
                newGift.setIsGroup(isGroupGift);
                newGift.setContributors(contributors != null ? contributors : new ArrayList<>());

                giftList.add(newGift);
                giftAdapter.notifyDataSetChanged();
                updateGifts();
                Toast.makeText(this, "Gift added successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateGifts() {
        if (giftList.isEmpty()) {
            giftBinding.giftList.setVisibility(View.VISIBLE);
            giftBinding.giftRecyclerView.setVisibility(View.GONE);
        } else {
            giftBinding.giftList.setVisibility(View.GONE);
            giftBinding.giftRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}