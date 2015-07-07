package ua.george_nika.webtester.dao.intface;

import ua.george_nika.webtester.entity.AccountVerificationEntity;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;

/**
 * Created by George on 09.06.2015.
 */
public interface AccountVerificationDao extends AbstractDao<AccountVerificationEntity> {

    AccountVerificationEntity getByCode(String code);

    int getCountRecordWithCode(String code);
}
