package ua.george_nika.webtester.services;

import ua.george_nika.webtester.dao.intface.AbstractDao;
import ua.george_nika.webtester.dao.intface.TestDao;
import ua.george_nika.webtester.dao.util.LimitedSortAndRestriction;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.QuestionEntity;
import ua.george_nika.webtester.entity.RoleEntity;
import ua.george_nika.webtester.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.util.WebTesterLogger;
import ua.george_nika.webtester.util.WebTesterRole;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TestService extends AbstractSortAndRestrictService{
    private static String LOGGER_NAME = TestService.class.getSimpleName();

    @Autowired
    private TestDao testDao;

    @Override
    AbstractDao getDao() {
        return testDao;
    }

    @Transactional(readOnly = false)
    public void createNewTest(AccountEntity account, TestEntity newTest) {
        try {
            checkTestPermissionForCreate(account);
            checkAllRequirements(newTest);

            newTest.setAccountByIdTutor(account);
            newTest.setCreated(new Timestamp(new Date().getTime()));
            newTest.setActive(false);
            testDao.save(newTest);
            WebTesterLogger.info(LOGGER_NAME, "create new test " + newTest.getName());
        } catch (Exception ex) {
            WebTesterLogger.info(LOGGER_NAME, "Can't create test: " + newTest.getName() + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create test: " + newTest.getName() + " - " + ex.getMessage());
        }
    }

    private void checkTestPermissionForCreate(AccountEntity accountEntity) {
        boolean result = false;
        for (RoleEntity tempRole : accountEntity.getRoleSet()) {
            if (tempRole.getIdRole() == WebTesterRole.ADVANCE_TUTOR.getId()
                    || tempRole.getIdRole() == WebTesterRole.TUTOR.getId()) {
                result = true;
                break;
            }
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO permission to " + accountEntity.getLogin());
        }
    }

    private void checkAllRequirements(TestEntity test) {
        if (test.getName().isEmpty()) {
            throw new UserWrongInputException("Empty name of test already exist.");
        }
        if (!isUniqueName(test.getName())) {
            throw new UserWrongInputException("Name of test already exist.");
        }
        if (test.getDurationInSecond() <= 0) {
            throw new UserWrongInputException("Wrong duration for question.");
        }
    }

    private boolean isUniqueName(String name) {
        int count = testDao.getCountRecordWithName(name);
        if (count != 0) {
            return false;
        }
        return true;
    }

    public TestEntity getTestById(AccountEntity accountEntity, int idTest) {
        try {
            TestEntity testEntity = testDao.getById(idTest);
            checkTestPermission(accountEntity, testEntity);
            return testEntity;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get test by id: " + idTest + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get test by id: " + idTest + " - " + ex.getMessage());
        }
    }

    public TestEntity getTestByIdWithoutCheck(int idTest) {
        try {
            TestEntity testEntity = testDao.getById(idTest);
            return testEntity;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get test by id: " + idTest + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get test by id: " + idTest + " - " + ex.getMessage());
        }
    }

    public void checkTestPermission(AccountEntity accountEntity, TestEntity testEntity) {
        boolean result = false;
        for (RoleEntity tempRole : accountEntity.getRoleSet()) {
            if (tempRole.getIdRole() == WebTesterRole.ADVANCE_TUTOR.getId()) {
                result = true;
                break;
            }
        }
        if (result == false && testEntity.getAccountByIdTutor().equals(accountEntity)) {
            result = true;
        }
        if (result == false) {
            throw new UserWrongInputException("have NO permission to " + accountEntity.getLogin());
        }
    }

    @Transactional(readOnly = false)
    public void setEnableTestById(AccountEntity accountEntity, int idTest) {
        try {
            TestEntity tempTest = getTestById(accountEntity, idTest);
            checkEnableTest(tempTest);
            tempTest.setActive(true);
            testDao.save(tempTest);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't set enable test by id: " + idTest + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set enable test by id: " + idTest + " - " + ex.getMessage());
        }
    }

    private void checkEnableTest(TestEntity testEntity) {
        Boolean result = false;
        for (QuestionEntity tempQuestion : testEntity.getQuestionList()) {
            if (tempQuestion.isActive()) {
                result = true;
                break;
            }
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO enabled question");
        }
    }

    @Transactional(readOnly = false)
    public void setDisableTestById(AccountEntity accountEntity, int idTest) {
        try {
            TestEntity tempTest = getTestById(accountEntity, idTest);
            tempTest.setActive(false);
            testDao.save(tempTest);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't set disable test by id: " + idTest + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set disable test by id: " + idTest + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void updateTest(AccountEntity accountEntity, TestEntity testEntity) {
        try {
            checkTestPermission(accountEntity, testEntity);
            testDao.update(testEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't update test id: " + testEntity.getIdTest()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update test id: " + testEntity.getIdTest()
                    + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteTestById(AccountEntity accountEntity, int idTest) {
        try {
            TestEntity tempTestEntity = getTestById(accountEntity, idTest);
            testDao.delete(tempTestEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't delete test id: " + idTest + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete test id: " + idTest + " - " + ex.getMessage());
        }
    }

    public void addTestActiveEqualRestriction( Boolean active) {
        limitedSortAndRestrict.addTestActiveEqualRestriction(active);
    }

    public void addTestAccountEqualRestriction( AccountEntity accountEntity) {
        limitedSortAndRestrict.addTestAccountEqualRestriction(accountEntity);
    }

    public void deleteTestAccountEqualRestriction() {
        limitedSortAndRestrict.deleteTestAccountEqualRestriction();
    }

    public void clearAllEqualRestriction() {
        limitedSortAndRestrict.clearAllEqualRestriction();
    }


    public List<TestEntity> getPartOfTest(int offset, int limit) {
        try {
            List<TestEntity> resultList = testDao.getFilteredAndSortedList(offset, limit, limitedSortAndRestrict);
            return resultList;
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't get tests offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get tests offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }

}
