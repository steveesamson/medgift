package com.apollo.medgift.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Setup Tasker with android handler
//        JobUtil.init(new Handler(Looper.getMainLooper()));
//        NotificationUtil.createNotificationChannel(this, getString(R.string.channel_name), getString(R.string.channel_description));


    }

    protected void applyWindowInsetsListenerTo(ComponentActivity activity, ViewGroup view) {
        EdgeToEdge.enable(activity);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    // Handle session
    @Override
    protected void onStart() {
        super.onStart();

//        Notifier.getInstance().beginWatches(this);
    }



    protected void onNotified(boolean isActive){
//        this.menu.getItem(0).setIcon(isActive? R.drawable.notification_active: R.drawable.notification);
    }
    // Inflate menu based on user type

    // Helper method to start activity
    protected void navigateTo(Class<?> destinationClass) {
        startActivity(new Intent(this, destinationClass));
    }

//    // LogOut on click
//    protected void logout() {
//        Firebase.logout();
//        Notifier.getInstance().release();
//        Intent intent = new Intent(this, LogInActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }


}
