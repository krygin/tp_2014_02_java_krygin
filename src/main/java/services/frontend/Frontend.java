package services.frontend;

import message_system.Abonent;
import message_system.Address;
import message_system.MessageSystem;
import message_system.messages.AuthorizationRequestMsg;
import message_system.messages.RegistrationRequestMsg;
import resource_system.ResourceFactory;
import resource_system.VFS;
import resource_system.resources.FrontendConfiguration;
import resource_system.resources.ServerConfiguration;
import results.authorization.AuthorizationResult;
import results.registration.RegistrationResult;
import utils.PageGenerator;
import utils.TimeHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static services.frontend.UserState.*;

public class Frontend extends HttpServlet implements Runnable, Abonent {

    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final Address address;
    private final MessageSystem messageSystem;

    private FrontendConfiguration frontendConfiguration;
    private ServerConfiguration serverConfiguration;

    public Frontend(MessageSystem messageSystem, VFS vfs) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
        messageSystem.getAddressService().setFrontend(address);

        frontendConfiguration = (FrontendConfiguration) ResourceFactory.getInstance().
                getResource(vfs.getPath("frontend.cfg.xml"));
        serverConfiguration = (ServerConfiguration) ResourceFactory.getInstance()
                .getResource(vfs.getPath("server.cfg.xml"));
    }

    private String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat(frontendConfiguration.HTML_DATE_FORMAT());
        return formatter.format(date);
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
            TimeHelper.sleep(frontendConfiguration.TICK_TIME());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public void handleRegistrationResponse(String sessionId, RegistrationResult result) {
        UserSession userSession = sessions.get(sessionId);
        switch (result.getResult()) {
            case SUCCESS:
                userSession.setUserState(JUST_REGISTERED);
                break;
            case FAILURE:
                userSession.setUserState(REGISTRATION_ERROR);
                break;
        }
    }

    public void handleAuthorizationResponse(String sessionId, AuthorizationResult result) {
        UserSession userSession = sessions.get(sessionId);
        switch (result.getResult()) {
            case SUCCESS:
                userSession.setUserState(LOGGED_IN);
                userSession.setIdUser(result.getIdUser());
                break;
            case FAILURE:
                userSession.setUserState(AUTHORIZATION_ERROR);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(frontendConfiguration.HTML_CONTENT_TYPE());
        resp.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        if (sessions.get(sessionId) == null)
            sessions.put(sessionId, new UserSession(sessionId));
        UserSession userSession = sessions.get(sessionId);
        String path = req.getPathInfo();

        if (path.equals(frontendConfiguration.AUTHORIZATION_PAGE()))
            responseAuthorizationPage(resp, userSession);
        else if (path.equals(frontendConfiguration.REGISTRATION_PAGE()))
            responseRegistrationPage(resp, userSession);
        else if (path.equals(frontendConfiguration.USER_PAGE()))
            responseUserPage(resp, userSession);
        else if (path.equals(frontendConfiguration.LOADING_PAGE()))
            responseLoadingPage(resp, userSession);
        else if (path.equals(frontendConfiguration.INDEX_PAGE()))
            responseIndexPage(resp, userSession);
        else if (path.equals(frontendConfiguration.LOGOUT_ACTION()))
            handleLogoutRequest(resp, userSession);
        else
            resp.sendRedirect(frontendConfiguration.INDEX_PAGE());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(frontendConfiguration.HTML_CONTENT_TYPE());
        resp.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        if (sessions.get(sessionId) == null)
            sessions.put(sessionId, new UserSession(sessionId));
        UserSession userSession = sessions.get(sessionId);
        String path = req.getPathInfo();

        if (path.equals(frontendConfiguration.REGISTER_ACTION()))
            handleRegistrationRequest(req, resp, userSession);
        else if (path.equals(frontendConfiguration.AUTHORIZE_ACTION()))
            handleAuthorizationRequest(req, resp, userSession);

        resp.sendRedirect(frontendConfiguration.LOADING_PAGE());
    }

    protected void responseIndexPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME();
                break;
            case LOGGED_IN:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME() + ", " + userSession.getUsername();
                break;
            default:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME();
        }
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), message);
        resp.getWriter().println(PageGenerator.getPage(frontendConfiguration.INDEX_TEMPLATE(), pageVariables));
    }

    protected void responseRegistrationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME();
                break;
            case LOGGED_IN:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME() + ", " + userSession.getUsername();
                break;
            case REGISTRATION_ERROR:
                message = frontendConfiguration.RESPONSE_MESSAGE_REGISTRATION_ERROR();
                break;
            case WRONG_REGDATA_INPUT:
                message = frontendConfiguration.RESPONSE_MESSAGE_WRONG_REG_DATA_INPUT();
                break;
            default:
                message = frontendConfiguration.RESPONSE_MESSAGE_UNKNOWN_ERROR();
        }
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), message);
        resp.getWriter().println(PageGenerator.getPage(frontendConfiguration.REGFORM_TEMPLATE(), pageVariables));
    }

    protected void responseAuthorizationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME();
                break;
            case LOGGED_IN:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME() + ", " + userSession.getUsername();
                break;
            case AUTHORIZATION_ERROR:
                message = frontendConfiguration.RESPONSE_MESSAGE_AUTHORIZATION_ERROR();
                break;
            case JUST_REGISTERED:
                message = frontendConfiguration.RESPONSE_MESSAGE_JUST_REGISTERED();
                break;
            case WRONG_AUTHDATA_INPUT:
                message = frontendConfiguration.RESPONSE_MESSAGE_WRONG_AUTH_DATA_INPUT();
                break;
            default:
                message = frontendConfiguration.RESPONSE_MESSAGE_UNKNOWN_ERROR();
        }
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), message);
        resp.getWriter().println(PageGenerator.getPage(frontendConfiguration.AUTHFORM_TEMPLATE(), pageVariables));
    }

    protected void responseUserPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        String userId;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME();
                userId = frontendConfiguration.RESPONSE_MESSAGE_NO_ID_USER();
                break;
            case LOGGED_IN:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME() + ", " + userSession.getUsername();
                userId = userSession.getSessionId();
                break;
            default:
                message = frontendConfiguration.RESPONSE_MESSAGE_WELCOME();
                userId = frontendConfiguration.RESPONSE_MESSAGE_NO_ID_USER();
        }
        pageVariables.put(frontendConfiguration.HTML_REFRESH_PERIOD_PLACEHOLDER_NAME(), frontendConfiguration.HTML_REFRESH_PERIOD());
        pageVariables.put(frontendConfiguration.HTML_SERVER_TIME_PLACEHOLDER_NAME(), getTime());
        pageVariables.put(frontendConfiguration.HTML_USER_ID_PLACEHOLDER_NAME(), userId);
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), message);
        resp.getWriter().println(PageGenerator.getPage(frontendConfiguration.USER_TEMPLATE(), pageVariables));
    }

    protected void responseLoadingPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case WAITING_FOR_REGISTRATION:
                message = frontendConfiguration.RESPONSE_MESSAGE_WAITING_FOR_REGISTRATION();
                break;
            case WAITING_FOR_AUTHORIZATION:
                message = frontendConfiguration.RESPONSE_MESSAGE_WAITING_FOR_AUTHORIZATION();
                break;
            case LOGGED_IN:
                resp.sendRedirect(frontendConfiguration.USER_PAGE());
                return;
            case REGISTRATION_ERROR:
                resp.sendRedirect(frontendConfiguration.REGISTRATION_PAGE());
                return;
            case AUTHORIZATION_ERROR:
                resp.sendRedirect(frontendConfiguration.AUTHORIZATION_PAGE());
                return;
            case JUST_REGISTERED:
                resp.sendRedirect(frontendConfiguration.AUTHORIZATION_PAGE());
                return;
            default:
                message = frontendConfiguration.RESPONSE_MESSAGE_UNKNOWN_ERROR();
                break;
        }
        pageVariables.put(frontendConfiguration.HTML_REFRESH_PERIOD_PLACEHOLDER_NAME(), frontendConfiguration.HTML_REFRESH_PERIOD());
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), message);
        resp.getWriter().println(PageGenerator.getPage(frontendConfiguration.LOADING_TEMPLATE(), pageVariables));
    }

    protected void handleLogoutRequest(HttpServletResponse resp, UserSession userSession) throws IOException {
        sessions.remove(userSession.getSessionId());
        resp.sendRedirect(frontendConfiguration.INDEX_PAGE());
    }

    private void handleRegistrationRequest(HttpServletRequest req, HttpServletResponse resp, UserSession userSession) throws IOException {
        String username = req.getParameter(frontendConfiguration.REGFORM_USERNAME_INPUT_NAME());
        String password1 = req.getParameter(frontendConfiguration.REGFORM_PASSWORD1_INPUT_NAME());
        String password2 = req.getParameter(frontendConfiguration.REGFORM_PASSWORD2_INPUT_NAME());
        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || !password1.equals(password2)) {
            userSession.setUserState(WRONG_REGDATA_INPUT);
            resp.sendRedirect(frontendConfiguration.REGISTRATION_PAGE());
            return;
        }
        User user = new User(username, password1);
        Address frontendAddress = messageSystem.getAddressService().getFrontend();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService(user.hashCode() % serverConfiguration.NUMBER_OF_ACCOUNT_SERVICES());
        String sessionId = userSession.getSessionId();
        messageSystem.sendMessage(new RegistrationRequestMsg(frontendAddress, accountServiceAddress, user, sessionId));
        userSession.setUserState(WAITING_FOR_REGISTRATION);
    }

    private void handleAuthorizationRequest(HttpServletRequest req, HttpServletResponse resp, UserSession userSession) throws IOException {
        String username = req.getParameter(frontendConfiguration.AUTHFORM_USERNAME_INPUT_NAME());
        String password = req.getParameter(frontendConfiguration.AUTHFORM_PASSWORD_INPUT_NAME());
        if (username.isEmpty() || password.isEmpty()) {
            userSession.setUserState(WRONG_AUTHDATA_INPUT);
            resp.sendRedirect(frontendConfiguration.AUTHORIZATION_PAGE());
            return;
        }
        User user = new User(username, password);
        Address frontendAddress = messageSystem.getAddressService().getFrontend();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService(user.hashCode() % serverConfiguration.NUMBER_OF_ACCOUNT_SERVICES());
        String sessionId = userSession.getSessionId();
        messageSystem.sendMessage(new AuthorizationRequestMsg(frontendAddress, accountServiceAddress, user, sessionId));
        userSession.setUserState(WAITING_FOR_AUTHORIZATION);
        userSession.setUsername(username);
    }
}