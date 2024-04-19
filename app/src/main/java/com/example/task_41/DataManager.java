package com.example.task_41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


/**
 * The Data manager provides an abstraction layer for database CRUD operations
 */
public class DataManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_TITLE = "TITLE";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_CREATED_AT = "CREATED_AT";
    private static final String COLUMN_MODIFIED_AT = "MODIFIED_AT";
    private static final String COLUMN_DUE_DATE = "DUE_DATE";

//    constructor
    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    overriding the onCreate method to create the tasks table to our specification
@Override
public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_MODIFIED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_DUE_DATE + " DATETIME"
            + ")";
    db.execSQL(CREATE_TABLE);

    // Insert filler data
    String INSERT_FILLER_DATA = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_TITLE + ", "
            + COLUMN_DESCRIPTION + ", " + COLUMN_CREATED_AT + ", " + COLUMN_MODIFIED_AT + ") VALUES (?, ?, ?, ?)";
    db.execSQL(INSERT_FILLER_DATA, new Object[]{"Example Task 1", "This is a filler task", System.currentTimeMillis(), System.currentTimeMillis()});
    db.execSQL(INSERT_FILLER_DATA, new Object[]{"Example Task 2", "This is a filler task", System.currentTimeMillis(), System.currentTimeMillis()});
    db.execSQL(INSERT_FILLER_DATA, new Object[]{"Example Task 3", "This is a filler task", System.currentTimeMillis(), System.currentTimeMillis()});
}

//    overriding the onUpgrade method to drop the table if it exists and create a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < DATABASE_VERSION) {
//            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DUE_DATE + " DATETIME"); // Add the new column to the existing table
//        }
        // Insert filler data
        String INSERT_FILLER_DATA = "INSERT INTO " + TABLE_NAME + " ("
                + COLUMN_TITLE + ", "
                + COLUMN_DESCRIPTION + ", " + COLUMN_CREATED_AT + ", " + COLUMN_MODIFIED_AT + ") VALUES (?, ?, ?, ?)";
        db.execSQL(INSERT_FILLER_DATA, new Object[]{"Example Task 1", "This is a filler task", System.currentTimeMillis(), System.currentTimeMillis()});
        db.execSQL(INSERT_FILLER_DATA, new Object[]{"Example Task 2", "This is a filler task", System.currentTimeMillis(), System.currentTimeMillis()});
        db.execSQL(INSERT_FILLER_DATA, new Object[]{"Example Task 3", "This is a filler task", System.currentTimeMillis(), System.currentTimeMillis()});
    }

//    get all rows from the table order by created_at date in descending order
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT *, " + COLUMN_ID + " AS _id FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DUE_DATE + " DESC", null);
    }


//    get row by id
public Cursor getTask(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    return db.rawQuery("SELECT *, " + COLUMN_ID + " AS _id FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id, null);
}

//    insert row
    public boolean insertTask(String title, String description, long dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_CREATED_AT, System.currentTimeMillis());
        contentValues.put(COLUMN_MODIFIED_AT, System.currentTimeMillis());
        contentValues.put(COLUMN_DUE_DATE, dueDate);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

//    update row by id
    public boolean updateTask(int id, String title, String description, long dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_MODIFIED_AT, System.currentTimeMillis());
        contentValues.put(COLUMN_DUE_DATE, dueDate);
        int result = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

//    delete row by id
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
