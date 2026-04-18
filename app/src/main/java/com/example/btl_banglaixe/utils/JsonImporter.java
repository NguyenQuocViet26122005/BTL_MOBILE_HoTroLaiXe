package com.example.btl_banglaixe.utils;

import android.content.Context;
import com.example.btl_banglaixe.database.QuestionDAO;
import com.example.btl_banglaixe.models.Question;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonImporter {
    public static int importQuestionsFromAssets(Context context, String fileName) {
        QuestionDAO dao = new QuestionDAO(context);
        try {
            InputStream is = context.getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return parseAndImport(dao, new String(buffer, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return 0;
        } finally {
            dao.close();
        }
    }

    public static int importQuestionsFromString(Context context, String json) {
        QuestionDAO dao = new QuestionDAO(context);
        try {
            return parseAndImport(dao, json);
        } catch (Exception e) {
            return 0;
        } finally {
            dao.close();
        }
    }

    private static int parseAndImport(QuestionDAO dao, String json) throws Exception {
        JSONArray arr = new JSONObject(json).getJSONArray("questions");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Question q = new Question();
            q.setQuestionText(obj.getString("question_text"));
            q.setOptionA(obj.getString("option_a"));
            q.setOptionB(obj.getString("option_b"));
            q.setOptionC(obj.optString("option_c", null));
            q.setOptionD(obj.optString("option_d", null));
            q.setCorrectAnswer(obj.getString("correct_answer"));
            q.setCategory(obj.getString("category"));
            q.setCritical(obj.getInt("is_critical") == 1);
            q.setImagePath(obj.optString("image_path", null));
            q.setExplanation(obj.optString("explanation", ""));
            dao.addQuestion(q);
        }
        return arr.length();
    }
}
