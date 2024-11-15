package com.apollo.medgift.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apollo.medgift.views.HomePageActivity;
import com.apollo.medgift.views.LogInActivity;
import com.apollo.medgift.views.ProviderHomePageActivity;
import com.apollo.medgift.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    protected void setToolBar(Toolbar toolBar, String title ){
        setSupportActionBar(toolBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            // Navigate to HomePageActivity
            startActivity(new Intent(this, HomePageActivity.class));
            return true;
        } else if (id == R.id.categories) {
            startActivity(new Intent(this, ProviderHomePageActivity.class));
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
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
