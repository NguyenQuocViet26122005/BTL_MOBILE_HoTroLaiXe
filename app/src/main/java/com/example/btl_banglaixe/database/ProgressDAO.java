package com.example.btl_banglaixe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.Map;

public class ProgressDAO {
    private DatabaseHelper dbHelper;

    public ProgressDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void updateProgress(String category, int completed, int correct) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS, completed);
        v.put(DatabaseHelper.COLUMN_CORRECT_ANSWERS, correct);
        db.update(DatabaseHelper.TABLE_PROGRESS, v, DatabaseHelper.COLUMN_CATEGORY_NAME + " = ?", new String[]{category});
    }

    public Map<String, Integer> getProgressByCategory(String category) {
        Map<String, Integer> m = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_PROGRESS, null, DatabaseHelper.COLUMN_CATEGORY_NAME + " = ?", new String[]{category}, null, null, null);
        if (c.moveToFirst()) {
            m.put("total", c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_QUESTIONS)));
            m.put("completed", c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS)));
            m.put("correct", c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORRECT_ANSWERS)));
        }
        c.close();
        return m;
    }

    public Map<String, Integer> getTotalProgress() {
        Map<String, Integer> m = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_PROGRESS, null, null, null, null, null, null);
        int total = 0, completed = 0, correct = 0;
        if (c.moveToFirst()) {
            do {
                total += c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_QUESTIONS));
                completed += c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS));
                correct += c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORRECT_ANSWERS));
            } while (c.moveToNext());
        }
        m.put("total", total);
        m.put("completed", completed);
        m.put("correct", correct);
        c.close();
        return m;
    }

    public void resetAllProgress() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS, 0);
        v.put(DatabaseHelper.COLUMN_CORRECT_ANSWERS, 0);
        db.update(DatabaseHelper.TABLE_PROGRESS, v, null, null);
    }

    public void close() {
        dbHelper.close();
    }
}
