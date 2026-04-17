package com.example.btl_banglaixe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_banglaixe.utils.JsonImporter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Import câu hỏi lần đầu tiên
        importQuestionsFirstTime();

        setupListeners();
    }

    private void importQuestionsFirstTime() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        
        // Kiểm tra đã import chưa
        if (!prefs.getBoolean("questions_imported", false)) {
            // Import câu hỏi từ file JSON
            int count = JsonImporter.importQuestionsFromAssets(this, "questions_hang_a.json");
            
            if (count > 0) {
                // Đánh dấu đã import
                prefs.edit().putBoolean("questions_imported", true).apply();
                Toast.makeText(this, "Đã import " + count + " câu hỏi thành công!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Lỗi khi import câu hỏi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupListeners() {
        // Nút Cài đặt
        findViewById(R.id.btnSettings).setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class))
        );

        // Setup các card chủ đề với lambda
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
}
