package message_system.messages;

import message_system.Address;
import results.registration.RegistrationResult;
import services.frontend.Frontend;

/**
 * Created by Ivan on 30.03.2014 in 19:42.
 */
final class RegistrationResponseMsg extends MsgToFrontend {

    private final RegistrationResult result;
    private final String sessionId;

    RegistrationResponseMsg(Address from, Address to, RegistrationResult result, String sessionId) {
        super(from, to);
        this.result = result;
        this.sessionId = sessionId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.handleRegistrationResponse(sessionId, result);
    }
}
