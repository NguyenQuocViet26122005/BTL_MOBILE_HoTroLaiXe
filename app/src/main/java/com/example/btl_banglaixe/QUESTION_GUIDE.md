# Hướng dẫn sử dụng Giao diện Câu hỏi

## 🎨 Tính năng

### 1. Hiển thị linh hoạt
- ✅ Hỗ trợ 2, 3, hoặc 4 đáp án
- ✅ Tự động ẩn đáp án C, D nếu null
- ✅ Hiển thị ảnh câu hỏi (nếu có)
- ✅ Badge "CÂU ĐIỂM LIỆT" cho câu quan trọng

### 2. Tương tác
- Chọn đáp án bằng cách nhấn vào card
- Màu xanh = Đúng
- Màu đỏ = Sai (và hiện đáp án đúng)
- Hiển thị giải thích sau khi trả lời

### 3. Điều hướng
- Nút "← Câu trước" và "Câu tiếp →"
- Progress bar hiển thị tiến độ
- Nút back về trang chủ
- Nút bookmark để lưu câu hỏi

## 📱 Cách sử dụng

### 1. Mở QuestionActivity từ MainActivity

```java
// Trong MainActivity.java
Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
intent.putExtra("category", "Khái niệm và quy tắc"); // Tùy chọn
startActivity(intent);
```

### 2. Thêm câu hỏi có 2 đáp án

```java
Question question = new Question();
question.setQuestionText("Người điều khiển xe mô tô có được phép sử dụng xe để kéo, đẩy xe khác không?");
question.setOptionA("Được phép");
question.setOptionB("Không được phép");
question.setOptionC(null); // Không có đáp án C
question.setOptionD(null); // Không có đáp án D
question.setCorrectAnswer("B");
question.setCategory("Khái niệm và quy tắc");
question.setCritical(true); // Câu điểm liệt
question.setImagePath(null);
question.setExplanation("Không được phép sử dụng xe mô tô để kéo, đẩy xe khác.");

questionDAO.addQuestion(question);
```

### 3. Thêm câu hỏi có ảnh

```java
Question question = new Question();
question.setQuestionText("Biển báo này có ý nghĩa gì?");
question.setOptionA("Cấm đi thẳng");
question.setOptionB("Cấm rẽ trái");
question.setOptionC("Cấm rẽ phải");
question.setOptionD("Cấm quay đầu");
question.setCorrectAnswer("A");
question.setCategory("Biển báo đường bộ");
question.setCritical(false);
question.setImagePath("signs/sign_no_straight.png"); // Đường dẫn ảnh
question.setExplanation("Biển báo cấm đi thẳng.");

questionDAO.addQuestion(question);
```

### 4. Load ảnh từ assets (cần thêm Glide)

Thêm vào `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
```

Trong `QuestionActivity.java`:
```java
if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
    imageCard.setVisibility(View.VISIBLE);
    Glide.with(this)
        .load("file:///android_asset/" + question.getImagePath())
        .into(ivQuestion);
}
```

## 🎯 Cấu trúc thư mục ảnh

```
app/src/main/assets/
├── signs/              # Biển báo
│   ├── sign_stop.png
│   ├── sign_yield.png
│   └── ...
├── questions/          # Ảnh câu hỏi
│   ├── question_1.jpg
│   ├── question_2.jpg
│   └── ...
└── situations/         # Sa hình
    ├── situation_1.jpg
    └── ...
```

## 🎨 Màu sắc

- **Đáp án chưa chọn:** #1A1A1A (xám đen)
- **Đáp án đúng:** #4CAF50 (xanh lá)
- **Đáp án sai:** #F44336 (đỏ)
- **Câu điểm liệt:** #FF5252 (đỏ sáng)

## 📝 Lưu ý

1. Đáp án C và D sẽ tự động ẩn nếu null hoặc rỗng
2. Ảnh câu hỏi chỉ hiện khi có imagePath
3. Giải thích chỉ hiện sau khi chọn đáp án
4. Không thể chọn lại sau khi đã trả lời
5. Nhấn "Câu tiếp" để chuyển sang câu mới

## 🚀 Tính năng mở rộng

- [ ] Lưu câu hỏi yêu thích (bookmark)
- [ ] Lưu lịch sử trả lời
- [ ] Chế độ thi thử có thời gian
- [ ] Xem lại câu sai
- [ ] Thống kê kết quả
