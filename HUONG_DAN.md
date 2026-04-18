# Hướng Dẫn Convert PDF sang SQLite

## Bước 1: Cài Python (nếu chưa có)

1. Tải Python: https://www.python.org/downloads/
2. Chạy file cài đặt
3. **QUAN TRỌNG:** Tick vào ô "Add Python to PATH"
4. Click "Install Now"
5. Khởi động lại PowerShell

## Bước 2: Cài thư viện

Mở PowerShell, chạy:

```bash
python -m pip install pdfplumber
```

## Bước 3: Đặt file PDF

Đặt file PDF vào thư mục gốc project (cùng cấp với file `convert_pdf.py`)

## Bước 4: Chạy tool

```bash
python convert_pdf.py questions_250.pdf
```

(Thay `questions_250.pdf` bằng tên file PDF của bạn)

## Bước 5: Trả lời câu hỏi

Khi tool hỏi đáp án đúng, nhập A/B/C/D hoặc 1/2/3/4 rồi Enter.

## Bước 6: Copy database vào app

1. Tạo thư mục: `app/src/main/assets/databases/`
2. Copy file `questions.db` vào đó
3. Chạy app

## Lưu ý về ảnh

Nếu câu hỏi có ảnh (câu 29, 30, 126-250 trừ 229):
- Trích xuất ảnh từ PDF (dùng Adobe Acrobat hoặc online tool)
- Đặt tên: `question_29.jpg`, `question_30.jpg`, ...
- Copy vào: `app/src/main/assets/images/`

## Nếu gặp lỗi

### Lỗi: "pip is not recognized"

Chạy:
```bash
python -m pip install pdfplumber
```

### Lỗi: "python is not recognized"

Python chưa được cài hoặc chưa thêm vào PATH. Cài lại Python và nhớ tick "Add Python to PATH".

### Lỗi: "No module named 'pdfplumber'"

Chạy:
```bash
python -m pip install pdfplumber
```

## Hỗ trợ

Nếu gặp vấn đề, chụp màn hình lỗi và hỏi lại.
