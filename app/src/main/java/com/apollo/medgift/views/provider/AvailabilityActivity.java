package com.apollo.medgift.views.provider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apollo.medgift.R;
import com.apollo.medgift.adapters.provider.AvailabilityAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityAvailabilityBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.models.SessionUser;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityActivity extends BaseActivity {
    private ActivityAvailabilityBinding availabilityBinding;

    private final List<Availability> availabilityList = new ArrayList<>();
    private AvailabilityAdapter availabilityAdapter;

    private Query query;
    private ValueEventListener availabilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        availabilityBinding = ActivityAvailabilityBinding.inflate(getLayoutInflater());
        setContentView(availabilityBinding.getRoot());

        setupToolbar(availabilityBinding.homeAppBar.getRoot(), getString(R.string.availability_title), true);
        applyWindowInsetsListenerTo(this, availabilityBinding.availabilityActivity);

        setPageUp();
    }

    private void setPageUp() {
        availabilityBinding.btnAddAvailability.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
            intent.putExtra(Availability.STORE, new Availability(""));
            startActivity(intent);
        });

        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;
        this.query = Firebase.database(Availability.STORE).orderByChild("createdBy").equalTo(sessionUser.getUserId());

        RecyclerView recyclerView = availabilityBinding.availabilityList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        availabilityAdapter = new AvailabilityAdapter(availabilityList, this);
        recyclerView.setAdapter(availabilityAdapter);


        fetchAndListenOnAvailability();
    }

    private void unRegisterValueListener() {
        if (availabilityListener != null) {
            this.query.removeEventListener(availabilityListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    private void fetchAndListenOnAvailability() {
        if (this.query != null) {
            ValueEvents<Availability> valueEvents = new ValueEvents<>();
            Util.startProgress(availabilityBinding.progress, "Fetching Availability...");


            availabilityListener = valueEvents.registerListener(
                    this.query,
                    this,
                    availabilityAdapter,
                    availabilityList,
                    Availability.class,
                    (list) -> {
                        Util.stopProgress(availabilityBinding.progress);

                        boolean isEmpty = (list == null || list.isEmpty());
                        availabilityBinding.emptyItem.txtEmpty.setText(isEmpty ? Util.getEmpty("availability") : "");
                        availabilityBinding.emptyItem.getRoot().setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                    }
            );
        }
    }
}