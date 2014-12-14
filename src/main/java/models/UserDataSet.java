package models;

import utils.Hasher;

import javax.persistence.*;

/**
 * Created by Ivan on 12.03.14 in 19:44.
 */
@Entity
@Table(name = "user", schema = "", catalog = "javadb")
public class UserDataSet {
    private int idUser;
    private String username;
    private String password;

    public UserDataSet() {

    }

    public UserDataSet(String username, String password) {
        this.username = username;
        this.password = Hasher.md5(Hasher.md5(password));
        this.idUser = -1;
    }

    @Id
    @Column(name = "idUser")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "Username", unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataSet that = (UserDataSet) o;

        if (idUser != that.idUser) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (username != null && !username.equals(that.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
