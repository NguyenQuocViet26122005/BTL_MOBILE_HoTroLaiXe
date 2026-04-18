package com.example.btl_banglaixe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.database.ExamResultDAO;
import com.example.btl_banglaixe.models.Question;
import com.example.btl_banglaixe.utils.ExamGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExamQuestionActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private TextView tvTimer, tvWrongCount, tvExplanation;
    private CardView cardOptionA, cardOptionB, cardOptionC, cardOptionD, criticalBadge, imageCard, explanationCard;
    private ImageView ivQuestion, btnBack;
    private Button btnPrevious, btnNext, btnSubmit;
    private ProgressBar progressBar;

    private QuestionDAO questionDAO;
    private List<Question> examQuestions;
    private List<String> userAnswers;
    private int currentQuestionIndex = 0;
    private int examId;
    private boolean showAnswerImmediately;
    private CountDownTimer timer;
    private long timeLeftInMillis = 19 * 60 * 1000;
    
    private int wrongCount = 0;
    private int criticalWrongCount = 0;
    private int maxWrongAllowed = 4;
    private String licenseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_question);

        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(
                androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadExamSettings();
        loadExamQuestions();
        setupListeners();
        displayQuestion();
        startTimer();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        tvTimer = findViewById(R.id.tvTimer);
        tvWrongCount = findViewById(R.id.tvWrongCount);
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
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadExamSettings() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        licenseType = prefs.getString("license_type", "A1");
        maxWrongAllowed = licenseType.equals("A") ? 2 : 4;
        examId = getIntent().getIntExtra("exam_id", 0);
        showAnswerImmediately = getIntent().getBooleanExtra("show_answer_immediately", false);
    }

    private void loadExamQuestions() {
        questionDAO = new QuestionDAO(this);
        examQuestions = ExamGenerator.generateExam(questionDAO, getIntent().getIntExtra("exam_id", 0));
        
        if (examQuestions == null || examQuestions.isEmpty()) {
            new AlertDialog.Builder(this)
                .setTitle("Lỗi")
                .setMessage("Không thể tạo đề thi. Vui lòng thử lại.")
                .setPositiveButton("OK", (d, w) -> finish())
                .setCancelable(false)
                .show();
            return;
        }
        
        userAnswers = new ArrayList<>();
        for (int i = 0; i < examQuestions.size(); i++) userAnswers.add("");
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> showExitConfirmation());
        cardOptionA.setOnClickListener(v -> selectAnswer("A"));
        cardOptionB.setOnClickListener(v -> selectAnswer("B"));
        cardOptionC.setOnClickListener(v -> selectAnswer("C"));
        cardOptionD.setOnClickListener(v -> selectAnswer("D"));
        btnPrevious.setOnClickListener(v -> navigateQuestion(-1));
        btnNext.setOnClickListener(v -> navigateQuestion(1));
        btnSubmit.setOnClickListener(v -> submitExam());
    }

    private void startTimer() {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                submitExam();
            }
        }.start();
    }

    private void displayQuestion() {
        if (examQuestions == null || examQuestions.isEmpty()) return;

        Question q = examQuestions.get(currentQuestionIndex);

        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/25");
        progressBar.setMax(25);
        progressBar.setProgress(currentQuestionIndex + 1);

        tvQuestion.setText(q.getQuestionText());
        criticalBadge.setVisibility(q.isCritical() ? View.VISIBLE : View.GONE);

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

        String previousAnswer = userAnswers.get(currentQuestionIndex);
        if (!previousAnswer.isEmpty()) {
            highlightSelectedAnswer(previousAnswer);
            if (showAnswerImmediately) {
                highlightCorrectAnswer(q.getCorrectAnswer(), previousAnswer);
                explanationCard.setVisibility(View.VISIBLE);
            }
        } else {
            resetCardColors();
            explanationCard.setVisibility(View.GONE);
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
        String previousAnswer = userAnswers.get(currentQuestionIndex);
        if (!previousAnswer.isEmpty() && showAnswerImmediately) return;

        Question q = examQuestions.get(currentQuestionIndex);
        String correctAnswer = q.getCorrectAnswer();
        
        if (!previousAnswer.isEmpty() && !previousAnswer.equals(correctAnswer)) {
            wrongCount--;
            if (q.isCritical()) criticalWrongCount--;
        }
        
        userAnswers.set(currentQuestionIndex, answer);
        
        if (!answer.equals(correctAnswer)) {
            wrongCount++;
            if (q.isCritical()) criticalWrongCount++;
        }
        
        updateWrongCount();

        if (showAnswerImmediately) {
            highlightCorrectAnswer(correctAnswer, answer);
            explanationCard.setVisibility(View.VISIBLE);
        } else {
            highlightSelectedAnswer(answer);
        }
    }

    private void highlightSelectedAnswer(String answer) {
        resetCardColors();
        CardView card = getCardByAnswer(answer);
        if (card != null) {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        }
    }

    private void highlightCorrectAnswer(String correct, String selected) {
        resetCardColors();
        CardView selectedCard = getCardByAnswer(selected);
        if (selectedCard != null) {
            int color = selected.equals(correct) ? R.color.success : R.color.error;
            selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, color));
        }

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

    private void updateWrongCount() {
        tvWrongCount.setText("Sai: " + wrongCount + "/" + maxWrongAllowed);
        int color = criticalWrongCount > 0 ? R.color.error : 
                    wrongCount >= maxWrongAllowed - 1 ? R.color.accent_orange : R.color.text_primary_light;
        tvWrongCount.setTextColor(ContextCompat.getColor(this, color));
    }

    private void navigateQuestion(int direction) {
        int newIndex = currentQuestionIndex + direction;
        if (newIndex < 0 || newIndex >= examQuestions.size()) return;
        currentQuestionIndex = newIndex;
        displayQuestion();
    }

    private void submitExam() {
        int unansweredCount = 0;
        for (String answer : userAnswers) {
            if (answer.isEmpty()) unansweredCount++;
        }
        
        if (unansweredCount > 0) {
            new AlertDialog.Builder(this)
                .setTitle("Chưa hoàn thành")
                .setMessage("Bạn phải làm hết 25 câu mới được nộp bài!\n\nCòn " + unansweredCount + " câu chưa trả lời.")
                .setPositiveButton("OK", null)
                .show();
        } else {
            calculateResult();
        }
    }

    private void calculateResult() {
        if (timer != null) timer.cancel();
        
        int correctCount = 0;
        wrongCount = 0;
        criticalWrongCount = 0;
        
        for (int i = 0; i < examQuestions.size(); i++) {
            Question q = examQuestions.get(i);
            String userAnswer = userAnswers.get(i);
            
            if (userAnswer.equals(q.getCorrectAnswer())) {
                correctCount++;
            } else {
                wrongCount++;
                if (q.isCritical()) criticalWrongCount++;
            }
        }
        
        int requiredCorrect = licenseType.equals("A") ? 23 : 21;
        boolean passed = criticalWrongCount == 0 && correctCount >= requiredCorrect;
        showResultDialog(passed, correctCount, wrongCount, criticalWrongCount);
    }

    private void showResultDialog(boolean passed, int correct, int wrong, int criticalWrong) {
        ExamResultDAO dao = new ExamResultDAO(this);
        dao.saveExamResult(examId, licenseType, passed, correct, wrong);
        dao.close();
        
        String title = passed ? "🎉 Đậu" : "😢 Rớt";
        String message = "Kết quả thi của bạn:\n\n" +
                "✅ Đúng: " + correct + "/25 câu\n" +
                "❌ Sai: " + wrong + "/25 câu\n";
        
        if (criticalWrong > 0) {
            message += "⚠️ Sai câu điểm liệt: " + criticalWrong + " câu\n\nLý do: Sai câu điểm liệt";
        } else if (!passed) {
            int required = licenseType.equals("A") ? 23 : 21;
            message += "\nLý do: Cần đúng tối thiểu " + required + "/25 câu (Hạng " + licenseType + ")";
        } else {
            message += "\n🎊 Chúc mừng bạn đã đạt!";
        }
        
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Xem đáp án", (d, w) -> finish())
            .setNegativeButton("Về trang chủ", (d, w) -> finish())
            .setCancelable(false)
            .show();
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("Thoát bài thi")
            .setMessage("Bạn có chắc muốn thoát?\n\nKết quả sẽ không được lưu.")
            .setPositiveButton("Thoát", (d, w) -> finish())
            .setNegativeButton("Ở lại", null)
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (questionDAO != null) questionDAO.close();
    }
}
