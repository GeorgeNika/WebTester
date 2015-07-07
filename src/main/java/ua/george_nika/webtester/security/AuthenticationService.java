package ua.george_nika.webtester.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.george_nika.webtester.dao.intface.AccountDao;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.RoleEntity;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.util.WebTesterLogger;
import ua.george_nika.webtester.util.WebTesterRole;

import java.util.*;

/**
 * Created by George on 29.06.2015.
 */
@Service("accountAuthenticationService")
public class AuthenticationService implements UserDetailsService {

    private static String LOGGER_NAME = AccountService.class.getSimpleName();

    private static final Map<Integer, String> ROLES = new HashMap<Integer, String>();

    static {
        ROLES.put(WebTesterRole.ADMINISTRATOR.getId(), "ADMIN");
        ROLES.put(WebTesterRole.ADVANCE_TUTOR.getId(), "TUTOR");
        ROLES.put(WebTesterRole.TUTOR.getId(), "TUTOR");
        ROLES.put(WebTesterRole.STUDENT.getId(), "STUDENT");
    }

    static Collection<? extends GrantedAuthority> convert(Set<RoleEntity> roles) {
        Collection<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();
        for (RoleEntity tempRole : roles) {
            result.add(new SimpleGrantedAuthority(ROLES.get(tempRole.getIdRole())));
        }
        return result;
    }

    @Autowired
    private AccountDao accountDao;

    public UserDetails loadUserByUsername(String userName) {
        try {
            AccountEntity accountEntity = accountDao.getAccountByLogin(userName);
            return new CurrentAccount(accountEntity);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Account not found by id=" + userName);
            throw new UsernameNotFoundException("Account not found by id=" + userName);
        }
    }
}
