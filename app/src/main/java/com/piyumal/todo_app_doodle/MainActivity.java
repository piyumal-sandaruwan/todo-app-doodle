package com.piyumal.todo_app_doodle;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView dateText;
    private String selectedTaskDate = "";

    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        dateText = findViewById(R.id.dateText);
        updateCurrentDate();

        // RecyclerView Initialization
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

        // Load existing tasks from Firestore
        loadTasksFromFirestore();

        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme).setView(dialogView).create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextInputEditText etTaskName = dialogView.findViewById(R.id.etTaskName);
        TextView tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);

        // Date Picker logic
        dialogView.findViewById(R.id.datePickerLayout).setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                Calendar date = Calendar.getInstance();
                date.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                selectedTaskDate = sdf.format(date.getTime());
                tvSelectedDate.setText(selectedTaskDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save Task Logic
        dialogView.findViewById(R.id.btnSaveTask).setOnClickListener(v -> {
            String taskName = etTaskName.getText().toString().trim();
            if (taskName.isEmpty() || selectedTaskDate.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                saveNewTask(taskName);
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.btnDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnCancelTask).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void saveNewTask(String title) {
        String userId = mAuth.getCurrentUser().getUid();
        String taskId = db.collection("Users").document(userId).collection("MyTasks").document().getId();

        Task newTask = new Task(taskId, title, "", selectedTaskDate, "Anytime", System.currentTimeMillis());

        db.collection("Users").document(userId)
                .collection("MyTasks").document(taskId)
                .set(newTask)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Task Added!", Toast.LENGTH_SHORT).show();
                    taskList.add(0, newTask); // Add to the top of the list
                    taskAdapter.notifyItemInserted(0);
                    tasksRecyclerView.scrollToPosition(0);
                    selectedTaskDate = "";
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadTasksFromFirestore() {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(userId).collection("MyTasks")
                .orderBy("createdAt", Query.Direction.DESCENDING) // Show newest tasks first
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        taskList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Task t = document.toObject(Task.class);
                            taskList.add(t);
                        }
                        taskAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error loading tasks", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCurrentDate() {
        if (dateText != null) {
            String date = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Calendar.getInstance().getTime());
            dateText.setText(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dev_info) {
            startActivity(new Intent(this, DevInfoActivity.class));
            return true;
        } else if (id == R.id.user_info) {
            startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}