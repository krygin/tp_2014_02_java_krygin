/**
 * Created by Ivan on 26.03.2014.
 */
public abstract class Msg {
    final private Address from;
    final private Address to;

    public Msg(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected Address getFrom() {
        return from;
    }

    protected Address getTo() {
        return to;
    }

    public abstract void exec(Abonent abonent);
}
