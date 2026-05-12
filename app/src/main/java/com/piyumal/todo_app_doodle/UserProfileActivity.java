package com.piyumal.todo_app_doodle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Enable Edge-to-Edge for modern UI
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        // 2. Handle System Bar Insets (Fixing overlap with Notch/Status Bar)
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // 3. Sign Out Button Logic
        MaterialButton signOutBtn = findViewById(R.id.signOutBtn);
        if (signOutBtn != null) {
            signOutBtn.setOnClickListener(v -> showLogoutDialog());
        }

        // 4. Edit Profile Button Logic
        MaterialButton editProfileBtn = findViewById(R.id.editProfileBtn);
        if (editProfileBtn != null) {
            editProfileBtn.setOnClickListener(v -> showEditProfileSheet());
        }

        // 5. Back Button Action
        View backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }
    }

    private void showLogoutDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null);

        // Creating Dialog with Custom Theme
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                .setView(dialogView)
                .create();

        // IMPORTANT: Set transparent background to show rounded corners from dialog_background.xml
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Initialize Buttons inside Dialog
        Button btnLogout = dialogView.findViewById(R.id.btnLogout);
        Button btnNotNow = dialogView.findViewById(R.id.btnNotNow);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                dialog.dismiss();
                // Navigate to Login and clear stack
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (btnNotNow != null) {
            btnNotNow.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void showEditProfileSheet() {
        // Material 3 Bottom Sheet Dialog
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.sheet_edit_profile, null);
        bottomSheet.setContentView(sheetView);

        sheetView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            bottomSheet.dismiss();
        });

        sheetView.findViewById(R.id.btnCancel).setOnClickListener(v -> bottomSheet.dismiss());
        bottomSheet.show();
    }
}