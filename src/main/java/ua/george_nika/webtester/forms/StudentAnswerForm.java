package ua.george_nika.webtester.forms;

import ua.george_nika.webtester.entity.AnswerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 06.07.2015.
 */
public class StudentAnswerForm {

    private Map<Integer, String> studentAnswerMap;

    private List<Integer> studentAnswerList;

    public StudentAnswerForm(){
        studentAnswerList = new ArrayList<Integer>();
        studentAnswerMap = new HashMap<Integer, String>();
    }

    public StudentAnswerForm(List<AnswerEntity> answerEntityList){
        studentAnswerList = new ArrayList<Integer>();
        studentAnswerMap = new HashMap<Integer, String>();
        for (AnswerEntity tempAnswer : answerEntityList){
            studentAnswerMap.put(tempAnswer.getIdAnswer(), tempAnswer.getAnswer());
        }
    }

    public Map<Integer, String> getStudentAnswerMap() {
        return studentAnswerMap;
    }

    public void setStudentAnswerMap(Map<Integer, String> studentAnswerMap) {
        this.studentAnswerMap = studentAnswerMap;
    }

    public List<Integer> getStudentAnswerList() {
        return studentAnswerList;
    }

    public void setStudentAnswerList(List<Integer> studentAnswerList) {
        this.studentAnswerList = studentAnswerList;
    }
}
