package com.example.btl_banglaixe.utils;

import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.models.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExamGenerator {
    
    /**
     * Tạo đề thi 25 câu theo cấu trúc:
     * - 1 câu: Điểm liệt (bắt buộc đúng)
     * - 12 câu: Khái niệm, quy tắc giao thông đường bộ (Chương 1)
     * - 1 câu: Văn hóa, đạo đức người lái xe (Chương 2)
     * - 1 câu: Kỹ thuật lái xe (Chương 3)
     * - 5 câu: Biển báo đường bộ (Chương 4)
     * - 5 câu: Sa hình (Chương 5)
     */
    public static List<Question> generateExam(QuestionDAO questionDAO, int examId) {
        List<Question> examQuestions = new ArrayList<>();
        Random random = new Random();
        
        if (examId == 0) {
            // Đề ngẫu nhiên
            random = new Random();
        } else {
            // Đề có sẵn - dùng seed để tạo đề cố định
            random = new Random(examId * 1000);
        }
        
        try {
            // 1. Lấy 1 câu điểm liệt
            List<Question> criticalQuestions = questionDAO.getCriticalQuestions();
            if (!criticalQuestions.isEmpty()) {
                examQuestions.add(criticalQuestions.get(random.nextInt(criticalQuestions.size())));
            }
            
            // 2. Lấy 12 câu từ Chương 1: Quy định chung và quy tắc giao thông đường bộ
            List<Question> chapter1 = questionDAO.getQuestionsByCategory("Quy định chung và quy tắc giao thông đường bộ");
            examQuestions.addAll(getRandomQuestions(chapter1, 12, random));
            
            // 3. Lấy 1 câu từ Chương 2: Văn hóa giao thông, đạo đức người lái xe
            List<Question> chapter2 = questionDAO.getQuestionsByCategory("Văn hóa giao thông, đạo đức người lái xe");
            examQuestions.addAll(getRandomQuestions(chapter2, 1, random));
            
            // 4. Lấy 1 câu từ Chương 3: Kỹ thuật lái xe
            List<Question> chapter3 = questionDAO.getQuestionsByCategory("Kỹ thuật lái xe");
            examQuestions.addAll(getRandomQuestions(chapter3, 1, random));
            
            // 5. Lấy 5 câu từ Chương 4: Biển báo đường bộ
            List<Question> chapter4 = questionDAO.getQuestionsByCategory("Biển báo đường bộ");
            examQuestions.addAll(getRandomQuestions(chapter4, 5, random));
            
            // 6. Lấy 5 câu từ Chương 5: Sa hình
            List<Question> chapter5 = questionDAO.getQuestionsByCategory("Sa hình");
            examQuestions.addAll(getRandomQuestions(chapter5, 5, random));
            
            // Nếu không đủ 25 câu, lấy thêm từ tất cả câu hỏi
            if (examQuestions.size() < 25) {
                List<Question> allQuestions = questionDAO.getAllQuestions();
                List<Question> remaining = new ArrayList<>();
                for (Question q : allQuestions) {
                    boolean exists = false;
                    for (Question eq : examQuestions) {
                        if (eq.getId() == q.getId()) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        remaining.add(q);
                    }
                }
                examQuestions.addAll(getRandomQuestions(remaining, 25 - examQuestions.size(), random));
            }
            
            // Trộn thứ tự câu hỏi (trừ câu điểm liệt ở đầu)
            if (examQuestions.size() > 1) {
                Question criticalQuestion = examQuestions.get(0);
                List<Question> otherQuestions = new ArrayList<>(examQuestions.subList(1, examQuestions.size()));
                Collections.shuffle(otherQuestions, random);
                
                examQuestions.clear();
                examQuestions.add(criticalQuestion);
                examQuestions.addAll(otherQuestions);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi, lấy 25 câu ngẫu nhiên từ tất cả câu hỏi
            List<Question> allQuestions = questionDAO.getAllQuestions();
            examQuestions = getRandomQuestions(allQuestions, 25, random);
        }
        
        return examQuestions;
    }
    
    private static List<Question> getRandomQuestions(List<Question> source, int count, Random random) {
        List<Question> result = new ArrayList<>();
        
        if (source.isEmpty()) {
            return result;
        }
        
        // Tạo bản sao để không ảnh hưởng list gốc
        List<Question> shuffled = new ArrayList<>(source);
        Collections.shuffle(shuffled, random);
        
        // Lấy số câu cần thiết
        int actualCount = Math.min(count, shuffled.size());
        for (int i = 0; i < actualCount; i++) {
            result.add(shuffled.get(i));
        }
        
        return result;
    }
}
