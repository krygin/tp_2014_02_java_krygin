package services.frontend;

import constants.Constants;
import message_system.Abonent;
import message_system.Address;
import message_system.MessageSystem;
import message_system.messages.AuthorizationRequestMsg;
import message_system.messages.RegistrationRequestMsg;
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

import static constants.Actions.AUTHORIZE_ACTION;
import static constants.Actions.REGISTER_ACTION;
import static constants.Constants.TICK_TIME;
import static constants.Paths.*;
import static services.frontend.UserState.*;

public class Frontend extends HttpServlet implements Runnable, Abonent {

    private final Map<String, UserSession> sessions = new HashMap<>();
    private final Address address;
    private final MessageSystem messageSystem;

    public Frontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
        messageSystem.getAddressService().setFrontend(address);
    }

    private static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
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
            TimeHelper.sleep(TICK_TIME);
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
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        if (sessions.get(sessionId) == null)
            sessions.put(sessionId, new UserSession(sessionId));
        UserSession userSession = sessions.get(sessionId);
        switch (req.getPathInfo()) {
            case AUTHORIZATION_PAGE:
                responseAuthorizationPage(resp, userSession);
                break;
            case REGISTRATION_PAGE:
                responseRegistrationPage(resp, userSession);
                break;
            case USER_PAGE:
                responseUserPage(resp, userSession);
                break;
            case LOADING_PAGE:
                responseLoadingPage(resp, userSession);
                break;
            case INDEX_PAGE:
                responseIndexPage(resp, userSession);
                break;
            case LOGOUT_ACTION:
                handleLogoutRequest(resp, userSession);
            default:
                resp.sendRedirect(INDEX_PAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        if (sessions.get(sessionId) == null)
            sessions.put(sessionId, new UserSession(sessionId));
        UserSession userSession = sessions.get(sessionId);
        switch (req.getPathInfo()) {
            case REGISTER_ACTION:
                handleRegistrationRequest(req, resp, userSession);
                break;
            case AUTHORIZE_ACTION:
                handleAuthorizationRequest(req, resp, userSession);
                break;
            default:
                break;
        }
        resp.sendRedirect(LOADING_PAGE);
    }

    private void responseIndexPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = "Добро пожаловать";
                break;
            case LOGGED_IN:
                message = "Добро пожаловать, " + userSession.getUsername();
                break;
            default:
                message = "Добро пожаловать";
        }
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("index.tml", pageVariables));
    }

    private void responseRegistrationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = "Добро пожаловать";
                break;
            case LOGGED_IN:
                message = "Добро пожаловать, " + userSession.getUsername();
                break;
            case REGISTRATION_ERROR:
                message = "Во время регистрации произошла ошибка";
                break;
            case WRONG_DATA_INPUT:
                message = "Введены некорректные данные или пароли не совпадают";
                break;
            default:
                message = "ОШИБКА!!!";
        }
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
    }

    private void responseAuthorizationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = "Добро пожаловать";
                break;
            case LOGGED_IN:
                message = "Добро пожаловать, " + userSession.getUsername();
                break;
            case AUTHORIZATION_ERROR:
                message = "Во время авторизации произошла ошибка";
                break;
            case JUST_REGISTERED:
                message = "Вы успешно зарегистрировались, пройдите процедуру авторизации";
                break;
            default:
                message = "ОШИБКА!!!";
        }
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    private void responseUserPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        String userId;
        switch (userSession.getUserState()) {
            case IS_ANONYMOUS:
                message = "Привет, анонимный пользователь =)";
                userId = "Для того чтобы получить ID Вам необходимо пройти процедуру авторизации";
                break;
            case LOGGED_IN:
                message = "Добро пожаловать, " + userSession.getUsername();
                userId = userSession.getSessionId();
                break;
            default:
                message = "Привет, анонимный пользователь =)";
                userId = "Для того чтобы получить ID Вам необходимо пройти процедуру авторизации";
        }
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("userId", userId);
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("user.tml", pageVariables));
    }

    private void responseLoadingPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        switch (userSession.getUserState()) {
            case WAITING_FOR_REGISTRATION:
                message = "Подождите, идет регистрация";
                break;
            case WAITING_FOR_AUTHORIZATION:
                message = "Подождите, идет авторизация";
                break;
            case LOGGED_IN:
                resp.sendRedirect(USER_PAGE);
                return;
            case REGISTRATION_ERROR:
                resp.sendRedirect(REGISTRATION_PAGE);
                return;
            case AUTHORIZATION_ERROR:
                resp.sendRedirect(AUTHORIZATION_PAGE);
                return;
            case JUST_REGISTERED:
                resp.sendRedirect(AUTHORIZATION_PAGE);
                return;
            default:
                message = "Не";
                break;
        }
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("loading.tml", pageVariables));
    }

    private void handleLogoutRequest(HttpServletResponse resp, UserSession userSession) throws IOException {
        sessions.remove(userSession.getSessionId());
        resp.sendRedirect(INDEX_PAGE);
    }

    private void handleRegistrationRequest(HttpServletRequest req, HttpServletResponse resp, UserSession userSession) throws IOException {
        String username = req.getParameter("username");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || !password1.equals(password2)) {
            userSession.setUserState(WRONG_DATA_INPUT);
            resp.sendRedirect(REGISTRATION_PAGE);
            return;
        }
        User user = new User(username, password1);
        Address frontendAddress = messageSystem.getAddressService().getFrontend();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService(user.hashCode() % Constants.NUMBER_OF_ACCOUNT_SERVICES);
        String sessionId = userSession.getSessionId();
        messageSystem.sendMessage(new RegistrationRequestMsg(frontendAddress, accountServiceAddress, user, sessionId));
        userSession.setUserState(WAITING_FOR_REGISTRATION);
    }

    private void handleAuthorizationRequest(HttpServletRequest req, HttpServletResponse resp, UserSession userSession) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username.isEmpty() || password.isEmpty()) {
            userSession.setUserState(WRONG_DATA_INPUT);
            resp.sendRedirect(AUTHORIZATION_PAGE);
            return;
        }
        User user = new User(username, password);
        Address frontendAddress = messageSystem.getAddressService().getFrontend();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService(user.hashCode() % Constants.NUMBER_OF_ACCOUNT_SERVICES);
        String sessionId = userSession.getSessionId();
        messageSystem.sendMessage(new AuthorizationRequestMsg(frontendAddress, accountServiceAddress, user, sessionId));
        userSession.setUserState(WAITING_FOR_AUTHORIZATION);
        userSession.setUsername(username);
    }
}