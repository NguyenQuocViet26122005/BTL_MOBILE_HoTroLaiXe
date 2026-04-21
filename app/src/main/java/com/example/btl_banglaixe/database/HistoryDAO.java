package com.example.btl_banglaixe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;
import java.util.Set;

import static com.example.btl_banglaixe.database.DatabaseHelper.*;

public class HistoryDAO {
    private DatabaseHelper dbHelper;

    public HistoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean saveAnswer(int questionId, String userAnswer, String correctAnswer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_ID, questionId);
        values.put(COLUMN_USER_ANSWER, userAnswer);
        values.put(COLUMN_IS_CORRECT, userAnswer.equals(correctAnswer) ? 1 : 0);
        return db.insert(TABLE_HISTORY, null, values) != -1;
    }

    public boolean isAnswered(int questionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, new String[]{COLUMN_HISTORY_ID},
            COLUMN_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)},
            null, null, null);
        boolean answered = cursor.getCount() > 0;
        cursor.close();
        return answered;
    }

    public String getLastAnswer(int questionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, new String[]{COLUMN_USER_ANSWER},
            COLUMN_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)},
            null, null, COLUMN_TIMESTAMP + " DESC", "1");
        
        String answer = cursor.moveToFirst() ? cursor.getString(0) : null;
        cursor.close();
        return answer;
    }

    public Boolean getLastAnswerResult(int questionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, new String[]{COLUMN_IS_CORRECT},
            COLUMN_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)},
            null, null, COLUMN_TIMESTAMP + " DESC", "1");
        
        Boolean result = null;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0) == 1;
        }
        cursor.close();
        return result;
    }

    public int getAnsweredCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT COUNT(DISTINCT " + COLUMN_QUESTION_ID + ") FROM " + TABLE_HISTORY, null);
        int count = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    public int getCorrectCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM (" +
            "SELECT " + COLUMN_QUESTION_ID + ", " + COLUMN_IS_CORRECT + " " +
            "FROM " + TABLE_HISTORY + " " +
            "WHERE " + COLUMN_HISTORY_ID + " IN (" +
                "SELECT MAX(" + COLUMN_HISTORY_ID + ") " +
                "FROM " + TABLE_HISTORY + " " +
                "GROUP BY " + COLUMN_QUESTION_ID +
            ") AND " + COLUMN_IS_CORRECT + " = 1)";
        
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    public Set<Integer> getAnsweredQuestionIds() {
        Set<Integer> ids = new HashSet<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT DISTINCT " + COLUMN_QUESTION_ID + " FROM " + TABLE_HISTORY, null);
        
        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(0));
        }
        cursor.close();
        return ids;
    }

    public int getAnsweredCountByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(DISTINCT h." + COLUMN_QUESTION_ID + ") " +
            "FROM " + TABLE_HISTORY + " h " +
            "INNER JOIN " + TABLE_QUESTIONS + " q " +
            "ON h." + COLUMN_QUESTION_ID + " = q." + COLUMN_ID + " " +
            "WHERE q." + COLUMN_CATEGORY + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{category});
        int count = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    public void clearHistory() {
        dbHelper.getWritableDatabase().delete(TABLE_HISTORY, null, null);
    }

    public Set<Integer> getWrongQuestionIds() {
        Set<Integer> ids = new HashSet<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + COLUMN_QUESTION_ID + " " +
            "FROM " + TABLE_HISTORY + " " +
            "WHERE " + COLUMN_HISTORY_ID + " IN (" +
                "SELECT MAX(" + COLUMN_HISTORY_ID + ") " +
                "FROM " + TABLE_HISTORY + " " +
                "GROUP BY " + COLUMN_QUESTION_ID +
            ") AND " + COLUMN_IS_CORRECT + " = 0";
        
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(0));
        }
        cursor.close();
        return ids;
    }

    public void close() {
        dbHelper.close();
    }
}
