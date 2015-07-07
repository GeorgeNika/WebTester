package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.AccountVerificationDao;
import ua.george_nika.webtester.entity.AccountVerificationEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by George on 03.06.2015.
 */
@Repository
public class AccountVerificationDaoJpaImpl
        extends AbstractDaoJpaImpl<AccountVerificationEntity>
        implements AccountVerificationDao {

    @Override
    protected Class getEntityClass() {
        return AccountVerificationEntity.class;
    }

    public AccountVerificationEntity getByCode(String code) {
        return getSingleBy("code", code);
    }

    public int getCountRecordWithCode(String code) {
        return getCountRecordsWith("code", code);
    }
}
