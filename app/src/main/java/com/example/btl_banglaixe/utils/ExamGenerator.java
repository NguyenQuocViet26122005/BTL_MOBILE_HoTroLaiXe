package com.example.btl_banglaixe.utils;

import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.models.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExamGenerator {
    public static List<Question> generateExam(QuestionDAO dao, int examId) {
        List<Question> exam = new ArrayList<>();
        Random r = examId == 0 ? new Random() : new Random(examId * 1000);
        
        try {
            List<Question> critical = dao.getCriticalQuestions();
            if (!critical.isEmpty()) exam.add(critical.get(r.nextInt(critical.size())));
            
            exam.addAll(getRandom(dao.getQuestionsByCategory("Quy định chung và quy tắc giao thông đường bộ"), 12, r));
            exam.addAll(getRandom(dao.getQuestionsByCategory("Văn hóa giao thông, đạo đức người lái xe"), 1, r));
            exam.addAll(getRandom(dao.getQuestionsByCategory("Kỹ thuật lái xe"), 1, r));
            exam.addAll(getRandom(dao.getQuestionsByCategory("Biển báo đường bộ"), 5, r));
            exam.addAll(getRandom(dao.getQuestionsByCategory("Sa hình"), 5, r));
            
            if (exam.size() < 25) {
                List<Question> all = dao.getAllQuestions();
                List<Question> remaining = new ArrayList<>();
                for (Question q : all) {
                    boolean exists = false;
                    for (Question e : exam) {
                        if (e.getId() == q.getId()) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) remaining.add(q);
                }
                exam.addAll(getRandom(remaining, 25 - exam.size(), r));
            }
            
            if (exam.size() > 1) {
                Question first = exam.get(0);
                List<Question> rest = new ArrayList<>(exam.subList(1, exam.size()));
                Collections.shuffle(rest, r);
                exam.clear();
                exam.add(first);
                exam.addAll(rest);
            }
        } catch (Exception e) {
            exam = getRandom(dao.getAllQuestions(), 25, r);
        }
        return exam;
    }
    
    private static List<Question> getRandom(List<Question> src, int count, Random r) {
        List<Question> result = new ArrayList<>();
        if (src.isEmpty()) return result;
        List<Question> shuffled = new ArrayList<>(src);
        Collections.shuffle(shuffled, r);
        for (int i = 0; i < Math.min(count, shuffled.size()); i++) result.add(shuffled.get(i));
        return result;
    }
}
