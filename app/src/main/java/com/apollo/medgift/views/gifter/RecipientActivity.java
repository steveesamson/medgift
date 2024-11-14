package com.apollo.medgift.views.gifter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.adapters.gifters.RecipientAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
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
        recipients.add(new Recipient("Steve", "Samson", "00099887"));
        recipients.add(new Recipient("Sam", "Ugwu", "00099887"));
        recipients.add(new Recipient("Thomas", "Lowe", "00099887"));
        recipients.add(new Recipient("Asari", "Ikenga", "00099887"));
        recipients.add(new Recipient("Chinyera", "Don", "00099887"));
        recipientBinding = ActivityRecipientBinding.inflate(getLayoutInflater());
        setContentView(recipientBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(recipientBinding.recipientActivity, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setPageUp();
    }

    private void setPageUp() {
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
    private void fetchAndListenOnRecipients(){
        RecipientVModel recipientVModel = new ViewModelProvider(this).get(RecipientVModel.class);
        this.recipientsListener = Firebase.registerListener(db, this, this.recipientAdapter, recipientVModel, this.recipients);
    }


}