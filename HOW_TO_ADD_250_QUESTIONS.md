# 🎯 Cách Thêm 250 Câu Hỏi - Hướng Dẫn Đầy Đủ

## 📚 Tài Liệu Tham Khảo

Trước khi bắt đầu, hãy đọc các file sau:

1. **QUICK_START_250_QUESTIONS.md** - Hướng dẫn nhanh 5 phút
2. **HUONG_DAN_THEM_250_CAU.md** - Hướng dẫn chi tiết đầy đủ
3. **tools/README.md** - Hướng dẫn sử dụng công cụ
4. **SUMMARY_IMPROVEMENTS.md** - Tóm tắt các cải tiến

---

## 🚀 Bắt Đầu Nhanh

### Phương Án 1: Sử dụng Excel (Khuyến nghị)

```bash
# Bước 1: Tạo file Excel mẫu
python tools/convert_excel_to_json.py --sample

# Bước 2: Mở file sample_questions.xlsx và điền 250 câu

# Bước 3: Convert sang JSON
python tools/convert_excel_to_json.py questions_250.xlsx questions_hang_a.json

# Bước 4: Validate
python tools/validate_questions.py questions_hang_a.json

# Bước 5: Copy vào app
copy questions_hang_a.json app\src\main\assets\
```

### Phương Án 2: Tìm Nguồn Có Sẵn

```bash
# Tìm trên GitHub
https://github.com/search?q=gplx+vietnam+json

# Hoặc tải từ website chính thức
https://thitructuyen.gplx.gov.vn/

# Sau đó convert sang format của bạn
python tools/convert_excel_to_json.py downloaded_questions.xlsx
```

---

## 📋 Cấu Trúc Câu Hỏi

### Format JSON Chuẩn

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
      "category": "Khái niệm và quy tắc",
      "is_critical": 0,
      "image_path": null,
      "explanation": "Giải thích câu trả lời"
    }
  ]
}
```

### Các Category Hợp Lệ

| Category | Số câu | is_critical |
|----------|--------|-------------|
| Câu hỏi điểm liệt | 20 | 1 |
| Khái niệm và quy tắc | 100 | 0 |
| Văn hóa và đạo đức | 10 | 0 |
| Kỹ thuật lái xe | 15 | 0 |
| Biển báo đường bộ | 65 | 0 |
| Sa hình | 35 | 0 |

---

## 🛠️ Công Cụ Hỗ Trợ

### 1. convert_excel_to_json.py

Convert Excel/CSV sang JSON format cho app.

**Tính năng:**
- ✅ Tự động validate dữ liệu
- ✅ Thống kê theo chủ đề
- ✅ Báo lỗi chi tiết
- ✅ Tạo file mẫu

**Cách dùng:**
```bash
# Tạo file mẫu
python tools/convert_excel_to_json.py --sample

# Convert
python tools/convert_excel_to_json.py input.xlsx output.json
```

### 2. validate_questions.py

Kiểm tra và validate file JSON.

**Kiểm tra:**
- ✅ Syntax JSON
- ✅ Required fields
- ✅ Đáp án hợp lệ
- ✅ Số lượng câu
- ⚠️  Cảnh báo thiếu giải thích

**Cách dùng:**
```bash
python tools/validate_questions.py questions_hang_a.json
```

### 3. download_questions.py

Tạo template 250 câu hỏi.

**Cách dùng:**
```bash
python tools/download_questions.py questions_template.json
```

---

## 📝 Workflow Khuyến Nghị

### Bước 1: Chuẩn Bị

```bash
# Cài đặt Python dependencies
pip install -r requirements.txt

# Tạo file Excel mẫu
python tools/convert_excel_to_json.py --sample
```

### Bước 2: Nhập Dữ Liệu

Mở `sample_questions.xlsx` và điền theo format:

| STT | Câu hỏi | Đáp án A | Đáp án B | Đáp án C | Đáp án D | Đáp án đúng | Chủ đề | Điểm liệt | Giải thích |
|-----|---------|----------|----------|----------|----------|-------------|--------|-----------|------------|

**Tips:**
- Điền từng chủ đề một
- Copy-paste từ nguồn có sẵn
- Kiểm tra đáp án đúng
- Thêm giải thích cho mỗi câu

### Bước 3: Convert & Validate

```bash
# Convert
python tools/convert_excel_to_json.py questions_250.xlsx questions_hang_a.json

# Validate
python tools/validate_questions.py questions_hang_a.json
```

### Bước 4: Import vào App

```bash
# Copy file
copy questions_hang_a.json app\src\main\assets\

# Hoặc trên Linux/Mac
cp questions_hang_a.json app/src/main/assets/
```

### Bước 5: Test

```bash
# Build app
./gradlew assembleDebug

# Cài đặt trên thiết bị
adb install app/build/outputs/apk/debug/app-debug.apk

# Test từng chủ đề
```

---

## 🔍 Nguồn Dữ Liệu

### Website Chính Thức
- https://thitructuyen.gplx.gov.vn/
- https://csgt.vn/

### GitHub Repositories
```bash
# Tìm kiếm
https://github.com/search?q=gplx+vietnam
https://github.com/search?q=driving+license+vietnam+questions
```

### Ứng Dụng Tham Khảo
- 600 Câu Hỏi Thi GPLX (Google Play)
- Lái Xe Việt Nam
- Thi Bằng Lái Xe

### Sách Giáo Trình
- Sách 600 câu hỏi thi GPLX của Bộ GTVT
- Giáo trình học lái xe hạng A1, A2

---

## ⚠️ Lưu Ý Quan Trọng

### 1. Bản Quyền
- ✅ Sử dụng nguồn chính thức từ Bộ GTVT
- ✅ Chỉ dùng cho mục đích học tập
- ❌ Không sao chép từ ứng dụng thương mại

### 2. Độ Chính Xác
- ✅ Kiểm tra kỹ đáp án đúng
- ✅ Đối chiếu với giáo trình chính thức
- ✅ Test kỹ trước khi phát hành

### 3. Hình Ảnh
- Câu hỏi biển báo cần có hình
- Câu hỏi sa hình cần có hình
- Lưu trong `app/src/main/assets/images/`
- Format: JPG/PNG, kích thước < 500KB

---

## 🐛 Troubleshooting

### Lỗi: ModuleNotFoundError

```bash
# Cài đặt dependencies
pip install pandas openpyxl
```

### Lỗi: JSON Syntax Error

```bash
# Validate JSON
python tools/validate_questions.py questions_hang_a.json

# Hoặc dùng online tool
https://jsonlint.com/
```

### Lỗi: Import Thất Bại

```
Kiểm tra:
1. File có trong thư mục assets?
2. File name đúng: questions_hang_a.json?
3. Đã xóa app và cài lại?
4. SharedPreferences đã reset?
```

### Lỗi: Thiếu Câu Hỏi

```bash
# Kiểm tra số lượng
python tools/validate_questions.py questions_hang_a.json

# Xem thống kê theo chủ đề
```

---

## ✅ Checklist Hoàn Thành

### Chuẩn Bị
- [ ] Đã cài Python 3.7+
- [ ] Đã cài dependencies
- [ ] Đã đọc hướng dẫn

### Dữ Liệu
- [ ] Đã có đủ 250 câu hỏi
- [ ] Đã phân loại đúng chủ đề
- [ ] Đã đánh dấu câu điểm liệt
- [ ] Đã có giải thích cho mỗi câu
- [ ] Đã có hình ảnh (nếu cần)

### Validation
- [ ] JSON syntax hợp lệ
- [ ] Validate thành công
- [ ] Số lượng câu đúng
- [ ] Đáp án đúng 100%

### Testing
- [ ] Import thành công
- [ ] Hiển thị đúng từng chủ đề
- [ ] Làm bài được
- [ ] Đáp án hiển thị đúng
- [ ] Giải thích hiển thị đúng

---

## 📞 Cần Hỗ Trợ?

Nếu bạn cần:
- ✉️ Convert file khác format
- ✉️ Script tự động hóa
- ✉️ Fix lỗi import
- ✉️ Tối ưu performance

Hãy mô tả vấn đề cụ thể và tôi sẽ giúp bạn!

---

## 🎉 Kết Luận

Với hướng dẫn này, bạn có thể:
1. ✅ Thêm 250 câu hỏi nhanh chóng
2. ✅ Validate tự động
3. ✅ Tránh lỗi phổ biến
4. ✅ Test đầy đủ

**Chúc bạn thành công! 🚀**

---

## 📚 Tài Liệu Liên Quan

- [QUICK_START_250_QUESTIONS.md](QUICK_START_250_QUESTIONS.md) - Hướng dẫn nhanh
- [HUONG_DAN_THEM_250_CAU.md](HUONG_DAN_THEM_250_CAU.md) - Hướng dẫn chi tiết
- [tools/README.md](tools/README.md) - Hướng dẫn tools
- [SUMMARY_IMPROVEMENTS.md](SUMMARY_IMPROVEMENTS.md) - Tóm tắt cải tiến
