package ua.george_nika.webtester.services;

import ua.george_nika.webtester.dao.intface.QuestionDao;
import ua.george_nika.webtester.dao.util.SortAndRestrictForEntity;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.AnswerEntity;
import ua.george_nika.webtester.entity.QuestionEntity;
import ua.george_nika.webtester.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.util.WebTesterLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by George on 08.06.2015.
 */
@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QuestionService {
    private static String LOGGER_NAME = QuestionService.class.getSimpleName();
    private SortAndRestrictForEntity sortAndRestrict = new SortAndRestrictForEntity();

    @Autowired
    private TestService testService;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(readOnly = false)
    public void createQuestion(AccountEntity account, TestEntity test, QuestionEntity newQuestion) {
        try {
            testService.checkTestPermission(account, test);
            checkAllRequirements(newQuestion);

            newQuestion.setTest(test);
            newQuestion.setCreated(new Timestamp(new Date().getTime()));
            newQuestion.setActive(false);

            questionDao.save(newQuestion);
            WebTesterLogger.info(LOGGER_NAME, "create question " + newQuestion.getQuestion());
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't create new question : " + newQuestion.getQuestion()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create new question : " + newQuestion.getQuestion()
                    + " - " + ex.getMessage());

        }
    }

    private void checkAllRequirements(QuestionEntity question) {
        if (question.getQuestion().isEmpty()) {
            throw new UserWrongInputException("Empty question.");
        }
    }

    public QuestionEntity getQuestionById(AccountEntity accountEntity, int idQuestion) {
        try {
            QuestionEntity questionEntity = questionDao.getById(idQuestion);
            testService.checkTestPermission(accountEntity, questionEntity.getTest());
            return questionEntity;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get question by id: " + idQuestion + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get question by id: " + idQuestion + " - " + ex.getMessage());
        }
    }

    public QuestionEntity getQuestionByIdWithoutCheck(int idQuestion) {
        try {
            QuestionEntity questionEntity = questionDao.getById(idQuestion);
            return questionEntity;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get question by id: " + idQuestion + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get question by id: " + idQuestion + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void setEnableQuestionById(AccountEntity accountEntity, int idQuestion) {
        try {
            QuestionEntity tempQuestion = getQuestionById(accountEntity, idQuestion);
            checkEnableQuestion(tempQuestion);
            tempQuestion.setActive(true);
            questionDao.save(tempQuestion);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't set enable question by id: " + idQuestion + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set enable question by id: " + idQuestion + " - " + ex.getMessage());
        }
    }

    private void checkEnableQuestion(QuestionEntity questionEntity){
        Boolean result = false;
        for (AnswerEntity tempAnswer : questionEntity.getAnswerList()){
            if (tempAnswer.isActive()) {
                result = true;
                break;
            }
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO enabled answer");
        }
    }

    @Transactional(readOnly = false)
    public void setDisableQuestionById(AccountEntity accountEntity, int idQuestion) {
        try {
            QuestionEntity tempQuestion = getQuestionById(accountEntity, idQuestion);
            checkDisableOrDeleteQuestion(tempQuestion);
            tempQuestion.setActive(false);
            questionDao.save(tempQuestion);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't set disable question by id: " + idQuestion + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set disable question by id: " + idQuestion + " - " + ex.getMessage());
        }
    }
    private void checkDisableOrDeleteQuestion (QuestionEntity questionEntity){
        Boolean result = false;
        TestEntity testEntity = questionEntity.getTest();
        for (QuestionEntity tempQuestion : testEntity.getQuestionList()){
            if (tempQuestion.getIdQuestion() == questionEntity.getIdQuestion()) {
                continue;
            }
            if (tempQuestion.isActive()){
                result = true;
                break;
            }
        }
        if (result == false){
            throw new UserWrongInputException("Have NO enabled question");
        }
    }

    @Transactional(readOnly = false)
    public void updateQuestion(AccountEntity accountEntity, QuestionEntity questionEntity) {
        try {
            testService.checkTestPermission(accountEntity, questionEntity.getTest());
            questionDao.update(questionEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't update question id: " + questionEntity.getIdQuestion()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update question id: " + questionEntity.getIdQuestion()
                    + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteQuestionById(AccountEntity accountEntity, int idQuestion) {
        try {
            QuestionEntity tempQuestion = getQuestionById(accountEntity, idQuestion);
            checkDisableOrDeleteQuestion(tempQuestion);
            questionDao.delete(tempQuestion);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't delete question id: " + idQuestion + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete question id: " + idQuestion + " - " + ex.getMessage());
        }
    }

    public List<QuestionEntity> getPartOfQuestion(TestEntity testEntity, int offset, int limit) {
        try {
            //            todo paging;
            sortAndRestrict.addEqualRestriction("test", testEntity);
            List<QuestionEntity> resultList = questionDao.getFilteredAndSortedList(offset, limit, sortAndRestrict);
            //List<QuestionEntity> resultList = testEntity.getQuestionList();

            return resultList;
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't get question offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get question offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }
}
