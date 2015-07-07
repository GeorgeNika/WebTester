package ua.george_nika.webtester.forms;

import java.io.Serializable;

/**
 * Created by George on 16.06.2015.
 */
public class LoginForm implements Serializable {

    private String j_username;
    private String j_password;
    private Integer idRole;

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String login) {
        this.j_username = login;
    }

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String password) {
        this.j_password = password;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
}
