package com.piyumal.todo_app_doodle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the app theme before onCreate to bypass system splash icon
        setTheme(R.style.Theme_Doodle);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Transition to LoginActivity after a 2-second delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Remove SplashActivity from the back stack
            }
        }, 2000);
    }
}