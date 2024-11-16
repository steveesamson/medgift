package com.apollo.medgift.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollo.medgift.views.HomePageActivity;
import com.apollo.medgift.views.LogInActivity;
import com.apollo.medgift.views.ProviderHomePageActivity;
import com.apollo.medgift.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Set up toolbar with a dynamic title
    protected void setupToolbar(Toolbar toolbar, String title, boolean showBackButton) {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            getSupportActionBar().setDisplayShowHomeEnabled(showBackButton);
        }
    }

    // Inflate menu based on user type
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Retrieve usertype based on user status
        String userType = getUserType();

        MenuInflater inflater = getMenuInflater();

        // Check for user type and inflate menu
        if (userType.equals("Gifter")) {
            inflater.inflate(R.menu.homemenu, menu);
        } else if (userType.equals("Provider")) {
            inflater.inflate(R.menu.providermenu, menu);
        }
        return true;
    }

    // Retrieve user type from shared pref or backend
    private String getUserType() {
        return "Gifter"; // temp user type
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
        if (userType.equals("Gifter")) {
            return handleGifterMenuSelection(id);
        } else if (userType.equals("Provider")) {
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
            navigateTo(ProviderHomePageActivity.class);
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
        navigateTo(LogInActivity.class);
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
