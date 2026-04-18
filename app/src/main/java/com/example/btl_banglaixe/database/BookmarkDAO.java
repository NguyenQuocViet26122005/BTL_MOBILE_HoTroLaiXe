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

    // Thêm bookmark
    public boolean addBookmark(int questionId) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_BOOKMARK_QUESTION_ID, questionId);
            
            long result = db.insert(TABLE_BOOKMARKS, null, values);
            android.util.Log.d("BookmarkDAO", "addBookmark: questionId=" + questionId + ", result=" + result);
            return result != -1;
        } catch (Exception e) {
            android.util.Log.e("BookmarkDAO", "Error adding bookmark", e);
            return false;
        }
    }

    // Xóa bookmark
    public boolean removeBookmark(int questionId) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int result = db.delete(TABLE_BOOKMARKS, 
                COLUMN_BOOKMARK_QUESTION_ID + " = ?", 
                new String[]{String.valueOf(questionId)});
            android.util.Log.d("BookmarkDAO", "removeBookmark: questionId=" + questionId + ", result=" + result);
            return result > 0;
        } catch (Exception e) {
            android.util.Log.e("BookmarkDAO", "Error removing bookmark", e);
            return false;
        }
    }

    // Kiểm tra câu hỏi đã được bookmark chưa
    public boolean isBookmarked(int questionId) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_BOOKMARKS, 
                new String[]{COLUMN_BOOKMARK_ID},
                COLUMN_BOOKMARK_QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionId)},
                null, null, null);
            
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            android.util.Log.d("BookmarkDAO", "isBookmarked: questionId=" + questionId + ", exists=" + exists);
            return exists;
        } catch (Exception e) {
            android.util.Log.e("BookmarkDAO", "Error checking bookmark", e);
            return false;
        }
    }

    // Lấy tất cả câu hỏi đã bookmark
    public List<Question> getBookmarkedQuestions() {
        List<Question> bookmarkedQuestions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Join bookmarks với questions
        String query = "SELECT q.* FROM " + TABLE_QUESTIONS + " q " +
                      "INNER JOIN " + TABLE_BOOKMARKS + " b " +
                      "ON q." + COLUMN_ID + " = b." + COLUMN_BOOKMARK_QUESTION_ID + " " +
                      "ORDER BY b." + COLUMN_BOOKMARK_TIMESTAMP + " DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                bookmarkedQuestions.add(questionDAO.cursorToQuestion(cursor));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return bookmarkedQuestions;
    }

    // Lấy danh sách ID các câu đã bookmark
    public Set<Integer> getBookmarkedQuestionIds() {
        Set<Integer> ids = new HashSet<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_BOOKMARKS,
            new String[]{COLUMN_BOOKMARK_QUESTION_ID},
            null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return ids;
    }

    // Đếm số câu đã bookmark
    public int getBookmarkCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKMARKS, null);
        
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        
        cursor.close();
        return count;
    }

    public void close() {
        dbHelper.close();
    }
}
