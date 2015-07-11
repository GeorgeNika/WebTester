package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.AbstractDao;
import ua.george_nika.webtester.dao.util.MySortDirection;
import ua.george_nika.webtester.dao.util.SortAndRestrictForEntity;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;
import ua.george_nika.webtester.util.WebTesterLogger;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 02.06.2015.
 */
abstract public class AbstractDaoJpaImpl<T> implements AbstractDao<T> {

    private static String LOGGER_NAME = AbstractDaoJpaImpl.class.getSimpleName();

    protected abstract Class getEntityClass();

    @PersistenceContext
    EntityManager entityManager;

    public void save(Object abstractEntity) {
        try {
            entityManager.persist(abstractEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "save exception " + abstractEntity.toString());
            throw new WorkWithDataBaseRuntimeException("Error then save data");
        }
    }

    public void update(Object abstractEntity) {
        try {
            entityManager.merge(abstractEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "update exception " + abstractEntity.toString());
            throw new WorkWithDataBaseRuntimeException("Error then update data");
        }

    }

    public void delete(Object abstractEntity) {
        try {
            entityManager.remove(entityManager.merge(abstractEntity));
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "delete exception " + abstractEntity.toString());
            throw new WorkWithDataBaseRuntimeException("Error then delete data");
        }
    }

    public T getById(Integer id) {
        T resultObject;
        try {
            resultObject = (T) entityManager.find(getEntityClass(), id);
            if (resultObject == null) {
                throw new RuntimeException();
            }
        } catch (RuntimeException ex) {
            WebTesterLogger.warn(LOGGER_NAME, "can't find " + getEntityClass() + " with id " + id);
            throw new WorkWithDataBaseRuntimeException("can`t obtain result from database");
        }
        return resultObject;
    }

    public List<T> getAll() {
        List<T> resultObjectList;
        try {
            TypedQuery query = entityManager.createQuery("Select t from " + getEntityClass().getName() +
                    " t", getEntityClass());
            resultObjectList = query.getResultList();
            if (resultObjectList == null) {
                resultObjectList = new ArrayList<T>();
            }
        } catch (RuntimeException ex) {
            WebTesterLogger.warn(LOGGER_NAME, "can't find all " + getEntityClass());
            throw new WorkWithDataBaseRuntimeException("can`t obtain result from database");
        }
        return resultObjectList;
    }

    public T getSingleBy(String field, String value) {
        T result;
        try {
            Query query = entityManager.createQuery
                    ("SELECT t FROM " + getEntityClass().getName() + " t WHERE t." + field + " = :param1");
            query.setParameter("param1", value);

            result = (T) query.getSingleResult();
        } catch (RuntimeException ex) {
            WebTesterLogger.warn(LOGGER_NAME, "can't find single " + getEntityClass() + " - field: " + field
                    + " value: " + value);
            throw new WorkWithDataBaseRuntimeException("can`t obtain result from database");
        }
        return result;
    }

    public int getCountRecordsWith(String field, String value) {
        Number result;
        try {
            Query query = entityManager.createQuery
                    ("SELECT COUNT (t) FROM " + getEntityClass().getName() + " t WHERE t." + field + " = :param1");
            query.setParameter("param1", value);
            result = (Number) query.getSingleResult();
        } catch (RuntimeException ex) {
            WebTesterLogger.warn(LOGGER_NAME, "can't obtain counts  " + getEntityClass() + " - field: " + field
                    + " value: " + value);
            throw new WorkWithDataBaseRuntimeException("can`t obtain result from database");
        }
        return result.intValue();
    }

    public List<T> getFilteredAndSortedList
    (Integer offset, Integer limit, SortAndRestrictForEntity sortAndRestrict) {
        List resultList;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
            Root root = criteriaQuery.from(getEntityClass());
            CriteriaQuery select = criteriaQuery.select(root);

            select.where(getFullPredicate(criteriaBuilder, root, sortAndRestrict));

            select.orderBy(getOrderList(criteriaBuilder, root, sortAndRestrict));

            TypedQuery typedQuery = entityManager.createQuery(select);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(limit);
            resultList = typedQuery.getResultList();
        } catch (RuntimeException ex) {
            WebTesterLogger.warn(LOGGER_NAME, "can't obtain filtered and sorted list " + getEntityClass()
                    + " - offset: " + offset + " limit: " + limit + " sort - " + sortAndRestrict.toString());
            throw new WorkWithDataBaseRuntimeException("can`t obtain result from database");
        }

        return resultList;
    }

    private Predicate getFullPredicate (CriteriaBuilder criteriaBuilder,
                                        Root root,
                                        SortAndRestrictForEntity sortAndRestrict){
        List<Predicate> predicateList = new ArrayList<Predicate>();

        //add like restrict to predicate
        Map<String, String> likeRestrict = sortAndRestrict.getLikeRestrictionForEntity();
        for (String tempField : likeRestrict.keySet()) {
            Expression<String> tempFindLiteral = criteriaBuilder.literal("%" + likeRestrict.get(tempField) + "%");
            Expression<String> upperFindLiteral = criteriaBuilder.upper(tempFindLiteral);
            Expression<String> upperFieldLiteral = criteriaBuilder.upper(root.get(tempField).as(String.class));
            Predicate tempPredicate = criteriaBuilder.like(upperFieldLiteral, upperFindLiteral);
            predicateList.add(tempPredicate);
        }

        // add equal restrict to predicate
        Map<String, Object> equalRestrict = sortAndRestrict.getEqualRestrictionForEntity();
        for (String tempField : equalRestrict.keySet()) {
            Expression<Object> literal = criteriaBuilder.literal(equalRestrict.get(tempField));
            Predicate tempPredicate = criteriaBuilder.equal(root.get(tempField), literal);
            predicateList.add(tempPredicate);
        }
        Predicate fullPredicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));

        return fullPredicate;
    }
    private List<Order> getOrderList(CriteriaBuilder criteriaBuilder,
                                     Root root,
                                     SortAndRestrictForEntity sortAndRestrict){
        List<Order> orderList = new ArrayList<Order>();
        Map<String, MySortDirection> sort = sortAndRestrict.getSortForEntity();
        for (String tempField : sort.keySet()) {
            if (sort.get(tempField) == MySortDirection.NOSORT) {
                continue;
            }
            if (sort.get(tempField) == MySortDirection.ASCENDING) {
                orderList.add(criteriaBuilder.asc(root.get(tempField)));
            } else {
                orderList.add(criteriaBuilder.desc(root.get(tempField)));
            }
        }
        return orderList;
    }

    public int getCountRecordsFilteredAndSortedList (SortAndRestrictForEntity sortAndRestrict) {
        int result;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Integer.class);
            Root root = criteriaQuery.from(getEntityClass());
            CriteriaQuery select = criteriaQuery.select(criteriaBuilder.count(root));

            select.where(getFullPredicate(criteriaBuilder, root, sortAndRestrict));

            TypedQuery typedQuery = entityManager.createQuery(select);
            Long longResult = (Long) typedQuery.getSingleResult();
            result = longResult.intValue();
        } catch (RuntimeException ex) {
            WebTesterLogger.warn(LOGGER_NAME, "can't obtain count records filtered and sorted list " + getEntityClass()
                    + " sort - " + sortAndRestrict.toString());
            throw new WorkWithDataBaseRuntimeException("can`t obtain result from database");
        }

        return result;
    }
}
