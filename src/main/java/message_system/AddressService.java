package message_system;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 28.03.2014 in 19:42.
 */
public class AddressService {
    private Address frontend;
    private List<Address> accountServices = new ArrayList<>();

    public Address getFrontend() {
        return frontend;
    }

    public void setFrontend(Address frontend) {
        this.frontend = frontend;
    }

    public Address getAccountService(int index) {
        return accountServices.get(index);
    }

    public void setAccountService(Address accountService) {
        this.accountServices.add(accountService);
    }
}
