package ua.george_nika.webtester.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.RoleEntity;

/**
 * Created by George on 29.06.2015.
 */
public class SecurityUtils {

    public static long getCurrentIdAccount(){
        CurrentAccount currentAccount = getCurrentAccount();
        return currentAccount != null ? currentAccount.getIdAccount() : -1;
    }

    public static CurrentAccount getCurrentAccount (){
        Object principal;
        try {
            principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            return null;
        }
        if (principal instanceof CurrentAccount){
            return (CurrentAccount)principal;
        }
        return null;
    }

    public static void authenticate (AccountEntity accountEntity, RoleEntity roleEntity){
        CurrentAccount currentAccount = new CurrentAccount(accountEntity);
        currentAccount.setIdRole(roleEntity.getIdRole());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                currentAccount,
                accountEntity.getPassword(),
                AuthenticationService.convert(accountEntity.getRoleSet()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
