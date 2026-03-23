# SQLite Database - Hướng dẫn sử dụng

## 📊 Cấu trúc Database

### 1. Bảng `questions` - Lưu câu hỏi
```sql
- id: INTEGER (Primary Key)
- question_text: TEXT (Nội dung câu hỏi)
- option_a: TEXT (Đáp án A)
- option_b: TEXT (Đáp án B)
- option_c: TEXT (Đáp án C)
- option_d: TEXT (Đáp án D)
- correct_answer: TEXT (Đáp án đúng: A, B, C, D)
- category: TEXT (Chủ đề)
- is_critical: INTEGER (1 = câu điểm liệt, 0 = câu thường)
- image_path: TEXT (Đường dẫn ảnh)
- explanation: TEXT (Giải thích đáp án)
```

### 2. Bảng `history` - Lịch sử làm bài
```sql
- history_id: INTEGER (Primary Key)
- question_id: INTEGER (Foreign Key)
- user_answer: TEXT (Đáp án người dùng chọn)
- is_correct: INTEGER (1 = đúng, 0 = sai)
- timestamp: DATETIME (Thời gian làm bài)
```

### 3. Bảng `progress` - Tiến độ học tập
```sql
- progress_id: INTEGER (Primary Key)
- category_name: TEXT (Tên chủ đề)
- total_questions: INTEGER (Tổng số câu)
- completed_questions: INTEGER (Số câu đã làm)
- correct_answers: INTEGER (Số câu đúng)
```

## 🔧 Cách sử dụng

### 1. Khởi tạo Database trong Activity

```java
// Trong MainActivity.java
import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.database.ProgressDAO;
import com.example.btl_banglaixe.models.Question;

public class MainActivity extends AppCompatActivity {
    private QuestionDAO questionDAO;
    private ProgressDAO progressDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DAO
        questionDAO = new QuestionDAO(this);
        progressDAO = new ProgressDAO(this);

        // Sử dụng
        loadData();
    }

    private void loadData() {
        // Lấy tất cả câu hỏi
        List<Question> allQuestions = questionDAO.getAllQuestions();
        
        // Lấy câu hỏi theo chủ đề
        List<Question> criticalQuestions = questionDAO.getQuestionsByCategory("Câu hỏi điểm liệt");
        
        // Lấy tiến độ tổng
        Map<String, Integer> progress = progressDAO.getTotalProgress();
        int total = progress.get("total");
        int completed = progress.get("completed");
        int correct = progress.get("correct");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng database khi không dùng
        questionDAO.close();
        progressDAO.close();
    }
}
```

### 2. Thêm câu hỏi mới

```java
Question newQuestion = new Question();
newQuestion.setQuestionText("Câu hỏi mới?");
newQuestion.setOptionA("Đáp án A");
newQuestion.setOptionB("Đáp án B");
newQuestion.setOptionC("Đáp án C");
newQuestion.setOptionD("Đáp án D");
newQuestion.setCorrectAnswer("A");
newQuestion.setCategory("Khái niệm và quy tắc");
newQuestion.setCritical(false);
newQuestion.setImagePath(null);
newQuestion.setExplanation("Giải thích đáp án");

long id = questionDAO.addQuestion(newQuestion);
```

### 3. Lấy câu hỏi

```java
// Lấy tất cả câu hỏi
List<Question> allQuestions = questionDAO.getAllQuestions();

// Lấy câu hỏi theo chủ đề
List<Question> questions = questionDAO.getQuestionsByCategory("Biển báo đường bộ");

// Lấy câu hỏi điểm liệt
List<Question> criticalQuestions = questionDAO.getCriticalQuestions();

// Lấy câu hỏi theo ID
Question question = questionDAO.getQuestionById(1);
```

### 4. Cập nhật tiến độ

```java
// Cập nhật tiến độ cho chủ đề
progressDAO.updateProgress("Khái niệm và quy tắc", 50, 45);

// Lấy tiến độ theo chủ đề
Map<String, Integer> categoryProgress = progressDAO.getProgressByCategory("Khái niệm và quy tắc");

// Lấy tổng tiến độ
Map<String, Integer> totalProgress = progressDAO.getTotalProgress();
```

### 5. Xóa dữ liệu

```java
// Xóa câu hỏi
questionDAO.deleteQuestion(1);

// Reset tất cả tiến độ
progressDAO.resetAllProgress();
```

## 📝 Các chủ đề có sẵn

1. Câu hỏi điểm liệt (20 câu)
2. Khái niệm và quy tắc (100 câu)
3. Văn hóa và đạo đức (10 câu)
4. Kỹ thuật lái xe (15 câu)
5. Biển báo đường bộ (65 câu)
6. Sa hình (35 câu)

**Tổng: 250 câu**

## 🎯 Lưu ý

- Database tự động tạo khi app chạy lần đầu
- Có 2 câu hỏi mẫu được thêm sẵn
- Nhớ gọi `close()` khi không sử dụng DAO nữa
- Sử dụng try-catch khi thao tác với database
