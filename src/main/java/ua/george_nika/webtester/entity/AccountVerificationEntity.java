package ua.george_nika.webtester.entity;

import javax.persistence.*;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "account_verification", schema = "public", catalog = "webtester")
public class AccountVerificationEntity {
    private int idAccount;
    private String code;
    private AccountEntity account;

    @Id
    @Column(name = "id_account")
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @OneToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account", nullable = false, insertable =false, updatable = false)
    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountVerificationEntity that = (AccountVerificationEntity) o;

        if (idAccount != that.idAccount) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return !(account != null ? !account.equals(that.account) : that.account != null);

    }

    @Override
    public int hashCode() {
        int result = idAccount;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
