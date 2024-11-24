package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.provider.HealthTipAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddhealthtipBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.views.gifter.AddRecipientActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

public class AddHealthTipActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddhealthtipBinding addhealthtipBinding;
    private HealthTip healthTip;
    SharedPreferences sharedPreferences;
    String Role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        addhealthtipBinding = ActivityAddhealthtipBinding.inflate(getLayoutInflater());
        setContentView(addhealthtipBinding.getRoot());
        applyWindowInsetsListenerTo(this, addhealthtipBinding.addHealthtipActivity);


        Intent intent = getIntent();
        healthTip = (HealthTip) intent.getSerializableExtra(HealthTip.STORE);

        boolean exists = Util.exists(healthTip);
        String title = getString(exists ? R.string.editHealthtipTitle : R.string.addHealthtipTitle);
        setupToolbar(addhealthtipBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            healthTip.setCreatedBy(Firebase.currentUser().getUid());
            healthTip.setCreatedByName(Firebase.currentUser().getEmail());
        }
        setup();
        }
    private void setup() {
        addhealthtipBinding.tipTitle.setText(healthTip.getTitle());
        addhealthtipBinding.tipDescription.setText(healthTip.getContent());
        addhealthtipBinding.btnAddTip.setOnClickListener(this);
        if ("GIFTER".equals(getUserRole())) {
            // hide button for "Gifter"
            addhealthtipBinding.btnAddTip.setVisibility(View.GONE);
            addhealthtipBinding.tipTitle.setEnabled(false);
            addhealthtipBinding.tipTitle.setFocusable(false);
            addhealthtipBinding.tipDescription.setEnabled(false);
            addhealthtipBinding.tipDescription.setFocusable(false);
            setupToolbar(addhealthtipBinding.homeAppBar.getRoot(), "Health Tip", true);
        } else {
            // enable buttons for other roles
            addhealthtipBinding.btnAddTip.setVisibility(View.VISIBLE);
        }
    }

        private void clearErrors() {
        addhealthtipBinding.lytTitle.setError("");
        addhealthtipBinding.lytDescription.setError("");
    }
    private void saveHealthTip() {
        clearErrors();

        boolean formIsValid = true;
        String tipTitle = Util.valueOf(addhealthtipBinding.tipTitle);
        String tipDescription = Util.valueOf(addhealthtipBinding.tipDescription);

        if (tipTitle.isEmpty()) {
            addhealthtipBinding.lytTitle.setError("Title is required.");
            formIsValid = false;
        }
        if (tipDescription.isEmpty()) {
            addhealthtipBinding.lytDescription.setError("Description is required.");
            formIsValid = false;
        }

        if (formIsValid) {
            healthTip.setTitle(tipTitle);
            healthTip.setContent(tipDescription);

            boolean exists = Util.exists(healthTip);
            Util.startProgress(addhealthtipBinding.progress, "Adding Health Tip...");

            Firebase.save(healthTip, HealthTip.STORE, (task) -> {
                Util.stopProgress(addhealthtipBinding.progress);
                if (task.isSuccessful()) {

                    healthTip = null;
                    Util.notify(AddHealthTipActivity.this, Util.success("Health Tip", exists));
                    finish();

                } else {
                    Util.notify(AddHealthTipActivity.this, Util.fail("Health Tip", exists));
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v == addhealthtipBinding.btnAddTip) {
            // Handle save here
            saveHealthTip();
        }
    }
    public String getUserRole(){
        // getting shared preference data
        sharedPreferences = getSharedPreferences("Role", MODE_PRIVATE);
        Role = sharedPreferences.getString("UserRole", "Role");
        return Role;
    }

    }