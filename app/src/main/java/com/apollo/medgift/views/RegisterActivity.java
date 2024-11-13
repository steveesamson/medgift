package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);

        setContentView(registerBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(registerBinding.registerForm, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindEvents();
    }

    private void bindEvents() {
        this.registerBinding.btnLogin.setOnClickListener(this);
    }


    private void login(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if(vId == this.registerBinding.btnLogin.getId()){
            login();
        }
    }
}