# Tools - Công Cụ Hỗ Trợ

Thư mục này chứa các công cụ Python để hỗ trợ quản lý câu hỏi.

## 📦 Cài Đặt

```bash
# Cài đặt Python 3.7+
# Cài đặt thư viện cần thiết
pip install pandas openpyxl
```

## 🛠️ Các Tool Có Sẵn

### 1. convert_word_to_json.py

Convert file Word (.docx) sang JSON format cho app.

**Cách dùng:**

```bash
# Convert Word → JSON
python tools/convert_word_to_json.py questions.docx

# Convert với output tùy chỉnh
python tools/convert_word_to_json.py questions.docx output.json
```

**Tính năng:**
- ✅ Tự động phát hiện format Word
- ✅ Hỗ trợ nhiều format khác nhau
- ✅ Tự động phân loại chủ đề
- ✅ Phát hiện câu điểm liệt
- ✅ Thống kê theo chủ đề

**Lưu ý:**
- Cần cài: `pip install python-docx`
- Xem chi tiết: [HUONG_DAN_CONVERT_WORD.md](../HUONG_DAN_CONVERT_WORD.md)

---

### 3. convert_excel_to_json.py

Convert file Excel/CSV sang JSON format cho app.

**Cách dùng:**

```bash
# Tạo file Excel mẫu
python tools/convert_excel_to_json.py --sample

# Convert Excel → JSON
python tools/convert_excel_to_json.py questions.xlsx

# Convert với output tùy chỉnh
python tools/convert_excel_to_json.py questions.xlsx output.json
```

**Format Excel cần có:**

| STT | Câu hỏi | Đáp án A | Đáp án B | Đáp án C | Đáp án D | Đáp án đúng | Chủ đề | Điểm liệt | Giải thích | Hình ảnh |
|-----|---------|----------|----------|----------|----------|-------------|--------|-----------|------------|----------|
| 1   | ...     | ...      | ...      | ...      | ...      | A           | ...    | 1         | ...        | ...      |

**Lưu ý:**
- Cột bắt buộc: Câu hỏi, Đáp án A, Đáp án B, Đáp án đúng, Chủ đề
- Cột tùy chọn: Đáp án C, Đáp án D, Điểm liệt, Giải thích, Hình ảnh
- Điểm liệt: 1 (có) hoặc 0 (không)
- Đáp án đúng: A, B, C, hoặc D

### 4. validate_questions.py

Kiểm tra và validate file JSON câu hỏi.

**Cách dùng:**

```bash
python tools/validate_questions.py app/src/main/assets/questions_hang_a.json
```

**Kiểm tra:**
- ✅ Syntax JSON hợp lệ
- ✅ Có đủ các field bắt buộc
- ✅ Đáp án đúng hợp lệ (A/B/C/D)
- ✅ Chủ đề hợp lệ
- ✅ Số lượng câu theo từng chủ đề
- ⚠️  Cảnh báo nếu thiếu giải thích

---

## 📝 Workflow Khuyến Nghị

### Bước 1: Chuẩn bị dữ liệu

Tạo file Excel với 250 câu hỏi theo format mẫu:

```bash
python tools/convert_excel_to_json.py --sample
```

Mở file `sample_questions.xlsx` và điền đầy đủ 250 câu.

### Bước 2: Convert sang JSON

```bash
python tools/convert_excel_to_json.py questions_250.xlsx questions_hang_a.json
```

### Bước 3: Validate

```bash
python tools/validate_questions.py questions_hang_a.json
```

### Bước 4: Copy vào app

```bash
# Windows
copy questions_hang_a.json app\src\main\assets\

# Linux/Mac
cp questions_hang_a.json app/src/main/assets/
```

### Bước 5: Test trong app

1. Xóa app khỏi thiết bị
2. Build và cài đặt lại
3. Kiểm tra import thành công
4. Test từng chủ đề

---

## 🎯 Số Lượng Câu Chuẩn

| Chủ đề | Số câu |
|--------|--------|
| Câu hỏi điểm liệt | 20 |
| Khái niệm và quy tắc | 100 |
| Văn hóa và đạo đức | 10 |
| Kỹ thuật lái xe | 15 |
| Biển báo đường bộ | 65 |
| Sa hình | 35 |
| **TỔNG** | **250** |

---

## 🐛 Troubleshooting

### Lỗi: ModuleNotFoundError: No module named 'pandas'

```bash
pip install pandas openpyxl
```

### Lỗi: UnicodeDecodeError

Đảm bảo file Excel/CSV được lưu với encoding UTF-8.

### Lỗi: JSON syntax error

Chạy validate để kiểm tra:

```bash
python tools/validate_questions.py your_file.json
```

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, hãy:
1. Kiểm tra format Excel có đúng không
2. Chạy validate để xem lỗi cụ thể
3. Xem file `sample_questions.xlsx` để tham khảo
