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

public class SignupActivity extends AppCompatActivity {

    // UI Component declarations
    private TextInputEditText nameEt, emailEt, passwordEt, confirmPasswordEt;
    private MaterialButton signupBtn, loginRedirectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enabling Edge-to-Edge display support for modern UI look
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Handling system bar insets to ensure the layout doesn't overlap with status or navigation bars
        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize UI components by finding them in the XML layout
        nameEt = findViewById(R.id.nameEditText);
        emailEt = findViewById(R.id.emailEditText);
        passwordEt = findViewById(R.id.passwordEditText);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEditText);
        signupBtn = findViewById(R.id.signupButton);
        loginRedirectBtn = findViewById(R.id.loginRedirectButton);

        // Listener for the Sign Up action
        signupBtn.setOnClickListener(v -> {
            processSignup();
        });

        // Listener to navigate back to the Login screen
        loginRedirectBtn.setOnClickListener(v -> {
            finish(); // Closes SignupActivity and returns the user to the previous screen
        });
    }

    /**
     * Validates input and navigates to the Home screen
     */
    private void processSignup() {
        String name = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();

        // Check if required fields are empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Successful signup feedback
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to the MainActivity (Home Screen)
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);

        /*
           Navigation Logic:
           We use flags to clear the activity stack so the user cannot
           press 'Back' and return to the Signup screen after logging in.
        */
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Destroy this activity instance
    }
}
