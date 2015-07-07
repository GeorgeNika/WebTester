package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.TestDao;
import ua.george_nika.webtester.entity.TestEntity;
import org.springframework.stereotype.Repository;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;

/**
 * Created by George on 08.06.2015.
 */
@Repository
public class TestDaoJpaImpl extends AbstractDaoJpaImpl<TestEntity> implements TestDao {

    @Override
    protected Class getEntityClass() {
        return TestEntity.class;
    }

    public int getCountRecordWithName(String name) {
        return getCountRecordsWith("name", name);
    }
}
