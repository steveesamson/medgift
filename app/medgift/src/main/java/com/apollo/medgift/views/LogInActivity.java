package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityLoginBinding;
import com.apollo.medgift.models.Role;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    //login activity binding obj
    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate layout
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(loginBinding.getRoot());
        Util.applyWindowInsetsListenerTo(this, loginBinding.loginActivity);//adjust UI

        bindEvents();//bind events to handlers
    }

    private void bindEvents() {
        //set login button's on click listener
        this.loginBinding.btnLogin.setOnClickListener(this);
    }

    // Clear previous errors
    private void clearErrors() {
        //clear all user input fields
        loginBinding.lytEmail.setError("");
        loginBinding.lytPassword.setError("");
    }

    private void login() {
        clearErrors();

        String email = Util.valueOf(loginBinding.edtEmail);
        String password = Util.valueOf(loginBinding.edtPassword);
        boolean formIsValid = true;

        //validate email input
        if (email.isEmpty() || !Util.isEmail(email)) {
            loginBinding.lytEmail.setError("Email is required.");
            formIsValid = false;
        }

        //validate password input
        if (password.isEmpty()) {
            loginBinding.lytPassword.setError("Password is required.");
            formIsValid = false;
        }

        //if valid email and password then login
        if (formIsValid) {
            Util.startProgress(loginBinding.progress, "Signing in...");// show progress for signing in
            Firebase.login(email, password, (task) -> {

                if (task.isSuccessful()) {
                    String ROLE = Firebase.currentUser().getUserRole();//fetch the user role from firebase
                    Log.i("ROLE:", ROLE);// check the user role either gifter or provider login made
                    Intent intent = null;

                    //navgiate to the next activity based on user role here gifter is the role
                    if (ROLE.equals(Role.GIFTER)) {
                        intent = new Intent(LogInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                //invalid credentials display error
                else {
                    Util.notify(LogInActivity.this, "Invalid credentials.");
                }
                Util.stopProgress(loginBinding.progress);
            });

        }
    }

    @Override
    public void onClick(View view) {
        if (view == this.loginBinding.btnLogin) { // Handle btnLogin click
            login();//trigger login process
        }
    }
}