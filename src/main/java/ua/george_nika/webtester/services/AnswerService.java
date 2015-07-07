package ua.george_nika.webtester.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.webtester.dao.intface.AnswerDao;
import ua.george_nika.webtester.dao.intface.QuestionDao;
import ua.george_nika.webtester.dao.util.SortAndRestrictForEntity;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.AnswerEntity;
import ua.george_nika.webtester.entity.QuestionEntity;
import ua.george_nika.webtester.entity.TestEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.util.WebTesterLogger;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AnswerService {
    private static String LOGGER_NAME = QuestionService.class.getSimpleName();
    private SortAndRestrictForEntity sortAndRestrict = new SortAndRestrictForEntity();

    @Autowired
    private TestService testService;

    @Autowired
    private AnswerDao answerDao;


    public AnswerEntity getAnswerById(AccountEntity accountEntity, int idAnswer) {
        try {
            AnswerEntity answerEntity  = answerDao.getById(idAnswer);
            QuestionEntity questionEntity = answerEntity.getQuestion();
            testService.checkTestPermission(accountEntity, questionEntity.getTest());
            return answerEntity;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get answer by id: " + idAnswer + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get answer by id: " + idAnswer + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void createNewAnswer(AccountEntity accountEntity, QuestionEntity questionEntity, AnswerEntity answerEntity) {
        try {
            testService.checkTestPermission(accountEntity, questionEntity.getTest());
            checkAllRequirements(answerEntity);

            answerEntity.setQuestion(questionEntity);
            answerEntity.setCreated(new Timestamp(new Date().getTime()));
            answerEntity.setActive(true);
            answerDao.save(answerEntity);
            WebTesterLogger.info(LOGGER_NAME, "create answer " + answerEntity.getAnswer());
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't create new answer : " + answerEntity.getAnswer()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create new answer : " + answerEntity.getAnswer()
                    + " - " + ex.getMessage());

        }
    }
    private void checkAllRequirements(AnswerEntity answerEntity) {
        if (answerEntity.getAnswer().isEmpty()) {
            throw new UserWrongInputException("Empty answer.");
        }
    }

    @Transactional(readOnly = false)
    public void deleteAnswerById(AccountEntity accountEntity, int idAnswer) {
        try {
            AnswerEntity tempAnswer = getAnswerById(accountEntity, idAnswer);
            checkDeleteAnswer(tempAnswer);
            answerDao.delete(tempAnswer);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't delete answer id: " + idAnswer + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete answer id: " + idAnswer + " - " + ex.getMessage());
        }
    }

    private void checkDeleteAnswer (AnswerEntity answerEntity){
        Boolean result = false;
        QuestionEntity questionEntity = answerEntity.getQuestion();
        for (AnswerEntity tempAnswer : questionEntity.getAnswerList()){
            if (tempAnswer.getIdAnswer() == answerEntity.getIdAnswer()) {
                continue;
            }
            if (tempAnswer.isActive()){
                result = true;
                break;
            }
        }
        if (result == false){
            throw new UserWrongInputException("Have NO enabled answer");
        }
    }


}
