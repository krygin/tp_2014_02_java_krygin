package services.account_service;

import message_system.Abonent;
import message_system.Address;
import message_system.MessageSystem;
import models.UserDataSet;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import results.authorization.AuthorizationResult;
import results.authorization.AuthorizationResultEnum;
import results.registration.RegistrationResult;
import results.registration.RegistrationResultEnum;
import utils.HibernateUtil;
import utils.TimeHelper;

import static constants.Constants.TICK_TIME;

/**
 * Created by Ivan on 26.03.2014 in 19:40.
 */
public class AccountService implements Runnable, Abonent {
    private final Address address;
    private final MessageSystem messageSystem;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final UserDataSetDAO userDataSetDAO = new UserDataSetDAO(sessionFactory);

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
        //noinspection InfiniteLoopStatement
        while (true) {
            messageSystem.execForAbonent(this);
            TimeHelper.sleep(TICK_TIME);
        }
    }

    public RegistrationResult register(UserDataSet user) {
        try {
            userDataSetDAO.add(user);
            Integer idUser = userDataSetDAO.get(user.getUsername(), user.getPassword()).getIdUser();
            return new RegistrationResult(RegistrationResultEnum.SUCCESS, idUser);
        } catch (HibernateException | NullPointerException e) {
            return new RegistrationResult(RegistrationResultEnum.FAIL, null);
        }
    }

    public AuthorizationResult authorize(UserDataSet user) {
        try {
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