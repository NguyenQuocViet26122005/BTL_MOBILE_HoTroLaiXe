# 📖 Hướng Dẫn Thêm 250 Câu Hỏi - TÓM TẮT

## 🎯 Mục Tiêu
Thêm đầy đủ 250 câu hỏi thi bằng lái xe hạng A vào ứng dụng.

---

## ⚡ Cách Nhanh Nhất (5 phút)

### 1. Cài đặt công cụ
```bash
pip install pandas openpyxl
```

### 2. Tạo file Excel mẫu
```bash
python tools/convert_excel_to_json.py --sample
```

### 3. Điền 250 câu vào file `sample_questions.xlsx`

### 4. Convert sang JSON
```bash
python tools/convert_excel_to_json.py sample_questions.xlsx questions_hang_a.json
```

### 5. Validate
```bash
python tools/validate_questions.py questions_hang_a.json
```

### 6. Copy vào app
```bash
copy questions_hang_a.json app\src\main\assets\
```

### 7. Test
- Xóa app khỏi thiết bị
- Build lại và cài đặt
- Kiểm tra từng chủ đề

---

## 📊 Số Lượng Câu Cần Thiết

| Chủ đề | Số câu | Điểm liệt |
|--------|--------|-----------|
| Câu hỏi điểm liệt | 20 | ✅ |
| Khái niệm và quy tắc | 100 | ❌ |
| Văn hóa và đạo đức | 10 | ❌ |
| Kỹ thuật lái xe | 15 | ❌ |
| Biển báo đường bộ | 65 | ❌ |
| Sa hình | 35 | ❌ |
| **TỔNG** | **250** | **20** |

---

## 🛠️ Công Cụ Có Sẵn

### 1. convert_excel_to_json.py
Convert Excel/CSV → JSON

### 2. validate_questions.py
Kiểm tra và validate JSON

### 3. download_questions.py
Tạo template 250 câu

---

## 📚 Tài Liệu Chi Tiết

| File | Mô tả |
|------|-------|
| **HOW_TO_ADD_250_QUESTIONS.md** | Hướng dẫn đầy đủ nhất |
| **QUICK_START_250_QUESTIONS.md** | Hướng dẫn nhanh |
| **HUONG_DAN_THEM_250_CAU.md** | Hướng dẫn chi tiết |
| **tools/README.md** | Hướng dẫn sử dụng tools |
| **SUMMARY_IMPROVEMENTS.md** | Tóm tắt cải tiến code |

---

## 🔍 Nguồn Dữ Liệu

### Chính thức
- https://thitructuyen.gplx.gov.vn/
- https://csgt.vn/

### GitHub
- Tìm: "gplx vietnam json"
- Tìm: "driving license vietnam"

### Ứng dụng
- 600 Câu Hỏi Thi GPLX
- Lái Xe Việt Nam

---

## ✅ Checklist

- [ ] Cài đặt Python & dependencies
- [ ] Có đủ 250 câu hỏi
- [ ] Convert sang JSON
- [ ] Validate thành công
- [ ] Copy vào app/src/main/assets/
- [ ] Test import thành công
- [ ] Test từng chủ đề

---

## 🐛 Lỗi Thường Gặp

### Lỗi: ModuleNotFoundError
```bash
pip install pandas openpyxl
```

### Lỗi: JSON syntax
```bash
python tools/validate_questions.py questions_hang_a.json
```

### Lỗi: Import thất bại
- Kiểm tra file có trong assets/
- Xóa app và cài lại
- Reset SharedPreferences

---

## 📞 Cần Giúp?

Đọc file **HOW_TO_ADD_250_QUESTIONS.md** để biết chi tiết!

---

**🎉 Chúc bạn thành công!**
