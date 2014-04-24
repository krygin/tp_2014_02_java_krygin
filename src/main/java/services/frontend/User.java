package services.frontend;

/**
 * Created by Ivan on 05.04.2014 in 21:32.
 */
public class User {
    final private String username;
    final private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
