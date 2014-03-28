/**
 * Created by Ivan on 26.03.2014.
 */
public abstract class MsgToFrontend extends Msg {
    protected MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    public void exec(Abonent abonent) {
        if (abonent instanceof Frontend) {
            exec((Frontend) abonent);
        }
    }
    abstract void exec(Frontend accountService);
}