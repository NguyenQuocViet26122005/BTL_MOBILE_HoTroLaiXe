package com.example.btl_banglaixe;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.models.Question;

import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvExplanation;
    private CardView cardOptionA, cardOptionB, cardOptionC, cardOptionD, criticalBadge, imageCard, explanationCard;
    private ImageView ivQuestion, btnBack, btnBookmark;
    private Button btnPrevious, btnNext;
    private ProgressBar progressBar;

    private QuestionDAO questionDAO;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadQuestions();
        setupListeners();
        displayQuestion();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        tvExplanation = findViewById(R.id.tvExplanation);

        cardOptionA = findViewById(R.id.cardOptionA);
        cardOptionB = findViewById(R.id.cardOptionB);
        cardOptionC = findViewById(R.id.cardOptionC);
        cardOptionD = findViewById(R.id.cardOptionD);
        criticalBadge = findViewById(R.id.criticalBadge);
        imageCard = findViewById(R.id.imageCard);
        explanationCard = findViewById(R.id.explanationCard);

        ivQuestion = findViewById(R.id.ivQuestion);
        btnBack = findViewById(R.id.btnBack);
        btnBookmark = findViewById(R.id.btnBookmark);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadQuestions() {
        questionDAO = new QuestionDAO(this);
        
        // Lấy category từ Intent (nếu có)
        String category = getIntent().getStringExtra("category");
        
        if (category != null && !category.isEmpty()) {
            questions = questionDAO.getQuestionsByCategory(category);
        } else {
            questions = questionDAO.getAllQuestions();
        }

        if (questions.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnBookmark.setOnClickListener(v -> {
            // TODO: Implement bookmark functionality
            Toast.makeText(this, "Đã lưu câu hỏi", Toast.LENGTH_SHORT).show();
        });

        cardOptionA.setOnClickListener(v -> selectAnswer("A"));
        cardOptionB.setOnClickListener(v -> selectAnswer("B"));
        cardOptionC.setOnClickListener(v -> selectAnswer("C"));
        cardOptionD.setOnClickListener(v -> selectAnswer("D"));

        btnPrevious.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                resetQuestion();
                displayQuestion();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                resetQuestion();
                displayQuestion();
            } else {
                Toast.makeText(this, "Đã hết câu hỏi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) return;

        Question question = questions.get(currentQuestionIndex);

        // Cập nhật số câu hỏi
        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/" + questions.size());
        progressBar.setMax(questions.size());
        progressBar.setProgress(currentQuestionIndex + 1);

        // Hiển thị câu hỏi
        tvQuestion.setText(question.getQuestionText());

        // Hiển thị badge điểm liệt
        criticalBadge.setVisibility(question.isCritical() ? View.VISIBLE : View.GONE);

        // Hiển thị ảnh (nếu có)
        if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
            imageCard.setVisibility(View.VISIBLE);
            // TODO: Load image from path
            // Glide.with(this).load(question.getImagePath()).into(ivQuestion);
        } else {
            imageCard.setVisibility(View.GONE);
        }

        // Hiển thị đáp án
        tvOptionA.setText(question.getOptionA());
        tvOptionB.setText(question.getOptionB());

        // Ẩn/hiện đáp án C và D nếu null
        if (question.getOptionC() != null && !question.getOptionC().isEmpty()) {
            cardOptionC.setVisibility(View.VISIBLE);
            tvOptionC.setText(question.getOptionC());
        } else {
            cardOptionC.setVisibility(View.GONE);
        }

        if (question.getOptionD() != null && !question.getOptionD().isEmpty()) {
            cardOptionD.setVisibility(View.VISIBLE);
            tvOptionD.setText(question.getOptionD());
        } else {
            cardOptionD.setVisibility(View.GONE);
        }

        // Hiển thị giải thích
        if (question.getExplanation() != null && !question.getExplanation().isEmpty()) {
            tvExplanation.setText(question.getExplanation());
        }

        // Cập nhật nút Previous
        btnPrevious.setEnabled(currentQuestionIndex > 0);
    }

    private void selectAnswer(String answer) {
        if (isAnswered) return; // Không cho chọn lại

        selectedAnswer = answer;
        isAnswered = true;

        Question question = questions.get(currentQuestionIndex);
        String correctAnswer = question.getCorrectAnswer();

        // Hiển thị kết quả
        highlightAnswer(answer, correctAnswer);

        // Hiển thị giải thích
        explanationCard.setVisibility(View.VISIBLE);
    }

    private void highlightAnswer(String selected, String correct) {
        // Reset tất cả
        resetCardColors();

        // Highlight đáp án đã chọn
        CardView selectedCard = getCardByAnswer(selected);
        if (selectedCard != null) {
            if (selected.equals(correct)) {
                // Đúng - màu xanh
                selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_success));
            } else {
                // Sai - màu đỏ
                selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                
                // Hiển thị đáp án đúng
                CardView correctCard = getCardByAnswer(correct);
                if (correctCard != null) {
                    correctCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_success));
                }
            }
        }
    }

    private CardView getCardByAnswer(String answer) {
        switch (answer) {
            case "A": return cardOptionA;
            case "B": return cardOptionB;
            case "C": return cardOptionC;
            case "D": return cardOptionD;
            default: return null;
        }
    }

    private void resetCardColors() {
        int defaultColor = ContextCompat.getColor(this, R.color.card_background);
        cardOptionA.setCardBackgroundColor(defaultColor);
        cardOptionB.setCardBackgroundColor(defaultColor);
        cardOptionC.setCardBackgroundColor(defaultColor);
        cardOptionD.setCardBackgroundColor(defaultColor);
    }

    private void resetQuestion() {
        isAnswered = false;
        selectedAnswer = "";
        resetCardColors();
        explanationCard.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (questionDAO != null) {
            questionDAO.close();
        }
    }
}
