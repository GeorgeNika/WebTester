package ua.george_nika.webtester.services;

import ua.george_nika.webtester.dao.intface.RoleDao;
import ua.george_nika.webtester.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by George on 16.06.2015.
 */
@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public List<RoleEntity> getAllRole() {
        return roleDao.getAll();
    }

    public RoleEntity getRoleById(int idRole) {
        return roleDao.getRoleById(idRole);
    }

}
