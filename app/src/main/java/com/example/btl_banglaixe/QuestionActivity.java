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

import com.example.btl_banglaixe.database.BookmarkDAO;
import com.example.btl_banglaixe.database.HistoryDAO;
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
    private BookmarkDAO bookmarkDAO;
    private HistoryDAO historyDAO;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private boolean isAnswered = false;
    private boolean isBookmarked = false;

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
        bookmarkDAO = new BookmarkDAO(this);
        historyDAO = new HistoryDAO(this);
        
        // Lấy category từ Intent
        String category = getIntent().getStringExtra("category");
        
        if (category == null) {
            // Không có category = Học tập tất cả 250 câu
            questions = questionDAO.getAllQuestions();
        } else if (category.equals("critical")) {
            // Câu điểm liệt
            questions = questionDAO.getCriticalQuestions();
        } else if (category.equals("bookmarked")) {
            // Câu đã đánh dấu
            questions = bookmarkDAO.getBookmarkedQuestions();
        } else {
            // Category cụ thể
            questions = questionDAO.getQuestionsByCategory(category);
        }

        if (questions.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnBookmark.setOnClickListener(v -> toggleBookmark());

        // Setup các option cards
        cardOptionA.setOnClickListener(v -> selectAnswer("A"));
        cardOptionB.setOnClickListener(v -> selectAnswer("B"));
        cardOptionC.setOnClickListener(v -> selectAnswer("C"));
        cardOptionD.setOnClickListener(v -> selectAnswer("D"));

        btnPrevious.setOnClickListener(v -> navigateQuestion(-1));
        btnNext.setOnClickListener(v -> navigateQuestion(1));
    }

    private void toggleBookmark() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        int questionId = currentQuestion.getId();
        
        if (isBookmarked) {
            // Xóa bookmark
            bookmarkDAO.removeBookmark(questionId);
            isBookmarked = false;
            btnBookmark.setImageResource(R.drawable.ic_star_outline);
            Toast.makeText(this, "Đã bỏ đánh dấu", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm bookmark
            bookmarkDAO.addBookmark(questionId);
            isBookmarked = true;
            btnBookmark.setImageResource(R.drawable.ic_star_filled);
            Toast.makeText(this, "Đã đánh dấu câu hỏi", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateQuestion(int direction) {
        int newIndex = currentQuestionIndex + direction;
        
        if (newIndex < 0 || newIndex >= questions.size()) {
            if (direction > 0) {
                Toast.makeText(this, "Đã hết câu hỏi!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        
        currentQuestionIndex = newIndex;
        resetQuestion();
        displayQuestion();
    }

    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) return;

        Question question = questions.get(currentQuestionIndex);

        // Cập nhật số câu hỏi và progress
        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/" + questions.size());
        progressBar.setMax(questions.size());
        progressBar.setProgress(currentQuestionIndex + 1);

        // Hiển thị câu hỏi và badge điểm liệt
        tvQuestion.setText(question.getQuestionText());
        criticalBadge.setVisibility(question.isCritical() ? View.VISIBLE : View.GONE);

        // Kiểm tra và cập nhật trạng thái bookmark
        isBookmarked = bookmarkDAO.isBookmarked(question.getId());
        btnBookmark.setImageResource(isBookmarked ? 
            R.drawable.ic_star_filled : R.drawable.ic_star_outline);

        // Hiển thị ảnh nếu có
        if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
            try {
                // Load ảnh từ drawable
                int resId = getResources().getIdentifier(
                    question.getImagePath(), 
                    "drawable", 
                    getPackageName()
                );
                
                if (resId != 0) {
                    ivQuestion.setImageResource(resId);
                    imageCard.setVisibility(View.VISIBLE);
                } else {
                    imageCard.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                imageCard.setVisibility(View.GONE);
            }
        } else {
            imageCard.setVisibility(View.GONE);
        }

        // Hiển thị các đáp án
        tvOptionA.setText(question.getOptionA());
        tvOptionB.setText(question.getOptionB());
        setOptionVisibility(cardOptionC, tvOptionC, question.getOptionC());
        setOptionVisibility(cardOptionD, tvOptionD, question.getOptionD());

        // Hiển thị giải thích
        if (question.getExplanation() != null && !question.getExplanation().isEmpty()) {
            tvExplanation.setText(question.getExplanation());
        }

        // Cập nhật nút Previous
        btnPrevious.setEnabled(currentQuestionIndex > 0);
    }

    private void setOptionVisibility(CardView card, TextView textView, String option) {
        if (option != null && !option.isEmpty()) {
            card.setVisibility(View.VISIBLE);
            textView.setText(option);
        } else {
            card.setVisibility(View.GONE);
        }
    }

    private void selectAnswer(String answer) {
        if (isAnswered) return; // Không cho chọn lại

        selectedAnswer = answer;
        isAnswered = true;

        Question question = questions.get(currentQuestionIndex);
        String correctAnswer = question.getCorrectAnswer();

        // Lưu lịch sử trả lời
        historyDAO.saveAnswer(question.getId(), answer, correctAnswer);

        // Hiển thị kết quả
        highlightAnswer(answer, correctAnswer);

        // Hiển thị giải thích
        explanationCard.setVisibility(View.VISIBLE);
    }

    private void highlightAnswer(String selected, String correct) {
        resetCardColors();

        CardView selectedCard = getCardByAnswer(selected);
        if (selectedCard == null) return;

        // Highlight đáp án đã chọn
        int color = selected.equals(correct) 
            ? R.color.success 
            : R.color.error;
        selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, color));

        // Nếu sai, hiển thị đáp án đúng
        if (!selected.equals(correct)) {
            CardView correctCard = getCardByAnswer(correct);
            if (correctCard != null) {
                correctCard.setCardBackgroundColor(
                    ContextCompat.getColor(this, R.color.success)
                );
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
        int defaultColor = ContextCompat.getColor(this, R.color.card_light);
        for (CardView card : new CardView[]{cardOptionA, cardOptionB, cardOptionC, cardOptionD}) {
            card.setCardBackgroundColor(defaultColor);
        }
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
        if (bookmarkDAO != null) {
            bookmarkDAO.close();
        }
        if (historyDAO != null) {
            historyDAO.close();
        }
    }
}
