package ua.george_nika.webtester.forms;

import ua.george_nika.webtester.entity.TestEntity;

import java.io.Serializable;

/**
 * Created by George on 02.07.2015.
 */
public class TestForm implements Serializable {

    private String name;
    private String comment;
    private String time;

    public TestForm() {
    }

    public TestForm(TestEntity testEntity) {
        this.name = testEntity.getName();
        this.comment = testEntity.getComment();
        this.time = ""+testEntity.getDurationInSecond();
    }

    public void updateTest(TestEntity testEntity) {
        testEntity.setName(getName());
        testEntity.setComment(getComment());
        testEntity.setDurationInSecond(Integer.parseInt(getTime()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
