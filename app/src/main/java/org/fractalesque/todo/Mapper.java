package org.fractalesque.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class Mapper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_TODOS = "todos";
    private static final String TABLE_TEMP = "todos_temp";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_PARENT = "Parent";
    private static final String SQL_DELETE_TODOS_TABLE =
        "DROP TABLE " + TABLE_TODOS;
    private static final String SQL_DELETE_TEMP_TABLE =
        "DROP TABLE " + TABLE_TEMP;
    private static final String SQL_BACKUP_DATA_TO_TEMP =
        "INSERT INTO " + TABLE_TEMP + "(Id, Title, Description) SELECT (Id, Title, Description) FROM " + TABLE_TODOS;
    private static final String SQL_IMPORT_DATA_FROM_TEMP =
        "INSERT INTO " + TABLE_TODOS + "(Id, Title, Description) SELECT (Id, Title, Description) FROM " + TABLE_TEMP;
    private static final String SQL_CREATE_TODOS_TABLE =
        "CREATE TABLE " + TABLE_TODOS + " (" +
            COLUMN_ID + " INTEGER, " +
            COLUMN_PARENT + " INTEGER, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT);";
    private static final String SQL_CREATE_TEMP_TABLE =
        "CREATE TABLE " + TABLE_TEMP + " (" +
            COLUMN_ID + " INTEGER, " +
            COLUMN_PARENT + " INTEGER, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT);";

    private SQLiteDatabase writeDB;

    Mapper(Context context) {
        super(context, "todo", null, DATABASE_VERSION);
        this.writeDB = getWritableDatabase();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (newVersion > 4) {
                db.execSQL(SQL_CREATE_TEMP_TABLE);
                db.execSQL(SQL_BACKUP_DATA_TO_TEMP);
                db.execSQL(SQL_DELETE_TODOS_TABLE);
                db.execSQL(SQL_CREATE_TODOS_TABLE);
                db.execSQL(SQL_IMPORT_DATA_FROM_TEMP);
                db.execSQL(SQL_DELETE_TEMP_TABLE);
            } else {
                db.execSQL(SQL_DELETE_TODOS_TABLE);
                db.execSQL(SQL_CREATE_TODOS_TABLE);
            }
        } catch (SQLException exception) {
            System.out.println("Error " + exception.getMessage());
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODOS_TABLE);
    }

    public void delete(Task task) {
        writeDB.delete(TABLE_TODOS, "id=" + task.getId(), null);
    }

    public void update(Task task) {
        SQLiteStatement stmt = writeDB.compileStatement(
            "UPDATE " + TABLE_TODOS + " SET " +
                COLUMN_TITLE + "=?," +
                COLUMN_DESCRIPTION + "=? " +
                "WHERE " + COLUMN_ID + "=?");
        stmt.bindString(1, task.getTitle());
        stmt.bindString(2, task.getDescription());
        stmt.bindLong(3, task.getId());
        stmt.executeUpdateDelete();
    }

    public void insert(Task task) {
        SQLiteDatabase read = getReadableDatabase();
        String[] selectColumns = {"Max(" + COLUMN_ID + ")"};
        Cursor maxIdCursor = read.query(TABLE_TODOS, selectColumns, null, null, null, null, null);
        maxIdCursor.moveToNext();
        int nextId = maxIdCursor.getInt(0) + 1;
        maxIdCursor.close();
        SQLiteStatement stmt = writeDB.compileStatement(
            "INSERT INTO " + TABLE_TODOS + "(" +
                COLUMN_TITLE + "," + COLUMN_DESCRIPTION + "," + COLUMN_ID +
                ") VALUES (?,?,?)"
        );
        stmt.bindString(1, task.getTitle());
        stmt.bindString(2, task.getDescription());
        stmt.bindLong(3, nextId);
        stmt.executeInsert();
    }

    public Task[] getTasks() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[3];
        columns[0] = "Id";
        columns[1] = "Title";
        columns[2] = "Description";
        Cursor cursor = db.query(TABLE_TODOS, columns, null, null, null, null, null);
        Task[] tasks = new Task[cursor.getCount()];
        while (cursor.moveToNext()) {
            int pos = cursor.getPosition();
            tasks[pos] = new Task(cursor.getString(1), cursor.getString(2), cursor.getInt(0));
        }
        cursor.close();
        return tasks;
    }
}