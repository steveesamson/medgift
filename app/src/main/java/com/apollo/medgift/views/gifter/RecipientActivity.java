package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.gifters.RecipientAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityRecipientBinding;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.views.models.RecipientVModel;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipientActivity extends BaseActivity {
    private static final String TAG = RecipientActivity.class.getSimpleName();

    private ActivityRecipientBinding recipientBinding;

    private final List<Recipient> recipients = new ArrayList<>();
    private RecipientAdapter recipientAdapter;

    private Query query;
    private ValueEventListener recipientsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        recipientBinding = ActivityRecipientBinding.inflate(getLayoutInflater());
        setContentView(recipientBinding.getRoot());
        // Setup tool bar and title
        setupToolbar(recipientBinding.homeAppBar.getRoot(), getString(R.string.recipientTitle), true);
        applyWindowInsetsListenerTo(this, recipientBinding.recipientActivity);

        setPageUp();
    }

    private void setPageUp() {

        recipientBinding.btnAddRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRecipientActivity.class);
                intent.putExtra(Recipient.STORE, new Recipient());
                startActivity(intent);
            }
        });

        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;
        this.query = Firebase.database(Recipient.STORE).orderByChild("createdBy").equalTo(sessionUser.getUserId());
        RecyclerView recyclerView = recipientBinding.recipientsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipientAdapter = new RecipientAdapter(recipients, this);
        recyclerView.setAdapter(recipientAdapter);
        fetchAndListenOnRecipients();
    }

    // Unregister listeners on db
    private void unRegisterValueListener() {
        if (recipientsListener != null) {
            this.query.removeEventListener(recipientsListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    // Fetch Recipient and begin to
    // listen to changes on the list
    private void fetchAndListenOnRecipients() {
        if (this.query != null) {
            RecipientVModel recipientVModel = new ViewModelProvider(this).get(RecipientVModel.class);
            ValueEvents<Recipient> valueEvents = new ValueEvents<Recipient>();
            Util.startProgress(recipientBinding.progress, "Fetching recipients...");
            recipientsListener = valueEvents.registerListener(this.query, this, recipientAdapter, recipientVModel, recipients, Recipient.class, (list) -> {
                Util.stopProgress(recipientBinding.progress);
                recipientBinding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("recipients") : "");
                recipientBinding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });

        }
    }


}