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

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class AddFragment extends Fragment {

    private static final String DEBUG_TAG = "AddFragment";

    /*
     * Helper function to convert year, month, and day to milliseconds since unix epoch
     * */
    private long convertToMillis(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    /*
     * Helper function to display an overlay alerting user of state change
     * */
    private void displayOverlayMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    //    component references for use
    private EditText newTaskTitleTextEdit;
    private EditText newTaskDescriptionTextEdit;
    private CalendarView newTaskDueDate;
    private Button submitNewTaskButton;

    long newDueDate = -1;

    //    DB manager class
    private DataManager dataManager;

    //    handle submitting new task
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // component references
        newTaskTitleTextEdit = view.findViewById(R.id.newTaskTitleTextEdit);
        newTaskDescriptionTextEdit = view.findViewById(R.id.newTaskDescriptionTextEdit);
        newTaskDueDate = view.findViewById(R.id.newTaskDueDate);
        submitNewTaskButton = view.findViewById(R.id.submitNewTaskButton);


        // DB manager instance
        dataManager = new DataManager(getContext());

//    get the date the user enters into the calendar view
        newTaskDueDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(DEBUG_TAG, "onSelectedDayChange: " + year + "/" + month + "/" + dayOfMonth + " -- " + view.getDate());
                newDueDate = convertToMillis(year, month, dayOfMonth);
            }
        });

//    handles inserting new task to db
        submitNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = newTaskTitleTextEdit.getText().toString();
                String description = newTaskDescriptionTextEdit.getText().toString();

                // Check if the description is empty
                if(title.isEmpty()) {
                    displayOverlayMessage(submitNewTaskButton, "Please enter a title");
                    return;
                }

                long dateToUse;
//            if the user doesn't change the date, use current date
                if (newDueDate != -1) {
                    dateToUse = newDueDate;
                } else {
                    dateToUse = System.currentTimeMillis();
                }

                boolean insertWasSuccessful = dataManager.insertTask(title, description, dateToUse);

                if (insertWasSuccessful) {
//                navigate back to home
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                    displayOverlayMessage(submitNewTaskButton, "Task created!");
                } else {
                    // Display error
                    displayOverlayMessage(submitNewTaskButton, "Error creating task, please try again");
                }
            }
        });

        return view;
    }



}