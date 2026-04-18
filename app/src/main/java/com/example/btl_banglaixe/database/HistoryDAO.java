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

    // Lưu lịch sử trả lời
    public boolean saveAnswer(int questionId, String userAnswer, String correctAnswer) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUESTION_ID, questionId);
            values.put(COLUMN_USER_ANSWER, userAnswer);
            values.put(COLUMN_IS_CORRECT, userAnswer.equals(correctAnswer) ? 1 : 0);
            
            long result = db.insert(TABLE_HISTORY, null, values);
            android.util.Log.d("HistoryDAO", "saveAnswer: questionId=" + questionId + 
                ", userAnswer=" + userAnswer + ", correct=" + correctAnswer + ", result=" + result);
            return result != -1;
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error saving answer", e);
            return false;
        }
    }

    // Kiểm tra câu hỏi đã được trả lời chưa
    public boolean isAnswered(int questionId) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_HISTORY,
                new String[]{COLUMN_HISTORY_ID},
                COLUMN_QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionId)},
                null, null, null);
            
            boolean answered = cursor.getCount() > 0;
            cursor.close();
            return answered;
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error checking if answered", e);
            return false;
        }
    }

    // Lấy câu trả lời cuối cùng của user cho câu hỏi
    public String getLastAnswer(int questionId) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_HISTORY,
                new String[]{COLUMN_USER_ANSWER},
                COLUMN_QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionId)},
                null, null,
                COLUMN_TIMESTAMP + " DESC",
                "1");
            
            String answer = null;
            if (cursor.moveToFirst()) {
                answer = cursor.getString(0);
            }
            cursor.close();
            return answer;
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error getting last answer", e);
            return null;
        }
    }

    // Đếm số câu đã trả lời
    public int getAnsweredCount() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                "SELECT COUNT(DISTINCT " + COLUMN_QUESTION_ID + ") FROM " + TABLE_HISTORY,
                null);
            
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            return count;
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error getting answered count", e);
            return 0;
        }
    }

    // Đếm số câu trả lời đúng
    public int getCorrectCount() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            // Lấy câu trả lời cuối cùng của mỗi câu hỏi và đếm số câu đúng
            String query = "SELECT COUNT(*) FROM (" +
                "SELECT " + COLUMN_QUESTION_ID + ", " + COLUMN_IS_CORRECT + " " +
                "FROM " + TABLE_HISTORY + " " +
                "WHERE " + COLUMN_HISTORY_ID + " IN (" +
                    "SELECT MAX(" + COLUMN_HISTORY_ID + ") " +
                    "FROM " + TABLE_HISTORY + " " +
                    "GROUP BY " + COLUMN_QUESTION_ID +
                ") AND " + COLUMN_IS_CORRECT + " = 1" +
            ")";
            
            Cursor cursor = db.rawQuery(query, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            return count;
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error getting correct count", e);
            return 0;
        }
    }

    // Lấy danh sách ID các câu đã trả lời
    public Set<Integer> getAnsweredQuestionIds() {
        Set<Integer> ids = new HashSet<>();
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                "SELECT DISTINCT " + COLUMN_QUESTION_ID + " FROM " + TABLE_HISTORY,
                null);
            
            if (cursor.moveToFirst()) {
                do {
                    ids.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error getting answered question IDs", e);
        }
        return ids;
    }

    // Đếm số câu đã trả lời theo category
    public int getAnsweredCountByCategory(String category) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String query = "SELECT COUNT(DISTINCT h." + COLUMN_QUESTION_ID + ") " +
                "FROM " + TABLE_HISTORY + " h " +
                "INNER JOIN " + TABLE_QUESTIONS + " q " +
                "ON h." + COLUMN_QUESTION_ID + " = q." + COLUMN_ID + " " +
                "WHERE q." + COLUMN_CATEGORY + " = ?";
            
            Cursor cursor = db.rawQuery(query, new String[]{category});
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            return count;
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error getting answered count by category", e);
            return 0;
        }
    }

    // Xóa tất cả lịch sử
    public void clearHistory() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_HISTORY, null, null);
        } catch (Exception e) {
            android.util.Log.e("HistoryDAO", "Error clearing history", e);
        }
    }

    public void close() {
        dbHelper.close();
    }
}
