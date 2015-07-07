package ua.george_nika.webtester.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "result", schema = "public", catalog = "webtester")
public class ResultEntity {
    private int idResult;
    private Timestamp data;
    private int countQuestion;
    private int allRightAnswer;
    private int rightAnswer;
    private int wrongAnswer;
    private String comment;
    private AccountEntity accountByIdAccount;
    private TestEntity testByIdTest;

    @Id
    @Column(name = "id_result")
    @SequenceGenerator(name="result_seq", sequenceName="result_seq")
    @GeneratedValue(generator = "result_seq")
    public int getIdResult() {
        return idResult;
    }

    public void setIdResult(int idResult) {
        this.idResult = idResult;
    }

    @Basic
    @Column(name = "data")
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    @Basic
    @Column(name = "count_question")
    public int getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(int countQuestion) {
        this.countQuestion = countQuestion;
    }

    @Basic
    @Column(name = "all_right_answer")
    public int getAllRightAnswer() {
        return allRightAnswer;
    }

    public void setAllRightAnswer(int allRightAnswer) {
        this.allRightAnswer = allRightAnswer;
    }

    @Basic
    @Column(name = "right_answer")
    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    @Basic
    @Column(name = "wrong_answer")
    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account", nullable = false)
    public AccountEntity getAccountByIdAccount() {
        return accountByIdAccount;
    }

    public void setAccountByIdAccount(AccountEntity accountByIdAccount) {
        this.accountByIdAccount = accountByIdAccount;
    }

    @ManyToOne
    @JoinColumn(name = "id_test", referencedColumnName = "id_test", nullable = false)
    public TestEntity getTestByIdTest() {
        return testByIdTest;
    }

    public void setTestByIdTest(TestEntity testByIdTest) {
        this.testByIdTest = testByIdTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultEntity that = (ResultEntity) o;

        if (idResult != that.idResult) return false;
        if (countQuestion != that.countQuestion) return false;
        if (allRightAnswer != that.allRightAnswer) return false;
        if (rightAnswer != that.rightAnswer) return false;
        if (wrongAnswer != that.wrongAnswer) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (accountByIdAccount != null ? !accountByIdAccount.equals(that.accountByIdAccount) : that.accountByIdAccount != null)
            return false;
        return !(testByIdTest != null ? !testByIdTest.equals(that.testByIdTest) : that.testByIdTest != null);

    }

    @Override
    public int hashCode() {
        int result = idResult;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + countQuestion;
        result = 31 * result + allRightAnswer;
        result = 31 * result + rightAnswer;
        result = 31 * result + wrongAnswer;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (accountByIdAccount != null ? accountByIdAccount.hashCode() : 0);
        result = 31 * result + (testByIdTest != null ? testByIdTest.hashCode() : 0);
        return result;
    }
}
