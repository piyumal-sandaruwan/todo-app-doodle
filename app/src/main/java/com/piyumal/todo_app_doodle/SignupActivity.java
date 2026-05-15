package com.piyumal.todo_app_doodle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    // Step 1: Define variables for UI and Firebase
    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialButton signupButton, loginRedirectButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Variable for the Firestore Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Step 2: Initialize both Auth and Database
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Step 3: Link variables to XML IDs
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginRedirectButton = findViewById(R.id.loginRedirectButton);

        // Set listeners for buttons
        signupButton.setOnClickListener(v -> registerUser());
        loginRedirectButton.setOnClickListener(v -> finish());
    }

    // This method handles the registration process
    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation: Checking if fields are empty or if passwords match
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creating account in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If Auth is successful, get the User ID
                        String userId = mAuth.getCurrentUser().getUid();
                        // Call method to save name to database
                        saveUserInfo(userId, name, email);
                    }
                });
    }

    // Saving user name and email to Firestore Database
    private void saveUserInfo(String userId, String name, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("userId", userId);

        db.collection("Users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                });
    }
}