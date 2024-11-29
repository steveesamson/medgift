package com.apollo.medgift.views.provider;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityAvailabilityBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.models.SessionUser;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityActivity extends BaseActivity {
    private ActivityAvailabilityBinding availabilityBinding;

    private final List<Availability> availabilityList = new ArrayList<>();


    private Query query;
    private ValueEventListener availabilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        availabilityBinding = ActivityAvailabilityBinding.inflate(getLayoutInflater());
        setContentView(availabilityBinding.getRoot());

        setupToolbar(availabilityBinding.homeAppBar.getRoot(), getString(R.string.availability_title), true);
        applyWindowInsetsListenerTo(this, availabilityBinding.availabilityActivity);

        setPageUp();
    }

    private void setPageUp() {

        availabilityBinding.btnAddAvailability.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAvailabilityActivity.class);
            intent.putExtra(Availability.STORE, new Availability(""));
            startActivity(intent);
        });


        SessionUser sessionUser = Firebase.currentUser();
        if (sessionUser == null) {
            Util.notify(this, "User not authenticated");
            finish();
            return;
        }


        query = Firebase.database(Availability.STORE)
                .orderByChild("createdBy")
                .equalTo(sessionUser.getUserId());


    }



    private void unRegisterValueListener() {
        if (query != null && availabilityListener != null) {
            query.removeEventListener(availabilityListener);
            availabilityListener = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterValueListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }
}