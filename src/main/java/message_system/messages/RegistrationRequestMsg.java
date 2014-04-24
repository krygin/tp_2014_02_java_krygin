package message_system.messages;

import message_system.Address;
import results.registration.RegistrationResult;
import services.account_service.AccountService;
import services.frontend.User;

/**
 * Created by Ivan on 27.03.2014 in 19:43.
 */
public final class RegistrationRequestMsg extends MsgToAS {
    private final User user;
    private final String sessionId;

    public RegistrationRequestMsg(Address from, Address to, User user, String sessionId) {
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
