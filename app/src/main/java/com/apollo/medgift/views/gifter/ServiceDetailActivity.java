package com.apollo.medgift.views.gifter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.gifters.ScheduleAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Closeable;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.OnModelSelectCallback;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityServicedetailBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.models.DayTime;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Schedule;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDetailActivity extends BaseActivity implements View.OnClickListener, OnModelSelectCallback {
    private static final String TAG = ServiceDetailActivity.class.getSimpleName();

    private ActivityServicedetailBinding servicedetailBinding;
    private HealthcareService healthcareService;
    private Gift gift;
    private Map<String, DayTime> dayTimeMap = new HashMap<>();
    private Availability availability;
    private Schedule selectedSchedule;
    private Closeable closeable;
    private ActivityResultLauncher<Intent> serviceImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicedetailBinding = ActivityServicedetailBinding.inflate(getLayoutInflater());
        setContentView(servicedetailBinding.getRoot());
        applyWindowInsetsListenerTo(this, servicedetailBinding.main);
        // Setup tool bar and title
        setupToolbar(servicedetailBinding.homeAppBar.getRoot(), getString(R.string.serviceDetails), true);

        Intent intent = getIntent();
        healthcareService = (HealthcareService) intent.getSerializableExtra(HealthcareService.STORE);
        assert healthcareService != null;

        closeable = Firebase.getModelBy(Availability.STORE,"createdBy", healthcareService.getCreatedBy(), Availability.class, ( availability1 ) ->{
            availability = availability1;
            closeable.release();
            if(availability != null){
                if(availability.getTimes() != null){
                    for(DayTime d: availability.getTimes()){
                        dayTimeMap.put(d.getDay(), d);
                    }
                }
            }
        });

        gift = (Gift) intent.getSerializableExtra(Gift.STORE);

        setupToolbar(servicedetailBinding.homeAppBar.getRoot(), "", true);

        servicedetailBinding.scrollView.setVisibility(View.INVISIBLE);
        Util.startProgress(servicedetailBinding.progress, "Loading Service...");

        setup();

        servicedetailBinding.btnAddToGift.setOnClickListener(this);
        servicedetailBinding.btnCheckoutService.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setup() {

        servicedetailBinding.serviceTitle.setText(healthcareService.getServiceName());
        servicedetailBinding.provider.setText(" " + healthcareService.getCreatedBy());
        servicedetailBinding.serviceType.setText(" " + healthcareService.getServiceType());
        servicedetailBinding.price.setText("$ " + healthcareService.getPrice());
        servicedetailBinding.description.setText(healthcareService.getDescription());
        servicedetailBinding.calendarDialog.calAvailability.setMinDate(Calendar.getInstance().getTimeInMillis());
        servicedetailBinding.calendarDialog.btnCancel.setOnClickListener((v) -> {
            servicedetailBinding.calendarDialog.getRoot().setVisibility(View.GONE);
        });
        servicedetailBinding.calendarDialog.calAvailability.setOnDateChangeListener((calView, year, month, dayOfMonth) -> {
            if(availability != null){

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String day = Util.formatDayOfWeek(cal);
                DayTime dayTime = dayTimeMap.get(day);
                if(dayTime != null){
                   selectSchedule(dayTime, cal);
                }else{
                    Util.notify(ServiceDetailActivity.this, "There are no availability for the selected day.");
                }
            }else{
                Log.i("DBUG:", "No availability");
            }

        });
        if(gift != null){
            servicedetailBinding.txtGiftName.setText(String.format("You will be adding this service to the '%s' gift.",gift.getName()));
        }else{
            servicedetailBinding.btnAddToGift.setVisibility(View.GONE);
        }


        if (healthcareService.getBannerUrl() != null && !healthcareService.getBannerUrl().isEmpty()) {
            Util.loadImageUri(servicedetailBinding.serviceImage, healthcareService.getBannerUrl(), this);
        } else {
            servicedetailBinding.serviceImage.setImageResource(R.drawable.default_service_image);
        }

        servicedetailBinding.scrollView.setVisibility(View.VISIBLE);
        Util.stopProgress(servicedetailBinding.progress);
    }

    @Override
    public void onClick(View v) {
        if (v == servicedetailBinding.btnAddToGift) {
            if(gift == null){
                navigateTo(GiftActivity.class);
            }else{
            // DO ADD TO GIFT
            servicedetailBinding.calendarDialog.getRoot().setVisibility(View.VISIBLE);

            }
        }
        if (v == servicedetailBinding.btnCheckoutService) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(HealthcareService.STORE, healthcareService);
            intent.putExtra(Gift.STORE, this.gift);
            intent.putExtra(Schedule.STORE, this.selectedSchedule);

            this.startActivity(intent);
        }
    }

    private void selectSchedule(DayTime dayTime, Calendar cal) {
        servicedetailBinding.calendarDialog.getRoot().setVisibility(View.GONE);
        selectedSchedule = null;
        List<Schedule> schedules = Util.getTimes(cal, dayTime);

        RecyclerView recyclerView = servicedetailBinding.scheduleDialog.scheduleList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, schedules);
        recyclerView.setAdapter(scheduleAdapter);
        servicedetailBinding.scheduleDialog.scheduleTitle.setText(String.format("Select a time for %s", Util.formatToReadableDate(cal.getTime())));
        servicedetailBinding.scheduleDialog.getRoot().setVisibility(View.VISIBLE);
        servicedetailBinding.scheduleDialog.btnCancel.setOnClickListener((v) -> {
            selectedSchedule = null;
            scheduleAdapter.clearActiveSchedule();
            servicedetailBinding.calendarDialog.getRoot().setVisibility(View.VISIBLE);

            servicedetailBinding.scheduleDialog.getRoot().setVisibility(View.GONE);
        });
        servicedetailBinding.scheduleDialog.btnConfirm.setOnClickListener((v) -> {
            servicedetailBinding.scheduleDialog.getRoot().setVisibility(View.GONE);
            servicedetailBinding.btnAddToGift.setVisibility(View.GONE);
            servicedetailBinding.btnCheckoutService.setVisibility(View.VISIBLE);
        });
        scheduleAdapter.notifyDataSetChanged();

    }


    @Override
    public void onScheduleSelected(Schedule schedule) {
        this.selectedSchedule = schedule;
    }
}