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
        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWindowInsets();
        importQuestionsFirstTime();
        setupListeners();
        updateLicenseType();
    }

    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int mode = prefs.getBoolean("dark_mode", false) ? 
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void setupWindowInsets() {
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(
                androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLicenseType();
        updateProgress();
    }

    private void importQuestionsFirstTime() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        if (prefs.getBoolean("questions_imported", false)) return;
        
        int count = JsonImporter.importQuestionsFromAssets(this, "questions_hang_a.json");
        if (count > 0) {
            prefs.edit().putBoolean("questions_imported", true).apply();
            Toast.makeText(this, "Đã import " + count + " câu hỏi!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        findViewById(R.id.btnSettings).setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));

        findViewById(R.id.cardStudy).setOnClickListener(v -> openQuestionActivity(null));
        findViewById(R.id.cardSigns).setOnClickListener(v -> openQuestionActivity("Biển báo đường bộ"));
        findViewById(R.id.cardExam).setOnClickListener(v -> startActivity(new Intent(this, ExamActivity.class)));
        findViewById(R.id.cardBookmark).setOnClickListener(v -> openQuestionActivity("bookmarked"));

        setupCategoryCard(R.id.cardCriticalQuestions, "critical");
        setupCategoryCard(R.id.cardConceptsRules, "Quy định chung và quy tắc giao thông đường bộ");
        setupCategoryCard(R.id.cardCultureEthics, "Văn hóa giao thông, đạo đức người lái xe");
        setupCategoryCard(R.id.cardDrivingTechniques, "Kỹ thuật lái xe");
        setupCategoryCard(R.id.cardTrafficSigns, "Biển báo đường bộ");
        setupCategoryCard(R.id.cardSituations, "Sa hình");
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
        
        int total = questionDAO.getAllQuestions().size();
        int answered = historyDAO.getAnsweredCount();
        int correct = historyDAO.getCorrectCount();
        
        int progressPercent = total > 0 ? (answered * 100 / total) : 0;
        int accuracyPercent = answered > 0 ? (correct * 100 / answered) : 0;
        
        ((android.widget.TextView) findViewById(R.id.tvProgress))
            .setText(answered + "/" + total + " câu (" + progressPercent + "%)");
        ((android.widget.TextView) findViewById(R.id.tvAccuracy))
            .setText("Độ chính xác: " + accuracyPercent + "% (" + correct + "/" + answered + " đúng)");
        
        android.widget.ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(total);
        progressBar.setProgress(answered);
        
        historyDAO.close();
        questionDAO.close();
    }
}
