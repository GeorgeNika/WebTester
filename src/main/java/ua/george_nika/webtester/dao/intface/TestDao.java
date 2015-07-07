package ua.george_nika.webtester.dao.intface;

import ua.george_nika.webtester.entity.TestEntity;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;

/**
 * Created by George on 09.06.2015.
 */
public interface TestDao extends AbstractDao<TestEntity> {

    int getCountRecordWithName(String name);
}
