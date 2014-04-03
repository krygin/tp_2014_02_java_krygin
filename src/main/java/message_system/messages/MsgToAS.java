package message_system.messages;

import message_system.Abonent;
import message_system.Address;
import services.account_service.AccountService;

/**
 * Created by Ivan on 26.03.2014 in 19:43.
 */
public abstract class MsgToAS extends Msg {
    MsgToAS(Address from, Address to) {
        super(from, to);
    }

    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            exec((AccountService) abonent);
        }
    }

    abstract void exec(AccountService accountService);
}