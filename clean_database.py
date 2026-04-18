import sqlite3

def clean_database():
    """
    Xóa các câu hỏi thừa, chỉ giữ lại 249 câu (ID: 1-228, 230-250)
    """
    conn = sqlite3.connect('app/src/main/assets/databases/questions.db')
    cursor = conn.cursor()
    
    # Kiểm tra số câu hiện tại
    cursor.execute("SELECT COUNT(*) FROM questions")
    current_count = cursor.fetchone()[0]
    print(f"Current question count: {current_count}")
    
    # Lấy tất cả IDs
    cursor.execute("SELECT id FROM questions ORDER BY id")
    all_ids = [row[0] for row in cursor.fetchall()]
    print(f"All IDs: {all_ids[:20]}...{all_ids[-20:]}")
    
    # IDs hợp lệ (1-228, 230-250)
    valid_ids = list(range(1, 229)) + list(range(230, 251))
    print(f"\nValid IDs count: {len(valid_ids)}")
    
    # Tìm IDs thừa
    invalid_ids = [id for id in all_ids if id not in valid_ids]
    
    if invalid_ids:
        print(f"\nFound {len(invalid_ids)} invalid IDs: {invalid_ids}")
        
        # Xóa các câu thừa
        for id in invalid_ids:
            cursor.execute("DELETE FROM questions WHERE id = ?", (id,))
            print(f"Deleted question ID: {id}")
        
        conn.commit()
        print(f"\nDeleted {len(invalid_ids)} questions")
    else:
        print("\nNo invalid IDs found")
    
    # Kiểm tra lại
    cursor.execute("SELECT COUNT(*) FROM questions")
    final_count = cursor.fetchone()[0]
    print(f"\nFinal question count: {final_count}")
    
    # Hiển thị các IDs còn lại
    cursor.execute("SELECT id FROM questions ORDER BY id")
    remaining_ids = [row[0] for row in cursor.fetchall()]
    print(f"Remaining IDs: {remaining_ids[:10]}...{remaining_ids[-10:]}")
    
    # Kiểm tra xem có thiếu ID nào không
    missing_ids = [id for id in valid_ids if id not in remaining_ids]
    if missing_ids:
        print(f"\nWarning: Missing IDs: {missing_ids}")
    
    conn.close()

if __name__ == "__main__":
    print("="*80)
    print("CLEANING DATABASE")
    print("="*80)
    print()
    
    clean_database()
    
    print("\n" + "="*80)
    print("DONE!")
    print("="*80)
