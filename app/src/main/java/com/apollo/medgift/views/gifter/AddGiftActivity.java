package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.adapters.ContributorAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.databinding.ActivityAddgiftBinding;
import com.apollo.medgift.databinding.ContributorDialogBinding;
import com.apollo.medgift.models.Contributor;

import java.util.ArrayList;
import java.util.List;

public class AddGiftActivity extends BaseActivity implements View.OnClickListener {

    ActivityAddgiftBinding addgiftBinding;
    ContributorAdapter contributorAdapter;
    List<Contributor> contributors = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_addgift);
        addgiftBinding = ActivityAddgiftBinding.inflate(getLayoutInflater());
        View view = addgiftBinding.getRoot();
        setContentView(view);

        Toolbar toolbar = addgiftBinding.homeAppBar.getRoot();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Gift");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        CheckBox isGroupGift = addgiftBinding.isGroupGift;
        LinearLayout contributorsLyt = addgiftBinding.contributorsLyt;
        TextView inviteContributors = addgiftBinding.inviteContributors;

        isGroupGift.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                contributorsLyt.setVisibility(View.VISIBLE);
            } else {
                contributorsLyt.setVisibility(View.GONE);
            }
        });
        inviteContributors.setOnClickListener(this);

        RecyclerView recyclerView = addgiftBinding.contributorsRecyclerView;
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contributorAdapter = new ContributorAdapter(this, contributors);
        recyclerView.setAdapter(contributorAdapter);

        addgiftBinding.btnSave.setOnClickListener(v -> {
            String giftName = addgiftBinding.edtGiftName.getText().toString();
            String giftDescription = addgiftBinding.edtGiftDescription.getText().toString();

            if (giftName.isEmpty() || giftDescription.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("gift_name", giftName);
            resultIntent.putExtra("gift_description", giftDescription);
            resultIntent.putExtra("is_group_gift", isGroupGift.isChecked());
            resultIntent.putExtra("contributors", new ArrayList<>(contributors));
            setResult(RESULT_OK, resultIntent);
            Log.d("AddGiftActivity", "Finishing with result: " + giftName.toString() + ", " + contributors.size() + " contributors");
            finish();
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == addgiftBinding.inviteContributors.getId()) {

            showInviteContributorsDialog();
        }

    }

    private void showInviteContributorsDialog() {
        ContributorDialogBinding dialogBinding = ContributorDialogBinding.inflate(getLayoutInflater());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite Contributor")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Invite", (dialog, which) -> {
                    String email = dialogBinding.edtContributorEmail.getText().toString().trim();
                    String firstName = dialogBinding.edtContributorFirstName.getText().toString().trim();
                    String lastName = dialogBinding.edtContributorLastName.getText().toString().trim();

                    if (!email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
                        Contributor contributor = new Contributor(firstName, lastName, email);
                        contributors.add(contributor);
                        Log.d("AddGiftActivity", "Contributor added: " + contributor.getFirstName() + " " + contributor.getLastName() + " | Email: " + contributor.getEmail());
                        contributorAdapter.notifyDataSetChanged();
                        updateContributors();
                        Toast.makeText(this, "Contributor Invited: " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    } else {

                        Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void updateContributors() {

        if (contributors.isEmpty()) {

            addgiftBinding.contributorsList.setVisibility(View.VISIBLE);
            addgiftBinding.contributorsRecyclerView.setVisibility(View.GONE);
        } else {

            addgiftBinding.contributorsList.setVisibility(View.GONE);
            addgiftBinding.contributorsRecyclerView.setVisibility(View.VISIBLE);
        }

    }





}
