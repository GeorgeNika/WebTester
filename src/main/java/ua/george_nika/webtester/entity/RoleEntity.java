package ua.george_nika.webtester.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by George on 01.06.2015.
 */
@Entity
@Table(name = "role", schema = "public", catalog = "webtester")
public class RoleEntity implements Serializable {
    private int idRole;
    private String name;

    @Id
    @Column(name = "id_role")
    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleEntity that = (RoleEntity) o;

        if (idRole != that.idRole) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = idRole;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "idRole=" + idRole +
                ", name='" + name + '\'' +
                '}';
    }
}
