package services.frontend;

/**
 * Created by Ivan on 26.03.2014 in 19:44.
 */
class UserSession {
    private String sessionId;
    private String username;
    private int idUser;
    private UserState userState;

    public UserSession(String sessionId) {
        this.sessionId = sessionId;
        this.userState = UserState.IS_ANONYMOUS;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
