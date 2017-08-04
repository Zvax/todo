package org.fractalesque.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Mapper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DICTIONARY_TABLE_NAME = "todos";
    private static final String DICTIONARY_COLUMN_ID = "Id";
    private static final String DICTIONARY_TABLE_DELETE =
            "DROP TABLE " + DICTIONARY_TABLE_NAME;
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    DICTIONARY_COLUMN_ID + " INTEGER, " +
                    "Title TEXT, " +
                    "Description TEXT);";

    Mapper(Context context) {
        super(context, "todo", null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DICTIONARY_TABLE_DELETE);
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    public void update(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", task.getTitle());
        values.put("Description", task.getDescription());
        db.update(DICTIONARY_TABLE_NAME, values, "Id="+task.getId(), null);
    }

    public void insert(Task task) {
        SQLiteDatabase read = getReadableDatabase();
        String[] selectColumns = {"Max("+DICTIONARY_COLUMN_ID+")"};
        Cursor maxIdCursor = read.query(DICTIONARY_TABLE_NAME, selectColumns, null, null, null, null, null);
        maxIdCursor.moveToNext();
        int nextId = maxIdCursor.getInt(0) + 1;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into todos (Title, Description, Id) values ('"+task.getTitle()+"','"+task.getDescription()+"', '"+nextId+"')");
    }

    public Task[] getTasks() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[3];
        columns[0] = "Id";
        columns[1] = "Title";
        columns[2] = "Description";
        Cursor cursor = db.query(DICTIONARY_TABLE_NAME, columns, null, null, null, null, null);
        Task[] tasks = new Task[cursor.getCount()];
        while (cursor.moveToNext()) {
            int pos = cursor.getPosition();
            tasks[pos] = new Task(cursor.getString(1), cursor.getString(2), cursor.getInt(0));
        }
        cursor.close();
        return tasks;
    }
}