package com.apollo.medgift.views.provider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityAddavailabilityBinding;
import com.apollo.medgift.models.Availability;
import com.apollo.medgift.models.DateTimeValue;
import com.apollo.medgift.models.DayTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AddAvailabilityActivity extends BaseActivity {

    private static final String TAG = AddAvailabilityActivity.class.getSimpleName();
    private ActivityAddavailabilityBinding binding;
    private DayTime dayTime;
    private Availability availability;
    private Map<String, DayTime> dayTimeMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddavailabilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyWindowInsetsListenerTo(this, binding.addAvailability);

        Intent intent = getIntent();
        dayTime = (DayTime) intent.getSerializableExtra(DayTime.STORE);
        availability = (Availability) intent.getSerializableExtra(Availability.STORE);
        assert  dayTime != null;
        assert availability != null;

        for(DayTime dt: availability.getTimes()){
            dayTimeMap.put(dt.getDay(), dt);
        }

        boolean exists = !Util.isNullOrEmpty(dayTime.getDay());
        String title = getString(exists ? R.string.editDayTimeTitle : R.string.addDayTimeTitle);
        setupToolbar(binding.homeAppBar.getRoot(), title, true);

        setupActivity();
    }

    private void setupActivity() {
        binding.acDay.setText(dayTime.getDay());
        binding.edtStartTime.setText(new DateTimeValue(dayTime.getStartTime()).toAMPM());
        binding.edtEndTime.setText(new DateTimeValue(dayTime.getEndTime()).toAMPM());

        Set<String> keys = dayTimeMap.keySet();

        // Set up the Spinner
        List<String> days = Arrays.stream(getResources().getStringArray(R.array.availability_days)).filter(next -> !keys.contains(next)).collect(Collectors.toList());

        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(this, R.layout.recipient_acitem, days);
        binding.acDay.setAdapter(adapterItems);
        binding.acDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String r = (String)parent.getItemAtPosition(position);
                dayTime.setDay(r);
                binding.lytDay.setError("");
                binding.acDay.setText(r, false);
            }
        });

        binding.edtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {

                if(focused){
                    Util.showTimePickerFor(AddAvailabilityActivity.this, null, (dtv) ->{
//                        String time = String.format("%s:%s", dtv.hourOfDay, dtv.minute);
                        binding.lytStartTime.setError("");
                        binding.edtStartTime.setText(dtv.toAMPM());
                        dayTime.setStartTime(dtv.to24Hr());
                    });
                }

            }
        });

        binding.edtEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if(focused){
                    Calendar cal = Calendar.getInstance();
                    if(!Util.isNullOrEmpty(dayTime.getStartTime())){
                        String[] hourMin = dayTime.getStartTime().split(":");
                        int hour = Integer.parseInt(hourMin[0],10);
                        int min = Integer.parseInt(hourMin[1],10);
                        cal.set(Calendar.HOUR_OF_DAY, hour + 1);
                        cal.set(Calendar.MINUTE, min);
                    }
                    Util.showTimePickerFor(AddAvailabilityActivity.this, cal, (dtv) ->{

//                        String time = String.format("%s:%s", dtv.hourOfDay, dtv.minute);
                        binding.lytEndTime.setError("");
                        binding.edtEndTime.setText(dtv.toAMPM());
                        dayTime.setEndTime(dtv.to24Hr());
                    });
                }

            }
        });

        binding.btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean formIsValid = true;
                Log.i(TAG, dayTime.toString());
                if(Util.isNullOrEmpty(dayTime.getDay())){
                        binding.lytDay.setError("Day is required.");
                        formIsValid = false;
                }
                if(Util.isNullOrEmpty(dayTime.getStartTime())){
                    binding.lytStartTime.setError("Start Time is required.");
                    formIsValid = false;
                }
                if(Util.isNullOrEmpty(dayTime.getEndTime())){
                    binding.lytEndTime.setError("End Time is required.");
                    formIsValid = false;
                }
                if(formIsValid){
                    dayTimeMap.put(dayTime.getDay(), dayTime);
                    availability.setTimes(new ArrayList<DayTime>(dayTimeMap.values()));

                    boolean exists = Util.exists(availability);
                    Util.startProgress(binding.progress, "Adding availability...");

                    Firebase.save(availability, Availability.STORE, (task, key) -> {
                        Util.stopProgress(binding.progress);
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
        });
    }
}