package com.example.btl_banglaixe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.btl_banglaixe.database.HistoryDAO;
import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.utils.JsonImporter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before super.onCreate
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Xử lý system bars (status bar, navigation bar)
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            (v, insets) -> {
                androidx.core.graphics.Insets systemBars = insets.getInsets(
                    androidx.core.view.WindowInsetsCompat.Type.systemBars()
                );
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        );

        // Import câu hỏi lần đầu tiên
        importQuestionsFirstTime();

        setupListeners();
        updateLicenseType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLicenseType();
        updateProgress();
    }

    private void importQuestionsFirstTime() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        
        if (!prefs.getBoolean("questions_imported", false)) {
            int count = JsonImporter.importQuestionsFromAssets(this, "questions_hang_a.json");
            
            if (count > 0) {
                prefs.edit().putBoolean("questions_imported", true).apply();
                Toast.makeText(this, "Đã import " + count + " câu hỏi!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupListeners() {
        // Nút Cài đặt
        findViewById(R.id.btnSettings).setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class))
        );

        // Quick Actions
        findViewById(R.id.cardStudy).setOnClickListener(v -> 
            openQuestionActivity(null) // null = tất cả 250 câu
        );
        
        findViewById(R.id.cardSigns).setOnClickListener(v -> 
            openQuestionActivity("Biển báo đường bộ")
        );
        
        findViewById(R.id.cardExam).setOnClickListener(v -> 
            startActivity(new Intent(this, ExamActivity.class))
        );
        
        findViewById(R.id.cardBookmark).setOnClickListener(v -> 
            openQuestionActivity("bookmarked")
        );

        // Danh mục học tập - 5 chương theo PDF
        setupCategoryCard(R.id.cardCriticalQuestions, "critical"); // Câu điểm liệt
        setupCategoryCard(R.id.cardConceptsRules, "Quy định chung và quy tắc giao thông đường bộ"); // Chương 1: 1-100
        setupCategoryCard(R.id.cardCultureEthics, "Văn hóa giao thông, đạo đức người lái xe"); // Chương 2: 101-110
        setupCategoryCard(R.id.cardDrivingTechniques, "Kỹ thuật lái xe"); // Chương 3: 111-125
        setupCategoryCard(R.id.cardTrafficSigns, "Biển báo đường bộ"); // Chương 4: 126-215
        setupCategoryCard(R.id.cardSituations, "Sa hình"); // Chương 5: 216-250
    }

    private void setupCategoryCard(int cardId, String category) {
        findViewById(cardId).setOnClickListener(v -> openQuestionActivity(category));
    }

    private void openQuestionActivity(String category) {
        Intent intent = new Intent(this, QuestionActivity.class);
        if (category != null) {
            intent.putExtra("category", category);
        }
        startActivity(intent);
    }

    private void updateLicenseType() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String licenseType = prefs.getString("license_type", "A1");
        
        android.widget.TextView tvLicenseType = findViewById(R.id.tvLicenseType);
        tvLicenseType.setText("Hạng " + licenseType);
    }

    private void updateProgress() {
        HistoryDAO historyDAO = new HistoryDAO(this);
        QuestionDAO questionDAO = new QuestionDAO(this);
        
        int totalQuestions = questionDAO.getAllQuestions().size();
        int answeredCount = historyDAO.getAnsweredCount();
        int correctCount = historyDAO.getCorrectCount();
        
        // Tính phần trăm
        int progressPercent = totalQuestions > 0 ? (answeredCount * 100 / totalQuestions) : 0;
        int accuracyPercent = answeredCount > 0 ? (correctCount * 100 / answeredCount) : 0;
        
        // Cập nhật UI
        android.widget.TextView tvProgress = findViewById(R.id.tvProgress);
        android.widget.TextView tvAccuracy = findViewById(R.id.tvAccuracy);
        android.widget.ProgressBar progressBar = findViewById(R.id.progressBar);
        
        if (tvProgress != null) {
            tvProgress.setText(answeredCount + "/" + totalQuestions + " câu (" + progressPercent + "%)");
        }
        
        if (tvAccuracy != null) {
            tvAccuracy.setText("Độ chính xác: " + accuracyPercent + "% (" + correctCount + "/" + answeredCount + " đúng)");
        }
        
        if (progressBar != null) {
            progressBar.setMax(totalQuestions);
            progressBar.setProgress(answeredCount);
        }
        
        historyDAO.close();
        questionDAO.close();
    }
}
