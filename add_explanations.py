import sqlite3

db_path = 'app/src/main/assets/databases/questions.db'
conn = sqlite3.connect(db_path)
cursor = conn.cursor()

explanations = {
    "Quy định chung và quy tắc giao thông đường bộ": "Theo quy định của Luật Giao thông đường bộ Việt Nam.",
    "Văn hóa giao thông, đạo đức người lái xe": "Người lái xe cần có ý thức văn hóa và đạo đức khi tham gia giao thông.",
    "Kỹ thuật lái xe": "Kỹ thuật lái xe an toàn và đúng quy định.",
    "Biển báo đường bộ": "Biển báo này có ý nghĩa theo quy chuẩn biển báo giao thông đường bộ Việt Nam.",
    "Sa hình": "Quan sát hình ảnh và áp dụng quy tắc giao thông để xác định đáp án đúng."
}

cursor.execute("SELECT id, category, correct_answer, option_a, option_b, option_c, option_d FROM questions")
questions = cursor.fetchall()

for q in questions:
    q_id, category, correct, opt_a, opt_b, opt_c, opt_d = q
    
    options = {'A': opt_a, 'B': opt_b, 'C': opt_c, 'D': opt_d}
    correct_text = options.get(correct, "")
    
    base_explanation = explanations.get(category, "Đáp án đúng theo quy định.")
    explanation = f"Đáp án đúng là: {correct}. {correct_text}\n\n{base_explanation}"
    
    cursor.execute("UPDATE questions SET explanation = ? WHERE id = ?", (explanation, q_id))

conn.commit()
print(f"Đã thêm giải thích cho {len(questions)} câu hỏi!")
conn.close()
