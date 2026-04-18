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

    public long addQuestion(Question question) {
        return dbHelper.getWritableDatabase().insert(TABLE_QUESTIONS, null, createContentValues(question));
    }

    public int updateQuestion(Question question) {
        return dbHelper.getWritableDatabase().update(TABLE_QUESTIONS, createContentValues(question),
            COLUMN_ID + " = ?", new String[]{String.valueOf(question.getId())});
    }

    private ContentValues createContentValues(Question q) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_TEXT, q.getQuestionText());
        values.put(COLUMN_OPTION_A, q.getOptionA());
        values.put(COLUMN_OPTION_B, q.getOptionB());
        values.put(COLUMN_OPTION_C, q.getOptionC());
        values.put(COLUMN_OPTION_D, q.getOptionD());
        values.put(COLUMN_CORRECT_ANSWER, q.getCorrectAnswer());
        values.put(COLUMN_CATEGORY, q.getCategory());
        values.put(COLUMN_IS_CRITICAL, q.isCritical() ? 1 : 0);
        values.put(COLUMN_IMAGE_PATH, q.getImagePath());
        values.put(COLUMN_EXPLANATION, q.getExplanation());
        return values;
    }

    public List<Question> getAllQuestions() {
        return getQuestions(null, null);
    }

    public List<Question> getQuestionsByCategory(String category) {
        return getQuestions(COLUMN_CATEGORY + " = ?", new String[]{category});
    }

    public List<Question> getCriticalQuestions() {
        return getQuestions(COLUMN_IS_CRITICAL + " = ?", new String[]{"1"});
    }

    private List<Question> getQuestions(String selection, String[] selectionArgs) {
        List<Question> questions = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().query(TABLE_QUESTIONS, null, 
            selection, selectionArgs, null, null, null);
        
        while (cursor.moveToNext()) {
            questions.add(cursorToQuestion(cursor));
        }
        cursor.close();
        return questions;
    }

    public Question getQuestionById(int id) {
        Cursor cursor = dbHelper.getReadableDatabase().query(TABLE_QUESTIONS, null,
            COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        
        Question question = cursor.moveToFirst() ? cursorToQuestion(cursor) : null;
        cursor.close();
        return question;
    }

    public void deleteQuestion(int id) {
        dbHelper.getWritableDatabase().delete(TABLE_QUESTIONS, COLUMN_ID + " = ?", 
            new String[]{String.valueOf(id)});
    }

    public int getQuestionCountByCategory(String category) {
        Cursor cursor = dbHelper.getReadableDatabase().query(TABLE_QUESTIONS, null,
            COLUMN_CATEGORY + " = ?", new String[]{category}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Question cursorToQuestion(Cursor c) {
        Question q = new Question();
        q.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
        q.setQuestionText(c.getString(c.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT)));
        q.setOptionA(c.getString(c.getColumnIndexOrThrow(COLUMN_OPTION_A)));
        q.setOptionB(c.getString(c.getColumnIndexOrThrow(COLUMN_OPTION_B)));
        q.setOptionC(c.getString(c.getColumnIndexOrThrow(COLUMN_OPTION_C)));
        q.setOptionD(c.getString(c.getColumnIndexOrThrow(COLUMN_OPTION_D)));
        q.setCorrectAnswer(c.getString(c.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER)));
        q.setCategory(c.getString(c.getColumnIndexOrThrow(COLUMN_CATEGORY)));
        q.setCritical(c.getInt(c.getColumnIndexOrThrow(COLUMN_IS_CRITICAL)) == 1);
        q.setImagePath(c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
        q.setExplanation(c.getString(c.getColumnIndexOrThrow(COLUMN_EXPLANATION)));
        return q;
    }

    public void close() {
        dbHelper.close();
    }
}
