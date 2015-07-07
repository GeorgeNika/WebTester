package ua.george_nika.webtester.forms;

import ua.george_nika.webtester.entity.AccountEntity;

/**
 * Created by George on 23.06.2015.
 */
public class EditAccountForm {

    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;

    public EditAccountForm() {
    }

    public EditAccountForm(AccountEntity accountEntity) {
        this.login = accountEntity.getLogin();
        this.password = accountEntity.getPassword();
        this.email = accountEntity.getEmail();
        this.firstName = accountEntity.getFirstName();
        this.lastName = accountEntity.getLastName();
        this.middleName = accountEntity.getMiddleName();
    }

    public void updateAccount(AccountEntity accountEntity) {
        accountEntity.setLogin(getLogin());
        accountEntity.setPassword(getPassword());
        accountEntity.setEmail(getEmail());
        accountEntity.setFirstName(getFirstName());
        accountEntity.setLastName(getLastName());
        accountEntity.setMiddleName(getMiddleName());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
