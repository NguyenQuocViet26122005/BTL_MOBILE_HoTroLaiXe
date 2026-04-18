package com.example.btl_banglaixe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExamResultDAO {
    private DatabaseHelper dbHelper;

    public ExamResultDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void saveExamResult(int examId, String licenseType, boolean passed, int correctCount, int wrongCount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DatabaseHelper.COLUMN_EXAM_ID, examId);
        v.put(DatabaseHelper.COLUMN_LICENSE_TYPE, licenseType);
        v.put(DatabaseHelper.COLUMN_PASSED, passed ? 1 : 0);
        v.put(DatabaseHelper.COLUMN_CORRECT_COUNT, correctCount);
        v.put(DatabaseHelper.COLUMN_WRONG_COUNT, wrongCount);
        db.replace(DatabaseHelper.TABLE_EXAM_RESULTS, null, v);
    }

    public ExamResult getExamResult(int examId, String licenseType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_EXAM_RESULTS, null,
                DatabaseHelper.COLUMN_EXAM_ID + " = ? AND " + DatabaseHelper.COLUMN_LICENSE_TYPE + " = ?",
                new String[]{String.valueOf(examId), licenseType}, null, null, null);
        
        ExamResult result = null;
        if (c.moveToFirst()) {
            result = new ExamResult(
                c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXAM_ID)),
                c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LICENSE_TYPE)),
                c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSED)) == 1,
                c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORRECT_COUNT)),
                c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WRONG_COUNT))
            );
        }
        c.close();
        return result;
    }

    public void close() {
        dbHelper.close();
    }

    public static class ExamResult {
        public int examId;
        public String licenseType;
        public boolean passed;
        public int correctCount;
        public int wrongCount;

        public ExamResult(int examId, String licenseType, boolean passed, int correctCount, int wrongCount) {
            this.examId = examId;
            this.licenseType = licenseType;
            this.passed = passed;
            this.correctCount = correctCount;
            this.wrongCount = wrongCount;
        }
    }
}
