package message_system.messages;

import message_system.Address;
import results.authorization.AuthorizationResult;
import services.frontend.Frontend;

/**
 * Created by Ivan on 30.03.2014 in 19:42.
 */
final class AuthorizationResponseMsg extends MsgToFrontend {
    private final AuthorizationResult result;
    private final String sessionId;

    public AuthorizationResponseMsg(Address from, Address to, AuthorizationResult result, String sessionId) {
        super(from, to);
        this.result = result;
        this.sessionId = sessionId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.handleAuthorizationResponse(sessionId, result);
    }
}
