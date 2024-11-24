package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.ContributorAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddgiftBinding;
import com.apollo.medgift.databinding.ActivityAddrecipientBinding;
import com.apollo.medgift.databinding.ContributorDialogBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGiftActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAddgiftBinding addGiftBinding;
    private ContributorAdapter contributorAdapter;
    private Gift gift;
    private List<User> contributors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        contributors = new ArrayList<>();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        addGiftBinding = ActivityAddgiftBinding.inflate(getLayoutInflater());
        setContentView(addGiftBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(addGiftBinding.addGift, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        gift = (Gift)intent.getSerializableExtra(Gift.STORE);

        boolean exists = Util.exists(gift);
        String title = getString(exists ? R.string.editGiftTitle : R.string.addGiftTitle);
        setupToolbar(addGiftBinding.homeAppBar.getRoot(), title, true);
        setup();

    }

    private void setup() {
        addGiftBinding.edtGiftName.setText(gift.getName());
        addGiftBinding.edtRecipientId.setText(gift.getRecipientId());
        addGiftBinding.edtGiftDescription.setText(gift.getDescription());

        addGiftBinding.isGroupGift.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addGiftBinding.contributorsLyt.setVisibility(View.VISIBLE);
            } else {
                addGiftBinding.contributorsLyt.setVisibility(View.GONE);
            }
        });

        addGiftBinding.inviteContributors.setOnClickListener(this);
        addGiftBinding.btnSave.setOnClickListener(this);

        addGiftBinding.contributorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contributorAdapter = new ContributorAdapter(this, contributors);
        addGiftBinding.contributorsRecyclerView.setAdapter(contributorAdapter);
        updateContributors();
    }

    private void clearErrors() {
        addGiftBinding.edtGiftName.setError("");
        addGiftBinding.edtRecipientId.setError("");
        addGiftBinding.edtGiftDescription.setError("");
    }

    private void saveGift() {
        clearErrors();

        String giftName = Util.valueOf(addGiftBinding.edtGiftName);
        String recipientId= Util.valueOf(addGiftBinding.edtRecipientId);
        String giftDescription = Util.valueOf(addGiftBinding.edtGiftDescription);
        boolean isGroupGift = addGiftBinding.isGroupGift.isChecked();

        boolean formIsValid = true;

        if (TextUtils.isEmpty(giftName)) {
            addGiftBinding.lytGiftName.setError("Gift name is required.");
            formIsValid = false;
        }
        if (TextUtils.isEmpty(giftDescription)) {
            addGiftBinding.lytGiftDescription.setError("Gift description is required.");
            formIsValid = false;
        }

        if (formIsValid)
        {
            gift.setName(giftName);
            gift.setRecipientId(recipientId);
            gift.setDescription(giftDescription);

            boolean exists = Util.exists(gift);
            Util.startProgress(addGiftBinding.progress, "Adding Gift...");

            Firebase.save(gift, Gift.STORE, (task) -> {
                Util.stopProgress(addGiftBinding.progress);
                if (task.isSuccessful()) {

                    gift = null;
                    Util.notify(AddGiftActivity.this, Util.success("Gift", exists));
                    finish();

                } else {
                    Util.notify(AddGiftActivity.this, Util.fail("Gift", exists));
                }
            });
        }

    }


    @Override
    public void onClick(View view) {
        if (view == addGiftBinding.btnSave)
        {
            saveGift();
        }
        else if (view == addGiftBinding.inviteContributors)
        {
            showInviteContributorsDialog();
        }
    }

    private void showInviteContributorsDialog() {
        ContributorDialogBinding dialogBinding = ContributorDialogBinding.inflate(getLayoutInflater());

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Invite Contributor")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Invite", (dialog, which) -> {
                    String email = Util.valueOf(dialogBinding.edtContributorEmail);
                    if (!TextUtils.isEmpty(email)) {
                        User contributor = new User(User.Role.GIFTER);
                        contributor.setEmail(email);

                        contributors.add(contributor);
                        Log.d("AddGiftActivity", "Contributor added: " + email);
                        contributorAdapter.notifyDataSetChanged();
                        updateContributors();

                        Util.notify(this, "Contributor invited: " + email);
                    } else {
                        Util.notify(this, "Please provide a valid email.");
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void updateContributors() {
        if (contributors.isEmpty())
        {

            addGiftBinding.contributorsRecyclerView.setVisibility(View.GONE);
        }
        else
        {

            addGiftBinding.contributorsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}