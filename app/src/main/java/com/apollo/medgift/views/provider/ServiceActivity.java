package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.provider.ServiceAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityServiceBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.views.models.ServiceVModel;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends BaseActivity {

    private ActivityServiceBinding serviceBinding;
    private final List<HealthcareService> healthcareServices = new ArrayList<>();
    private ServiceAdapter serviceAdapter;
    private Query query;
    private Gift gift;
    private ValueEventListener serviceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceBinding = ActivityServiceBinding.inflate(getLayoutInflater());
        setContentView(serviceBinding.getRoot());

        String toolbarTitle = Firebase.currentUser().getUserRole().equals(Role.GIFTER) ? getString(R.string.available_services) : getString(R.string.myServicesTitle);
        setupToolbar(serviceBinding.homeAppBar.getRoot(), toolbarTitle, true);

        applyWindowInsetsListenerTo(this, serviceBinding.serviceActivity);
        Intent intent = getIntent();
        gift = (Gift)intent.getSerializableExtra(Gift.STORE);

        setPageUp();
    }

    private void setPageUp() {
        serviceBinding.btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddServiceActivity.class);
                intent.putExtra(HealthcareService.STORE, new HealthcareService());
                startActivity(intent);
            }
        });

        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;

        if (sessionUser.getUserRole().equals(Role.GIFTER)) {
            this.query = Firebase.database(HealthcareService.STORE);
        } else {
            this.query = Firebase.database(HealthcareService.STORE).orderByChild("createdBy").equalTo(sessionUser.getUserId());
        }

        serviceBinding.btnAddService.setVisibility(sessionUser.getUserRole().equals(Role.GIFTER) ? View.GONE : View.VISIBLE);

        RecyclerView recyclerView = serviceBinding.serviceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(healthcareServices, this.gift, this,"service");
        recyclerView.setAdapter(serviceAdapter);
        fetchAndListenOnRecipients();
    }

    private void unRegisterValueListener() {
        if (serviceListener != null) {
            query.removeEventListener(serviceListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    private void fetchAndListenOnRecipients() {
        if (query != null) {
            ServiceVModel serviceVModel = new ViewModelProvider(this).get(ServiceVModel.class);
            ValueEvents<HealthcareService> valueEvents = new ValueEvents<>();
            Util.startProgress(serviceBinding.progress, "Fetching Services...");
            serviceListener = valueEvents.registerListener(query, this, serviceAdapter, serviceVModel, healthcareServices, HealthcareService.class, (list) -> {
                Util.stopProgress(serviceBinding.progress);
                serviceBinding.emptyItem.txtEmpty.setText(list.isEmpty() ? Util.getEmpty("services") : "");
                serviceBinding.emptyItem.getRoot().setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });
        }
    }
}