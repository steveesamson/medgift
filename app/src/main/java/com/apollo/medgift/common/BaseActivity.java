package com.apollo.medgift.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.jobs.JobUtil;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.databinding.ContributorDialogBinding;
import com.apollo.medgift.models.InviteStatus;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.models.NotificationType;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.ServiceStatus;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.models.User;
import com.apollo.medgift.views.AboutUsActivity;
import com.apollo.medgift.views.AlertDetail;
import com.apollo.medgift.views.HomePageActivity;
import com.apollo.medgift.views.LogInActivity;
import com.apollo.medgift.views.NotificationActivity;
import com.apollo.medgift.views.ProviderHomePageActivity;
import com.apollo.medgift.R;
import com.apollo.medgift.views.provider.HealthTipActivity;
import com.apollo.medgift.views.provider.ServiceActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Setup Tasker with android handler
        JobUtil.init(new Handler(Looper.getMainLooper()));
        NotificationUtil.createNotificationChannel(this, getString(R.string.channel_name), getString(R.string.channel_description));


    }

    // Set up toolbar with a dynamic title
    protected void setupToolbar(Toolbar toolbar, String title, boolean showBackButton) {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setIcon(R.drawable.icon);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
        }
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
        SessionUser sessionUser = Firebase.currentUser();
        if (sessionUser == null) {
            finish();
        } else{
            Notifier.getInstance().beginWatches(this);
        }

    }



    protected void onNotified(boolean isActive){
        this.menu.getItem(0).setIcon(isActive? R.drawable.notification_active: R.drawable.notification);
    }
    // Inflate menu based on user type
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Retrieve usertype based on user status
        String userType = getUserType();
        Log.i(TAG, userType);

        MenuInflater inflater = getMenuInflater();

        // Check for user type and inflate menu
        if (userType.equals(Role.GIFTER)) {
            inflater.inflate(R.menu.homemenu, menu);
        } else if (userType.equals(Role.PROVIDER)) {
            inflater.inflate(R.menu.providermenu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    // Retrieve user type from shared pref or backend
    private String getUserType() {

        return Firebase.currentUser().getUserRole();
    }

    // Handle menu items selector based on user type
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle back button click globally
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // Set usertype
        String userType = getUserType();

        // Handle user types
        if (userType.equals(Role.GIFTER)) {
            return handleGifterMenuSelection(id);
        } else if (userType.equals(Role.PROVIDER)) {
            return handleProviderMenuSelection(id);
        }
        return super.onOptionsItemSelected(item);
    }

    // Handle Gifter menu
    private boolean handleGifterMenuSelection(int itemId) {
        if (itemId == R.id.notification) {
            navigateTo(NotificationActivity.class);
            return true;
        } else if (itemId == R.id.home) {
            navigateTo(HomePageActivity.class);
            return true;
        } else if (itemId == R.id.availableservices) {
            navigateTo(ServiceActivity.class);
            return true;
        } else if (itemId == R.id.healthTips) {
            navigateTo(HealthTipActivity.class);
            return true;
        } else if (itemId == R.id.aboutUs) {
            navigateTo(AboutUsActivity.class);
            return true;
        } else if (itemId == R.id.logOut) {
            logout();
            return true;
        }
        return false;
    }

    // Handle Provider menu
    private boolean handleProviderMenuSelection(int itemId) {
        if (itemId == R.id.notification) {
            navigateTo(NotificationActivity.class);
            return true;
        }else if (itemId == R.id.home) {
            navigateTo(ProviderHomePageActivity.class);
            return true;
        } else if (itemId == R.id.logOut) {
            logout();
            return true;
        }
        return false;
    }

    // Helper method to start activity
    protected void navigateTo(Class<?> destinationClass) {
        startActivity(new Intent(this, destinationClass));
    }

    // LogOut on click
    protected void logout() {
        Firebase.logout();
        Notifier.getInstance().release();
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void showIDialogFor(View view, String title, String positiveLabel, DialogInterface.OnClickListener onPositive) {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveLabel, onPositive)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

}
