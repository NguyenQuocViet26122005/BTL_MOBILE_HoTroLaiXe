# Hướng dẫn Import 600 câu hỏi vào ứng dụng

## 📥 Cách 1: Import từ file JSON

### Bước 1: Chuẩn bị file JSON

Tạo file `questions.json` theo template:

```json
{
  "questions": [
    {
      "question_text": "Nội dung câu hỏi?",
      "option_a": "Đáp án A",
      "option_b": "Đáp án B",
      "option_c": "Đáp án C (hoặc null nếu chỉ có 2 đáp án)",
      "option_d": "Đáp án D (hoặc null)",
      "correct_answer": "A",
      "category": "Khái niệm và quy tắc",
      "is_critical": 0,
      "image_path": null,
      "explanation": "Giải thích đáp án"
    }
  ]
}
```

### Bước 2: Đặt file vào thư mục assets

```
app/src/main/assets/
└── questions.json
```

### Bước 3: Import trong MainActivity

```java
// Trong MainActivity.java onCreate()
import com.example.btl_banglaixe.utils.JsonImporter;

// Import câu hỏi (chỉ chạy 1 lần)
int count = JsonImporter.importQuestionsFromAssets(this, "questions.json");
Toast.makeText(this, "Đã import " + count + " câu hỏi", Toast.LENGTH_LONG).show();
```

---

## 📥 Cách 2: Lấy dữ liệu từ nguồn online

### Nguồn 1: Web scraping
```
https://thi-sat-hach.com/
https://thitructuyen.vn/
https://600cauhoi.vn/
```

### Nguồn 2: API (nếu có)
Một số website cung cấp API để lấy câu hỏi

### Nguồn 3: Ứng dụng khác
- Decompile APK của app "600 câu hỏi GPLX"
- Extract database từ app

---

## 📥 Cách 3: Nhập thủ công qua code

### Tạo file QuestionSeeder.java

```java
package com.example.btl_banglaixe.utils;

import android.content.Context;
import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.models.Question;

public class QuestionSeeder {
    
    public static void seedQuestions(Context context) {
        QuestionDAO dao = new QuestionDAO(context);
        
        // Câu hỏi điểm liệt (20 câu)
        addQuestion(dao, "Người điều khiển xe mô tô...", "A", "B", null, null, "A", "Câu hỏi điểm liệt", true, null, "Giải thích");
        // ... thêm 19 câu nữa
        
        // Khái niệm và quy tắc (100 câu)
        addQuestion(dao, "Khái niệm...", "A", "B", "C", null, "C", "Khái niệm và quy tắc", false, null, "Giải thích");
        // ... thêm 99 câu nữa
        
        dao.close();
    }
    
    private static void addQuestion(QuestionDAO dao, String question, String a, String b, 
                                   String c, String d, String correct, String category, 
                                   boolean critical, String image, String explanation) {
        Question q = new Question();
        q.setQuestionText(question);
        q.setOptionA(a);
        q.setOptionB(b);
        q.setOptionC(c);
        q.setOptionD(d);
        q.setCorrectAnswer(correct);
        q.setCategory(category);
        q.setCritical(critical);
        q.setImagePath(image);
        q.setExplanation(explanation);
        dao.addQuestion(q);
    }
}
```

---

## 📊 Cấu trúc dữ liệu cần có

### Bằng lái hạng A (250 câu)
- Câu hỏi điểm liệt: 20 câu
- Khái niệm và quy tắc: 100 câu
- Văn hóa và đạo đức: 10 câu
- Kỹ thuật lái xe: 15 câu
- Biển báo đường bộ: 65 câu
- Sa hình: 35 câu

### Bằng lái hạng B1, B2 (600 câu)
- Câu hỏi điểm liệt: 60 câu
- Khái niệm và quy tắc: 200 câu
- Văn hóa và đạo đức: 40 câu
- Kỹ thuật lái xe: 80 câu
- Biển báo đường bộ: 120 câu
- Sa hình: 100 câu

---

## 🔧 Tools hỗ trợ

### 1. Excel to JSON Converter
- Nhập câu hỏi vào Excel
- Convert sang JSON online: https://www.convertcsv.com/csv-to-json.htm

### 2. Python Script để crawl dữ liệu

```python
import requests
from bs4 import BeautifulSoup
import json

# Crawl từ website
url = "https://example.com/questions"
response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

questions = []
# Parse HTML và extract câu hỏi
# ...

# Save to JSON
with open('questions.json', 'w', encoding='utf-8') as f:
    json.dump({"questions": questions}, f, ensure_ascii=False, indent=2)
```

---

## ⚠️ Lưu ý

1. **Bản quyền**: Đảm bảo nguồn dữ liệu hợp pháp
2. **Kiểm tra**: Test kỹ trước khi import hàng loạt
3. **Backup**: Backup database trước khi import
4. **Validation**: Kiểm tra format đúng (A, B, C, D)
5. **Ảnh**: Nếu có ảnh, đặt trong `assets/images/`

---

## 📞 Liên hệ lấy dữ liệu

Bạn có thể:
1. Mua bộ câu hỏi từ các trung tâm đào tạo lái xe
2. Liên hệ Tổng cục Đường bộ VN
3. Sử dụng API từ các website học lái xe
4. Tự nhập từ sách giáo trình

---

## 🚀 Quick Start

```java
// Trong MainActivity onCreate()
// Chỉ chạy 1 lần khi cài app
SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
if (!prefs.getBoolean("questions_imported", false)) {
    int count = JsonImporter.importQuestionsFromAssets(this, "questions.json");
    if (count > 0) {
        prefs.edit().putBoolean("questions_imported", true).apply();
        Toast.makeText(this, "Đã import " + count + " câu hỏi", Toast.LENGTH_LONG).show();
    }
}
```
