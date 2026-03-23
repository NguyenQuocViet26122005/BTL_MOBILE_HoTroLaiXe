package com.example.btl_banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        setupListeners();
    }

    private void setupListeners() {
        // Nút Cài đặt
        TextView btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Card chủ đề - Câu hỏi điểm liệt
        CardView cardCriticalQuestions = findViewById(R.id.cardCriticalQuestions);
        cardCriticalQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity("Câu hỏi điểm liệt");
            }
        });

        // Card chủ đề - Khái niệm và quy tắc
        CardView cardConceptsRules = findViewById(R.id.cardConceptsRules);
        cardConceptsRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity("Khái niệm và quy tắc");
            }
        });

        // Card chủ đề - Văn hóa và đạo đức
        CardView cardCultureEthics = findViewById(R.id.cardCultureEthics);
        cardCultureEthics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity("Văn hóa và đạo đức");
            }
        });

        // Card chủ đề - Kỹ thuật lái xe
        CardView cardDrivingTechniques = findViewById(R.id.cardDrivingTechniques);
        cardDrivingTechniques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity("Kỹ thuật lái xe");
            }
        });

        // Card chủ đề - Biển báo đường bộ
        CardView cardTrafficSigns = findViewById(R.id.cardTrafficSigns);
        cardTrafficSigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity("Biển báo đường bộ");
            }
        });

        // Card chủ đề - Sa hình
        CardView cardSituations = findViewById(R.id.cardSituations);
        cardSituations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity("Sa hình");
            }
        });
    }

    private void openQuestionActivity(String category) {
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
