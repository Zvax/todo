package org.fractalesque.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Mapper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DICTIONARY_TABLE_NAME = "todos";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    "Title TEXT, " +
                    "Description TEXT);";

    Mapper(Context context) {
        super(context, "todo", null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    public void insert(String title, String description) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO todos (title, description) VALUES ('"+title+"','"+description+"')");
    }

    public Task[] getTasks() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[2];
        columns[0] = "title";
        columns[1] = "description";
        Cursor cursor = db.query("todos", columns, null, null, null, null, null);
        Task[] tasks = new Task[cursor.getCount()];
        while (cursor.moveToNext()) {
            int pos = cursor.getPosition();
            tasks[pos] = new Task(cursor.getString(0), cursor.getString(1));
        }
        cursor.close();
        return tasks;
    }
}