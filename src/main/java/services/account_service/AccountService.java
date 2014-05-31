package services.account_service;

import message_system.Abonent;
import message_system.Address;
import message_system.MessageSystem;
import models.UserDataSet;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import resource_system.ResourceFactory;
import resource_system.VFS;
import resource_system.resources.AccountServiceConfiguration;
import results.authorization.AuthorizationResult;
import results.authorization.AuthorizationResultEnum;
import results.registration.RegistrationResult;
import results.registration.RegistrationResultEnum;
import services.frontend.User;
import utils.HibernateUtil;
import utils.TimeHelper;

/**
 * Created by Ivan on 26.03.2014 in 19:40.
 */
public class AccountService implements Runnable, Abonent {
    private final Address address;
    private final MessageSystem messageSystem;
    private final UserDataSetDAO userDataSetDAO;

    private AccountServiceConfiguration accountServiceConfiguration;

    public AccountService(MessageSystem messageSystem, VFS vfs) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        accountServiceConfiguration = (AccountServiceConfiguration) ResourceFactory.getInstance().getResource(vfs.getPath("account_service.cfg.xml"));
        SessionFactory sessionFactory = HibernateUtil.getInstance().getSessionFactory(accountServiceConfiguration.DATABASE_CONFIGURATION_FILE_PATH());
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
            TimeHelper.sleep(accountServiceConfiguration.TICK_TIME());
        }
    }

    public RegistrationResult register(User user) {
        try {
            Thread.sleep(5000);
            UserDataSet userDataSet = new UserDataSet(user.getUsername(), user.getPassword());
            userDataSetDAO.addUser(userDataSet);
            UserDataSet returnedUserDataSet = userDataSetDAO.getUser(userDataSet.getUsername(), userDataSet.getPassword());
            return new RegistrationResult(RegistrationResultEnum.SUCCESS, returnedUserDataSet.getIdUser());
        } catch (HibernateException | NullPointerException | InterruptedException e) {
            return new RegistrationResult(RegistrationResultEnum.FAILURE, null);
        }
    }

    public AuthorizationResult authorize(User user) {
        try {
            Thread.sleep(10000);
            UserDataSet userDataSet = new UserDataSet(user.getUsername(), user.getPassword());
            UserDataSet returnedUserDataSet = userDataSetDAO.getUser(userDataSet.getUsername(), userDataSet.getPassword());
            return new AuthorizationResult(AuthorizationResultEnum.SUCCESS, returnedUserDataSet.getIdUser());
        } catch (HibernateException | NullPointerException | InterruptedException e) {
            return new AuthorizationResult(AuthorizationResultEnum.FAILURE, null);
        }
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}