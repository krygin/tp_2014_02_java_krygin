import results.authorization.AuthorizationResult;

/**
 * Created by Ivan on 30.03.2014.
 */
final public class AuthorizationResponseMsg extends MsgToFrontend {
    private AuthorizationResult result;
    private String sessionId;

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
