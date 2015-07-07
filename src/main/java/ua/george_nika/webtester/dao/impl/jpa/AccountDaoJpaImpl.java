package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.AccountDao;
import ua.george_nika.webtester.entity.AccountEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by George on 02.06.2015.
 */
@Repository
//@Transactional(propagation = Propagation.MANDATORY)
public class AccountDaoJpaImpl extends AbstractDaoJpaImpl<AccountEntity> implements AccountDao {
    @Override
    protected Class getEntityClass() {
        return AccountEntity.class;
    }

    public int getCountAccountsWithLogin(String login) {
        return getCountRecordsWith("login", login);
    }

    public int getCountAccountsWithEmail(String email) {
        return getCountRecordsWith("email", email);
    }

    public AccountEntity getAccountByEmail(String email) {
        return getSingleBy("email", email);
    }

    public AccountEntity getAccountByLogin(String login) {
        return getSingleBy("login", login);
    }

    public AccountEntity getAccountById(int id) {
        return getById(id);
    }

}
