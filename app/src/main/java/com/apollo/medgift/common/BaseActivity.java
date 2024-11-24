package com.apollo.medgift.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.models.User;
import com.apollo.medgift.views.HomePageActivity;
import com.apollo.medgift.views.LogInActivity;
import com.apollo.medgift.views.ProviderHomePageActivity;
import com.apollo.medgift.R;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected String ROLE = "";
    protected String DISPLAY = "";

    // Set up toolbar with a dynamic title
    protected void setupToolbar(Toolbar toolbar, String title, boolean showBackButton) {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            getSupportActionBar().setDisplayShowHomeEnabled(showBackButton);
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
        FirebaseUser currentUser = Firebase.currentUser();
        if (currentUser == null) {
            finish();
        } else {
            String[] nameRole = currentUser.getDisplayName().split("\\|");
            DISPLAY = nameRole[0];
            ROLE = nameRole[1];
        }
    }

    // Inflate menu based on user type
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Retrieve usertype based on user status
        String userType = getUserType();


        MenuInflater inflater = getMenuInflater();

        // Check for user type and inflate menu
        if (userType.equals(User.Role.GIFTER.name())) {
            inflater.inflate(R.menu.homemenu, menu);
        } else if (userType.equals(User.Role.PROVIDER.name())) {
            inflater.inflate(R.menu.providermenu, menu);
        }
        return true;
    }

    // Retrieve user type from shared pref or backend
    private String getUserType() {

        return ROLE;
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
        if (userType.equals(User.Role.GIFTER.name())) {
            return handleGifterMenuSelection(id);
        } else if (userType.equals(User.Role.PROVIDER.name())) {
            return handleProviderMenuSelection(id);
        }
        return super.onOptionsItemSelected(item);
    }

    // Handle Gifter menu
    private boolean handleGifterMenuSelection(int itemId) {
        if (itemId == R.id.home) {
            navigateTo(HomePageActivity.class);
            return true;
        } else if (itemId == R.id.categories) {
            return true;
        } else if (itemId == R.id.healthTips) {
            //
            return true;
        } else if (itemId == R.id.aboutUs) {
            //
            return true;
        } else if (itemId == R.id.logOut) {
            logout();
            return true;
        } else {
            return false;
        }
    }

    // Handle Provider menu
    private boolean handleProviderMenuSelection(int itemId) {
        if (itemId == R.id.home) {
            navigateTo(ProviderHomePageActivity.class);
            return true;
        } else if (itemId == R.id.logOut) {
            logout();
            return true;
        } else {
            return false;
        }
    }

    // Helper method to start activity
    private void navigateTo(Class<?> destinationClass) {
        startActivity(new Intent(this, destinationClass));
    }

    // LogOut on click
    protected void logout() {
        Firebase.logout();
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
