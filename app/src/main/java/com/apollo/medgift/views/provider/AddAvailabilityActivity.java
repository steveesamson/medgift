package com.apollo.medgift.views.provider;

import static com.apollo.medgift.common.BaseModel.STORE;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;


import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddavailabilityBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.views.gifter.DAYS;

import java.util.Calendar;

public class AddAvailabilityActivity extends BaseActivity {
    private ActivityAddavailabilityBinding addAvailabilityBinding;
    private String sessionUserId;
    private Availability availability;
    private String startTime, endTime;

    private Availability monday = new Availability(DAYS.MON.name());
    private Availability tuesday = new Availability(DAYS.TUE.name());
    private Availability wednesday = new Availability(DAYS.WED.name());
    private Availability thursday = new Availability(DAYS.THU.name());
    private Availability friday = new Availability(DAYS.FRI.name());
    private Availability saturday = new Availability(DAYS.SAT.name());
    private Availability sunday = new Availability(DAYS.SUN.name());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        addAvailabilityBinding = ActivityAddavailabilityBinding.inflate(getLayoutInflater());
        View view = addAvailabilityBinding.getRoot();
        setContentView(view);

        // Setup toolbar
        setupToolbar(addAvailabilityBinding.homeAppBar.getRoot(), getString(R.string.availability_title), true);
        applyWindowInsetsListenerTo(this, addAvailabilityBinding.addAvailabilityActivity);

        Intent intent = getIntent();
        availability = (Availability) intent.getSerializableExtra(STORE);


        String title = getString(availability != null ? R.string.availability_title : R.string.addAvailability_title);
        setupToolbar(addAvailabilityBinding.homeAppBar.getRoot(), title, true);


        sessionUserId = Firebase.currentUser().getUserId();

        // Setup onClick listeners for each day's start and end time fields
        addAvailabilityBinding.edtMondayStartTime.setOnClickListener(v -> showTimePicker(v,true, monday));
        addAvailabilityBinding.edtMondayEndTime.setOnClickListener(v -> showTimePicker(v,false, monday));

        addAvailabilityBinding.edtTuesdayStartTime.setOnClickListener(v -> showTimePicker(v,true, tuesday));
        addAvailabilityBinding.edtTuesdayEndTime.setOnClickListener(v -> showTimePicker(v,false, tuesday));

        addAvailabilityBinding.edtWednesdayStartTime.setOnClickListener(v -> showTimePicker(v,true, wednesday));
        addAvailabilityBinding.edtWednesdayEndTime.setOnClickListener(v -> showTimePicker(v,false, wednesday));

        addAvailabilityBinding.edtThursdayStartTime.setOnClickListener(v -> showTimePicker(v,true, thursday));
        addAvailabilityBinding.edtThursdayEndTime.setOnClickListener(v -> showTimePicker(v,false, thursday));

        addAvailabilityBinding.edtFridayStartTime.setOnClickListener(v -> showTimePicker(v,true, friday));
        addAvailabilityBinding.edtFridayEndTime.setOnClickListener(v -> showTimePicker(v,false, friday));

        addAvailabilityBinding.edtSaturdayStartTime.setOnClickListener(v -> showTimePicker(v, true, saturday));
        addAvailabilityBinding.edtSaturdayEndTime.setOnClickListener(v -> showTimePicker(v,false, saturday));

        addAvailabilityBinding.edtSundayStartTime.setOnClickListener(v -> showTimePicker(v,true, sunday));
        addAvailabilityBinding.edtSundayEndTime.setOnClickListener(v -> showTimePicker(v,false, sunday));
    }

    /**
     * Shows a time picker dialog for the selected day and updates the time in the UI and model.
     *
     * @param isStartTime     Flag to indicate if the start or end time is being set.
     * @param dayAvailability The availability object for the respective day.
     */
    private void showTimePicker(View v, boolean isStartTime, Availability dayAvailability) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Format the time as 24-hour format (HH:mm)
            String time = String.format("%02d:%02d", hourOfDay, minute1);

            ((EditText) v).setText(time);

            if (isStartTime) {
                dayAvailability.setStart(time);

//                updateTimeInUI(dayAvailability, true);
            } else {
                dayAvailability.setEnd(time);

//                updateTimeInUI(dayAvailability, false);
            }


        }, hour, minute, true);

        timePickerDialog.show();
    }
/*
    private void updateTimeInUI(Availability aval, boolean isStartTime) {
        String time = isStartTime ? dayAvailability.getStart() : dayAvailability.getEnd();
        DAYS day = DAYS.valueOf(aval.getDay());
        switch (day) {
            case MON:
                if (isStartTime) {
                    addAvailabilityBinding.edtMondayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtMondayEndTime.setText(time);
                }
                break;
            case TUE:
                if (isStartTime) {
                    addAvailabilityBinding.edtTuesdayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtTuesdayEndTime.setText(time);
                }
                break;
            case "WED":
                if (isStartTime) {
                    addAvailabilityBinding.edtWednesdayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtWednesdayEndTime.setText(time);
                }
                break;
            case "THU":
                if (isStartTime) {
                    addAvailabilityBinding.edtThursdayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtThursdayEndTime.setText(time);
                }
                break;
            case "FRI":
                if (isStartTime) {
                    addAvailabilityBinding.edtFridayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtFridayEndTime.setText(time);
                }
                break;
            case "SAT":
                if (isStartTime) {
                    addAvailabilityBinding.edtSaturdayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtSaturdayEndTime.setText(time);
                }
                break;
            case "SUN":
                if (isStartTime) {
                    addAvailabilityBinding.edtSundayStartTime.setText(time);
                } else {
                    addAvailabilityBinding.edtSundayEndTime.setText(time);
                }
                break;
        }
    } */
}