package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.gifters.RecipientAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.BaseModel;
import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityRecipientBinding;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.views.models.RecipientVModel;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class RecipientActivity extends BaseActivity {
    private ActivityRecipientBinding recipientBinding;

    private final List<Recipient> recipients = new ArrayList<>();
    private RecipientAdapter recipientAdapter;


    private DatabaseReference db;
    private ValueEventListener recipientsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        recipientBinding = ActivityRecipientBinding.inflate(getLayoutInflater());
        setContentView(recipientBinding.getRoot());
        setToolBar(recipientBinding.homeAppBar.getRoot(), getString(R.string.recipientTitle));
        ViewCompat.setOnApplyWindowInsetsListener(recipientBinding.recipientActivity, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setPageUp();
    }

    private void setPageUp() {

        recipientBinding.btnAddRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRecipientActivity.class);
                Recipient recipient = new Recipient();
                intent.putExtra(Recipient.STORE, recipient);
                startActivity(intent);
            }
        });
        this.db = Firebase.database(Recipient.STORE);
        RecyclerView recyclerView = recipientBinding.recipientsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipientAdapter = new RecipientAdapter(recipients, this);
        recyclerView.setAdapter(recipientAdapter);
        fetchAndListenOnRecipients();
    }

    // Unregister listeners on db
    private void unRegisterValueListener() {
        if (recipientsListener != null) {
            db.removeEventListener(recipientsListener);
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
        if (db != null) {
            RecipientVModel recipientVModel = new ViewModelProvider(this).get(RecipientVModel.class);
            ValueEvents<Recipient> valueEvents = new ValueEvents<Recipient>();
            recipientsListener = valueEvents.registerListener(db, this, recipientAdapter, recipientVModel, recipients, Recipient.class, (list) -> {
                recipientBinding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("recipients") : "");
                recipientBinding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });

        }
    }


}