package com.example.btl_banglaixe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

        // Import câu hỏi lần đầu tiên
        importQuestionsFirstTime();

        setupListeners();
        updateLicenseType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLicenseType();
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
            openQuestionActivity("Khái niệm và quy tắc")
        );
        
        findViewById(R.id.cardSigns).setOnClickListener(v -> 
            openQuestionActivity("Biển báo đường bộ")
        );
        
        findViewById(R.id.cardExam).setOnClickListener(v -> 
            Toast.makeText(this, "Chức năng thi thử đang phát triển", Toast.LENGTH_SHORT).show()
        );
        
        findViewById(R.id.cardBookmark).setOnClickListener(v -> 
            Toast.makeText(this, "Chức năng đánh dấu đang phát triển", Toast.LENGTH_SHORT).show()
        );

        // Danh mục học tập
        setupCategoryCard(R.id.cardCriticalQuestions, "Câu hỏi điểm liệt");
        setupCategoryCard(R.id.cardConceptsRules, "Khái niệm và quy tắc");
        setupCategoryCard(R.id.cardCultureEthics, "Văn hóa và đạo đức");
        setupCategoryCard(R.id.cardDrivingTechniques, "Kỹ thuật lái xe");
        setupCategoryCard(R.id.cardTrafficSigns, "Biển báo đường bộ");
        setupCategoryCard(R.id.cardSituations, "Sa hình");
    }

    private void setupCategoryCard(int cardId, String category) {
        findViewById(cardId).setOnClickListener(v -> openQuestionActivity(category));
    }

    private void openQuestionActivity(String category) {
        startActivity(new Intent(this, QuestionActivity.class)
            .putExtra("category", category));
    }

    private void updateLicenseType() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String licenseType = prefs.getString("license_type", "A1");
        
        android.widget.TextView tvLicenseType = findViewById(R.id.tvLicenseType);
        tvLicenseType.setText("Hạng " + licenseType);
    }
}
