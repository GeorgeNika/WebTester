package ua.george_nika.webtester.forms;

import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;

public class EditAccountForNonAdminForm {

    private String password;
    private String confirmPassword;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;

    public EditAccountForNonAdminForm() {
    }

    public EditAccountForNonAdminForm(AccountEntity accountEntity) {
        this.password = accountEntity.getPassword();
        this.confirmPassword = accountEntity.getPassword();
        this.email = accountEntity.getEmail();
        this.firstName = accountEntity.getFirstName();
        this.lastName = accountEntity.getLastName();
        this.middleName = accountEntity.getMiddleName();
    }

    public void updateAccount(AccountEntity accountEntity) {
        if (!password.equals(confirmPassword)) {
            throw new UserWrongInputException("Password not the same");
        }
        accountEntity.setPassword(getPassword());
        accountEntity.setEmail(getEmail());
        accountEntity.setFirstName(getFirstName());
        accountEntity.setLastName(getLastName());
        accountEntity.setMiddleName(getMiddleName());
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

