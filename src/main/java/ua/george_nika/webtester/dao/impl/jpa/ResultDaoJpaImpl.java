package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.ResultDao;
import ua.george_nika.webtester.entity.ResultEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by George on 08.06.2015.
 */
@Repository
public class ResultDaoJpaImpl extends AbstractDaoJpaImpl<ResultEntity> implements ResultDao {
    @Override
    protected Class getEntityClass() {
        return ResultEntity.class;
    }
}
