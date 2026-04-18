#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Tool đơn giản convert PDF sang SQLite
"""

import sqlite3
import re
import sys

try:
    import pdfplumber
except ImportError:
    print("\n❌ Chưa cài thư viện pdfplumber!")
    print("\n📦 Hãy chạy lệnh sau:")
    print("   python -m pip install pdfplumber")
    print("\nSau đó chạy lại tool này.")
    sys.exit(1)

def get_category(num):
    """Phân loại câu hỏi"""
    if 1 <= num <= 60: return "Khái niệm và quy tắc"
    elif 61 <= num <= 75: return "Văn hóa và đạo đức"
    elif 76 <= num <= 108: return "Kỹ thuật lái xe"
    elif 109 <= num <= 164: return "Biển báo đường bộ"
    elif 165 <= num <= 250: return "Sa hình"
    return "Khác"

def is_critical(text):
    """Kiểm tra câu điểm liệt"""
    keywords = ['ma túy', 'rượu', 'bia', 'nồng độ cồn']
    return any(k in text.lower() for k in keywords)

def has_image(num):
    """Kiểm tra có ảnh không"""
    return num in [29, 30] or (126 <= num <= 250 and num != 229)

def parse_pdf(pdf_file):
    """Đọc PDF và trích xuất câu hỏi"""
    print(f"\n📄 Đang đọc file: {pdf_file}")
    
    with pdfplumber.open(pdf_file) as pdf:
        text = ""
        for i, page in enumerate(pdf.pages, 1):
            text += page.extract_text() + "\n"
            print(f"   Trang {i}/{len(pdf.pages)}", end='\r')
    
    print("\n\n🔍 Đang tìm câu hỏi...")
    
    # Tìm tất cả câu hỏi
    pattern = r'Câu\s+(?:hỏi\s+)?(\d+)[:\.]?\s*(.*?)(?=Câu\s+(?:hỏi\s+)?\d+|$)'
    matches = list(re.finditer(pattern, text, re.DOTALL | re.IGNORECASE))
    
    questions = []
    for match in matches:
        num = int(match.group(1))
        content = match.group(2).strip()
        
        q = parse_question(content, num)
        if q:
            questions.append(q)
            print(f"   ✓ Câu {num}")
    
    return questions

def parse_question(content, num):
    """Parse 1 câu hỏi"""
    lines = [l.strip() for l in content.split('\n') if l.strip()]
    
    question = ""
    options = {"A": "", "B": "", "C": "", "D": ""}
    correct = ""
    
    for line in lines:
        if "CÂU LIỆT" in line.upper():
            continue
        
        # Tìm đáp án
        m = re.match(r'^([1-4A-D])[\.:\)]\s*(.+)', line, re.IGNORECASE)
        if m:
            opt = m.group(1).upper()
            text = m.group(2).strip()
            
            # Convert số sang chữ
            if opt.isdigit():
                opt = chr(64 + int(opt))
            
            options[opt] = text
            
            # Kiểm tra đáp án đúng
            if '✓' in line or '✔' in line:
                correct = opt
        else:
            if not any(options.values()):
                question += " " + line
    
    question = question.strip()
    
    # Nếu không tìm được đáp án đúng, hỏi
    if not correct:
        print(f"\n⚠️  Câu {num}: {question[:60]}...")
        for k, v in options.items():
            if v:
                print(f"    {k}. {v[:50]}...")
        
        while True:
            ans = input("    👉 Đáp án đúng (A/B/C/D hoặc 1/2/3/4): ").strip().upper()
            if ans in ['A', 'B', 'C', 'D']:
                correct = ans
                break
            elif ans in ['1', '2', '3', '4']:
                correct = chr(64 + int(ans))
                break
    
    return {
        'id': num,
        'question_text': question,
        'option_a': options['A'],
        'option_b': options['B'],
        'option_c': options['C'] or None,
        'option_d': options['D'] or None,
        'correct_answer': correct,
        'category': get_category(num),
        'is_critical': 1 if is_critical(question) else 0,
        'image_path': f"question_{num}" if has_image(num) else None
    }

def create_database(questions):
    """Tạo SQLite database"""
    print("\n💾 Đang tạo database...")
    
    conn = sqlite3.connect('questions.db')
    c = conn.cursor()
    
    # Tạo bảng
    c.execute('''
        CREATE TABLE IF NOT EXISTS questions (
            id INTEGER PRIMARY KEY,
            question_text TEXT NOT NULL,
            option_a TEXT NOT NULL,
            option_b TEXT NOT NULL,
            option_c TEXT,
            option_d TEXT,
            correct_answer TEXT NOT NULL,
            category TEXT NOT NULL,
            is_critical INTEGER DEFAULT 0,
            image_path TEXT,
            explanation TEXT
        )
    ''')
    
    # Insert câu hỏi
    for q in questions:
        c.execute('''
            INSERT OR REPLACE INTO questions 
            (id, question_text, option_a, option_b, option_c, option_d, 
             correct_answer, category, is_critical, image_path)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ''', (
            q['id'], q['question_text'], q['option_a'], q['option_b'],
            q['option_c'], q['option_d'], q['correct_answer'],
            q['category'], q['is_critical'], q['image_path']
        ))
    
    conn.commit()
    conn.close()
    
    print("   ✓ Đã tạo file: questions.db")

def main():
    print("=" * 60)
    print("  🚗 TOOL CONVERT PDF SANG SQLITE - BẰNG LÁI XE")
    print("=" * 60)
    
    if len(sys.argv) < 2:
        print("\n📖 Cách dùng:")
        print("   python convert_pdf.py <file.pdf>")
        print("\n💡 Ví dụ:")
        print("   python convert_pdf.py questions_250.pdf")
        print("\n" + "=" * 60)
        sys.exit(1)
    
    pdf_file = sys.argv[1]
    
    # Parse PDF
    questions = parse_pdf(pdf_file)
    
    if not questions:
        print("\n❌ Không tìm thấy câu hỏi nào!")
        sys.exit(1)
    
    # Tạo database
    create_database(questions)
    
    # Thống kê
    print("\n" + "=" * 60)
    print(f"✅ HOÀN THÀNH!")
    print(f"   📊 Tổng câu: {len(questions)}")
    print(f"   📷 Câu có ảnh: {sum(1 for q in questions if q['image_path'])}")
    print(f"   ⚠️  Câu điểm liệt: {sum(1 for q in questions if q['is_critical'])}")
    
    # Phân loại
    cats = {}
    for q in questions:
        cats[q['category']] = cats.get(q['category'], 0) + 1
    
    print("\n   📚 Phân loại:")
    for cat, count in sorted(cats.items()):
        print(f"      • {cat}: {count} câu")
    
    print("\n" + "=" * 60)
    print("📱 BƯỚC TIẾP THEO:")
    print("   1. Tạo thư mục: app/src/main/assets/databases/")
    print("   2. Copy file 'questions.db' vào thư mục đó")
    print("   3. Chạy app")
    print("=" * 60)

if __name__ == "__main__":
    main()
