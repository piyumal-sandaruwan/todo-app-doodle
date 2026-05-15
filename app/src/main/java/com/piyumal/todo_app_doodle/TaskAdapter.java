package com.piyumal.todo_app_doodle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

/**
 * TaskAdapter: The "Bridge" between the Task data list and the RecyclerView UI.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // The data source: a list of Task objects
    private List<Task> taskList;

    // CONSTRUCTOR: Receives the list of tasks from MainActivity
    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // INFLATION: This step loads the 'item_task.xml' layout for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // BINDING: Get the data for the current position
        Task task = taskList.get(position);

        // DATA MAPPING: Injecting Task data into the UI elements
        holder.tvTitle.setText(task.getTitle());
        holder.tvDate.setText(task.getScheduledDate());
        holder.taskCheckBox.setChecked(task.isCompleted());

        // DELETE LOGIC: Triggered when the user clicks the delete (trash) button
        holder.btnDelete.setOnClickListener(v -> {
            deleteTaskFromFirestore(task.getTaskId(), position);
        });

        // EDIT LOGIC: Placeholder for future update functionality
        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Edit: " + task.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * DATABASE SYNC: Deletes the task from Cloud Firestore and updates UI.
     */
    private void deleteTaskFromFirestore(String taskId, int position) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Target the specific task document in the user's sub-collection
        db.collection("Users").document(userId)
                .collection("MyTasks").document(taskId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // UI REFRESH: Remove from the local list and notify the RecyclerView
                    taskList.remove(position);
                    notifyItemRemoved(position);
                    // Adjust positions for remaining items to prevent index errors
                    notifyItemRangeChanged(position, taskList.size());
                })
                .addOnFailureListener(e -> {
                    // Error handling if deletion fails
                });
    }

    @Override
    public int getItemCount() {
        // Returns the total number of tasks to be displayed
        return taskList.size();
    }

    /**
     * VIEWHOLDER: A static class that holds the IDs for the XML views.
     * This improves performance by preventing repetitive 'findViewById' calls.
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;
        CheckBox taskCheckBox;
        ImageButton btnEdit, btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            //ensure memory efficiency and smooth scrolling
            //Without this, the app would have to call findViewById() thousands of times while scrolling,
            super(itemView);
            // Linking Java variables to the XML IDs in 'item_task.xml'
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDate = itemView.findViewById(R.id.tvTaskDate);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
            btnEdit = itemView.findViewById(R.id.btnEditTask);
            btnDelete = itemView.findViewById(R.id.btnDeleteTask);
        }
    }
}