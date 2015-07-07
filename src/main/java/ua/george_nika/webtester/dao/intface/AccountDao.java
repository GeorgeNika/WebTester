package ua.george_nika.webtester.dao.intface;

import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;

/**
 * Created by George on 09.06.2015.
 */
public interface AccountDao extends AbstractDao<AccountEntity> {

    int getCountAccountsWithLogin(String login);

    int getCountAccountsWithEmail(String email);

    AccountEntity getAccountByLogin(String login);

    AccountEntity getAccountByEmail(String email);

    AccountEntity getAccountById(int id);

}
