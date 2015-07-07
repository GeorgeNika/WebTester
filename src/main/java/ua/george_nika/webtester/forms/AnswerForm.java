package ua.george_nika.webtester.forms;

import ua.george_nika.webtester.entity.AnswerEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 05.07.2015.
 */
public class AnswerForm {
    private List<AnswerEntity> answerList;

    public AnswerForm() {
    }

    public AnswerForm(List<AnswerEntity> answerEntityList) {
        this.answerList = answerEntityList;
    }

    public void updateAnswerList(List<AnswerEntity> answerEntityList) {
        Map<Integer, String> answerMap = new HashMap<Integer, String>();
        Map<Integer, Boolean> checkRightMap = new HashMap<Integer, Boolean>();

        for (AnswerEntity tempAnswer : answerList){
            answerMap.put(tempAnswer.getIdAnswer(), tempAnswer.getAnswer());
            checkRightMap.put(tempAnswer.getIdAnswer(), tempAnswer.isCheckRight());
        }
        for (AnswerEntity tempAnswer : answerEntityList) {
            try {
                tempAnswer.setAnswer(answerMap.get(tempAnswer.getIdAnswer()));
                tempAnswer.setCheckRight(checkRightMap.get(tempAnswer.getIdAnswer()));
            }catch (Exception ex){
                throw new UserWrongInputException("Error in update answer");
            }
        }
    }

    public List<AnswerEntity> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerEntity> answerList) {
        this.answerList = answerList;
    }
}
