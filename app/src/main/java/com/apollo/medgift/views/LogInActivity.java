package com.apollo.medgift.views;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.apollo.medgift.databinding.ActivityLoginBinding;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(loginBinding.getRoot());

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

    private void login() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if(vId == this.loginBinding.btnSignup.getId()){
            register();
        } else if (vId == this.loginBinding.btnLogin.getId()) { // Handle btnLogin click
            // VALIDATE INPUTS HERE
            login();
        }
    }
}