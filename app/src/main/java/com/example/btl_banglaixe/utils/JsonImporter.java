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
        int count = 0;
        QuestionDAO questionDAO = new QuestionDAO(context);

        try {
            String json = readJsonFromAssets(context, fileName);
            count = parseAndImportQuestions(questionDAO, json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            questionDAO.close();
        }

        return count;
    }

    public static int importQuestionsFromString(Context context, String jsonString) {
        int count = 0;
        QuestionDAO questionDAO = new QuestionDAO(context);

        try {
            count = parseAndImportQuestions(questionDAO, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            questionDAO.close();
        }

        return count;
    }

    private static String readJsonFromAssets(Context context, String fileName) throws Exception {
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private static int parseAndImportQuestions(QuestionDAO questionDAO, String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray questionsArray = jsonObject.getJSONArray("questions");
        
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObj = questionsArray.getJSONObject(i);
            Question question = createQuestionFromJson(questionObj);
            questionDAO.addQuestion(question);
        }
        
        return questionsArray.length();
    }

    private static Question createQuestionFromJson(JSONObject questionObj) throws Exception {
        Question question = new Question();
        question.setQuestionText(questionObj.getString("question_text"));
        question.setOptionA(questionObj.getString("option_a"));
        question.setOptionB(questionObj.getString("option_b"));
        question.setOptionC(questionObj.optString("option_c", null));
        question.setOptionD(questionObj.optString("option_d", null));
        question.setCorrectAnswer(questionObj.getString("correct_answer"));
        question.setCategory(questionObj.getString("category"));
        question.setCritical(questionObj.getInt("is_critical") == 1);
        question.setImagePath(questionObj.optString("image_path", null));
        question.setExplanation(questionObj.optString("explanation", ""));
        return question;
    }
}
