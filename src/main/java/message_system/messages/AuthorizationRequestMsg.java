package message_system.messages;

import message_system.Address;
import models.UserDataSet;
import results.authorization.AuthorizationResult;
import services.account_service.AccountService;

/**
 * Created by Ivan on 30.03.2014 in 19:42.
 */
public final class AuthorizationRequestMsg extends MsgToAS {
    private final UserDataSet user;
    private final String sessionId;

    public AuthorizationRequestMsg(Address from, Address to, UserDataSet user, String sessionId) {
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