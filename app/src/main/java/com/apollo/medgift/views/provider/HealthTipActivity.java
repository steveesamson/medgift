package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.gifters.RecipientAdapter;
import com.apollo.medgift.adapters.provider.HealthTipAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityHealthtipBinding;
import com.apollo.medgift.databinding.ActivityRecipientBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.views.gifter.AddRecipientActivity;
import com.apollo.medgift.views.models.HealthtipVModel;
import com.apollo.medgift.views.models.RecipientVModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HealthTipActivity extends BaseActivity {

    private ActivityHealthtipBinding healthtipBinding;
    private final List<HealthTip> healthTips = new ArrayList<>();
    private HealthTipAdapter healthTipAdapter;
    private DatabaseReference db;
    String Role="";
    SharedPreferences sharedPreferences;
    private ValueEventListener healthTipListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        healthtipBinding = ActivityHealthtipBinding.inflate(getLayoutInflater());
        setContentView(healthtipBinding.getRoot());
        // Setup tool bar and title
        setupToolbar(healthtipBinding.homeAppBar.getRoot(), getString(R.string.recipientTitle), true);
        applyWindowInsetsListenerTo(this, healthtipBinding.healthTipActivity);

        //getting role
        Intent intent=getIntent();
        Role = intent.getStringExtra("Role");
        sharedPreferences = getSharedPreferences("Role", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserRole", Role);
        editor.apply();
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
        this.db = Firebase.database(HealthTip.STORE);

        // checking role of user
        checkUserRole(Role);

        RecyclerView recyclerView = healthtipBinding.healthTipList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthTipAdapter = new HealthTipAdapter(healthTips, this);
        recyclerView.setAdapter(healthTipAdapter);
        fetchAndListenOnRecipients();
    }
    private void unRegisterValueListener() {
        if (healthTipListener != null) {
            db.removeEventListener(healthTipListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    private void fetchAndListenOnRecipients() {
        if (db != null) {
            HealthtipVModel healthtipVModel = new ViewModelProvider(this).get(HealthtipVModel.class);
            ValueEvents<HealthTip> valueEvents = new ValueEvents<HealthTip>();
            Util.startProgress(healthtipBinding.progress, "Fetching Health Tips...");
            healthTipListener = valueEvents.registerListener(db, this, healthTipAdapter, healthtipVModel, healthTips, HealthTip.class, (list) -> {
                Util.stopProgress(healthtipBinding.progress);
                healthtipBinding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("health tips") : "");
                healthtipBinding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });

        }
    }
    private void checkUserRole(String role) {
        if ("GIFTER".equals(role)) {
            // hide button for "Gifter"
            healthtipBinding.btnAddHealthtip.setVisibility(View.GONE);
        } else {
            // enable for other roles
            healthtipBinding.btnAddHealthtip.setVisibility(View.VISIBLE);
        }
    }
}