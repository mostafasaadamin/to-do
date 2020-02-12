package com.example.unknown.todo.Databasepakage;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.unknown.todo.model.tasks;
import java.util.ArrayList;
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TODOTask";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tasks.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + tasks.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long insertProduct(String title,String description,String time,String date,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tasks.Task_title, title);
        values.put(tasks.Task_description,description);
        values.put(tasks.Task_time, time);
        values.put(tasks.Task_date,date);
        values.put(tasks.Task_status,status);
        long idd = db.insert(tasks.TABLE_NAME, null, values);
        db.close();
        return idd;
    }
    public ArrayList<tasks> getAllNotes() {
        ArrayList<tasks> notes = new ArrayList<>();
//        String selectQuery = "SELECT  * FROM " + tasks.TABLE_NAME + " ORDER BY " +
//               tasks.Task_date + " DESC";
        String selectQuery = "SELECT  * FROM " + tasks.TABLE_NAME;
                SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tasks orderr = new tasks();
                orderr.setID(cursor.getString(cursor.getColumnIndex(tasks.Task_ID)));
                orderr.setTitle(cursor.getString(cursor.getColumnIndex(tasks.Task_title)));
                orderr.setDescription(cursor.getString(cursor.getColumnIndex(tasks.Task_description)));
                orderr.setDate(cursor.getString(cursor.getColumnIndex(tasks.Task_date)));
                orderr.setTime(cursor.getString(cursor.getColumnIndex(tasks.Task_time)));
                orderr.setTaskStatus(cursor.getString(cursor.getColumnIndex(tasks.Task_status)));
                notes.add(orderr);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }
    public void deleteNote(tasks or,String note_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tasks.TABLE_NAME, tasks.Task_ID + " = ?",
                new String[]{note_id});
        db.close();
    }
    public int updateTaskStatus(String  status,String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tasks.Task_status, status);
        // updating row

        return db.update(tasks.TABLE_NAME, values, tasks.Task_ID + " = ?",
                new String[]{String.valueOf(id)});

    }


}