#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Tool Convert Excel/CSV sang JSON cho ứng dụng Thi Bằng Lái Xe
Hỗ trợ: Excel (.xlsx), CSV (.csv)
"""

import json
import pandas as pd
import sys
from pathlib import Path


def convert_excel_to_json(input_file, output_file):
    """
    Convert file Excel/CSV sang JSON format cho app
    
    Format Excel cần có các cột:
    - STT (optional)
    - Câu hỏi
    - Đáp án A
    - Đáp án B
    - Đáp án C (optional)
    - Đáp án D (optional)
    - Đáp án đúng (A/B/C/D)
    - Chủ đề
    - Điểm liệt (1/0 hoặc Có/Không)
    - Giải thích (optional)
    - Hình ảnh (optional)
    """
    
    print(f"📖 Đang đọc file: {input_file}")
    
    # Đọc file
    if input_file.endswith('.csv'):
        df = pd.read_csv(input_file, encoding='utf-8')
    else:
        df = pd.read_excel(input_file)
    
    print(f"✅ Đã đọc {len(df)} dòng")
    
    # Mapping tên cột (case-insensitive)
    column_mapping = {
        'câu hỏi': 'question_text',
        'nội dung': 'question_text',
        'đáp án a': 'option_a',
        'a': 'option_a',
        'đáp án b': 'option_b',
        'b': 'option_b',
        'đáp án c': 'option_c',
        'c': 'option_c',
        'đáp án d': 'option_d',
        'd': 'option_d',
        'đáp án đúng': 'correct_answer',
        'đáp án': 'correct_answer',
        'đúng': 'correct_answer',
        'chủ đề': 'category',
        'loại': 'category',
        'điểm liệt': 'is_critical',
        'critical': 'is_critical',
        'giải thích': 'explanation',
        'lý do': 'explanation',
        'hình ảnh': 'image_path',
        'ảnh': 'image_path'
    }
    
    # Rename columns
    df.columns = df.columns.str.lower().str.strip()
    df.rename(columns=column_mapping, inplace=True)
    
    # Validate required columns
    required = ['question_text', 'option_a', 'option_b', 'correct_answer', 'category']
    missing = [col for col in required if col not in df.columns]
    if missing:
        print(f"❌ Thiếu các cột bắt buộc: {missing}")
        return False
    
    # Convert data
    questions = []
    
    for idx, row in df.iterrows():
        # Xử lý is_critical
        is_critical = 0
        if 'is_critical' in row:
            val = str(row['is_critical']).lower()
            if val in ['1', 'có', 'yes', 'true', 'x']:
                is_critical = 1
        
        # Xử lý correct_answer
        correct = str(row['correct_answer']).upper().strip()
        if correct not in ['A', 'B', 'C', 'D']:
            print(f"⚠️  Dòng {idx+2}: Đáp án đúng không hợp lệ: {correct}")
            continue
        
        # Xử lý option_c, option_d
        option_c = row.get('option_c', None)
        option_d = row.get('option_d', None)
        
        if pd.isna(option_c) or str(option_c).strip() == '':
            option_c = None
        if pd.isna(option_d) or str(option_d).strip() == '':
            option_d = None
        
        # Xử lý explanation
        explanation = row.get('explanation', '')
        if pd.isna(explanation):
            explanation = ''
        
        # Xử lý image_path
        image_path = row.get('image_path', None)
        if pd.isna(image_path) or str(image_path).strip() == '':
            image_path = None
        
        question = {
            "question_text": str(row['question_text']).strip(),
            "option_a": str(row['option_a']).strip(),
            "option_b": str(row['option_b']).strip(),
            "option_c": option_c,
            "option_d": option_d,
            "correct_answer": correct,
            "category": str(row['category']).strip(),
            "is_critical": is_critical,
            "image_path": image_path,
            "explanation": str(explanation).strip()
        }
        
        questions.append(question)
    
    # Tạo JSON output
    output_data = {
        "questions": questions
    }
    
    # Ghi file
    print(f"💾 Đang ghi file: {output_file}")
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(output_data, f, ensure_ascii=False, indent=2)
    
    print(f"✅ Hoàn thành! Đã convert {len(questions)} câu hỏi")
    
    # Thống kê
    print("\n📊 Thống kê theo chủ đề:")
    categories = {}
    for q in questions:
        cat = q['category']
        categories[cat] = categories.get(cat, 0) + 1
    
    for cat, count in sorted(categories.items()):
        print(f"  - {cat}: {count} câu")
    
    critical_count = sum(1 for q in questions if q['is_critical'] == 1)
    print(f"\n⚠️  Câu hỏi điểm liệt: {critical_count} câu")
    
    return True


def create_sample_excel(output_file='sample_questions.xlsx'):
    """Tạo file Excel mẫu"""
    
    data = {
        'STT': [1, 2, 3],
        'Câu hỏi': [
            'Người điều khiển phương tiện giao thông đường bộ mà trong cơ thể có chất ma túy có bị nghiêm cấm hay không?',
            'Khái niệm "Người tham gia giao thông đường bộ" được hiểu như thế nào?',
            'Người lái xe phải làm gì khi tham gia giao thông?'
        ],
        'Đáp án A': [
            'Bị nghiêm cấm',
            'Là người điều khiển phương tiện',
            'Điều khiển đúng tốc độ'
        ],
        'Đáp án B': [
            'Không bị nghiêm cấm',
            'Là người đi bộ',
            'Chấp hành quy tắc giao thông'
        ],
        'Đáp án C': [
            None,
            'Là người điều khiển, người sử dụng phương tiện và người đi bộ',
            'Cả ý 1 và ý 2'
        ],
        'Đáp án D': [None, None, None],
        'Đáp án đúng': ['A', 'C', 'C'],
        'Chủ đề': [
            'Câu hỏi điểm liệt',
            'Khái niệm và quy tắc',
            'Văn hóa và đạo đức'
        ],
        'Điểm liệt': [1, 0, 0],
        'Giải thích': [
            'Tuyệt đối nghiêm cấm điều khiển phương tiện khi có chất ma túy trong cơ thể.',
            'Người tham gia giao thông bao gồm cả người điều khiển, người sử dụng và người đi bộ.',
            'Người lái xe phải chấp hành đầy đủ các quy tắc giao thông.'
        ],
        'Hình ảnh': [None, None, None]
    }
    
    df = pd.DataFrame(data)
    df.to_excel(output_file, index=False)
    print(f"✅ Đã tạo file mẫu: {output_file}")


if __name__ == "__main__":
    print("=" * 60)
    print("🚗 TOOL CONVERT EXCEL/CSV → JSON CHO ỨNG DỤNG THI BẰNG LÁI XE")
    print("=" * 60)
    print()
    
    if len(sys.argv) < 2:
        print("📝 Cách sử dụng:")
        print("  python convert_excel_to_json.py <input_file> [output_file]")
        print()
        print("  Ví dụ:")
        print("    python convert_excel_to_json.py questions.xlsx")
        print("    python convert_excel_to_json.py questions.csv output.json")
        print()
        print("🎯 Tạo file Excel mẫu:")
        print("    python convert_excel_to_json.py --sample")
        print()
        sys.exit(1)
    
    if sys.argv[1] == '--sample':
        create_sample_excel()
        sys.exit(0)
    
    input_file = sys.argv[1]
    
    if not Path(input_file).exists():
        print(f"❌ File không tồn tại: {input_file}")
        sys.exit(1)
    
    # Output file
    if len(sys.argv) >= 3:
        output_file = sys.argv[2]
    else:
        output_file = Path(input_file).stem + '.json'
    
    # Convert
    success = convert_excel_to_json(input_file, output_file)
    
    if success:
        print(f"\n🎉 Thành công! File JSON đã được tạo tại: {output_file}")
        print(f"📋 Copy file này vào: app/src/main/assets/questions_hang_a.json")
    else:
        print("\n❌ Có lỗi xảy ra trong quá trình convert")
        sys.exit(1)
