package com.apollo.medgift.views.provider;

import static com.apollo.medgift.common.BaseModel.STORE;
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
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class AddAvailabilityActivity extends BaseActivity {
    private ActivityAddavailabilityBinding addAvailabilityBinding;
    private String sessionUserId;
    private Availability availability;

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


        setupToolbar(addAvailabilityBinding.homeAppBar.getRoot(), getString(R.string.availability_title), true);
        applyWindowInsetsListenerTo(this, addAvailabilityBinding.addAvailabilityActivity);

        Intent intent = getIntent();
        availability = (Availability) intent.getSerializableExtra(STORE);


        String title = getString(availability != null ? R.string.availability_title : R.string.addAvailability_title);
        setupToolbar(addAvailabilityBinding.homeAppBar.getRoot(), title, true);


        sessionUserId = Firebase.currentUser().getUserId();

        setUpTimePickers();


        addAvailabilityBinding.btnSave.setOnClickListener(v -> saveAvailability());
    }

    private void setUpTimePickers() {
        addAvailabilityBinding.edtMondayStartTime.setOnClickListener(v -> showTimePicker(v, true, monday));
        addAvailabilityBinding.edtMondayEndTime.setOnClickListener(v -> showTimePicker(v, false, monday));

        addAvailabilityBinding.edtTuesdayStartTime.setOnClickListener(v -> showTimePicker(v, true, tuesday));
        addAvailabilityBinding.edtTuesdayEndTime.setOnClickListener(v -> showTimePicker(v, false, tuesday));

        addAvailabilityBinding.edtWednesdayStartTime.setOnClickListener(v -> showTimePicker(v, true, wednesday));
        addAvailabilityBinding.edtWednesdayEndTime.setOnClickListener(v -> showTimePicker(v, false, wednesday));

        addAvailabilityBinding.edtThursdayStartTime.setOnClickListener(v -> showTimePicker(v, true, thursday));
        addAvailabilityBinding.edtThursdayEndTime.setOnClickListener(v -> showTimePicker(v, false, thursday));

        addAvailabilityBinding.edtFridayStartTime.setOnClickListener(v -> showTimePicker(v, true, friday));
        addAvailabilityBinding.edtFridayEndTime.setOnClickListener(v -> showTimePicker(v, false, friday));

        addAvailabilityBinding.edtSaturdayStartTime.setOnClickListener(v -> showTimePicker(v, true, saturday));
        addAvailabilityBinding.edtSaturdayEndTime.setOnClickListener(v -> showTimePicker(v, false, saturday));

        addAvailabilityBinding.edtSundayStartTime.setOnClickListener(v -> showTimePicker(v, true, sunday));
        addAvailabilityBinding.edtSundayEndTime.setOnClickListener(v -> showTimePicker(v, false, sunday));
    }

    private void showTimePicker(View v, boolean isStartTime, Availability dayAvailability) {
        Calendar calendar = Calendar.getInstance();
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select Time")
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
            int selectedHour = materialTimePicker.getHour();
            int selectedMinute = materialTimePicker.getMinute();
            String selectedTime = formatTime(selectedHour, selectedMinute);

            ((EditText) v).setText(selectedTime);

            if (isStartTime) {
                dayAvailability.setStart(selectedTime);
                handleEndTimeIfNecessary(dayAvailability);
            } else {
                handleEndTimeSelection(selectedTime, dayAvailability);
            }
        });

        materialTimePicker.show(getSupportFragmentManager(), materialTimePicker.toString());
    }

    private void handleEndTimeIfNecessary(Availability dayAvailability) {
        String startTime = dayAvailability.getStart();
        if (startTime != null && !startTime.isEmpty() && dayAvailability.getEnd() == null) {
            Util.notify(this, getString(R.string.invalid_end_time));
        }
    }

    private void handleEndTimeSelection(String selectedTime, Availability dayAvailability) {
        String startTime = dayAvailability.getStart();
        if (startTime == null || startTime.isEmpty()) {
            Util.notify(this, getString(R.string.start_time_not_set));
            return;
        }

        if (isEndTimeValid(startTime, selectedTime)) {
            dayAvailability.setEnd(selectedTime);
        } else {
            Util.notify(this, getString(R.string.invalid_end_time));
        }
    }

    private boolean isEndTimeValid(String startTime, String endTime) {
        return convertToMinutes(endTime) > convertToMinutes(startTime);
    }

    private String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

    private int convertToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private void validateAndSave() {
        boolean isValid = true;


        resetFieldErrors();

        if (!validateDay(monday, addAvailabilityBinding.edtMondayStartTime, addAvailabilityBinding.edtMondayEndTime)) {
            isValid = false;
        }
        if (!validateDay(tuesday, addAvailabilityBinding.edtTuesdayStartTime, addAvailabilityBinding.edtTuesdayEndTime)) {
            isValid = false;
        }
        if (!validateDay(wednesday, addAvailabilityBinding.edtWednesdayStartTime, addAvailabilityBinding.edtWednesdayEndTime)) {
            isValid = false;
        }
        if (!validateDay(thursday, addAvailabilityBinding.edtThursdayStartTime, addAvailabilityBinding.edtThursdayEndTime)) {
            isValid = false;
        }
        if (!validateDay(friday, addAvailabilityBinding.edtFridayStartTime, addAvailabilityBinding.edtFridayEndTime)) {
            isValid = false;
        }
        if (!validateDay(saturday, addAvailabilityBinding.edtSaturdayStartTime, addAvailabilityBinding.edtSaturdayEndTime)) {
            isValid = false;
        }
        if (!validateDay(sunday, addAvailabilityBinding.edtSundayStartTime, addAvailabilityBinding.edtSundayEndTime)) {
            isValid = false;
        }

        if (isValid) {
            saveAvailability();  // Save availability logic here
            Intent resultIntent = new Intent();
            resultIntent.putExtra(STORE, availability);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private boolean validateDay(Availability dayAvailability, EditText startTimeField, EditText endTimeField) {
        boolean isValid = true;

        String startTime = dayAvailability.getStart();
        String endTime = dayAvailability.getEnd();

        if (startTime == null || startTime.isEmpty()) {
            startTimeField.setError(getString(R.string.start_time_required));
            isValid = false;
        }
        if (endTime == null || endTime.isEmpty()) {
            endTimeField.setError(getString(R.string.end_time_required));
            isValid = false;
        }
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            if (convertToMinutes(endTime) <= convertToMinutes(startTime)) {
                endTimeField.setError(getString(R.string.end_time_after_start_time));
                isValid = false;
            }
        }

        return isValid;
    }

    private void resetFieldErrors() {
        addAvailabilityBinding.edtMondayStartTime.setError(null);
        addAvailabilityBinding.edtMondayEndTime.setError(null);
        addAvailabilityBinding.edtTuesdayStartTime.setError(null);
        addAvailabilityBinding.edtTuesdayEndTime.setError(null);
        addAvailabilityBinding.edtWednesdayStartTime.setError(null);
        addAvailabilityBinding.edtWednesdayEndTime.setError(null);
        addAvailabilityBinding.edtThursdayStartTime.setError(null);
        addAvailabilityBinding.edtThursdayEndTime.setError(null);
        addAvailabilityBinding.edtFridayStartTime.setError(null);
        addAvailabilityBinding.edtFridayEndTime.setError(null);
        addAvailabilityBinding.edtSaturdayStartTime.setError(null);
        addAvailabilityBinding.edtSaturdayEndTime.setError(null);
        addAvailabilityBinding.edtSundayStartTime.setError(null);
        addAvailabilityBinding.edtSundayEndTime.setError(null);
    }

    private void saveAvailability()
    {
        boolean exists = Util.exists(availability);
        Util.startProgress(addAvailabilityBinding.progress, "Adding Availability...");

        Firebase.save(availability, Availability.STORE, (task) -> {
            Util.stopProgress(addAvailabilityBinding.progress);
            if (task.isSuccessful()) {

                availability = null;
                Util.notify(AddAvailabilityActivity.this, Util.success("Availability", exists));
                finish();

            } else {
                Util.notify(AddAvailabilityActivity.this, Util.fail("Availability", exists));
            }
        });
    }
}