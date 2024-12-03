package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.provider.DayTimeAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.BaseModel;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.OnModelManageCallback;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityAvailabilityBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.models.DayTime;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.views.models.AvailabilityVModel;
import com.apollo.medgift.views.models.DayTimeVModel;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvailabilityActivity extends BaseActivity implements OnModelManageCallback {

    private static final String TAG = AvailabilityActivity.class.getSimpleName();

    private ActivityAvailabilityBinding availabilityBinding;

    private final List<DayTime> dayTimes = new ArrayList<>();
    private DayTimeAdapter dayTimeAdapter;

    private Query query;
    private ValueEventListener availabilitiesListener;

    private Availability availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        availabilityBinding = ActivityAvailabilityBinding.inflate(getLayoutInflater());
        setContentView(availabilityBinding.getRoot());
        // Setup tool bar and title
        setupToolbar(availabilityBinding.homeAppBar.getRoot(), getString(R.string.availabilityTitle), true);
        applyWindowInsetsListenerTo(this, availabilityBinding.availabilityActivity);

        setPageUp();
    }

    private void setPageUp() {
        availabilityBinding.btnAddAvailability.setVisibility(View.GONE);
        availabilityBinding.btnAddAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
                intent.putExtra(DayTime.STORE, new DayTime());
                intent.putExtra(Availability.STORE, AvailabilityActivity.this.availability);
                startActivity(intent);
            }
        });

        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;
        this.query = Firebase.database(Availability.STORE).orderByChild("createdBy").equalTo(sessionUser.getUserId());
        RecyclerView recyclerView = availabilityBinding.dayTimeList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayTimeAdapter = new DayTimeAdapter(dayTimes, this);
        recyclerView.setAdapter(dayTimeAdapter);
        fetchAndListenOnAvailabilities();
    }

    // Unregister listeners on db
    private void unRegisterValueListener() {
        if (availabilitiesListener != null) {
            this.query.removeEventListener(availabilitiesListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }

    // Fetch Availabilities and begin to
    // listen to changes on the list
    private void fetchAndListenOnAvailabilities() {
        if (this.query != null) {
            AvailabilityVModel availabilityVModel = new ViewModelProvider(this).get(AvailabilityVModel.class);
            ValueEvents<Availability> valueEvents = new ValueEvents<Availability>();
            Util.startProgress(availabilityBinding.progress, "Fetching availabilities...");
            availabilitiesListener = valueEvents.registerListener(this.query, this, null, availabilityVModel, new ArrayList<>(), Availability.class, (list) -> {
                Util.stopProgress(availabilityBinding.progress);
                processAvailabilities(list);

            });

        }
    }

    private void processAvailabilities(List<Availability> availabilities) {
        List<String> days = Arrays.stream(getResources().getStringArray(R.array.availability_days)).collect(Collectors.toList());
        DayTimeVModel dayTimeVModel = new ViewModelProvider(this).get(DayTimeVModel.class);
        dayTimeVModel.getModel().observe((LifecycleOwner) this, list -> {
            // Update the selected filters UI.
            dayTimes.clear();
            dayTimes.addAll(list);
            dayTimeAdapter.notifyDataSetChanged();
        });
        if(!availabilities.isEmpty()){
             availability = availabilities.get(0);
             List<DayTime> times = new ArrayList<>();
             Map<String, DayTime> map = new HashMap<>();

             for(DayTime d: availability.getTimes()){
                 map.put(d.getDay(), d);
             }
             for(String s:days){
                 DayTime dt = map.get(s);
                 if(dt != null){
                     times.add(dt);
                 }
             }

            dayTimeVModel.setModel(times);
            if(times.size() < 7){
                availabilityBinding.btnAddAvailability.setVisibility(View.VISIBLE);
            }
            availabilityBinding.emptyItem.txtEmpty.setText(times.isEmpty()? Util.getEmpty("availabilities") : "");
            availabilityBinding.emptyItem.getRoot().setVisibility(times.isEmpty()? View.VISIBLE : View.GONE);
        }else {
            availability = new Availability();
            availabilityBinding.btnAddAvailability.setVisibility(View.VISIBLE);
            availability.setCreatedBy(Firebase.currentUser().getUserId());
            availabilityBinding.emptyItem.txtEmpty.setText( Util.getEmpty("availabilities"));
            availabilityBinding.emptyItem.getRoot().setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDeleting(BaseModel model) {
        DayTime dTime = (DayTime)model;
        List<DayTime> remaining = availability.getTimes().stream().filter( next -> !next.getDay().equals(dTime.getDay())).collect(Collectors.toList());
        availability.setTimes(remaining);

        Util.startProgress(availabilityBinding.progress, "Deleting availability...");
        Firebase.save(availability, Availability.STORE, (task, key) -> {
            Util.stopProgress(availabilityBinding.progress);
            if (task.isSuccessful()) {

                availability = null;
                if(task.isSuccessful()){
                    Util.notify(getApplicationContext(), "Availability deleted!");
                }

            } else {
                Util.notify(getApplicationContext(), "Availability delete failed.");
            }
        });

    }

    @Override
    public void onEdit(BaseModel model) {
        DayTime dTime = (DayTime)model;
        Log.i(TAG, dTime.toString());
        Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
        intent.putExtra(DayTime.STORE, dTime);
        intent.putExtra(Availability.STORE, AvailabilityActivity.this.availability);
        startActivity(intent);
    }

}