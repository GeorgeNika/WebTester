package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.QuestionDao;
import ua.george_nika.webtester.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by George on 08.06.2015.
 */
@Repository
public class QuestionDaoJpaImpl extends AbstractDaoJpaImpl<QuestionEntity> implements QuestionDao {
    @Override
    protected Class getEntityClass() {
        return QuestionEntity.class;
    }
}
