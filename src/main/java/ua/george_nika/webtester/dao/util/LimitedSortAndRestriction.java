package ua.george_nika.webtester.dao.util;

import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.NameOfFieldConstants;
import ua.george_nika.webtester.entity.TestEntity;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.services.QuestionService;
import ua.george_nika.webtester.services.ResultService;
import ua.george_nika.webtester.services.TestService;
import ua.george_nika.webtester.util.WebTesterLogger;

import java.util.HashMap;
import java.util.Map;

public class LimitedSortAndRestriction extends SortAndRestrictForEntity {

    private static String LOGGER_NAME = LimitedSortAndRestriction.class.getSimpleName();

    public static Map<Class, String> idMap = new HashMap<Class, String>();
    public static Map<Class, String> nameMap = new HashMap<Class, String>();

    static {
        idMap.put(ResultService.class, NameOfFieldConstants.RESULT_ID);
        idMap.put(AccountService.class, NameOfFieldConstants.ACCOUNT_ID);
        idMap.put(TestService.class, NameOfFieldConstants.TEST_ID);
        idMap.put(QuestionService.class, NameOfFieldConstants.QUESTION_ID);

        nameMap.put(ResultService.class, NameOfFieldConstants.RESULT_COMMENT);
        nameMap.put(AccountService.class, NameOfFieldConstants.ACCOUNT_LOGIN);
        nameMap.put(TestService.class, NameOfFieldConstants.TEST_NAME);
        nameMap.put(QuestionService.class, NameOfFieldConstants.QUESTION_NAME);
    }

    private String getIdFieldName(Class typeClass) {
        if (idMap.containsKey(typeClass)) {
            return idMap.get(typeClass);
        } else {
            WebTesterLogger.error(LOGGER_NAME, "Wrong class for Id :" + typeClass);
            throw new RuntimeException("Wrong class for Id :" + typeClass);
        }
    }

    private String getNameFieldName(Class typeClass) {
        if (nameMap.containsKey(typeClass)) {
            return nameMap.get(typeClass);
        } else {
            WebTesterLogger.error(LOGGER_NAME, "Wrong class for Name :" + typeClass);
            throw new RuntimeException("Wrong class for Name :" + typeClass);
        }
    }


    // Like Block


    public void addIdLikeRestriction(Class typeClass, String likeRestriction) {
        addLikeRestriction(getIdFieldName(typeClass), likeRestriction);
    }

    public void addNameLikeRestriction(Class typeClass, String likeRestriction) {
        addLikeRestriction(getNameFieldName(typeClass), likeRestriction);
    }

    public void deleteIdLikeRestriction(Class typeClass) {
        deleteLikeRestriction(getIdFieldName(typeClass));
    }

    public void deleteNameLikeRestriction(Class typeClass) {
        deleteLikeRestriction(getNameFieldName(typeClass));
    }

    public String getIdLikeRestriction(Class typeClass) {
        String result = getLikeRestrictionForEntity().get(getIdFieldName(typeClass));
        if (result != null) {
            return result;
        } else {
            return "";
        }
    }

    public String getNameLikeRestriction(Class typeClass) {
        String result = getLikeRestrictionForEntity().get(getNameFieldName(typeClass));
        if (result != null) {
            return result;
        } else {
            return "";
        }
    }

    //Sort Block

    public void nextIdSort(Class typeClass) {
        nextSort(getIdFieldName(typeClass));
    }

    public void nextNameSort(Class typeClass) {
        nextSort(getNameFieldName(typeClass));
    }


    // Equal Block

    public void addQuestionTestEqualRestriction(TestEntity testEntity) {
        addEqualRestriction(NameOfFieldConstants.QUESTION_TEST, testEntity);
    }

    public void addTestActiveEqualRestriction(Boolean active) {
        addEqualRestriction(NameOfFieldConstants.TEST_ACTIVE, active);
    }

    public void addTestAccountEqualRestriction(AccountEntity accountEntity) {
        addEqualRestriction(NameOfFieldConstants.TEST_ACCOUNT, accountEntity);
    }

    public void deleteTestAccountEqualRestriction() {
        deleteEqualRestriction(NameOfFieldConstants.TEST_ACCOUNT);
    }

    public void addResultAccountEqualRestriction(AccountEntity accountEntity) {
        addEqualRestriction(NameOfFieldConstants.RESULT_ACCOUNT, accountEntity);
    }

    public void addResultTestEqualRestriction(TestEntity testEntity) {
        addEqualRestriction(NameOfFieldConstants.RESULT_TEST, testEntity);
    }

}
