/**
 * Created by Ivan on 28.03.2014.
 */
public class AddressService {
    private Address frontend;
    private Address accountService;

    public Address getFrontend() {
        return frontend;
    }

    public void setFrontend(Address frontend) {
        this.frontend = frontend;
    }

    public Address getAccountService() {
        return accountService;
    }

    public void setAccountService(Address accountService) {
        this.accountService = accountService;
    }
}
