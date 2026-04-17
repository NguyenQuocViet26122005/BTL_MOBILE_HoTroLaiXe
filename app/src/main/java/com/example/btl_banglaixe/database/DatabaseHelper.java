package com.example.btl_banglaixe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "driving_license.db";
    private static final int DATABASE_VERSION = 1;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng câu hỏi
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUESTION_TEXT + " TEXT NOT NULL,"
                + COLUMN_OPTION_A + " TEXT NOT NULL,"
                + COLUMN_OPTION_B + " TEXT NOT NULL,"
                + COLUMN_OPTION_C + " TEXT,"
                + COLUMN_OPTION_D + " TEXT,"
                + COLUMN_CORRECT_ANSWER + " TEXT NOT NULL,"
                + COLUMN_CATEGORY + " TEXT NOT NULL,"
                + COLUMN_IS_CRITICAL + " INTEGER DEFAULT 0,"
                + COLUMN_IMAGE_PATH + " TEXT,"
                + COLUMN_EXPLANATION + " TEXT"
                + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        // Tạo bảng lịch sử
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUESTION_ID + " INTEGER NOT NULL,"
                + COLUMN_USER_ANSWER + " TEXT,"
                + COLUMN_IS_CORRECT + " INTEGER,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_QUESTION_ID + ") REFERENCES " 
                + TABLE_QUESTIONS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_HISTORY_TABLE);

        // Tạo bảng tiến độ
        String CREATE_PROGRESS_TABLE = "CREATE TABLE " + TABLE_PROGRESS + "("
                + COLUMN_PROGRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATEGORY_NAME + " TEXT NOT NULL UNIQUE,"
                + COLUMN_TOTAL_QUESTIONS + " INTEGER DEFAULT 0,"
                + COLUMN_COMPLETED_QUESTIONS + " INTEGER DEFAULT 0,"
                + COLUMN_CORRECT_ANSWERS + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_PROGRESS_TABLE);

        // Thêm dữ liệu mẫu
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Thêm các chủ đề vào bảng tiến độ
        String[] categories = {
            "Câu hỏi điểm liệt", "Khái niệm và quy tắc", "Văn hóa và đạo đức",
            "Kỹ thuật lái xe", "Biển báo đường bộ", "Sa hình"
        };
        int[] questionCounts = {20, 100, 10, 15, 65, 35};
        
        for (int i = 0; i < categories.length; i++) {
            db.execSQL("INSERT INTO " + TABLE_PROGRESS + " (" + COLUMN_CATEGORY_NAME + ", " 
                    + COLUMN_TOTAL_QUESTIONS + ") VALUES (?, ?)", 
                    new Object[]{categories[i], questionCounts[i]});
        }

        // Thêm câu hỏi mẫu
        insertSampleQuestion(db, 
            "Khái niệm 'Người tham gia giao thông đường bộ' được hiểu như thế nào?",
            "Là người điều khiển phương tiện tham gia giao thông đường bộ",
            "Là người đi bộ tham gia giao thông đường bộ",
            "Là người điều khiển, người sử dụng phương tiện và người đi bộ tham gia giao thông đường bộ",
            null, "C", "Khái niệm và quy tắc", 1, null,
            "Người tham gia giao thông bao gồm cả người điều khiển phương tiện, người sử dụng phương tiện và người đi bộ."
        );

        insertSampleQuestion(db,
            "Người điều khiển phương tiện giao thông đường bộ mà trong cơ thể có chất ma túy có bị nghiêm cấm hay không?",
            "Bị nghiêm cấm", "Không bị nghiêm cấm",
            "Không bị nghiêm cấm nếu có chất ma túy ở mức nhẹ",
            null, "A", "Câu hỏi điểm liệt", 1, null,
            "Đây là câu hỏi điểm liệt. Tuyệt đối nghiêm cấm điều khiển phương tiện khi có chất ma túy trong cơ thể."
        );
    }

    private void insertSampleQuestion(SQLiteDatabase db, String question, String optionA, 
                                     String optionB, String optionC, String optionD,
                                     String correctAnswer, String category, int isCritical,
                                     String imagePath, String explanation) {
        String sql = "INSERT INTO " + TABLE_QUESTIONS + " ("
                + COLUMN_QUESTION_TEXT + ", "
                + COLUMN_OPTION_A + ", "
                + COLUMN_OPTION_B + ", "
                + COLUMN_OPTION_C + ", "
                + COLUMN_OPTION_D + ", "
                + COLUMN_CORRECT_ANSWER + ", "
                + COLUMN_CATEGORY + ", "
                + COLUMN_IS_CRITICAL + ", "
                + COLUMN_IMAGE_PATH + ", "
                + COLUMN_EXPLANATION
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        db.execSQL(sql, new Object[]{question, optionA, optionB, optionC, optionD, 
                                     correctAnswer, category, isCritical, imagePath, explanation});
    }
}
