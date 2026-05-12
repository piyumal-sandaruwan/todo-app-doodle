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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
        taskList = new ArrayList<>(); // Empty list at start

        taskAdapter = new TaskAdapter(taskList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

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

        // Date Picker with Specific Format (e.g., May 12, 2026)
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

        dialogView.findViewById(R.id.btnSaveTask).setOnClickListener(v -> {
            String taskName = etTaskName.getText().toString().trim();
            if (taskName.isEmpty() || selectedTaskDate.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                taskList.add(new Task(taskName, selectedTaskDate));
                taskAdapter.notifyDataSetChanged();

                selectedTaskDate = ""; // Reset
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.btnDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnCancelTask).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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