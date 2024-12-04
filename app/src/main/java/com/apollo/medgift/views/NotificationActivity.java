package com.apollo.medgift.views;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.NotificationAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityNotificationBinding;
import com.apollo.medgift.models.Notification;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.views.models.NotificationVModel;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {

    private ActivityNotificationBinding binding;
    private NotificationAdapter notificationAdapter;
    private ValueEventListener notificationListener;
    private List<Notification> notifications = new ArrayList<>();
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.main);
        // Setup tool bar and title
        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.notification), true);
        setupActivity();
    }
    private void setupActivity() {

        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;
        this.query = Firebase.database(Notification.STORE).orderByChild("createdFor").equalTo(sessionUser.getUserId());
        RecyclerView recyclerView = binding.notificationList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(notifications, this);
        recyclerView.setAdapter(notificationAdapter);
        fetchAndListenOnNotifications();
    }

    // Unregister listeners on db
    private void unRegisterValueListener() {
        if (notificationListener != null) {
            this.query.removeEventListener(notificationListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    // Fetch Recipient and begin to
    // listen to changes on the list
    private void fetchAndListenOnNotifications() {
        if (this.query != null) {
            NotificationVModel notificationVModel = new ViewModelProvider(this).get(NotificationVModel.class);
            ValueEvents<Notification> valueEvents = new ValueEvents<Notification>();
            Util.startProgress(binding.progress, "Fetching notifications...");
            notificationListener = valueEvents.registerListener(this.query, this, notificationAdapter, notificationVModel, notifications, Notification.class, (list) -> {
                Util.stopProgress(binding.progress);
                binding.emptyItem.txtEmpty.setText(list.isEmpty()? Util.getEmpty("notifications") : "");
                binding.emptyItem.getRoot().setVisibility(list.isEmpty()? View.VISIBLE : View.GONE);
            });

        }
    }
}