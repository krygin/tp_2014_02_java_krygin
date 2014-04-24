package services.account_service;

import message_system.Address;
import message_system.MessageSystem;
import models.UserDataSet;
import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Test;
import results.authorization.AuthorizationResult;
import results.authorization.AuthorizationResultEnum;
import results.registration.RegistrationResult;
import results.registration.RegistrationResultEnum;
import services.frontend.User;

import static org.mockito.Mockito.*;

/**
 * Created by Ivan on 04.04.2014 in 0:31.
 */

public class AccountServiceTest {
    private static final MessageSystem messageSystem = mock(MessageSystem.class);
    private static final Address address = mock(Address.class);
    private static final UserDataSetDAO userDataSetDAO = mock(UserDataSetDAO.class);

    private final AccountService accountService = new AccountService(messageSystem, address, userDataSetDAO);

    @Test
    public void testGetAddress() throws Exception {
        Address address = accountService.getAddress();
        Assert.assertNotNull(address);
    }

    @Test
    public void testGetMessageSystem() throws Exception {
        MessageSystem messageSystem = accountService.getMessageSystem();
        Assert.assertNotNull(messageSystem);
    }

    @Test
    public void testAuthorizeSuccess() throws Exception {
        String TEST_USERNAME = "username";
        String TEST_PASSWORD = "password";
        Integer TEST_ID_USER = 0;
        UserDataSet TEST_USER_DATA_SET = new UserDataSet(TEST_USERNAME, TEST_PASSWORD);
        doReturn(TEST_USER_DATA_SET).when(userDataSetDAO).getUser(TEST_USER_DATA_SET.getUsername(), TEST_USER_DATA_SET.getPassword());
        AuthorizationResult result = accountService.authorize(new User(TEST_USERNAME, TEST_PASSWORD));
        Assert.assertEquals(AuthorizationResultEnum.SUCCESS, result.getResult());
    }

    @Test
    public void testAuthorizeFailure() throws Exception {
        String TEST_USERNAME = "username";
        String TEST_PASSWORD = "password";
        UserDataSet TEST_USER_DATA_SET = new UserDataSet(TEST_USERNAME, TEST_PASSWORD);
        doThrow(new HibernateException("")).when(userDataSetDAO).getUser(TEST_USER_DATA_SET.getUsername(), TEST_USER_DATA_SET.getPassword());
        AuthorizationResult result = accountService.authorize(new User(TEST_USERNAME, TEST_PASSWORD));
        Assert.assertEquals(AuthorizationResultEnum.FAILURE, result.getResult());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        Integer TEST_ID_USER = 0;
        String TEST_USERNAME = "username";
        String TEST_PASSWORD = "password";
        User TEST_USER = new User(TEST_USERNAME, TEST_PASSWORD);
        UserDataSet TEST_USER_DATA_SET = new UserDataSet(TEST_USERNAME, TEST_PASSWORD);
        doReturn(TEST_ID_USER).when(userDataSetDAO).getUser(TEST_USER_DATA_SET.getUsername(), TEST_USER_DATA_SET.getPassword());
        doNothing().when(userDataSetDAO).addUser(TEST_USER_DATA_SET);
        RegistrationResult result = accountService.register(TEST_USER);
        Assert.assertEquals(RegistrationResultEnum.SUCCESS, result.getResult());
    }

    @Test
    public void testRegisterFailure() throws Exception {
        String TEST_USERNAME = "username";
        String TEST_PASSWORD = "password";
        User TEST_USER = new User(TEST_USERNAME, TEST_PASSWORD);
        UserDataSet TEST_USER_DATA_SET = new UserDataSet(TEST_USERNAME, TEST_PASSWORD);
        doThrow(new HibernateException("")).when(userDataSetDAO).addUser(TEST_USER_DATA_SET);
        doThrow(new HibernateException("")).when(userDataSetDAO).getUser(TEST_USER_DATA_SET.getUsername(), TEST_USER_DATA_SET.getPassword());
        RegistrationResult result = accountService.register(TEST_USER);
        Assert.assertEquals(RegistrationResultEnum.FAILURE, result.getResult());
        Assert.assertNull(result.getIdUser());
    }
}