package com.piyumal.todo_app_doodle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * LoginActivity for Doodle App
 * Handles user authentication UI and navigation to Main/Signup screens.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Enable Edge-to-Edge for modern UI support
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 2. Handle System Bar Insets to prevent Notch/Camera overlap
        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // 3. Initialize UI Components using IDs from activity_login.xml
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        MaterialButton loginButton = findViewById(R.id.loginButton);
        MaterialButton createAccountButton = findViewById(R.id.createAccountButton);

        // 4. Login Button Logic -> Navigate to MainActivity (Home Screen)
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                // Navigate from LoginActivity to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                /* Best Practice: Clear activity stack.
                   Prevents user from returning to Login screen when pressing back from Home.
                */
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();

                // Close the login activity instance
                finish();
            });
        }

        // 5. Signup Button Logic -> Navigate to SignupActivity
        if (createAccountButton != null) {
            createAccountButton.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            });
        }
    }
}
