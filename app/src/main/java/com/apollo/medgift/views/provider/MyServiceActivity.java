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
import com.apollo.medgift.databinding.ActivityMyserviceBinding;
import com.apollo.medgift.models.Service;
import com.apollo.medgift.models.User;
import com.apollo.medgift.views.models.ServiceVModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyServiceActivity extends BaseActivity {

    private ActivityMyserviceBinding myserviceBinding;
    private final List<Service> services = new ArrayList<>();
    private ServiceAdapter serviceAdapter;
    private DatabaseReference db;

    private ValueEventListener serviceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        myserviceBinding = ActivityMyserviceBinding.inflate(getLayoutInflater());
        setContentView(myserviceBinding.getRoot());

        setupToolbar(myserviceBinding.homeAppBar.getRoot(), getString(R.string.myServicesTitle), true);
        applyWindowInsetsListenerTo(this, myserviceBinding.serviceActivity);

        setPageUp();
    }

    private void setPageUp() {
        myserviceBinding.btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateServiceActivity.class);
                intent.putExtra(Service.STORE, new Service());
                startActivity(intent);
            }
        });

        this.db = Firebase.database(Service.STORE);

        checkUserRole();

        RecyclerView recyclerView = myserviceBinding.serviceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(services, this);
        recyclerView.setAdapter(serviceAdapter);
        fetchAndListenOnRecipients();

    }

    private void unRegisterValueListener() {
        if (serviceListener != null) {
            db.removeEventListener(serviceListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    private void fetchAndListenOnRecipients() {
        if (db != null) {
            ServiceVModel serviceVModel = new ViewModelProvider(this).get(ServiceVModel.class);
            ValueEvents<Service> valueEvents = new ValueEvents<>();
            Util.startProgress(myserviceBinding.progress, "Fetching Services...");
            serviceListener = valueEvents.registerListener(db, this, serviceAdapter, serviceVModel, services, Service.class, (list) -> {
                Util.stopProgress(myserviceBinding.progress);
                myserviceBinding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("services") : "");
                myserviceBinding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });
        }
    }

    private void checkUserRole() {
        if (User.Role.GIFTER.name().equals(Firebase.currentUser().getUserRole())) {
            // hide button for "Gifter"
            myserviceBinding.btnAddService.setVisibility(View.GONE);
        } else {
            // enable for other roles
            myserviceBinding.btnAddService.setVisibility(View.VISIBLE);
        }
    }
}