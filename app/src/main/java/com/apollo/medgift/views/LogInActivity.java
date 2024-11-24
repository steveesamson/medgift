package com.apollo.medgift.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityLoginBinding;
import com.apollo.medgift.models.User;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(loginBinding.getRoot());
        Util.applyWindowInsetsListenerTo(this, loginBinding.loginActivity);

        bindEvents();
    }

    private void bindEvents() {
        this.loginBinding.btnSignup.setOnClickListener(this);
        this.loginBinding.btnLogin.setOnClickListener(this);
    }


    private void register(){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Clear previous errors
    private void clearErrors() {
        loginBinding.lytEmail.setError("");
        loginBinding.lytPassword.setError("");
    }

    private void login() {
        clearErrors();

        String email = Util.valueOf(loginBinding.edtEmail);
        String password = Util.valueOf(loginBinding.edtPassword);
        boolean formIsValid = true;

        if(email.isEmpty() || !Util.isEmail(email)){
            loginBinding.lytEmail.setError("Email is required.");
            formIsValid = false;
        }

        if(password.isEmpty()){
            loginBinding.lytPassword.setError("Password is required.");
            formIsValid = false;
        }

        if(formIsValid){
            Util.startProgress(loginBinding.progress, "Signing in...");
            Firebase.login(email, password, (task) -> {

                if(task.isSuccessful()){
                    String ROLE = Firebase.currentUser().getUserRole();
                    Log.i("ROLE:", ROLE);
                    Intent intent = null;

                    if(ROLE.equals(User.Role.GIFTER.name())){
                        intent = new Intent(LogInActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }else if(ROLE.equals(User.Role.PROVIDER.name())){
                        intent = new Intent(LogInActivity.this, ProviderHomePageActivity.class);
                        startActivity(intent);
                    }

                }else{
                    Util.notify(LogInActivity.this, "Invalid credentials.");
                }
                Util.stopProgress(loginBinding.progress);
            });

        }
    }

    @Override
    public void onClick(View view) {
        if(view == this.loginBinding.btnSignup){
            register();
        } else if (view == this.loginBinding.btnLogin) { // Handle btnLogin click
            login();
        }
    }
}