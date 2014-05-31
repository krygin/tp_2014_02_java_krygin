package services.frontend;

import message_system.Abonent;
import message_system.Address;
import message_system.AddressService;
import message_system.MessageSystem;
import org.junit.BeforeClass;
import org.junit.Test;
import resource_system.ResourceFactory;
import resource_system.VFS;
import resource_system.resources.FrontendConfiguration;
import utils.RandomStringGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Ivan on 23.05.2014 in 16:09.
 */
public class FrontendTest {
    private static final int SESSION_ID_LENGTH = 10;
    private static MessageSystem testMessageSystem = mock(MessageSystem.class);
    private static HttpServletRequest testRequest = mock(HttpServletRequest.class);
    private static HttpServletResponse testResponse = mock(HttpServletResponse.class);
    private static Abonent testAbonent = mock(Abonent.class);
    private static Address testAddress = mock(Address.class);
    private static AddressService testAddressService = mock(AddressService.class);
    private static HttpSession testSession = mock(HttpSession.class);
    private static String testSessionId;
    private static VFS vfs;
    private static FrontendConfiguration frontendConfiguration;

    @BeforeClass
    public static void setUpOnce() {
        vfs = new VFS("src/main/resources/");
        frontendConfiguration = (FrontendConfiguration) ResourceFactory.getInstance().
                getResource(vfs.getPath("frontend.cfg.xml"));
        testSessionId = RandomStringGenerator.getString(SESSION_ID_LENGTH);
    }


    @Test
    public void testRouting() throws ServletException, IOException {

        doNothing().when(testMessageSystem).addService(testAbonent);
        doNothing().when(testAddressService).setFrontend(testAddress);
        doReturn(testAddressService).when(testMessageSystem).getAddressService();

        doNothing().when(testResponse).sendRedirect(frontendConfiguration.INDEX_PAGE());
        doReturn(testSessionId).when(testSession).getId();
        doReturn(testSession).when(testRequest).getSession();

        boolean hasRoute = routes("/authform");
        assertTrue(hasRoute);
    }

    private boolean routes(String path) throws ServletException, IOException {
        doReturn(path).when(testRequest).getPathInfo();
        RoutingTestFrontend frontend = new RoutingTestFrontend(testMessageSystem, vfs);
        frontend.doGet(testRequest, testResponse);
        return frontend.responseIndexPageWasExecuted || frontend.responseRegistrationPageWasExecuted ||
                frontend.responseAuthorizationPageWasExecuted || frontend.responseLoadingPageWasExecuted ||
                frontend.responseUserPageWasExecuted || frontend.handleLogoutRequestWasExecuted;
    }

    private class RoutingTestFrontend extends Frontend {
        private boolean responseIndexPageWasExecuted = false;
        private boolean responseRegistrationPageWasExecuted = false;
        private boolean responseAuthorizationPageWasExecuted = false;
        private boolean responseUserPageWasExecuted = false;
        private boolean responseLoadingPageWasExecuted = false;
        private boolean handleLogoutRequestWasExecuted = false;

        public RoutingTestFrontend(MessageSystem messageSystem, VFS vfs) {
            super(messageSystem, vfs);
        }

        @Override
        protected void responseIndexPage(HttpServletResponse resp, UserSession userSession) throws IOException {
            responseIndexPageWasExecuted = true;
        }

        @Override
        protected void responseRegistrationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
            responseRegistrationPageWasExecuted = true;
        }

        @Override
        protected void responseAuthorizationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
            responseAuthorizationPageWasExecuted = true;
        }

        @Override
        protected void responseUserPage(HttpServletResponse resp, UserSession userSession) throws IOException {
            responseUserPageWasExecuted = true;
        }

        @Override
        protected void responseLoadingPage(HttpServletResponse resp, UserSession userSession) throws IOException {
            responseLoadingPageWasExecuted = true;
        }

        @Override
        protected void handleLogoutRequest(HttpServletResponse resp, UserSession userSession) throws IOException {
            handleLogoutRequestWasExecuted = true;
        }
    }
}