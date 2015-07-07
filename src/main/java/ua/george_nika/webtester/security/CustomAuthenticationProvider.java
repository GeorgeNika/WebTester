package ua.george_nika.webtester.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.RoleEntity;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.util.WebTesterRole;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by George on 29.06.2015.
 */
@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    @Autowired
    @Qualifier("accountAuthenticationService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    @Autowired
    @Qualifier("pwdEncoder")
    public void setPasswordEncoder(Object passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }

    @Autowired
    AccountService accountService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);
        CurrentAccount currentAccount = (CurrentAccount) userDetails;
        AccountEntity accountEntity = accountService.getAccountByIdWithoutCheck(currentAccount.getIdAccount());
        Set<RoleEntity> roleEntitySet = accountEntity.getRoleSet();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int idRole = Integer.parseInt(request.getParameter("idRole"));
        Boolean temp = false;
        for (RoleEntity tempRole : roleEntitySet) {
            if (tempRole.getIdRole() == idRole) {
                temp = true;
                break;
            }
        }
        if (temp == false) {
            throw new AuthenticationException("Invalid role") {
            };
        }
        currentAccount.setIdRole(idRole);
    }
}
