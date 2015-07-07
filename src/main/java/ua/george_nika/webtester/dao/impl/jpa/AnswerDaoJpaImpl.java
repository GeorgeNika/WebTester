package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.AnswerDao;
import ua.george_nika.webtester.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by George on 08.06.2015.
 */
@Repository
public class AnswerDaoJpaImpl extends AbstractDaoJpaImpl<AnswerEntity> implements AnswerDao {
    @Override
    protected Class getEntityClass() {
        return AnswerEntity.class;
    }
}
