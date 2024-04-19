package com.example.task_41;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class HomeFragment extends Fragment {
    private ListView taskListView;
    private DataManager dataManager;
    private TaskCursorAdapter taskCursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        taskListView = view.findViewById(R.id.taskListView);

        dataManager = new DataManager(getActivity());
        Cursor cursor = dataManager.getAllTasks();

        taskCursorAdapter = new TaskCursorAdapter(getActivity(), cursor);
        taskListView.setAdapter(taskCursorAdapter);

//        when user clicks on task, send to edit fragment with task details
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) taskListView.getItemAtPosition(position);

                // get task details from cursor
                int taskId = cursor.getInt(cursor.getColumnIndex("ID"));
                String taskTitle = cursor.getString(cursor.getColumnIndex("TITLE"));
                String taskDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
                long taskDueDate = cursor.getLong(cursor.getColumnIndex("DUE_DATE"));

                // create a new instance of EditFragment and pass the task details
                EditFragment editFragment = EditFragment.newInstance(taskId, taskTitle, taskDescription, taskDueDate);

                // navigate to the EditFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, editFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}