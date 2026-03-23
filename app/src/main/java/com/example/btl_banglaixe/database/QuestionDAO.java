package com.example.btl_banglaixe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_banglaixe.models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private DatabaseHelper dbHelper;

    public QuestionDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm câu hỏi mới
    public long addQuestion(Question question) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_QUESTION_TEXT, question.getQuestionText());
        values.put(DatabaseHelper.COLUMN_OPTION_A, question.getOptionA());
        values.put(DatabaseHelper.COLUMN_OPTION_B, question.getOptionB());
        values.put(DatabaseHelper.COLUMN_OPTION_C, question.getOptionC());
        values.put(DatabaseHelper.COLUMN_OPTION_D, question.getOptionD());
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWER, question.getCorrectAnswer());
        values.put(DatabaseHelper.COLUMN_CATEGORY, question.getCategory());
        values.put(DatabaseHelper.COLUMN_IS_CRITICAL, question.isCritical() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, question.getImagePath());
        values.put(DatabaseHelper.COLUMN_EXPLANATION, question.getExplanation());

        long id = db.insert(DatabaseHelper.TABLE_QUESTIONS, null, values);
        db.close();
        return id;
    }

    // Lấy tất cả câu hỏi
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_QUESTIONS, null, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Question question = cursorToQuestion(cursor);
                questions.add(question);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return questions;
    }

    // Lấy câu hỏi theo chủ đề
    public List<Question> getQuestionsByCategory(String category) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = DatabaseHelper.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_QUESTIONS, null, selection, 
                                selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Question question = cursorToQuestion(cursor);
                questions.add(question);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return questions;
    }

    // Lấy câu hỏi điểm liệt
    public List<Question> getCriticalQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = DatabaseHelper.COLUMN_IS_CRITICAL + " = ?";
        String[] selectionArgs = {"1"};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_QUESTIONS, null, selection, 
                                selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Question question = cursorToQuestion(cursor);
                questions.add(question);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return questions;
    }

    // Lấy câu hỏi theo ID
    public Question getQuestionById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_QUESTIONS, null, selection, 
                                selectionArgs, null, null, null);
        
        Question question = null;
        if (cursor.moveToFirst()) {
            question = cursorToQuestion(cursor);
        }
        
        cursor.close();
        db.close();
        return question;
    }

    // Cập nhật câu hỏi
    public int updateQuestion(Question question) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COLUMN_QUESTION_TEXT, question.getQuestionText());
        values.put(DatabaseHelper.COLUMN_OPTION_A, question.getOptionA());
        values.put(DatabaseHelper.COLUMN_OPTION_B, question.getOptionB());
        values.put(DatabaseHelper.COLUMN_OPTION_C, question.getOptionC());
        values.put(DatabaseHelper.COLUMN_OPTION_D, question.getOptionD());
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWER, question.getCorrectAnswer());
        values.put(DatabaseHelper.COLUMN_CATEGORY, question.getCategory());
        values.put(DatabaseHelper.COLUMN_IS_CRITICAL, question.isCritical() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, question.getImagePath());
        values.put(DatabaseHelper.COLUMN_EXPLANATION, question.getExplanation());

        String whereClause = DatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(question.getId())};
        
        int rowsAffected = db.update(DatabaseHelper.TABLE_QUESTIONS, values, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }

    // Xóa câu hỏi
    public void deleteQuestion(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        String whereClause = DatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        
        db.delete(DatabaseHelper.TABLE_QUESTIONS, whereClause, whereArgs);
        db.close();
    }

    // Đếm số câu hỏi theo chủ đề
    public int getQuestionCountByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = DatabaseHelper.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_QUESTIONS, null, selection, 
                                selectionArgs, null, null, null);
        
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Chuyển đổi Cursor thành Question object
    private Question cursorToQuestion(Cursor cursor) {
        Question question = new Question();
        
        question.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        question.setQuestionText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUESTION_TEXT)));
        question.setOptionA(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OPTION_A)));
        question.setOptionB(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OPTION_B)));
        question.setOptionC(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OPTION_C)));
        question.setOptionD(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OPTION_D)));
        question.setCorrectAnswer(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORRECT_ANSWER)));
        question.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)));
        question.setCritical(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_CRITICAL)) == 1);
        question.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_PATH)));
        question.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPLANATION)));
        
        return question;
    }

    // Đóng database
    public void close() {
        dbHelper.close();
    }
}
