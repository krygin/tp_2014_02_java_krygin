package services.account_service;import models.UserDataSet;import org.hibernate.HibernateException;import org.hibernate.Session;import org.hibernate.SessionFactory;import org.hibernate.Transaction;import org.hibernate.criterion.Restrictions;import org.junit.*;import resource_system.ResourceFactory;import resource_system.VFS;import resource_system.resources.AccountServiceConfiguration;import utils.HibernateUtil;import utils.RandomStringGenerator;/** * Created by Ivan on 04.04.2014 in 21:14. */public class UserDataSetDAOTest {    private static final int PASSWORD_LENGTH = 10;    private static final int USERNAME_LENGTH = 10;    private static VFS vfs;    private static String testUsername;    private static String testPassword;    private static UserDataSet testUserDataSet;    private SessionFactory sessionFactory;    private UserDataSetDAO testUserDataSetDAO;    @BeforeClass    public static void setUpOnce() {        vfs = new VFS("src/main/resources/");        testUsername = RandomStringGenerator.getString(USERNAME_LENGTH);        testPassword = RandomStringGenerator.getString(PASSWORD_LENGTH);        testUserDataSet = new UserDataSet(testUsername, testPassword);    }    @Before    public void setUp() {        sessionFactory = HibernateUtil.getInstance().getSessionFactory(                ((AccountServiceConfiguration) ResourceFactory.getInstance()                        .getResource(vfs.getPath("account_service.cfg.xml")))                        .TEST_DATABASE_CONFIGURATION_FILE_PATH()        );        testUserDataSetDAO = new UserDataSetDAO(sessionFactory);    }    @After    public void tearDown() {        sessionFactory.close();    }    @Test    public void testAddUserSuccess() {        testUserDataSetDAO.addUser(testUserDataSet);        UserDataSet user = getAddedUser(testUserDataSet);        Assert.assertEquals(testUserDataSet.getUsername(), user.getUsername());        Assert.assertEquals(testUserDataSet.getPassword(), user.getPassword());    }    @Test(expected = HibernateException.class)    public void testAddUserFailure() throws HibernateException {        testUserDataSetDAO.addUser(testUserDataSet);        testUserDataSetDAO.addUser(testUserDataSet);    }    @Test    public void testGetUserSuccess() {        addUserToGet(testUserDataSet);        UserDataSet user = testUserDataSetDAO.getUser(testUserDataSet.getUsername(), testUserDataSet.getPassword());        Assert.assertEquals(testUserDataSet.getUsername(), user.getUsername());        Assert.assertEquals(testUserDataSet.getPassword(), user.getPassword());    }    @Test    public void testGetUserFailure() {        UserDataSet user = testUserDataSetDAO.getUser(testUserDataSet.getUsername(), testUserDataSet.getPassword());        Assert.assertNull(user);    }    private UserDataSet getAddedUser(UserDataSet userDataSet) {        Session session = sessionFactory.openSession();        UserDataSet user = (UserDataSet) session.createCriteria(UserDataSet.class)                .add(Restrictions.eq("username", userDataSet.getUsername()))                .add(Restrictions.eq("password", userDataSet.getPassword()))                .uniqueResult();        return user;    }    private void addUserToGet(UserDataSet userDataSet) {        Session session = sessionFactory.openSession();        Transaction transaction = session.beginTransaction();        session.save(userDataSet);        transaction.commit();        session.close();    }}