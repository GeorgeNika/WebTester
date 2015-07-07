package ua.george_nika.webtester.dao.intface;

import ua.george_nika.webtester.entity.RoleEntity;
import ua.george_nika.webtester.errors.WorkWithDataBaseRuntimeException;

/**
 * Created by George on 09.06.2015.
 */
public interface RoleDao extends AbstractDao<RoleEntity> {

    RoleEntity getRoleById(int id);
}
