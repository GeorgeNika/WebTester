package ua.george_nika.webtester.dao.intface;

import ua.george_nika.webtester.dao.util.SortAndRestrictForEntity;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;

import java.util.List;

/**
 * Created by George on 09.06.2015.
 */
public interface AbstractDao<T> {

    void save(Object abstractEntity);

    void update(Object abstractEntity);

    void delete(Object abstractEntity);

    T getById(Integer id);

    List<T> getAll();

    T getSingleBy(String field, String value);

    int getCountRecordsWith(String field, String value);

    List<T> getFilteredAndSortedList(Integer offset, Integer limit, SortAndRestrictForEntity sortAndRestrict);

    int getCountRecordsFilteredAndSortedList (SortAndRestrictForEntity sortAndRestrict);

}
