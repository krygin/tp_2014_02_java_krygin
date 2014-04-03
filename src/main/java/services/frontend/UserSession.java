package services.frontend;

/**
 * Created by Ivan on 26.03.2014 in 19:44.
 */
class UserSession {
    private String sessionId;
    private String username;
    private Long userId;
    private UserState userState;

    public UserSession(String sessionId) {
        this.sessionId = sessionId;
        this.userState = UserState.IS_ANONYMOUS;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
