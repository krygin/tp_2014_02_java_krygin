package services.account_service;

import message_system.Address;
import message_system.MessageSystem;
import models.UserDataSet;
import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import results.authorization.AuthorizationResult;
import results.authorization.AuthorizationResultEnum;
import results.registration.RegistrationResult;
import results.registration.RegistrationResultEnum;
import services.frontend.User;
import utils.RandomStringGenerator;
import utils.ReflectionHelper;

import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by Ivan on 04.04.2014 in 0:31.
 */

public class AccountServiceTest {
    private static final MessageSystem messageSystem = mock(MessageSystem.class);
    private static final Address address = mock(Address.class);
    private static final UserDataSetDAO userDataSetDAO = mock(UserDataSetDAO.class);
    private final AccountService accountService = new AccountService(messageSystem, address, userDataSetDAO);
    private static final int PASSWORD_LENGTH = 10;
    private static final int USERNAME_LENGTH = 10;
    private static UserDataSet testUserDataSet;
    private static User testUser;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        String testUsername = RandomStringGenerator.getString(USERNAME_LENGTH);
        String testPassword = RandomStringGenerator.getString(PASSWORD_LENGTH);
        Integer testIdUser = new Random().nextInt();


        testUserDataSet = new UserDataSet(testUsername, testPassword);
        testUser = new User(testUsername, testPassword);
        ReflectionHelper.setFieldValue(testUserDataSet, "idUser", testIdUser.toString());
    }


    @Test
    public void testGetAddress() throws Exception {
        Address address = accountService.getAddress();
        assertNotNull(address);
    }

    @Test
    public void testGetMessageSystem() throws Exception {
        MessageSystem messageSystem = accountService.getMessageSystem();
        assertNotNull(messageSystem);
    }

    @Test
    public void testAuthorizeSuccess() throws Exception {
        doReturn(testUserDataSet).when(userDataSetDAO).getUser(testUserDataSet.getUsername(), testUserDataSet.getPassword());
        AuthorizationResult result = accountService.authorize(testUser);
        Assert.assertEquals(AuthorizationResultEnum.SUCCESS, result.getResult());
    }

    @Test
    public void testAuthorizeFailure() throws Exception {
        doThrow(new HibernateException("")).when(userDataSetDAO).getUser(testUserDataSet.getUsername(), testUserDataSet.getPassword());
        AuthorizationResult result = accountService.authorize(testUser);
        Assert.assertEquals(AuthorizationResultEnum.FAILURE, result.getResult());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        doReturn(testUserDataSet).when(userDataSetDAO).getUser(testUserDataSet.getUsername(), testUserDataSet.getPassword());
        doNothing().when(userDataSetDAO).addUser(testUserDataSet);
        RegistrationResult result = accountService.register(testUser);
        Assert.assertEquals(RegistrationResultEnum.SUCCESS, result.getResult());
    }

    @Test
    public void testRegisterFailure() throws Exception {
        doThrow(new HibernateException("")).when(userDataSetDAO).addUser(testUserDataSet);
        doThrow(new HibernateException("")).when(userDataSetDAO).getUser(testUserDataSet.getUsername(), testUserDataSet.getPassword());
        RegistrationResult result = accountService.register(testUser);
        Assert.assertEquals(RegistrationResultEnum.FAILURE, result.getResult());
        Assert.assertNull(result.getIdUser());
    }
}