package com.example.task_41;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The TaskCursorAdapter provides an abstraction layer for the task list view
 * Specifically, for populating the task list view with task data from the database
 */
public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // find fields to populate in inflated template
        TextView tvTitle = view.findViewById(R.id.title);
        TextView tvDescription = view.findViewById(R.id.description);
//        not currently used but was used in debugging
//        TextView tvCreatedAt = view.findViewById(R.id.created_at);
//        TextView tvModifiedAt = view.findViewById(R.id.modified_at);
        TextView tvDueDate = view.findViewById(R.id.due_date);

        // get properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
        long dueDate = cursor.getLong(cursor.getColumnIndexOrThrow("DUE_DATE"));
//        converting epoch-based date to formatted date
        Date dateToFormat = new Date(dueDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(dateToFormat);

        // populate fields with extracted properties
        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDueDate.setText(formattedDate);
    }
}