package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.provider.HealthTipAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityHealthtipBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.models.User;
import com.apollo.medgift.views.models.HealthtipVModel;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HealthTipActivity extends BaseActivity {

    private ActivityHealthtipBinding healthtipBinding;
    private final List<HealthTip> healthTips = new ArrayList<>();
    private HealthTipAdapter healthTipAdapter;
    private Query query;

    private ValueEventListener healthTipListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        healthtipBinding = ActivityHealthtipBinding.inflate(getLayoutInflater());
        setContentView(healthtipBinding.getRoot());
        // Setup tool bar and title
        setupToolbar(healthtipBinding.homeAppBar.getRoot(), getString(R.string.healthTipTitle), true);
        applyWindowInsetsListenerTo(this, healthtipBinding.healthTipActivity);

        setPageUp();

    }
    private void setPageUp() {

        healthtipBinding.btnAddHealthtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddHealthTipActivity.class);
                intent.putExtra(HealthTip.STORE, new HealthTip());
                startActivity(intent);
            }
        });
        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;
        if(sessionUser.getUserRole().equals(Role.PROVIDER)) {
            this.query = Firebase.database(HealthTip.STORE).orderByChild("createdBy").equalTo(sessionUser.getUserId());
        } else {
            this.query = Firebase.database(HealthTip.STORE);
        }
        // checking role of user
        healthtipBinding.btnAddHealthtip.setVisibility(Role.GIFTER.equals(sessionUser.getUserRole())? View.GONE : View.VISIBLE);

        RecyclerView recyclerView = healthtipBinding.healthTipList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthTipAdapter = new HealthTipAdapter(healthTips, this);
        recyclerView.setAdapter(healthTipAdapter);
        fetchAndListenOnHealthTip();
    }
    private void unRegisterValueListener() {
        if (healthTipListener != null) {
            query.removeEventListener(healthTipListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    private void fetchAndListenOnHealthTip() {
        if (query != null) {
            HealthtipVModel healthtipVModel = new ViewModelProvider(this).get(HealthtipVModel.class);
            ValueEvents<HealthTip> valueEvents = new ValueEvents<HealthTip>();
            Util.startProgress(healthtipBinding.progress, "Fetching Health Tips...");
            healthTipListener = valueEvents.registerListener(query, this, healthTipAdapter, healthtipVModel, healthTips, HealthTip.class, (list) -> {
                Util.stopProgress(healthtipBinding.progress);
                healthtipBinding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("health tips") : "");
                healthtipBinding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });

        }
    }
}