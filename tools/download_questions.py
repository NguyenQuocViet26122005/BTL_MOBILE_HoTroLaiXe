#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Tool tải bộ câu hỏi GPLX từ các nguồn online
"""

import json
import sys


def get_sample_250_questions():
    """
    Trả về bộ 250 câu hỏi mẫu cho bằng lái hạng A
    Đây là dữ liệu mẫu, bạn cần thay thế bằng dữ liệu thật
    """
    
    questions = []
    
    # ============================================
    # PHẦN 1: CÂU HỎI ĐIỂM LIỆT (20 câu)
    # ============================================
    
    diem_liet = [
        {
            "question_text": "Người điều khiển phương tiện giao thông đường bộ mà trong cơ thể có chất ma túy có bị nghiêm cấm hay không?",
            "option_a": "Bị nghiêm cấm",
            "option_b": "Không bị nghiêm cấm",
            "option_c": None,
            "option_d": None,
            "correct_answer": "A",
            "category": "Câu hỏi điểm liệt",
            "is_critical": 1,
            "image_path": None,
            "explanation": "Tuyệt đối nghiêm cấm điều khiển phương tiện khi có chất ma túy trong cơ thể."
        },
        {
            "question_text": "Người điều khiển xe mô tô hai bánh, xe mô tô ba bánh, xe gắn máy có được phép sử dụng xe để kéo hoặc đẩy các phương tiện khác khi tham gia giao thông không?",
            "option_a": "Được phép",
            "option_b": "Không được phép",
            "option_c": "Được phép tuỳ trường hợp",
            "option_d": None,
            "correct_answer": "B",
            "category": "Câu hỏi điểm liệt",
            "is_critical": 1,
            "image_path": None,
            "explanation": "Không được phép sử dụng xe mô tô để kéo hoặc đẩy các phương tiện khác."
        },
        {
            "question_text": "Người điều khiển xe mô tô hai bánh, ba bánh, xe gắn máy có được phép sử dụng xe để kéo, đẩy xe khác khi tham gia giao thông không?",
            "option_a": "Được phép",
            "option_b": "Nếu phương tiện được kéo, đẩy có khối lượng nhỏ hơn phương tiện của mình thì được phép",
            "option_c": "Tuỳ trường hợp",
            "option_d": "Không được phép",
            "correct_answer": "D",
            "category": "Câu hỏi điểm liệt",
            "is_critical": 1,
            "image_path": None,
            "explanation": "Tuyệt đối không được phép sử dụng xe mô tô để kéo, đẩy xe khác."
        },
        # TODO: Thêm 17 câu điểm liệt nữa
    ]
    
    # ============================================
    # PHẦN 2: KHÁI NIỆM VÀ QUY TẮC (100 câu)
    # ============================================
    
    khai_niem = [
        {
            "question_text": "Khái niệm 'Người tham gia giao thông đường bộ' được hiểu như thế nào?",
            "option_a": "Là người điều khiển phương tiện tham gia giao thông đường bộ",
            "option_b": "Là người đi bộ tham gia giao thông đường bộ",
            "option_c": "Là người điều khiển, người sử dụng phương tiện và người đi bộ tham gia giao thông đường bộ",
            "option_d": None,
            "correct_answer": "C",
            "category": "Khái niệm và quy tắc",
            "is_critical": 0,
            "image_path": None,
            "explanation": "Người tham gia giao thông bao gồm cả người điều khiển phương tiện, người sử dụng phương tiện và người đi bộ."
        },
        {
            "question_text": "'Phương tiện giao thông đường bộ' gồm những loại nào?",
            "option_a": "Phương tiện giao thông cơ giới đường bộ",
            "option_b": "Phương tiện giao thông thô sơ đường bộ và xe máy chuyên dùng",
            "option_c": "Cả ý 1 và ý 2",
            "option_d": None,
            "correct_answer": "C",
            "category": "Khái niệm và quy tắc",
            "is_critical": 0,
            "image_path": None,
            "explanation": "Phương tiện giao thông đường bộ gồm cả phương tiện cơ giới và phương tiện thô sơ."
        },
        # TODO: Thêm 98 câu khái niệm nữa
    ]
    
    # ============================================
    # PHẦN 3: VĂN HÓA VÀ ĐẠO ĐỨC (10 câu)
    # ============================================
    
    van_hoa = [
        {
            "question_text": "Người lái xe phải làm gì khi tham gia giao thông và có hành vi văn hóa giao thông?",
            "option_a": "Điều khiển phương tiện đúng tốc độ quy định và chấp hành hiệu lệnh của biển báo hiệu",
            "option_b": "Chấp hành quy tắc giao thông đường bộ, không gây cản trở giao thông",
            "option_c": "Cả ý 1 và ý 2",
            "option_d": None,
            "correct_answer": "C",
            "category": "Văn hóa và đạo đức",
            "is_critical": 0,
            "image_path": None,
            "explanation": "Người lái xe phải chấp hành đầy đủ các quy tắc giao thông và có ý thức văn hóa."
        },
        # TODO: Thêm 9 câu văn hóa nữa
    ]
    
    # ============================================
    # PHẦN 4: KỸ THUẬT LÁI XE (15 câu)
    # ============================================
    
    ky_thuat = [
        {
            "question_text": "Khi điều khiển xe mô tô tay ga xuống đường dốc dài, độ dốc cao, người lái xe cần thực hiện các thao tác nào?",
            "option_a": "Giữ tay ga ở mức độ phù hợp, sử dụng phanh trước và phanh sau để giảm tốc độ",
            "option_b": "Nhả hết tay ga, tắt động cơ, sử dụng phanh trước và phanh sau để giảm tốc độ",
            "option_c": "Sử dụng phanh trước để giảm tốc độ kết hợp với tắt chìa khóa điện của xe",
            "option_d": None,
            "correct_answer": "A",
            "category": "Kỹ thuật lái xe",
            "is_critical": 0,
            "image_path": None,
            "explanation": "Khi xuống dốc cần giữ tay ga phù hợp và sử dụng cả hai phanh để kiểm soát tốc độ."
        },
        # TODO: Thêm 14 câu kỹ thuật nữa
    ]
    
    # ============================================
    # PHẦN 5: BIỂN BÁO ĐƯỜNG BỘ (65 câu)
    # ============================================
    
    bien_bao = [
        {
            "question_text": "Biển nào cấm xe mô tô hai bánh đi vào?",
            "option_a": "Biển 1",
            "option_b": "Biển 2",
            "option_c": "Biển 3",
            "option_d": None,
            "correct_answer": "B",
            "category": "Biển báo đường bộ",
            "is_critical": 0,
            "image_path": "bien_bao_1.jpg",
            "explanation": "Biển 2 là biển cấm xe mô tô hai bánh."
        },
        # TODO: Thêm 64 câu biển báo nữa
    ]
    
    # ============================================
    # PHẦN 6: SA HÌNH (35 câu)
    # ============================================
    
    sa_hinh = [
        {
            "question_text": "Theo hướng mũi tên, những hướng nào xe mô tô được phép đi?",
            "option_a": "Cả ba hướng",
            "option_b": "Hướng 1 và 2",
            "option_c": "Hướng 1 và 3",
            "option_d": "Hướng 2 và 3",
            "correct_answer": "B",
            "category": "Sa hình",
            "is_critical": 0,
            "image_path": "sa_hinh_1.jpg",
            "explanation": "Xe mô tô được phép đi theo hướng 1 và 2."
        },
        # TODO: Thêm 34 câu sa hình nữa
    ]
    
    # Gộp tất cả câu hỏi
    questions.extend(diem_liet)
    questions.extend(khai_niem)
    questions.extend(van_hoa)
    questions.extend(ky_thuat)
    questions.extend(bien_bao)
    questions.extend(sa_hinh)
    
    return questions


def save_questions_to_json(output_file='questions_hang_a.json'):
    """Lưu câu hỏi ra file JSON"""
    
    print("🔄 Đang tạo bộ câu hỏi...")
    questions = get_sample_250_questions()
    
    data = {
        "questions": questions
    }
    
    print(f"💾 Đang lưu vào file: {output_file}")
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    
    print(f"✅ Đã tạo {len(questions)} câu hỏi")
    
    # Thống kê
    categories = {}
    for q in questions:
        cat = q['category']
        categories[cat] = categories.get(cat, 0) + 1
    
    print("\n📊 Thống kê:")
    for cat, count in sorted(categories.items()):
        print(f"  - {cat}: {count} câu")
    
    print(f"\n⚠️  LƯU Ý: Đây chỉ là {len(questions)} câu mẫu!")
    print("📝 Bạn cần bổ sung thêm để đủ 250 câu theo đúng tỷ lệ:")
    print("   - Câu hỏi điểm liệt: 20 câu")
    print("   - Khái niệm và quy tắc: 100 câu")
    print("   - Văn hóa và đạo đức: 10 câu")
    print("   - Kỹ thuật lái xe: 15 câu")
    print("   - Biển báo đường bộ: 65 câu")
    print("   - Sa hình: 35 câu")


if __name__ == "__main__":
    print("=" * 60)
    print("🚗 TẠO BỘ CÂU HỎI GPLX HẠNG A")
    print("=" * 60)
    print()
    
    output_file = sys.argv[1] if len(sys.argv) > 1 else 'questions_hang_a.json'
    
    save_questions_to_json(output_file)
    
    print(f"\n🎉 Hoàn thành! File đã được tạo tại: {output_file}")
    print(f"📋 Bước tiếp theo:")
    print(f"   1. Mở file và bổ sung đầy đủ 250 câu")
    print(f"   2. Chạy validate: python tools/validate_questions.py {output_file}")
    print(f"   3. Copy vào: app/src/main/assets/questions_hang_a.json")
