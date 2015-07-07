package ua.george_nika.webtester.dao.impl.jpa;

import ua.george_nika.webtester.dao.intface.RoleDao;
import ua.george_nika.webtester.entity.RoleEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by George on 01.06.2015.
 */
@Repository
public class RoleDaoJpaImpl extends AbstractDaoJpaImpl<RoleEntity> implements RoleDao {

    protected Class getEntityClass() {
        return RoleEntity.class;
    }

    public RoleEntity getRoleById(int id) {
        return getById(id);
    }
}
