package message_system;

import message_system.messages.Msg;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ivan on 31.05.2014 in 9:20.
 */
public class MessageSystemTest {
    private static final MessageSystem messageSystem = new MessageSystem();

    private BobAbonent bob;
    private AliceAbonent alice;

    @BeforeClass
    public static void setUpOnce() {
    }

    @Test
    public void testMessageSystemExecutesMessages() {
        alice = new AliceAbonent(messageSystem);
        bob = new BobAbonent(messageSystem);
        TestMessage message = new TestMessage(alice.getAddress(), bob.getAddress());

        messageSystem.sendMessage(message);
        assertFalse(message.wasExecuted());
        messageSystem.execForAbonent(alice);
        assertFalse(message.wasExecuted());
        messageSystem.execForAbonent(bob);
        assertTrue(message.wasExecuted());

    }

    private class AliceAbonent implements Abonent {
        private final Address address = new Address();

        public AliceAbonent(MessageSystem messageSystem) {
            messageSystem.addService(this);
        }

        @Override
        public Address getAddress() {
            return address;
        }
    }

    private class BobAbonent implements Abonent {
        private final Address address = new Address();

        public BobAbonent(MessageSystem messageSystem) {
            messageSystem.addService(this);
        }

        @Override
        public Address getAddress() {
            return null;
        }
    }

    private abstract class MsgToBobAbonent extends Msg {
        protected MsgToBobAbonent(Address from, Address to) {
            super(from, to);
        }

        public void exec(Abonent abonent) {
            if (abonent instanceof BobAbonent) {
                exec((BobAbonent) abonent);
            }
        }

        abstract void exec(BobAbonent accountService);
    }

    private class TestMessage extends MsgToBobAbonent {
        private boolean wasExecuted;

        protected TestMessage(Address from, Address to) {
            super(from, to);
            wasExecuted = false;
        }

        public boolean wasExecuted() {
            return wasExecuted;
        }

        @Override
        void exec(BobAbonent accountService) {
            wasExecuted = true;
        }
    }
}
