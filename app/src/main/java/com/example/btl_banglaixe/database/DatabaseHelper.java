package com.example.btl_banglaixe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "questions.db";
    private static final int DATABASE_VERSION = 3;
    
    private final Context context;

    // Bảng câu hỏi
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION_TEXT = "question_text";
    public static final String COLUMN_OPTION_A = "option_a";
    public static final String COLUMN_OPTION_B = "option_b";
    public static final String COLUMN_OPTION_C = "option_c";
    public static final String COLUMN_OPTION_D = "option_d";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IS_CRITICAL = "is_critical";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_EXPLANATION = "explanation";

    // Bảng lịch sử làm bài
    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_HISTORY_ID = "history_id";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_USER_ANSWER = "user_answer";
    public static final String COLUMN_IS_CORRECT = "is_correct";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Bảng tiến độ
    public static final String TABLE_PROGRESS = "progress";
    public static final String COLUMN_PROGRESS_ID = "progress_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_TOTAL_QUESTIONS = "total_questions";
    public static final String COLUMN_COMPLETED_QUESTIONS = "completed_questions";
    public static final String COLUMN_CORRECT_ANSWERS = "correct_answers";

    // Bảng đánh dấu
    public static final String TABLE_BOOKMARKS = "bookmarks";
    public static final String COLUMN_BOOKMARK_ID = "bookmark_id";
    public static final String COLUMN_BOOKMARK_QUESTION_ID = "question_id";
    public static final String COLUMN_BOOKMARK_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabase();
    }

    private void copyDatabase() {
        try {
            String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
            File dbFile = new File(dbPath);
            
            if (!dbFile.exists()) {
                File dbDir = dbFile.getParentFile();
                if (dbDir != null && !dbDir.exists()) {
                    dbDir.mkdirs();
                }
                
                InputStream input = context.getAssets().open("databases/" + DATABASE_NAME);
                OutputStream output = new FileOutputStream(dbPath);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                
                output.flush();
                output.close();
                input.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error copying database", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            createBookmarksTable(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        createBookmarksTable(db);
    }

    private void createTables(SQLiteDatabase db) {
        // Tạo bảng lịch sử
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "("
                + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUESTION_ID + " INTEGER NOT NULL,"
                + COLUMN_USER_ANSWER + " TEXT,"
                + COLUMN_IS_CORRECT + " INTEGER,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_QUESTION_ID + ") REFERENCES " 
                + TABLE_QUESTIONS + "(" + COLUMN_ID + ")"
                + ")");

        // Tạo bảng tiến độ
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PROGRESS + "("
                + COLUMN_PROGRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATEGORY_NAME + " TEXT NOT NULL UNIQUE,"
                + COLUMN_TOTAL_QUESTIONS + " INTEGER DEFAULT 0,"
                + COLUMN_COMPLETED_QUESTIONS + " INTEGER DEFAULT 0,"
                + COLUMN_CORRECT_ANSWERS + " INTEGER DEFAULT 0"
                + ")");

        // Tạo bảng đánh dấu
        createBookmarksTable(db);
    }

    private void createBookmarksTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARKS + "("
                + COLUMN_BOOKMARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BOOKMARK_QUESTION_ID + " INTEGER NOT NULL UNIQUE,"
                + COLUMN_BOOKMARK_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_BOOKMARK_QUESTION_ID + ") REFERENCES " 
                + TABLE_QUESTIONS + "(" + COLUMN_ID + ")"
                + ")");
    }
}
