import results.registration.RegistrationResult;

/**
 * Created by Ivan on 30.03.2014.
 */
final public class RegistrationResponseMsg extends MsgToFrontend {

    private RegistrationResult result;
    private String sessionId;

    protected RegistrationResponseMsg(Address from, Address to, RegistrationResult result, String sessionId) {
        super(from, to);
        this.result = result;
        this.sessionId = sessionId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.handleRegistrationResponse(sessionId, result);
    }
}
