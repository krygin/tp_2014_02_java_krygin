package message_system;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ivan on 26.03.2014 in 19:42.
 */
public class Address {
    static private final AtomicInteger abonentIdCreator = new AtomicInteger();
    final private int abonentId;

    public Address() {
        this.abonentId = abonentIdCreator.getAndIncrement();
    }

    public int hashCode() {
        return abonentId;
    }
}
