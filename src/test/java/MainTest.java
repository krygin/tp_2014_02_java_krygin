import message_system.MessageSystem;
import models.UserDataSet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import resource_system.ResourceFactory;
import resource_system.VFS;
import resource_system.resources.AccountServiceConfiguration;
import resource_system.resources.ServerConfiguration;
import services.account_service.AccountService;
import services.frontend.Frontend;
import utils.HibernateUtil;
import utils.RandomStringGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 31.05.2014 in 1:04.
 */
public class MainTest {

    private static final int USERNAME_LENGTH = 10;
    private static final int PASSWORD_LENGTH = 10;
    private static HtmlUnitDriver driver;
    private static SessionFactory sessionFactory;
    private static VFS vfs;
    private static String testUsername;
    private static String testPassword;
    private static UserDataSet testUserDataSet;
    private static ServerConfiguration configuration;
    private static MessageSystem messageSystem;
    private static Frontend frontend;
    private static List<AccountService> accountServices;


    @BeforeClass
    public static void setUpOnce() throws Exception {
        vfs = new VFS("src/main/resources/");
        configuration = (ServerConfiguration) ResourceFactory.getInstance().getResource(vfs.getPath("server.cfg.xml"));
        messageSystem = new MessageSystem();
        frontend = new Frontend(messageSystem, vfs);
        accountServices = new ArrayList<AccountService>() {{
            for (int i = 0; i < configuration.NUMBER_OF_ACCOUNT_SERVICES(); i++)
                add(new AccountService(messageSystem, vfs));
        }};
        (new Thread(frontend)).start();
        for (AccountService accountService : accountServices)
            (new Thread(accountService)).start();
        Server server = new Server(configuration.PORT());
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, servletContextHandler});
        server.setHandler(handlers);

        server.start();

        sessionFactory = HibernateUtil.getInstance()
                .getSessionFactory(((AccountServiceConfiguration) ResourceFactory.getInstance()
                        .getResource(vfs.getPath("account_service.cfg.xml")))
                        .DATABASE_CONFIGURATION_FILE_PATH());
        driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);

        testUsername = RandomStringGenerator.getString(USERNAME_LENGTH);
        testPassword = RandomStringGenerator.getString(PASSWORD_LENGTH);
        testUserDataSet = new UserDataSet(testUsername, testPassword);
    }


    @Test
    public void testAuthorization() {
        addUser(testUserDataSet);

        driver.get("http://localhost:" +
                ((ServerConfiguration) ResourceFactory.getInstance().getResource(vfs.getPath("frontend.cfg.xml"))).PORT() +
                "/authform");

        WebElement element;
        element = driver.findElement(By.name("username"));
        element.sendKeys(testUsername);
        element = driver.findElement(By.name("password"));
        element.sendKeys(testPassword);
        element.submit();

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return webDriver.getPageSource().contains("<p>user page</p>");
            }
        });
    }


    private void addUser(UserDataSet userDataSet) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(userDataSet);
        transaction.commit();
        session.close();
    }
}
