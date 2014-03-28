/**
 * Created by Ivan on 26.03.2014.
 */
public abstract class MsgToAS extends Msg {
    protected MsgToAS(Address from, Address to) {
        super(from, to);
    }

    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            exec((AccountService) abonent);
        }
    }

    abstract void exec(AccountService accountService);
}