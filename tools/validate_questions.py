#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Tool kiểm tra và validate file JSON câu hỏi
"""

import json
import sys
from pathlib import Path


def validate_questions(json_file):
    """Validate file JSON câu hỏi"""
    
    print(f"🔍 Đang kiểm tra file: {json_file}")
    print("=" * 60)
    
    try:
        with open(json_file, 'r', encoding='utf-8') as f:
            data = json.load(f)
    except json.JSONDecodeError as e:
        print(f"❌ Lỗi JSON syntax: {e}")
        return False
    except Exception as e:
        print(f"❌ Lỗi đọc file: {e}")
        return False
    
    if 'questions' not in data:
        print("❌ Thiếu key 'questions' trong JSON")
        return False
    
    questions = data['questions']
    total = len(questions)
    
    print(f"✅ Tổng số câu hỏi: {total}")
    print()
    
    # Validate từng câu
    errors = []
    warnings = []
    
    required_fields = [
        'question_text', 'option_a', 'option_b', 
        'correct_answer', 'category', 'is_critical'
    ]
    
    valid_categories = [
        'Câu hỏi điểm liệt',
        'Khái niệm và quy tắc',
        'Văn hóa và đạo đức',
        'Kỹ thuật lái xe',
        'Biển báo đường bộ',
        'Sa hình'
    ]
    
    categories_count = {}
    critical_count = 0
    
    for idx, q in enumerate(questions, 1):
        # Check required fields
        for field in required_fields:
            if field not in q:
                errors.append(f"Câu {idx}: Thiếu field '{field}'")
        
        # Check correct_answer
        if 'correct_answer' in q:
            if q['correct_answer'] not in ['A', 'B', 'C', 'D']:
                errors.append(f"Câu {idx}: Đáp án đúng không hợp lệ: {q['correct_answer']}")
        
        # Check category
        if 'category' in q:
            cat = q['category']
            if cat not in valid_categories:
                warnings.append(f"Câu {idx}: Chủ đề không chuẩn: '{cat}'")
            categories_count[cat] = categories_count.get(cat, 0) + 1
        
        # Check is_critical
        if 'is_critical' in q:
            if q['is_critical'] not in [0, 1]:
                errors.append(f"Câu {idx}: is_critical phải là 0 hoặc 1")
            if q['is_critical'] == 1:
                critical_count += 1
        
        # Check empty fields
        if 'question_text' in q and not q['question_text'].strip():
            errors.append(f"Câu {idx}: Nội dung câu hỏi trống")
        
        if 'option_a' in q and not q['option_a'].strip():
            errors.append(f"Câu {idx}: Đáp án A trống")
        
        if 'option_b' in q and not q['option_b'].strip():
            errors.append(f"Câu {idx}: Đáp án B trống")
        
        # Check explanation
        if 'explanation' not in q or not q['explanation']:
            warnings.append(f"Câu {idx}: Không có giải thích")
    
    # Print results
    print("📊 THỐNG KÊ THEO CHỦ ĐỀ:")
    print("-" * 60)
    
    expected_counts = {
        'Câu hỏi điểm liệt': 20,
        'Khái niệm và quy tắc': 100,
        'Văn hóa và đạo đức': 10,
        'Kỹ thuật lái xe': 15,
        'Biển báo đường bộ': 65,
        'Sa hình': 35
    }
    
    for cat in valid_categories:
        count = categories_count.get(cat, 0)
        expected = expected_counts.get(cat, 0)
        status = "✅" if count == expected else "⚠️"
        print(f"{status} {cat}: {count}/{expected} câu")
    
    print()
    print(f"⚠️  Câu hỏi điểm liệt: {critical_count} câu")
    print()
    
    # Print errors
    if errors:
        print("❌ LỖI CẦN SỬA:")
        print("-" * 60)
        for error in errors[:20]:  # Show first 20 errors
            print(f"  • {error}")
        if len(errors) > 20:
            print(f"  ... và {len(errors) - 20} lỗi khác")
        print()
    
    # Print warnings
    if warnings:
        print("⚠️  CẢNH BÁO:")
        print("-" * 60)
        for warning in warnings[:10]:  # Show first 10 warnings
            print(f"  • {warning}")
        if len(warnings) > 10:
            print(f"  ... và {len(warnings) - 10} cảnh báo khác")
        print()
    
    # Summary
    print("=" * 60)
    if not errors:
        print("✅ VALIDATION THÀNH CÔNG!")
        print(f"   Tổng số câu: {total}")
        print(f"   Số lỗi: 0")
        print(f"   Số cảnh báo: {len(warnings)}")
        return True
    else:
        print("❌ VALIDATION THẤT BẠI!")
        print(f"   Tổng số câu: {total}")
        print(f"   Số lỗi: {len(errors)}")
        print(f"   Số cảnh báo: {len(warnings)}")
        return False


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("📝 Cách sử dụng:")
        print("  python validate_questions.py <json_file>")
        print()
        print("  Ví dụ:")
        print("    python validate_questions.py app/src/main/assets/questions_hang_a.json")
        sys.exit(1)
    
    json_file = sys.argv[1]
    
    if not Path(json_file).exists():
        print(f"❌ File không tồn tại: {json_file}")
        sys.exit(1)
    
    success = validate_questions(json_file)
    sys.exit(0 if success else 1)
