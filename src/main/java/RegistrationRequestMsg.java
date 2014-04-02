import models.UserDataSet;
import results.registration.RegistrationResult;

/**
 * Created by Ivan on 27.03.2014.
 */
final public class RegistrationRequestMsg extends MsgToAS {
    private UserDataSet user;
    private String sessionId;

    public RegistrationRequestMsg(Address from, Address to, UserDataSet user, String sessionId) {
        super(from, to);
        this.user = user;
        this.sessionId = sessionId;
    }

    @Override
    void exec(AccountService accountService) {
        RegistrationResult result = accountService.register(user);
        accountService.getMessageSystem().sendMessage(new RegistrationResponseMsg(getTo(), getFrom(), result, sessionId));
    }
}
