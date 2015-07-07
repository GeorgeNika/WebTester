package ua.george_nika.webtester.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "test", schema = "public", catalog = "webtester")
public class TestEntity {
    private int idTest;
    private String name;
    private boolean active;
    private String comment;
    private Timestamp created;
    private Timestamp updated;
    private AccountEntity accountByIdTutor;
    private int durationInSecond;
    private List<QuestionEntity> questionList;

    @Id
    @Column(name = "id_test")
    @SequenceGenerator(name="test_seq", sequenceName="test_seq")
    @GeneratedValue(generator = "test_seq")
    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
    @JoinColumn(name = "id_tutor", referencedColumnName = "id_account")
    public AccountEntity getAccountByIdTutor() {
        return accountByIdTutor;
    }

    public void setAccountByIdTutor(AccountEntity accountByIdTutor) {
        this.accountByIdTutor = accountByIdTutor;
    }

    @Basic
    @Column(name = "duration_in_second")
    public int getDurationInSecond() {
        return durationInSecond;
    }

    public void setDurationInSecond(int durationInSecond) {
        this.durationInSecond = durationInSecond;
    }

    @OneToMany (mappedBy = "test")
    public List<QuestionEntity> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionEntity> questionList) {
        this.questionList = questionList;
    }

    @Transient
    public int getCountActiveQuestion(){
        int result = 0;
        for (QuestionEntity tempQuestion : getQuestionList()){
            if (tempQuestion.isActive()) {
                result = result +1;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestEntity that = (TestEntity) o;

        if (idTest != that.idTest) return false;
        if (active != that.active) return false;
        if (durationInSecond != that.durationInSecond) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (updated != null ? !updated.equals(that.updated) : that.updated != null) return false;
        if (accountByIdTutor != null ? !accountByIdTutor.equals(that.accountByIdTutor) : that.accountByIdTutor != null)
            return false;
        return !(questionList != null ? !questionList.equals(that.questionList) : that.questionList != null);

    }

    @Override
    public int hashCode() {
        int result = idTest;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (accountByIdTutor != null ? accountByIdTutor.hashCode() : 0);
        result = 31 * result + durationInSecond;
        result = 31 * result + (questionList != null ? questionList.hashCode() : 0);
        return result;
    }
}
