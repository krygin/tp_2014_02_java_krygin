import utils.TimeHelper;

/**
 * Created by Ivan on 26.03.2014.
 */
public class AccountService implements Runnable, Abonent {
    private Address address;
    private MessageSystem messageSystem;

    public AccountService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);
            TimeHelper.sleep(GlobalConstants.TICK_TIME);
        }

    }
}
