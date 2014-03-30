/**
 * Created by Ivan on 30.03.2014.
 */
final public class AuthorizationRequestMsg extends MsgToAS {
    private String username;
    private String password;
    private String sessionId;

    protected AuthorizationRequestMsg(Address from, Address to, String username, String password, String sessionId) {
        super(from, to);
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    void exec(AccountService accountService) {
        AuthorizationResult result = accountService.authorize(username, password);
        accountService.getMessageSystem().sendMessage(new AuthorizationResponseMsg(getTo(), getFrom(), result, sessionId));
    }
}