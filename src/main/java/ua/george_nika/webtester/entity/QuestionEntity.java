package ua.george_nika.webtester.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "question", schema = "public", catalog = "webtester")
public class QuestionEntity {
    private int idQuestion;
    private String question;
    private boolean active;
    private Timestamp created;
    private Timestamp updated;
    private TestEntity test;
    private List<AnswerEntity> answerList;

    @Id
    @Column(name = "id_question")
    @SequenceGenerator(name="question_seq", sequenceName="question_seq", allocationSize=1)
    @GeneratedValue(generator = "question_seq")
    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    @Basic
    @Column(name = "question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
    @JoinColumn(name = "id_test", referencedColumnName = "id_test", nullable = false)
    public TestEntity getTest() {
        return test;
    }

    public void setTest(TestEntity test) {
        this.test = test;
    }

    @OneToMany (mappedBy = "question")
    public List<AnswerEntity> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerEntity> answerList) {
        this.answerList = answerList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionEntity that = (QuestionEntity) o;

        if (idQuestion != that.idQuestion) return false;
        if (active != that.active) return false;
        if (question != null ? !question.equals(that.question) : that.question != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (updated != null ? !updated.equals(that.updated) : that.updated != null) return false;
        if (test != null ? !test.equals(that.test) : that.test != null) return false;
        return !(answerList != null ? !answerList.equals(that.answerList) : that.answerList != null);

    }

    @Override
    public int hashCode() {
        int result = idQuestion;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (test != null ? test.hashCode() : 0);
        result = 31 * result + (answerList != null ? answerList.hashCode() : 0);
        return result;
    }
}
