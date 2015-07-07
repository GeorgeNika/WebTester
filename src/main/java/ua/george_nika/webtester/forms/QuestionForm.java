package ua.george_nika.webtester.forms;

import ua.george_nika.webtester.entity.QuestionEntity;

/**
 * Created by George on 03.07.2015.
 */
public class QuestionForm {

    private String question;

    public QuestionForm() {
    }

    public QuestionForm(QuestionEntity questionEntity) {
        this.question = questionEntity.getQuestion();
    }
    public void updateQuestion(QuestionEntity questionEntity) {
        questionEntity.setQuestion(getQuestion());
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
