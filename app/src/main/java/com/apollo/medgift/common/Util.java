package com.apollo.medgift.common;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.databinding.ProgressBinding;
import com.apollo.medgift.models.DateTimeValue;
import com.apollo.medgift.models.DayTime;
import com.apollo.medgift.models.Schedule;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Util, alass to house all shared operations
 */
public class Util {


    // These following methods help in managing
    // the display/hiding of the the progress during
    // network activities.
    public static void startProgress(ProgressBinding progress, String message) {
        progress.progressText.setText(message);
        progress.getRoot().setVisibility(View.VISIBLE);
    }

    public static void stopProgress(ProgressBinding progress) {
        progress.getRoot().setVisibility(View.GONE);
    }

    // Extracts resource name from Uri string
    public static String uriToName(String url) {
        try {
            String[] parts = url.split("/");

            int lastIndex = parts.length - 1;
            String last = parts[lastIndex];
            last = last.substring(0, last.indexOf("?"));
            return last.replace("%2F", "/").trim();
        } catch (Exception x) {
            Log.e("Storage Extraction: ", x.toString());
            return null;
        }

    }

    public static boolean exists(BaseModel model) {
        return model != null && model.getKey() != null && !model.getKey().isEmpty();
    }

    public static String success(String type, boolean exists) {
        return String.format("%s %s successfully", type, exists ? "updated" : "added");
    }

    public static String fail(String type, boolean exists) {
        return String.format("%s %s failed", (exists ? "Updating" : "Adding"), type.toLowerCase());
    }

    public static String getEmpty(String type) {
        return String.format("No %s added yet.", type);
    }

    // Convenience for simple toasts
    public static void notify(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // Reusable Dialog for informing users of
    // outcomes of actions
    public static void showDialog(Context context, String title, String body) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton("Ok, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void showConfirm(Context context, String title, String ask, DialogInterface.OnClickListener confirm) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(ask)
                .setPositiveButton("Yes, go ahead", confirm)
                .setNegativeButton("No, don't", (dialog1, which) -> {
                    dialog1.dismiss();
                }).show();
    }

    // Conenience for loading storage images into imageview
    public static void loadImageUri(ImageView imageView, String path, Context context) {
        Glide.with(context).load(path).into(imageView);
    }

    public static boolean isEmail(String input) {
        return !input.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    private static final String POSTAL_CODE = "^[A-Z][0-9][A-Z]\\s[0-9][A-Z][0-9]$";

    public static boolean isPostalCode(String input) {

        return !input.isEmpty() && input.matches(POSTAL_CODE);
    }

    public static boolean isPhoneNo(String input) {
        return !input.isEmpty() && Patterns.PHONE.matcher(input).matches();
    }

    public static boolean isMinLen6(String input) {
        return !input.isEmpty() && input.length() >= 6;
    }

    public static boolean isSame(String owner, String uid) {
        return owner.equals(uid);
    }

    // Get the text from input fields
    public static String valueOf(TextInputEditText input) {
        return String.valueOf(input.getText());
    }


    public static String valueOf(AutoCompleteTextView input) {
        return String.valueOf(input.getText());
    }

    public static void showTimePickerFor(Context context, Calendar start, OnModel<DateTimeValue> onComplete){
        // Get Current Time
        Calendar cal = start == null? Calendar.getInstance() : start;
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                AlertDialog.THEME_HOLO_LIGHT,
//                com.google.android.material.R.style.Theme_Material3_Light_Dialog_Alert,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        view.setIs24HourView(true);
                        DateTimeValue dateTimeValue = new DateTimeValue();
                        dateTimeValue.hourOfDay = hourOfDay;
                        dateTimeValue.minute = minute;
                        onComplete.onComplete(dateTimeValue);
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }
    // Display date picker for date fields
    public static void showDatePickerFor(Context context, Calendar start, Calendar end,  OnModel<DateTimeValue> onComplete) {
        // This is the date field
        // Retrieve a Calendar instance
        Calendar cal = start == null ? Calendar.getInstance() : start;
        // Get the current Day of Month
        int dayOfSale = cal.get(Calendar.DAY_OF_MONTH);
        // Get the current Month
        int monthOfSale = cal.get(Calendar.MONTH);
        // Get the current year
        int yearOfSale = cal.get(Calendar.YEAR);




        // Create and instance of DatePickerDialog
        // with the extracted information
        // and registering the onDateSet listener
        DatePickerDialog datePicker = new DatePickerDialog(
                context,
                AlertDialog.THEME_HOLO_LIGHT,
//                com.google.android.material.R.style.Theme_Material3_Light_Dialog_Alert,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Clear previous error if any
//                edtDate.setError(null);
                // Update edtDate view
//                edtDate.setText(String.format("%s/%s/%s", dayOfMonth, (month + 1), year));
                DateTimeValue dateTimeValue = new DateTimeValue();
                dateTimeValue.year = year;
                dateTimeValue.month = month;
                dateTimeValue.dayOfMonth = dayOfMonth;
                onComplete.onComplete(dateTimeValue);
            }
        }, yearOfSale, monthOfSale, dayOfSale);

        // Restrict min date to current date.

        datePicker.getDatePicker().setMinDate(cal.getTimeInMillis());
        if(end != null){
            datePicker.getDatePicker().setMaxDate(end.getTimeInMillis());
        }
        // Display the date picker dialog
        datePicker.show();
    }


    public static void applyWindowInsetsListenerTo(ComponentActivity activity, ViewGroup view) {
        EdgeToEdge.enable(activity);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static boolean isNullOrEmpty(String field) {
            return field == null || field.isEmpty();
    }

    public static String formatTime(Date date){
        String DATE_PATTERN = "h:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }
    // Date formater method
    public static String formatedDueTime(Date date){
        String DATE_PATTERN = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    public static Date parseTime(String date)  {
        try {
//        String DATE_PATTERN = "HH:mm. MMM dd, yyyy";
            String DATE_PATTERN = "dd-MM-yyyy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
            return simpleDateFormat.parse(date);
        }catch (ParseException e){
            return null;
        }
    }

    public static String formatToReadableDate(Date date){
//        String DATE_PATTERN = "HH:mm. MMM dd, yyyy";
        String DATE_PATTERN = "MMM dd yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    public static String today() {
        Calendar c = Calendar.getInstance();
        return formatedDueTime(c.getTime());
    }

    public static String formatDayOfWeek(Calendar cal) {
            String DATE_PATTERN = "EEE";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
            return simpleDateFormat.format(cal.getTime()).toUpperCase();

    }

    public static List<Schedule> getTimes(Calendar instance, DayTime dayTime) {

        List<Schedule> schedules = new ArrayList<>();

        DateTimeValue startv = new DateTimeValue(dayTime.getStartTime());
        DateTimeValue endv = new DateTimeValue(dayTime.getEndTime());

        Calendar start = Calendar.getInstance();
        start.setTime(instance.getTime());
        Calendar end = Calendar.getInstance();
        end.setTime(instance.getTime());

        start.set(Calendar.HOUR_OF_DAY, startv.hourOfDay);
        start.set(Calendar.MINUTE, startv.minute);

        end.set(Calendar.HOUR_OF_DAY, endv.hourOfDay);
        end.set(Calendar.MINUTE, endv.minute);

        while(start.getTime().before(end.getTime())){
            schedules.add(new Schedule(formatedDueTime(start.getTime())));
            start.add(Calendar.HOUR, 1);
        }
        return schedules;
    }
}

