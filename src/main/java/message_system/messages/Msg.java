package message_system.messages;

import message_system.Abonent;
import message_system.Address;

/**
 * Created by Ivan on 26.03.2014 in 19:43.
 */
public abstract class Msg {
    final private Address from;
    final private Address to;

    protected Msg(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract void exec(Abonent abonent);
}
