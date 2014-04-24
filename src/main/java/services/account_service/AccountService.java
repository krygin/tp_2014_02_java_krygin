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
import services.frontend.User;
import utils.HibernateUtil;
import utils.TimeHelper;

import static constants.Constants.TICK_TIME;

/**
 * Created by Ivan on 26.03.2014 in 19:40.
 */
public class AccountService implements Runnable, Abonent {
    private final Address address;
    private final MessageSystem messageSystem;
    private final UserDataSetDAO userDataSetDAO;

    public AccountService(MessageSystem messageSystem, String configuration) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        SessionFactory sessionFactory = new HibernateUtil(configuration).getSessionFactory();
        this.userDataSetDAO = new UserDataSetDAO(sessionFactory);
        messageSystem.addService(this);
        messageSystem.getAddressService().setAccountService(address);
    }


    @SuppressWarnings("SameParameterValue")
    AccountService(MessageSystem messageSystem, Address address, UserDataSetDAO userDataSetDAO) {
        this.messageSystem = messageSystem;
        this.address = address;
        this.userDataSetDAO = userDataSetDAO;
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

    public RegistrationResult register(User user) {
        try {
            UserDataSet userDataSet = new UserDataSet(user.getUsername(), user.getPassword());
            userDataSetDAO.addUser(userDataSet);
            Integer idUser = userDataSetDAO.getUser(userDataSet.getUsername(), userDataSet.getPassword());
            return new RegistrationResult(RegistrationResultEnum.SUCCESS, idUser);
        } catch (HibernateException | NullPointerException e) {
            return new RegistrationResult(RegistrationResultEnum.FAILURE, null);
        }
    }

    public AuthorizationResult authorize(User user) {
        try {
            UserDataSet userDataSet = new UserDataSet(user.getUsername(), user.getPassword());
            int id = userDataSetDAO.getUser(userDataSet.getUsername(), userDataSet.getPassword());
            return new AuthorizationResult(AuthorizationResultEnum.SUCCESS, id);
        } catch (HibernateException | NullPointerException e) {
            return new AuthorizationResult(AuthorizationResultEnum.FAILURE, null);
        }
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}