package com.example.btl_banglaixe.database;

import static com.example.btl_banglaixe.database.DatabaseHelper.*;

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
        ContentValues values = createContentValues(question);
        long id = db.insert(TABLE_QUESTIONS, null, values);
        db.close();
        return id;
    }

    // Cập nhật câu hỏi
    public int updateQuestion(Question question) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = createContentValues(question);
        
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(question.getId())};
        
        int rowsAffected = db.update(TABLE_QUESTIONS, values, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }

    // Tạo ContentValues từ Question object
    private ContentValues createContentValues(Question question) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_TEXT, question.getQuestionText());
        values.put(COLUMN_OPTION_A, question.getOptionA());
        values.put(COLUMN_OPTION_B, question.getOptionB());
        values.put(COLUMN_OPTION_C, question.getOptionC());
        values.put(COLUMN_OPTION_D, question.getOptionD());
        values.put(COLUMN_CORRECT_ANSWER, question.getCorrectAnswer());
        values.put(COLUMN_CATEGORY, question.getCategory());
        values.put(COLUMN_IS_CRITICAL, question.isCritical() ? 1 : 0);
        values.put(COLUMN_IMAGE_PATH, question.getImagePath());
        values.put(COLUMN_EXPLANATION, question.getExplanation());
        return values;
    }

    // Lấy tất cả câu hỏi
    public List<Question> getAllQuestions() {
        return getQuestions(null, null);
    }

    // Lấy câu hỏi theo chủ đề
    public List<Question> getQuestionsByCategory(String category) {
        return getQuestions(COLUMN_CATEGORY + " = ?", new String[]{category});
    }

    // Lấy câu hỏi điểm liệt
    public List<Question> getCriticalQuestions() {
        return getQuestions(COLUMN_IS_CRITICAL + " = ?", new String[]{"1"});
    }

    // Method chung để lấy câu hỏi
    private List<Question> getQuestions(String selection, String[] selectionArgs) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_QUESTIONS, null, selection, selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                questions.add(cursorToQuestion(cursor));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return questions;
    }

    // Lấy câu hỏi theo ID
    public Question getQuestionById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        
        Cursor cursor = db.query(TABLE_QUESTIONS, null, selection, selectionArgs, null, null, null);
        
        Question question = null;
        if (cursor.moveToFirst()) {
            question = cursorToQuestion(cursor);
        }
        
        cursor.close();
        db.close();
        return question;
    }

    // Xóa câu hỏi
    public void deleteQuestion(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Đếm số câu hỏi theo chủ đề
    public int getQuestionCountByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, COLUMN_CATEGORY + " = ?", 
                                new String[]{category}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Chuyển đổi Cursor thành Question object
    public Question cursorToQuestion(Cursor cursor) {
        Question question = new Question();
        question.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        question.setQuestionText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT)));
        question.setOptionA(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_A)));
        question.setOptionB(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_B)));
        question.setOptionC(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_C)));
        question.setOptionD(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_D)));
        question.setCorrectAnswer(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER)));
        question.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
        question.setCritical(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CRITICAL)) == 1);
        question.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
        question.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPLANATION)));
        return question;
    }

    // Đóng database
    public void close() {
        dbHelper.close();
    }
}
