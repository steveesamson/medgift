package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.OnModelManageCallback;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddhealthtipBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.User;

public class AddHealthTipActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddhealthtipBinding addhealthtipBinding;
    private HealthTip healthTip;

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

            healthTip.setCreatedBy(Firebase.currentUser().getUserId());
            healthTip.setCreatedByName(Firebase.currentUser().getUserName());

        }
        setup();

    }

    private void setup() {
        addhealthtipBinding.tipTitle.setText(healthTip.getTitle());
        addhealthtipBinding.tipDescription.setText(healthTip.getContent());

        if (Role.GIFTER.equals(Firebase.currentUser().getUserRole())) {
            // hide button for "Gifter"
            // disable the edit texts
            addhealthtipBinding.btnAddTip.setVisibility(View.GONE);
            addhealthtipBinding.tipTitle.setEnabled(false);
            addhealthtipBinding.tipTitle.setFocusable(false);
            addhealthtipBinding.tipDescription.setEnabled(false);
            addhealthtipBinding.tipDescription.setFocusable(false);
            setupToolbar(addhealthtipBinding.homeAppBar.getRoot(), "Health Tip", true);
        } else {
            addhealthtipBinding.btnAddTip.setOnClickListener(this);
            if(!Util.isNullOrEmpty(this.healthTip.getKey())){
                addhealthtipBinding.btnDeleteHealthTip.setOnClickListener(this);
                addhealthtipBinding.btnDeleteHealthTip.setVisibility(View.VISIBLE);
            }
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

            Firebase.save(healthTip, HealthTip.STORE, (task, key) -> {
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
        if(v == addhealthtipBinding.btnDeleteHealthTip){
            // Delete here
                Util.showConfirm(this, "Delete", "Do your really want to delete this tip?", (dialog, which) -> {
                    // Implement delete
                    Firebase.delete(AddHealthTipActivity.this.healthTip, HealthTip.STORE, (task) -> {
                        if(task.isSuccessful()){
                            Util.notify(AddHealthTipActivity.this, "Health tip deleted!");
                        }
                        finish();
                    });
                    dialog.dismiss();
                });
        }
    }

}