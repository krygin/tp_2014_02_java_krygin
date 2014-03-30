/**
 * Created by Ivan on 27.03.2014.
 */
final public class RegistrationRequestMsg extends MsgToAS {
    private String username;
    private String password;
    private String sessionId;

    public RegistrationRequestMsg(Address from, Address to, String username, String password, String sessionId) {
        super(from, to);
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    void exec(AccountService accountService) {
        RegistrationResult result = accountService.register(username, password);
        accountService.getMessageSystem().sendMessage(new RegistrationResponseMsg(getTo(), getFrom(), result, sessionId));
    }
}
