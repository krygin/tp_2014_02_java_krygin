import models.UserDataSet;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;
import utils.TimeHelper;

/**
 * Created by Ivan on 26.03.2014.
 */
public class AccountService implements Runnable, Abonent {
    private Address address;
    private MessageSystem messageSystem;

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private UserDataSetDAO userDataSetDAO = new UserDataSetDAOImpl(sessionFactory);

    public AccountService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
        messageSystem.getAddressService().setAccountService(address);
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

    public RegistrationResult register(String username, String password) {
        try {
            UserDataSet user = new UserDataSet(username, password);
            userDataSetDAO.add(user);
            Integer idUser = userDataSetDAO.get(user.getUsername(), user.getPassword()).getIdUser();
            return new RegistrationResult(RegistrationResultEnum.SUCCESS, idUser);
        } catch (HibernateException | NullPointerException e) {
            return new RegistrationResult(RegistrationResultEnum.FAIL, null);
        }
    }

    public AuthorizationResult authorize(String username, String password) {
        try {
            UserDataSet user = new UserDataSet(username, password);
            Integer idUser = userDataSetDAO.get(user.getUsername(), user.getPassword()).getIdUser();
            return new AuthorizationResult(AuthorizationResultEnum.SUCCESS, idUser);
        } catch (HibernateException | NullPointerException e) {
            return new AuthorizationResult(AuthorizationResultEnum.FAIL, null);
        }
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}