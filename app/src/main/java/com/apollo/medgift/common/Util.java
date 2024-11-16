package com.apollo.medgift.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.apollo.medgift.databinding.ProgressBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

/**
 * Util, alass to house all shared operations
 */
public class Util {



    // These following methods help in managing
    // the display/hiding of the the progress during
    // network activities.
    public static void startProgress(ProgressBinding progress, String message){
        progress.progressText.setText(message);
        progress.progressLoader.setVisibility(View.VISIBLE);
    }
    public static void stopProgress(ProgressBinding progress){
        progress.progressLoader.setVisibility(View.GONE);
    }
    // Extracts resource name from Uri string
    public static String uriToName(String url){
        try {
            String[] parts = url.split("/");

            int lastIndex = parts.length - 1;
            String last = parts[lastIndex];
            last = last.substring(0, last.indexOf("?"));
            return last.replace("%2F", "/").trim();
        }catch(Exception x){
            Log.e("Storage Extraction: ", x.toString());
            return null;
        }

    }

    public static String getEmpty(String type){
        return String.format("No %s added yet.", type);
    }

    // Convenience for simple toasts
    public static void notify(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // Reusable Dialog for informing users of
    // outcomes of actions
    public  static void showDialog(Context context, String title, String body){
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
    public  static void showConfirm(Context context, String title, String ask,  DialogInterface.OnClickListener confirm){
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

    public static boolean isEmail(String input){
        return !input.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }
    private static final String POSTAL_CODE = "^[A-Z][0-9][A-Z]\\s[0-9][A-Z][0-9]$";
    public static boolean isPostalCode(String input){

        return !input.isEmpty() &&  input.matches(POSTAL_CODE);
    }
    public static boolean isPhoneNo(String input){
        return !input.isEmpty() && Patterns.PHONE.matcher(input).matches();
    }
    public static boolean isMinLen6(String input){
        return !input.isEmpty() && input.length() >= 6;
    }

    public static boolean isSame(String owner, String uid) {
        return owner.equals(uid);
    }

    // Get the text from input fields
    public static String valueOf(TextInputEditText input){
        return String.valueOf(input.getText());
    }


    public static String valueOf(AutoCompleteTextView input){
        return String.valueOf(input.getText());
    }

    // Display date picker for date fields
    public static void showDatePickerFor(TextInputEditText edtDate, Context context ){
        // This is the date field
        // Retrieve a Calendar instance
        Calendar cal = Calendar.getInstance();
        // Get the current Day of Month
        int dayOfSale = cal.get(Calendar.DAY_OF_MONTH);
        // Get the current Month
        int monthOfSale = cal.get(Calendar.MONTH);
        // Get the current year
        int yearOfSale = cal.get(Calendar.YEAR);

        // Create and instance of DatePickerDialog
        // with the extracted information
        // and registering the onDateSet listener
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Clear previous error if any
                edtDate.setError(null);
                // Update edtDate view
                edtDate.setText(String.format("%s/%s/%s", dayOfMonth, (month + 1), year));
            }
        }, yearOfSale, monthOfSale, dayOfSale);

        // Restrict min date to current date.

        datePicker.getDatePicker().setMinDate(cal.getTimeInMillis());

        // Display the date picker dialog
        datePicker.show();
    }
}
