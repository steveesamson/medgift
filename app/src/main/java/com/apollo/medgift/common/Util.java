package com.apollo.medgift.common;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Util, alass to house all shared operations
 */
public class Util {



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


    // Convenience for simple toasts
    public static void notify(Context context, String message){
        Toast.makeText(context, "You must select a plan type.", Toast.LENGTH_LONG).show();
    }

    // Reusable Dialog for informing users of
    // outcomes of actions
    public  static void showDialog(Context context, String title, String body){
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
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
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(ask)
                .setPositiveButton("Yes", confirm)
                .setNegativeButton("No", (dialog1, which) -> {
                    dialog1.dismiss();
                }).show();
//        dialog.show();
//        AlertDialog.Builder alert = new AlertDialog.Builder(context);
//        alert.setTitle(title);
//        alert.setMessage(ask);
//        alert.setPositiveButton("Yes", (DialogInterface.OnClickListener) confirm);
//
//        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        alert.show();
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


}

