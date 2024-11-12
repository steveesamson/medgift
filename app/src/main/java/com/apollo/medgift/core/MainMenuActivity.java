package com.apollo.medgift.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apollo.medgift.HomePageActivity;
import com.apollo.medgift.MainActivity;
import com.apollo.medgift.R;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            // Navigate to HomePageActivity
            startActivity(new Intent(this, HomePageActivity.class));
            return true;
        } else if (id == R.id.categories) {
            return true;
        } else if (id == R.id.healthTips) {
            //
            return true;
        } else if (id == R.id.aboutUs) {
            //
            return true;
        } else if (id == R.id.logOut) {
            logout();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
