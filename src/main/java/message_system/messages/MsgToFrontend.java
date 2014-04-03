package message_system.messages;

import message_system.Abonent;
import message_system.Address;
import services.frontend.Frontend;

/**
 * Created by Ivan on 26.03.2014 in 19:43.
 */
public abstract class MsgToFrontend extends Msg {
    MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    public void exec(Abonent abonent) {
        if (abonent instanceof Frontend) {
            exec((Frontend) abonent);
        }
    }

    abstract void exec(Frontend frontend);
}