package ua.george_nika.webtester.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "account", schema = "public", catalog = "webtester")
public class AccountEntity implements Serializable{
    private int idAccount;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private Timestamp created;
    private Timestamp updated;
    private boolean active;
    private boolean emailVerified;
    private AccountVerificationEntity accountVerification;
    private Set<RoleEntity> roleSet;

    @Id
    @Column(name = "id_account")
    @SequenceGenerator(name="account_seq", sequenceName="account_seq")
    @GeneratedValue(generator = "account_seq")
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "updated")
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Basic
    @Column(name = "active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "email_verified")
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @OneToOne(mappedBy = "account")
    public AccountVerificationEntity getAccountVerification() {
        return accountVerification;
    }

    public void setAccountVerification(AccountVerificationEntity accountVerificationByIdAccount) {
        this.accountVerification = accountVerificationByIdAccount;
    }

    @ManyToMany
    @JoinTable(name = "account_role",
                joinColumns = @JoinColumn(name = "id_account"),
                inverseJoinColumns = @JoinColumn(name = "id_role"))
    public Set<RoleEntity> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<RoleEntity> roleSet) {
        this.roleSet = roleSet;
    }

    public void addRoleToRoleSet(RoleEntity newRole){
        if (roleSet ==null) {
            roleSet = new HashSet<RoleEntity>();
        }
        roleSet.add(newRole);
    }

    public void deleteRoleFromRoleSet(RoleEntity deletedRole){
        if (roleSet != null) {
            roleSet.remove(deletedRole);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity that = (AccountEntity) o;

        if (idAccount != that.idAccount) return false;
        if (active != that.active) return false;
        if (emailVerified != that.emailVerified) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (middleName != null ? !middleName.equals(that.middleName) : that.middleName != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (updated != null ? !updated.equals(that.updated) : that.updated != null) return false;
        if (accountVerification != null ? !accountVerification.equals(that.accountVerification) : that.accountVerification != null)
            return false;
        return !(roleSet != null ? !roleSet.equals(that.roleSet) : that.roleSet != null);

    }

    @Override
    public int hashCode() {
        int result = idAccount;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (emailVerified ? 1 : 0);
        result = 31 * result + (accountVerification != null ? accountVerification.hashCode() : 0);
        result = 31 * result + (roleSet != null ? roleSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "idAccount=" + idAccount +
                ", login='" + login + '\'' +
                '}';
    }
}
