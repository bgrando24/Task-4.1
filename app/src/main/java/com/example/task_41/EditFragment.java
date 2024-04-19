package com.example.task_41;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EditFragment extends Fragment {

    /*
    * Helper function to convert year, month, and day to milliseconds since unix epoch
    * */
    public long convertToMillis(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    private static final String TAG = "EditFragment";

//    component (and new due date) references for use
    private TextView editTaskHeadingTextView;
    private TextView editTaskDueDateTextView;
    private EditText editTaskTitleTextEdit;
    private EditText editTaskDescriptionTextEdit;
    private CalendarView editTaskDateEdit;
    private Button editTaskSubmitButton;

    long newDueDate = -1;

//    DB manager class
    private DataManager dataManager;

    public static EditFragment newInstance(int id, String title, String description, long dueDate) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        args.putString("TITLE", title);
        args.putString("DESCRIPTION", description);
        args.putLong("DUE_DATE", dueDate);
        fragment.setArguments(args);
        return fragment;
    }

//    apply data from db to list view, as well as handle submitting updated data
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_edit, container, false);

    // component references
    editTaskHeadingTextView = view.findViewById(R.id.editTaskHeadingTextView);
    editTaskDueDateTextView = view.findViewById(R.id.editTaskDueDateTextView);
    editTaskTitleTextEdit = view.findViewById(R.id.editTaskTitleTextEdit);
    editTaskDescriptionTextEdit = view.findViewById(R.id.editTaskDescriptionTextEdit);
    editTaskDateEdit = view.findViewById(R.id.editTaskDateEdit);
    editTaskSubmitButton = view.findViewById(R.id.editTaskSubmitButton);

    // DB manager instance
    dataManager = new DataManager(getContext());

    // set the data from the selected task to the edit text components
    if (getArguments() != null) {
        String taskTitle = getArguments().getString("TITLE");
        String taskDescription = getArguments().getString("DESCRIPTION");
        long taskDueDate = getArguments().getLong("DUE_DATE");

        editTaskTitleTextEdit.setText(taskTitle);
        editTaskDescriptionTextEdit.setText(taskDescription);
        editTaskDateEdit.setDate(taskDueDate);
    } else {
        // disable the submit button
        editTaskSubmitButton.setEnabled(false);

        // hide the UI components
        editTaskHeadingTextView.setText("No task has been selected! Select a task to edit by tapping on a task on the home screen.");
        editTaskDueDateTextView.setVisibility(View.GONE);
        editTaskTitleTextEdit.setVisibility(View.GONE);
        editTaskDescriptionTextEdit.setVisibility(View.GONE);
        editTaskDateEdit.setVisibility(View.GONE);

    }


//    get the date the user enters into the calendar view
    editTaskDateEdit.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            Log.d(TAG, "onSelectedDayChange: " + year + "/" + month + "/" + dayOfMonth + " -- " + view.getDate());
            newDueDate = convertToMillis(year, month, dayOfMonth);
        }
    });

//    handles updating task data in DB
    editTaskSubmitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = editTaskTitleTextEdit.getText().toString();
            String description = editTaskDescriptionTextEdit.getText().toString();

            long dateToUse;
//            if the user doesn't change the date, use the original date
            if (newDueDate != -1) {
                dateToUse = newDueDate;
            } else {
                dateToUse = getArguments().getLong("DUE_DATE");
            }

            // Assuming you have a task id to update
            int taskId = getArguments().getInt("ID");

            boolean updateWasSuccessful = dataManager.updateTask(taskId, title, description, dateToUse);

            if (updateWasSuccessful) {
//                navigate back to home
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                Toast.makeText(getContext(), "Task updated!", Toast.LENGTH_SHORT).show();
            } else {
                // Display error
                Toast.makeText(getContext(), "Error updating task", Toast.LENGTH_SHORT).show();
            }
        }
    });

//    set the data from the selected task to the edit text components
        if (getArguments() != null) {
            String taskTitle = getArguments().getString("TITLE");
            String taskDescription = getArguments().getString("DESCRIPTION");
            long taskDueDate = getArguments().getLong("DUE)_DATE");

            editTaskTitleTextEdit.setText(taskTitle);
            editTaskDescriptionTextEdit.setText(taskDescription);
            editTaskDateEdit.setDate(taskDueDate);
        }

        return view;
    }



}