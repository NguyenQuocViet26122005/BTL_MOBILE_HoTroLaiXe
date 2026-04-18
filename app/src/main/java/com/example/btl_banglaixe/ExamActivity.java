package com.example.btl_banglaixe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_banglaixe.adapters.ExamAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExamAdapter adapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(
                androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupViews();
        loadExams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExams();
    }

    private void setupViews() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyclerExams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadExams() {
        String licenseType = prefs.getString("license_type", "A1");
        List<ExamItem> exams = new ArrayList<>();
        exams.add(new ExamItem(0, "Đề ngẫu nhiên", "25 câu hỏi được chọn ngẫu nhiên", true));
        for (int i = 1; i <= 15; i++) {
            exams.add(new ExamItem(i, "Đề số " + i, "25 câu hỏi theo cấu trúc thi thật", false));
        }
        
        com.example.btl_banglaixe.database.ExamResultDAO dao = new com.example.btl_banglaixe.database.ExamResultDAO(this);
        for (ExamItem exam : exams) {
            com.example.btl_banglaixe.database.ExamResultDAO.ExamResult result = dao.getExamResult(exam.getId(), licenseType);
            if (result != null) {
                exam.setStatus(result.passed ? "✅ Đã đỗ" : "❌ Trượt");
            }
        }
        dao.close();
        
        adapter = new ExamAdapter(exams, this::startExam);
        recyclerView.setAdapter(adapter);
    }

    private void startExam(ExamItem exam) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Chế độ thi")
            .setMessage("Bạn muốn hiển thị đáp án ngay khi chọn?")
            .setPositiveButton("Hiện đáp án ngay", (d, w) -> launchExam(exam.getId(), true))
            .setNegativeButton("Không hiện", (d, w) -> launchExam(exam.getId(), false))
            .show();
    }

    private void launchExam(int examId, boolean showAnswerImmediately) {
        Intent intent = new Intent(this, ExamQuestionActivity.class);
        intent.putExtra("exam_id", examId);
        intent.putExtra("show_answer_immediately", showAnswerImmediately);
        startActivity(intent);
    }

    public static class ExamItem {
        private int id;
        private String title;
        private String description;
        private boolean isRandom;
        private String status;

        public ExamItem(int id, String title, String description, boolean isRandom) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.isRandom = isRandom;
            this.status = "";
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public boolean isRandom() { return isRandom; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
