package com.piyumal.todo_app_doodle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity: The entry point of the application.
 * Responsibility: Display branding and manage the initial transition.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // PRE-CONDITION: Set the custom app theme immediately.
        // This prevents the default white screen/system icon from showing.
        setTheme(R.style.Theme_Doodle);

        super.onCreate(savedInstanceState);

        // UI BINDING: Linking the layout file that contains the logo and branding.
        setContentView(R.layout.activity_splash);

        // FLOW CONTROL: Using a Handler to manage the timed transition.
        // This is an asynchronous task that doesn't block the UI thread.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // NAVIGATION: Explicit Intent to move from Splash to Login.
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                // BEST PRACTICE: finish() destroys this Activity.
                // This prevents the user from going back to the Splash screen via the back button.
                finish();
            }
        }, 2000); // TIMING: 2000ms (2 seconds) for optimal user experience.
    }
}