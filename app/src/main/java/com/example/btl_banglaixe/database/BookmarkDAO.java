package com.example.btl_banglaixe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_banglaixe.models.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.btl_banglaixe.database.DatabaseHelper.*;

public class BookmarkDAO {
    private DatabaseHelper dbHelper;
    private QuestionDAO questionDAO;

    public BookmarkDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        questionDAO = new QuestionDAO(context);
    }

    public boolean addBookmark(int questionId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKMARK_QUESTION_ID, questionId);
        return db.insert(TABLE_BOOKMARKS, null, values) != -1;
    }

    public boolean removeBookmark(int questionId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TABLE_BOOKMARKS, COLUMN_BOOKMARK_QUESTION_ID + " = ?", 
            new String[]{String.valueOf(questionId)}) > 0;
    }

    public boolean isBookmarked(int questionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARKS, new String[]{COLUMN_BOOKMARK_ID},
            COLUMN_BOOKMARK_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)},
            null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Question> getBookmarkedQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT q.* FROM " + TABLE_QUESTIONS + " q " +
                      "INNER JOIN " + TABLE_BOOKMARKS + " b " +
                      "ON q." + COLUMN_ID + " = b." + COLUMN_BOOKMARK_QUESTION_ID + " " +
                      "ORDER BY b." + COLUMN_BOOKMARK_TIMESTAMP + " DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            questions.add(questionDAO.cursorToQuestion(cursor));
        }
        cursor.close();
        return questions;
    }

    public Set<Integer> getBookmarkedQuestionIds() {
        Set<Integer> ids = new HashSet<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARKS, new String[]{COLUMN_BOOKMARK_QUESTION_ID},
            null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(0));
        }
        cursor.close();
        return ids;
    }

    public int getBookmarkCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKMARKS, null);
        int count = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    public void close() {
        dbHelper.close();
    }
}
