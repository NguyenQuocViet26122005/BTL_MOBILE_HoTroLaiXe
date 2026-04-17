mnq# 📝 Hướng dẫn thêm 250 câu hỏi Hạng A vào SQLite

## ✅ Đã làm xong:

1. ✅ Tạo file `app/src/main/assets/questions_hang_a.json`
2. ✅ Tạo class `JsonImporter.java` để import
3. ✅ Cập nhật `MainActivity.java` để tự động import lần đầu

## 🎯 Bây giờ bạn cần làm:

### Bước 1: Thêm 250 câu hỏi vào file JSON

Mở file: `app/src/main/assets/questions_hang_a.json`

Thêm câu hỏi theo format:

```json
{
  "questions": [
    {
      "question_text": "Nội dung câu hỏi?",
      "option_a": "Đáp án A",
      "option_b": "Đáp án B",
      "option_c": "Đáp án C hoặc null",
      "option_d": "Đáp án D hoặc null",
      "correct_answer": "A",
      "category": "Tên chủ đề",
      "is_critical": 0,
      "image_path": null,
      "explanation": "Giải thích"
    }
  ]
}
```

### Bước 2: Phân loại 250 câu theo chủ đề

#### 1. Câu hỏi điểm liệt (20 câu)
```json
{
  "category": "Câu hỏi điểm liệt",
  "is_critical": 1
}
```

#### 2. Khái niệm và quy tắc (100 câu)
```json
{
  "category": "Khái niệm và quy tắc",
  "is_critical": 0
}
```

#### 3. Văn hóa và đạo đức (10 câu)
```json
{
  "category": "Văn hóa và đạo đức",
  "is_critical": 0
}
```

#### 4. Kỹ thuật lái xe (15 câu)
```json
{
  "category": "Kỹ thuật lái xe",
  "is_critical": 0
}
```

#### 5. Biển báo đường bộ (65 câu)
```json
{
  "category": "Biển báo đường bộ",
  "is_critical": 0
}
```

#### 6. Sa hình (35 câu)
```json
{
  "category": "Sa hình",
  "is_critical": 0
}
```

---

## 📋 Template Excel để nhập dữ liệu

Tạo file Excel với các cột:

| STT | Câu hỏi | Đáp án A | Đáp án B | Đáp án C | Đáp án D | Đáp án đúng | Chủ đề | Điểm liệt | Giải thích |
|-----|---------|----------|----------|----------|----------|-------------|--------|-----------|------------|
| 1 | Người điều khiển... | Bị nghiêm cấm | Không bị... | | | A | Câu hỏi điểm liệt | 1 | Tuyệt đối... |
| 2 | Khái niệm... | Là người... | Là người... | Là người... | | C | Khái niệm và quy tắc | 0 | Người tham gia... |

Sau đó convert sang JSON bằng tool online:
- https://www.convertcsv.com/csv-to-json.htm
- https://csvjson.com/csv2json

---

## 🔧 Cách test

### 1. Xóa dữ liệu cũ (nếu cần)

```java
// Trong MainActivity, thêm nút test
Button btnReimport = findViewById(R.id.btnReimport);
btnReimport.setOnClickListener(v -> {
    // Xóa flag
    getSharedPreferences("app_prefs", MODE_PRIVATE)
        .edit()
        .putBoolean("questions_imported", false)
        .apply();
    
    // Xóa database
    deleteDatabase("driving_license.db");
    
    // Restart app
    recreate();
});
```

### 2. Kiểm tra số câu hỏi

```java
QuestionDAO dao = new QuestionDAO(this);
int total = dao.getAllQuestions().size();
Toast.makeText(this, "Tổng: " + total + " câu", Toast.LENGTH_SHORT).show();

// Kiểm tra từng chủ đề
int critical = dao.getQuestionsByCategory("Câu hỏi điểm liệt").size();
int concepts = dao.getQuestionsByCategory("Khái niệm và quy tắc").size();
// ...
```

---

## 🚀 Cách chạy

1. **Build project**: Ctrl + F9
2. **Run app**: Shift + F10
3. **Lần đầu chạy**: App sẽ tự động import câu hỏi
4. **Xem Toast**: "Đã import X câu hỏi thành công!"
5. **Click vào card chủ đề**: Xem câu hỏi đã import

---

## ⚠️ Lưu ý quan trọng

### 1. Format đúng
- `correct_answer`: Phải là "A", "B", "C", hoặc "D" (viết hoa)
- `is_critical`: 0 hoặc 1 (số, không phải boolean)
- `option_c`, `option_d`: Dùng `null` nếu không có (không phải chuỗi "null")

### 2. Category phải khớp
Tên category phải giống y hệt trong database:
- "Câu hỏi điểm liệt"
- "Khái niệm và quy tắc"
- "Văn hóa và đạo đức"
- "Kỹ thuật lái xe"
- "Biển báo đường bộ"
- "Sa hình"

### 3. Encoding UTF-8
File JSON phải save với encoding UTF-8 để hiển thị tiếng Việt đúng

---

## 📥 Nguồn lấy 250 câu hỏi

### Option 1: Tự nhập
- Mua sách "600 câu hỏi thi GPLX"
- Nhập vào Excel
- Convert sang JSON

### Option 2: Tìm online
```
🔍 Google: "250 câu hỏi thi bằng lái hạng A"
🔍 Website: https://600cauhoi.vn/
🔍 GitHub: Tìm repo có dữ liệu
```

### Option 3: Crawl từ website
```python
# Python script để crawl
import requests
from bs4 import BeautifulSoup
import json

# Crawl và save to JSON
```

---

## 🎉 Kết quả mong đợi

Sau khi import thành công:
- ✅ Tổng 250 câu hỏi trong database
- ✅ Click vào "Câu hỏi điểm liệt" → Hiện 20 câu
- ✅ Click vào "Khái niệm và quy tắc" → Hiện 100 câu
- ✅ Các chủ đề khác tương tự
- ✅ Có thể làm bài, chọn đáp án, xem giải thích

---

## 🆘 Troubleshooting

### Lỗi: "Đã import 0 câu hỏi"
- Kiểm tra file JSON có đúng trong `assets/` không
- Kiểm tra format JSON có đúng không (dùng JSONLint.com)

### Lỗi: Không hiển thị câu hỏi
- Kiểm tra tên category có đúng không
- Kiểm tra database đã có dữ liệu chưa

### Lỗi: Crash khi mở QuestionActivity
- Kiểm tra `correct_answer` có đúng format không
- Kiểm tra có câu hỏi nào thiếu field bắt buộc không

---

## 📞 Cần giúp đỡ?

Nếu gặp khó khăn, hãy:
1. Check logcat để xem lỗi
2. Kiểm tra file JSON bằng JSONLint
3. Test với 5-10 câu trước khi import hết 250 câu
