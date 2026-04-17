# Hướng Dẫn Thêm Đầy Đủ 250 Câu Hỏi Bằng Lái Xe Hạng A

## 📋 Tổng Quan

Bạn cần thêm 250 câu hỏi theo cấu trúc:
- **Câu hỏi điểm liệt**: 20 câu (is_critical = 1)
- **Khái niệm và quy tắc**: 100 câu
- **Văn hóa và đạo đức**: 10 câu
- **Kỹ thuật lái xe**: 15 câu
- **Biển báo đường bộ**: 65 câu
- **Sa hình**: 35 câu

---

## 🎯 Phương Án 1: Sử dụng Tool Python (Khuyến nghị)

### Bước 1: Chuẩn bị dữ liệu

Tạo file Excel hoặc CSV với các cột:
```
STT | Câu hỏi | Đáp án A | Đáp án B | Đáp án C | Đáp án D | Đáp án đúng | Chủ đề | Điểm liệt | Giải thích
```

### Bước 2: Chạy tool Python

```bash
# Cài đặt thư viện cần thiết
pip install pandas openpyxl

# Chạy tool convert
python tools/convert_excel_to_json.py
```

Tool sẽ tự động convert Excel → JSON với format đúng.

---

## 📝 Phương Án 2: Nhập Thủ Công

### Cấu trúc JSON chuẩn:

```json
{
  "question_text": "Nội dung câu hỏi?",
  "option_a": "Đáp án A",
  "option_b": "Đáp án B",
  "option_c": "Đáp án C (hoặc null nếu không có)",
  "option_d": "Đáp án D (hoặc null nếu không có)",
  "correct_answer": "A",
  "category": "Khái niệm và quy tắc",
  "is_critical": 0,
  "image_path": null,
  "explanation": "Giải thích câu trả lời"
}
```

### Các category hợp lệ:
- `"Câu hỏi điểm liệt"`
- `"Khái niệm và quy tắc"`
- `"Văn hóa và đạo đức"`
- `"Kỹ thuật lái xe"`
- `"Biển báo đường bộ"`
- `"Sa hình"`

### Lưu ý quan trọng:
- `is_critical`: 1 = câu điểm liệt, 0 = câu thường
- `correct_answer`: "A", "B", "C", hoặc "D"
- `option_c`, `option_d`: có thể để `null` nếu chỉ có 2 đáp án
- `image_path`: để `null` nếu không có hình, hoặc `"images/cau_1.jpg"` nếu có

---

## 🌐 Phương Án 3: Tải Bộ Câu Hỏi Có Sẵn

### Nguồn dữ liệu:

1. **Website chính thức**:
   - https://thitructuyen.gplx.gov.vn/
   - Tải bộ câu hỏi PDF/Word từ Cục CSGT

2. **Ứng dụng tham khảo**:
   - 600 Câu Hỏi Thi GPLX
   - Lái Xe Việt Nam
   - Thi Bằng Lái Xe

3. **GitHub**:
   - Tìm kiếm: "gplx vietnam questions json"
   - Nhiều repo có sẵn bộ 250 câu format JSON

---

## 🛠️ Phương Án 4: Sử dụng API/Web Scraping

Nếu bạn có kỹ năng lập trình, có thể:
1. Scrape từ website thi thử online
2. Sử dụng API công khai (nếu có)
3. OCR từ file PDF chính thức

---

## ✅ Kiểm Tra Sau Khi Thêm

### 1. Validate JSON:
```bash
# Kiểm tra syntax JSON
python -m json.tool app/src/main/assets/questions_hang_a.json
```

### 2. Kiểm tra số lượng:
```python
import json

with open('app/src/main/assets/questions_hang_a.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    print(f"Tổng số câu: {len(data['questions'])}")
    
    # Đếm theo category
    categories = {}
    for q in data['questions']:
        cat = q['category']
        categories[cat] = categories.get(cat, 0) + 1
    
    for cat, count in categories.items():
        print(f"{cat}: {count} câu")
```

### 3. Test trong app:
- Xóa app khỏi thiết bị
- Cài đặt lại
- Kiểm tra import thành công
- Thử làm bài từng chủ đề

---

## 📦 Template Nhanh

### Câu hỏi điểm liệt (20 câu):
```json
{
  "question_text": "...",
  "option_a": "...",
  "option_b": "...",
  "option_c": null,
  "option_d": null,
  "correct_answer": "A",
  "category": "Câu hỏi điểm liệt",
  "is_critical": 1,
  "image_path": null,
  "explanation": "..."
}
```

### Câu hỏi thường:
```json
{
  "question_text": "...",
  "option_a": "...",
  "option_b": "...",
  "option_c": "...",
  "option_d": null,
  "correct_answer": "C",
  "category": "Khái niệm và quy tắc",
  "is_critical": 0,
  "image_path": null,
  "explanation": "..."
}
```

---

## 🚀 Khuyến Nghị

**Cách nhanh nhất**: 
1. Tìm bộ câu hỏi có sẵn trên GitHub
2. Convert sang format JSON của bạn bằng tool Python
3. Import vào app và test

**Cách chất lượng nhất**:
1. Tải bộ câu hỏi chính thức từ Cục CSGT
2. Nhập thủ công hoặc dùng tool
3. Review kỹ từng câu

---

## 📞 Hỗ Trợ

Nếu cần tool Python để convert, hãy cho tôi biết format dữ liệu bạn có (Excel, CSV, PDF, etc.) và tôi sẽ viết script cho bạn!
