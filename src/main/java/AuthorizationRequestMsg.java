import models.UserDataSet;
import results.authorization.AuthorizationResult;

/**
 * Created by Ivan on 30.03.2014.
 */
final public class AuthorizationRequestMsg extends MsgToAS {
    private UserDataSet user;
    private String sessionId;

    protected AuthorizationRequestMsg(Address from, Address to, UserDataSet user, String sessionId) {
        super(from, to);
        this.user = user;
        this.sessionId = sessionId;
    }

    @Override
    void exec(AccountService accountService) {
        AuthorizationResult result = accountService.authorize(user);
        accountService.getMessageSystem().sendMessage(new AuthorizationResponseMsg(getTo(), getFrom(), result, sessionId));
    }
}