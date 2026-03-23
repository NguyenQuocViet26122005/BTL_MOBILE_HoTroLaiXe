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

    // Cập nhật tiến độ
    public void updateProgress(String category, int completedQuestions, int correctAnswers) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS, completedQuestions);
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWERS, correctAnswers);

        String whereClause = DatabaseHelper.COLUMN_CATEGORY_NAME + " = ?";
        String[] whereArgs = {category};
        
        db.update(DatabaseHelper.TABLE_PROGRESS, values, whereClause, whereArgs);
        db.close();
    }

    // Lấy tiến độ theo chủ đề
    public Map<String, Integer> getProgressByCategory(String category) {
        Map<String, Integer> progress = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = DatabaseHelper.COLUMN_CATEGORY_NAME + " = ?";
        String[] selectionArgs = {category};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_PROGRESS, null, selection, 
                                selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            progress.put("total", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_QUESTIONS)));
            progress.put("completed", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS)));
            progress.put("correct", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORRECT_ANSWERS)));
        }
        
        cursor.close();
        db.close();
        return progress;
    }

    // Lấy tổng tiến độ
    public Map<String, Integer> getTotalProgress() {
        Map<String, Integer> progress = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_PROGRESS, null, null, null, null, null, null);
        
        int totalQuestions = 0;
        int completedQuestions = 0;
        int correctAnswers = 0;
        
        if (cursor.moveToFirst()) {
            do {
                totalQuestions += cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_QUESTIONS));
                completedQuestions += cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS));
                correctAnswers += cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORRECT_ANSWERS));
            } while (cursor.moveToNext());
        }
        
        progress.put("total", totalQuestions);
        progress.put("completed", completedQuestions);
        progress.put("correct", correctAnswers);
        
        cursor.close();
        db.close();
        return progress;
    }

    // Xóa tất cả tiến độ
    public void resetAllProgress() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_COMPLETED_QUESTIONS, 0);
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWERS, 0);
        
        db.update(DatabaseHelper.TABLE_PROGRESS, values, null, null);
        db.close();
    }

    // Đóng database
    public void close() {
        dbHelper.close();
    }
}
