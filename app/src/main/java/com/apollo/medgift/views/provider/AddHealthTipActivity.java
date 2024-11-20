package com.apollo.medgift.views.provider;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class AddHealthTipActivity extends BaseActivity {
    private ActivityAddhealthtipBinding addhealthtipBinding;
    private HealthTip healthTip;
    private final List<HealthTip> healthTips = new ArrayList<>();
    private HealthTipAdapter healthTipAdapter;
    private DatabaseReference db;
    private ValueEventListener healthTipListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        addhealthtipBinding = ActivityAddhealthtipBinding.inflate(getLayoutInflater());
        setContentView(addhealthtipBinding.getRoot());

        Toolbar homeAppBar = addhealthtipBinding.homeAppBar.getRoot();
        setSupportActionBar(homeAppBar);

        // Set Appbar Title and enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Health Tips");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupToolbar(addhealthtipBinding.homeAppBar.getRoot(), getString(R.string.recipientTitle), true); //new toolbar
        ViewCompat.setOnApplyWindowInsetsListener(addhealthtipBinding.addHealthtipActivity, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        healthTip = (HealthTip) intent.getSerializableExtra(Recipient.STORE);

        boolean exists = Util.exists(healthTip);
        String title = getString(exists ? R.string.editRecipientTitle : R.string.addRecipientTitle);
        setupToolbar(addhealthtipBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            healthTip.setCreatedBy(Firebase.currentUser().getUid());
        }
        setup();
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setup() {
        addhealthtipBinding.tipTitle.setText(healthTip.getTitle());
        addhealthtipBinding.tipDescription.setText(healthTip.getContent());
        addhealthtipBinding.btnAddTip.setOnClickListener(this::onClick);
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

            Firebase.save(healthTip, Recipient.STORE, (task) -> {
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
    public void onClick(View view) {

        if (view == addhealthtipBinding.btnAddTip) {
            // Handle save here
            saveHealthTip();
        }

    }
    }