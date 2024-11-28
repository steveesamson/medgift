package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityRegisterBinding;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ActivityRegisterBinding registerBinding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);

        setContentView(registerBinding.getRoot());
        Util.applyWindowInsetsListenerTo(this, registerBinding.registerForm);

        bindEvents();
    }

    private void bindEvents() {
        user = new User(User.Type.GIFTER);
        registerBinding.rdGifter.setChecked(true);
        registerBinding.rdProvider.setChecked(false);
        registerBinding.btnLogin.setOnClickListener(this);
        registerBinding.btnRegister.setOnClickListener(this);
        registerBinding.rgUserType.setOnCheckedChangeListener(this);
    }


    private void login() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }


    // Clear previous errors
    private void clearErrors() {
        registerBinding.lytFirstName.setError("");
        registerBinding.lytLastName.setError("");
        registerBinding.lytPassword.setError("");
        registerBinding.lytConfirmPassword.setError("");
        registerBinding.lytEmail.setError("");
        registerBinding.lytFirstName.setError("");
    }
    // Process Room Save
    @Override
    public void onClick(View view) {
        if (view == registerBinding.btnLogin) {
            login();
        } else if (view == registerBinding.btnRegister) {
            clearErrors();
            boolean formIsValid = true;
            String firstName = Util.valueOf(registerBinding.edtFirstName);
            String lastName = Util.valueOf(registerBinding.edtLastName);
            String email = Util.valueOf(registerBinding.edtEmail);
            String password = Util.valueOf(registerBinding.edtPassword);
            String confirm = Util.valueOf(registerBinding.edtConfirmPassword);

            if (firstName.isEmpty()) {
                registerBinding.lytFirstName.setError("First name is required.");
                formIsValid = false;
            }
            if (lastName.isEmpty()) {
                registerBinding.lytLastName.setError("Last name is required.");
                formIsValid = false;
            }
            if (email.isEmpty() || !Util.isEmail(email)) {
                registerBinding.lytEmail.setError("Email is required.");
                formIsValid = false;
            }
            if (password.isEmpty() || !Util.isMinLen6(password)) {
                registerBinding.lytPassword.setError("Password is required.");
                formIsValid = false;
            }

            if (confirm.isEmpty() || !Util.isSame(confirm, password)) {
                registerBinding.lytConfirmPassword.setError("Passwords do not match.");
                formIsValid = false;
            }

            if (formIsValid) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                Util.startProgress(registerBinding.progress, "Registering...");
                Firebase.createUser(user.getEmail(), password, (task) -> {
                    if (task.isSuccessful()) {
                        Firebase.updateProfile(user, (updateTask) -> {
                            if (updateTask.isSuccessful()) {

                                user = null;
                                Util.notify(RegisterActivity.this, "Registration was successful.");
                                finish();
                            }
                            Util.stopProgress(registerBinding.progress);
                        });
                        // Keep a copy of user
                        Firebase.save(user, User.STORE, (tsk, key) -> {});

                    } else {
                        Util.stopProgress(registerBinding.progress);
                        Util.notify(RegisterActivity.this, "Registration failed.");
                    }
                });
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == registerBinding.rdGifter.getId()) {
            user.setRole(User.Type.GIFTER);
        } else if (checkedId == registerBinding.rdProvider.getId()) {

                user.setRole(User.Type.PROVIDER);

        }
    }
}