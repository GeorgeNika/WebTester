package ua.george_nika.webtester.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.webtester.dao.intface.ResultDao;
import ua.george_nika.webtester.dao.util.SortAndRestrictForEntity;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.ResultEntity;
import ua.george_nika.webtester.entity.TestEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.util.WebTesterLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResultService {
    private static String LOGGER_NAME = ResultService.class.getSimpleName();
    private SortAndRestrictForEntity sortAndRestrict = new SortAndRestrictForEntity();

    @Autowired
    private ResultDao resultDao;


    @Transactional(readOnly = false)
    public void createNewResult(AccountEntity account, TestEntity test, ResultEntity newResult) {
        try {
            checkAllRequirements(account, test);

            newResult.setAccountByIdAccount(account);
            newResult.setTestByIdTest(test);
            newResult.setData(new Timestamp(new Date().getTime()));
            newResult.setComment("" + test.getName() + " - " + test.getQuestionList().size() + " - " + test.getComment());
            newResult.setAllRightAnswer(0);
            newResult.setCountQuestion(0);
            newResult.setRightAnswer(0);
            newResult.setWrongAnswer(0);
            resultDao.save(newResult);
            WebTesterLogger.info(LOGGER_NAME, "create new result " + newResult.getIdResult());
        } catch (Exception ex) {
            WebTesterLogger.info(LOGGER_NAME, "Can't create result: " + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create result: " + " - " + ex.getMessage());
        }
    }

    private void checkAllRequirements(AccountEntity accountEntity, TestEntity testEntity) {
        if (!accountEntity.isActive()) {
            throw new UserWrongInputException("Account is disable");
        }

        if (!testEntity.isActive()) {
            throw new UserWrongInputException("Test is disable");
        }
    }

    @Transactional(readOnly = false)
    public void updateResult(ResultEntity resultEntity) {
        try {
            resultDao.update(resultEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't update result id: " + resultEntity.getIdResult()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update result id: " + resultEntity.getIdResult()
                    + " - " + ex.getMessage());
        }
    }

    public List<ResultEntity> getPartOfResultByAccount(AccountEntity accountEntity, int offset, int limit) {
        try {
            //            todo paging;
            sortAndRestrict.clearAllEqualRestriction();
            sortAndRestrict.addEqualRestriction("accountByIdAccount", accountEntity);
            List<ResultEntity> resultList = resultDao.getFilteredAndSortedList(offset, limit, sortAndRestrict);

            return resultList;
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't get result offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get result offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }

    public List<ResultEntity> getPartOfResultByTest(TestEntity testEntity, int offset, int limit) {
        try {
            //            todo paging;
            sortAndRestrict.clearAllEqualRestriction();
            sortAndRestrict.addEqualRestriction("testByIdTest", testEntity);
            List<ResultEntity> resultList = resultDao.getFilteredAndSortedList(offset, limit, sortAndRestrict);

            return resultList;
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't get result offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get result offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }

}
