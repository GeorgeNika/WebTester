package ua.george_nika.webtester.security;

import org.springframework.security.core.userdetails.User;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterRole;

public class CurrentAccount extends User {

    private final int idAccount;
    private int idRole;

    public CurrentAccount (AccountEntity accountEntity){
        super(accountEntity.getLogin(), accountEntity.getPassword(), accountEntity.isActive(), true, true, true,
                AuthenticationService.convert(accountEntity.getRoleSet()));
        this.idAccount = accountEntity.getIdAccount();
        this.idRole = WebTesterRole.STUDENT.getId();
    }

    public int getIdAccount(){
        return idAccount;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CurrentAccount that = (CurrentAccount) o;

        if (idAccount != that.idAccount) return false;
        return idRole == that.idRole;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + idAccount;
        result = 31 * result + idRole;
        return result;
    }
}
