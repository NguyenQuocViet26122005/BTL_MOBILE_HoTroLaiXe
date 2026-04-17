# 🚀 Hướng Dẫn Nhanh: Thêm 250 Câu Hỏi

## ⚡ Cách Nhanh Nhất (5 phút)

### Bước 1: Tìm bộ câu hỏi có sẵn

Tìm trên Google/GitHub:
```
"600 câu hỏi thi bằng lái xe json"
"gplx vietnam questions dataset"
"driving license questions vietnam"
```

Hoặc tải từ các nguồn:
- https://github.com/search?q=gplx+vietnam
- https://thitructuyen.gplx.gov.vn/

### Bước 2: Convert sang format của bạn

Nếu có file Excel/CSV:
```bash
python tools/convert_excel_to_json.py questions.xlsx
```

Nếu có file JSON khác format:
- Mở file JSON
- Copy-paste và sửa lại theo format mẫu

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

### Bước 5: Test

1. Xóa app khỏi thiết bị
2. Build lại: `./gradlew assembleDebug`
3. Cài đặt và test

---

## 📝 Cách Thủ Công (Nếu không tìm được nguồn)

### Bước 1: Tạo file Excel mẫu

```bash
python tools/convert_excel_to_json.py --sample
```

### Bước 2: Mở file `sample_questions.xlsx`

Điền 250 câu theo format:

| STT | Câu hỏi | Đáp án A | Đáp án B | Đáp án C | Đáp án D | Đáp án đúng | Chủ đề | Điểm liệt | Giải thích |
|-----|---------|----------|----------|----------|----------|-------------|--------|-----------|------------|
| 1   | ...     | ...      | ...      | ...      | ...      | A           | ...    | 1         | ...        |

### Bước 3: Convert

```bash
python tools/convert_excel_to_json.py questions_250.xlsx questions_hang_a.json
```

### Bước 4-5: Giống như trên

---

## 🎯 Số Lượng Cần Thiết

| Chủ đề | Số câu | Điểm liệt |
|--------|--------|-----------|
| Câu hỏi điểm liệt | 20 | ✅ Có |
| Khái niệm và quy tắc | 100 | ❌ Không |
| Văn hóa và đạo đức | 10 | ❌ Không |
| Kỹ thuật lái xe | 15 | ❌ Không |
| Biển báo đường bộ | 65 | ❌ Không |
| Sa hình | 35 | ❌ Không |
| **TỔNG** | **250** | **20** |

---

## 🔍 Nguồn Tham Khảo

### Website chính thức:
- https://thitructuyen.gplx.gov.vn/
- https://csgt.vn/

### Ứng dụng tham khảo:
- 600 Câu Hỏi Thi GPLX (Google Play)
- Lái Xe Việt Nam
- Thi Bằng Lái Xe

### GitHub:
```bash
# Tìm kiếm trên GitHub
https://github.com/search?q=gplx+vietnam
https://github.com/search?q=driving+license+vietnam
```

### Sách giáo trình:
- Sách 600 câu hỏi thi GPLX của Bộ GTVT
- Giáo trình học lái xe hạng A1, A2

---

## ⚠️ Lưu Ý Quan Trọng

### 1. Bản quyền
- Chỉ sử dụng cho mục đích học tập
- Không sao chép trực tiếp từ ứng dụng thương mại
- Ưu tiên nguồn chính thức từ Bộ GTVT

### 2. Độ chính xác
- Kiểm tra kỹ đáp án đúng
- Đối chiếu với giáo trình chính thức
- Test kỹ trước khi phát hành

### 3. Hình ảnh
- Câu hỏi biển báo cần có hình
- Câu hỏi sa hình cần có hình
- Lưu hình trong `app/src/main/assets/images/`

---

## 🐛 Troubleshooting

### Lỗi: Import thất bại
```
Kiểm tra:
1. File JSON có đúng format không?
2. File có trong thư mục assets không?
3. Đã xóa app và cài lại chưa?
```

### Lỗi: Không hiển thị câu hỏi
```
Kiểm tra:
1. SharedPreferences đã được reset chưa?
2. Database có dữ liệu không?
3. Category name có đúng không?
```

### Lỗi: Thiếu câu hỏi
```
Chạy validate:
python tools/validate_questions.py questions_hang_a.json
```

---

## 📞 Cần Giúp Đỡ?

Nếu bạn:
- Có file Excel/CSV cần convert
- Có file JSON khác format cần chuyển đổi
- Gặp lỗi khi import
- Cần script tự động hóa

Hãy cho tôi biết format dữ liệu bạn có và tôi sẽ viết tool hỗ trợ!

---

## ✅ Checklist

- [ ] Đã có đủ 250 câu hỏi
- [ ] Đã validate JSON thành công
- [ ] Đã test import trong app
- [ ] Đã kiểm tra từng chủ đề
- [ ] Đã test làm bài
- [ ] Đáp án đúng 100%
- [ ] Có giải thích cho mỗi câu
- [ ] Hình ảnh hiển thị đúng (nếu có)

---

**🎉 Chúc bạn thành công!**
