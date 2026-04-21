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

    private TextView tvQuestionNumber, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvExplanation, tvHistoryStatus, tvCategory;
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
        tvHistoryStatus = findViewById(R.id.tvHistoryStatus);
        tvCategory = findViewById(R.id.tvCategory);
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
        
        String category = getIntent().getStringExtra("category");
        
        if (category == null) {
            questions = questionDAO.getAllQuestions();
            tvCategory.setText("Tất cả câu hỏi");
        } else if (category.equals("critical")) {
            questions = questionDAO.getCriticalQuestions();
            tvCategory.setText("⚠️ Câu hỏi điểm liệt");
        } else if (category.equals("bookmarked")) {
            questions = bookmarkDAO.getBookmarkedQuestions();
            tvCategory.setText("⭐ Đánh dấu");
        } else if (category.equals("wrong")) {
            java.util.Set<Integer> wrongIds = historyDAO.getWrongQuestionIds();
            questions = questionDAO.getQuestionsByIds(wrongIds);
            tvCategory.setText("❌ Câu hay sai");
        } else {
            questions = questionDAO.getQuestionsByCategory(category);
            if (category.equals("Quy định chung và quy tắc giao thông đường bộ")) {
                tvCategory.setText("📖 Khái niệm và quy tắc");
            } else if (category.equals("Văn hóa giao thông, đạo đức người lái xe")) {
                tvCategory.setText("🤝 Văn hóa và đạo đức");
            } else if (category.equals("Kỹ thuật lái xe")) {
                tvCategory.setText("🏍️ Kỹ thuật lái xe");
            } else if (category.equals("Biển báo đường bộ")) {
                tvCategory.setText("🚦 Biển báo đường bộ");
            } else if (category.equals("Sa hình")) {
                tvCategory.setText("🛣️ Sa hình");
            } else {
                tvCategory.setText(category);
            }
        }

        if (questions.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnBookmark.setOnClickListener(v -> toggleBookmark());
        cardOptionA.setOnClickListener(v -> selectAnswer("A"));
        cardOptionB.setOnClickListener(v -> selectAnswer("B"));
        cardOptionC.setOnClickListener(v -> selectAnswer("C"));
        cardOptionD.setOnClickListener(v -> selectAnswer("D"));
        btnPrevious.setOnClickListener(v -> navigateQuestion(-1));
        btnNext.setOnClickListener(v -> navigateQuestion(1));
    }

    private void toggleBookmark() {
        Question q = questions.get(currentQuestionIndex);
        if (isBookmarked) {
            bookmarkDAO.removeBookmark(q.getId());
            btnBookmark.setImageResource(R.drawable.ic_star_outline);
        } else {
            bookmarkDAO.addBookmark(q.getId());
            btnBookmark.setImageResource(R.drawable.ic_star_filled);
        }
        isBookmarked = !isBookmarked;
    }

    private void navigateQuestion(int direction) {
        int newIndex = currentQuestionIndex + direction;
        if (newIndex < 0 || newIndex >= questions.size()) return;
        
        currentQuestionIndex = newIndex;
        resetQuestion();
        displayQuestion();
    }

    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) return;

        Question q = questions.get(currentQuestionIndex);

        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/" + questions.size());
        progressBar.setMax(questions.size());
        progressBar.setProgress(currentQuestionIndex + 1);

        tvQuestion.setText(q.getQuestionText());
        criticalBadge.setVisibility(q.isCritical() ? View.VISIBLE : View.GONE);

        isBookmarked = bookmarkDAO.isBookmarked(q.getId());
        btnBookmark.setImageResource(isBookmarked ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);

        Boolean lastResult = historyDAO.getLastAnswerResult(q.getId());
        if (lastResult != null) {
            tvHistoryStatus.setVisibility(View.VISIBLE);
            if (lastResult) {
                tvHistoryStatus.setText("✓");
                tvHistoryStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_green));
            } else {
                tvHistoryStatus.setText("✗");
                tvHistoryStatus.setTextColor(ContextCompat.getColor(this, R.color.error));
            }
        } else {
            tvHistoryStatus.setVisibility(View.GONE);
        }

        if (q.getImagePath() != null && !q.getImagePath().isEmpty()) {
            int resId = getResources().getIdentifier(q.getImagePath(), "drawable", getPackageName());
            if (resId != 0) {
                ivQuestion.setImageResource(resId);
                imageCard.setVisibility(View.VISIBLE);
            } else {
                imageCard.setVisibility(View.GONE);
            }
        } else {
            imageCard.setVisibility(View.GONE);
        }

        tvOptionA.setText(q.getOptionA());
        tvOptionB.setText(q.getOptionB());
        setOptionVisibility(cardOptionC, tvOptionC, q.getOptionC());
        setOptionVisibility(cardOptionD, tvOptionD, q.getOptionD());

        if (q.getExplanation() != null && !q.getExplanation().isEmpty()) {
            tvExplanation.setText(q.getExplanation());
        }

        btnPrevious.setEnabled(currentQuestionIndex > 0);
    }

    private void setOptionVisibility(CardView card, TextView tv, String option) {
        if (option != null && !option.isEmpty()) {
            card.setVisibility(View.VISIBLE);
            tv.setText(option);
        } else {
            card.setVisibility(View.GONE);
        }
    }

    private void selectAnswer(String answer) {
        if (isAnswered) return;

        selectedAnswer = answer;
        isAnswered = true;

        Question q = questions.get(currentQuestionIndex);
        boolean isCorrect = answer.equals(q.getCorrectAnswer());
        historyDAO.saveAnswer(q.getId(), answer, q.getCorrectAnswer());
        highlightAnswer(answer, q.getCorrectAnswer());
        explanationCard.setVisibility(View.VISIBLE);
        
        tvHistoryStatus.setVisibility(View.VISIBLE);
        if (isCorrect) {
            tvHistoryStatus.setText("✓");
            tvHistoryStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_green));
        } else {
            tvHistoryStatus.setText("✗");
            tvHistoryStatus.setTextColor(ContextCompat.getColor(this, R.color.error));
        }
    }

    private void highlightAnswer(String selected, String correct) {
        resetCardColors();
        CardView selectedCard = getCardByAnswer(selected);
        if (selectedCard == null) return;

        int color = selected.equals(correct) ? R.color.success : R.color.error;
        selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, color));

        if (!selected.equals(correct)) {
            CardView correctCard = getCardByAnswer(correct);
            if (correctCard != null) {
                correctCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.success));
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
        int color = ContextCompat.getColor(this, R.color.card_light);
        for (CardView card : new CardView[]{cardOptionA, cardOptionB, cardOptionC, cardOptionD}) {
            card.setCardBackgroundColor(color);
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
        if (questionDAO != null) questionDAO.close();
        if (bookmarkDAO != null) bookmarkDAO.close();
        if (historyDAO != null) historyDAO.close();
    }
}
