/**
 * Created by Ivan on 27.03.2014.
 */
public class CreateNewUserMsg extends MsgToAS {
    private String username;
    private String password;
    private String sessionId;

    public CreateNewUserMsg(Address from, Address to, String username, String password, String sessionId) {
        super(from, to);
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    void exec(AccountService accountService) {

    }
}
