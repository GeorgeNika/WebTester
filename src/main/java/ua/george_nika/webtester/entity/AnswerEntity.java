package ua.george_nika.webtester.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "answer", schema = "public", catalog = "webtester")
public class AnswerEntity {
    private int idAnswer;
    private String answer;
    private boolean checkRight;
    private boolean active;
    private Timestamp created;
    private Timestamp updated;
    private QuestionEntity question;

    @Id
    @Column(name = "id_answer")
    @SequenceGenerator(name="answer_seq", sequenceName="answer_seq")
    @GeneratedValue(generator = "answer_seq")
    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    @Basic
    @Column(name = "answer")
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name = "check_right")
    public boolean isCheckRight() {
        return checkRight;
    }

    public void setCheckRight(boolean checkRight) {
        this.checkRight = checkRight;
    }

    @Basic
    @Column(name = "active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "updated")
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @ManyToOne
    @JoinColumn(name = "id_question", referencedColumnName = "id_question", nullable = false)
    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerEntity that = (AnswerEntity) o;

        if (idAnswer != that.idAnswer) return false;
        if (checkRight != that.checkRight) return false;
        if (active != that.active) return false;
        if (answer != null ? !answer.equals(that.answer) : that.answer != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (updated != null ? !updated.equals(that.updated) : that.updated != null) return false;
        return !(question != null ? !question.equals(that.question) : that.question != null);

    }

    @Override
    public int hashCode() {
        int result = idAnswer;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        result = 31 * result + (checkRight ? 1 : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }
}
