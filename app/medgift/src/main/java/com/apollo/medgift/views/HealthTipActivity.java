package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.apollo.medgift.views.models.HealthtipVModel;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HealthTipActivity extends BaseActivity {

    private ActivityHealthtipBinding healthtipBinding;
    private final List<HealthTip> healthTips = new ArrayList<>();//store health tips data
    private HealthTipAdapter healthTipAdapter;//adapter to display health tips recycler view
    private Query query;//varieable to hold database query

    private ValueEventListener healthTipListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        healthtipBinding = ActivityHealthtipBinding.inflate(getLayoutInflater());
        setContentView(healthtipBinding.getRoot());
        // Setup tool bar and title
        applyWindowInsetsListenerTo(this, healthtipBinding.main);

        setPageUp();//setup the page and fetch data

    }

    private void setPageUp() {
        //get current user
        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;//check if user is not null

        this.query = Firebase.database(HealthTip.STORE);

        //set up recycler view
        RecyclerView recyclerView = healthtipBinding.healthTipsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initialize health tip adapter
        healthTipAdapter = new HealthTipAdapter(healthTips, this);
        recyclerView.setAdapter(healthTipAdapter);
        Log.i("HealthT", "Here");
        fetchAndListenOnHealthTip();
    }

    private void unRegisterValueListener() {
        if (healthTipListener != null) {
            //remove listener
            query.removeEventListener(healthTipListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //clean up on destory
        unRegisterValueListener();
    }

    private void fetchAndListenOnHealthTip() {
        if (query != null) {
            //view model to handle health tip data
            HealthtipVModel healthtipVModel = new ViewModelProvider(this).get(HealthtipVModel.class);
            //manage firebase events
            ValueEvents<HealthTip> valueEvents = new ValueEvents<HealthTip>();
            //show progress for loading
            Util.startProgress(healthtipBinding.progress, "Fetching Health Tips...");
            healthTipListener = valueEvents.registerListener(query, this, healthTipAdapter, healthtipVModel, healthTips, HealthTip.class, (list) -> {

                //stop the loading indicator
                Util.stopProgress(healthtipBinding.progress);

                if (!list.isEmpty()) {
                    HealthTip ht = list.get(0);
                    Log.i("TAG", ht.getTitle());
                }
                //update UI
                healthtipBinding.emptyItem.txtEmpty.setText(list.isEmpty() ? Util.getEmpty("health tips") : "");
                healthtipBinding.emptyItem.getRoot().setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });

        }
    }
}