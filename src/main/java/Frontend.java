import models.UserDataSet;
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

public class Frontend extends HttpServlet implements Runnable, Abonent {

    private static final String AUTHORIZATION_PAGE = "/authform";
    private static final String REGISTRATION_PAGE = "/regform";
    private static final String USER_PAGE = "/user";
    private static final String AUTHORIZE_ACTION = "/authorize";
    private static final String REGISTER_ACTION = "/register";
    private static final String LOADING_PAGE = "/loading";
    private Map<String, UserSession> sessions = new HashMap<>();
    private Address address;
    private MessageSystem messageSystem;

    public Frontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
        messageSystem.getAddressService().setFrontend(address);
    }

    public static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    public void handleRegistrationResponse(String sessionId, RegistrationResult result) {
        UserSession userSession = sessions.get(sessionId);
        switch (result.getResult()) {
            case SUCCESS:
                userSession.setUserState(UserState.JUST_REGISTERED);
                break;
            case FAIL:
                userSession.setUserState(UserState.REGISTRATION_ERROR);
                break;
        }
    }

    public void handleAuthorizationResponse(String sessionId, AuthorizationResult result) {
        UserSession userSession = sessions.get(sessionId);
        switch (result.getResult()) {
            case SUCCESS:
                userSession.setUserState(UserState.LOGGED_IN);
                break;
            case FAIL:
                userSession.setUserState(UserState.AUTHORIZATION_ERROR);
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
            sessions.put(sessionId, new UserSession("", sessionId));
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
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        if (sessions.get(sessionId) == null)
            sessions.put(sessionId, new UserSession("", sessionId));
        UserSession userSession = sessions.get(sessionId);
        switch (req.getPathInfo()) {
            case REGISTER_ACTION:
                handleRegistrationRequest(req, resp, userSession, messageSystem);
                break;
            case AUTHORIZE_ACTION:
                handleAuthorizationRequest(req, resp, userSession, messageSystem);
                break;
            default:
                break;
        }
        resp.sendRedirect(LOADING_PAGE);
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
                userId = userSession.getSessionId().toString();
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

    private void handleRegistrationRequest(HttpServletRequest req, HttpServletResponse resp, UserSession userSession, MessageSystem messageSystem) throws IOException {
        String username = req.getParameter("username");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        if (username.equals("") || password1.equals("") || password2.equals("") || !password1.equals(password2)) {
            userSession.setUserState(UserState.WRONG_DATA_INPUT);
            resp.sendRedirect(REGISTRATION_PAGE);
            return;
        }
        UserDataSet user = new UserDataSet(username, password1);
        Address frontendAddress = messageSystem.getAddressService().getFrontend();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService();
        String sessionId = userSession.getSessionId();
        messageSystem.sendMessage(new RegistrationRequestMsg(frontendAddress, accountServiceAddress, user, sessionId));
        userSession.setUserState(UserState.WAITING_FOR_REGISTRATION);
    }

    private void handleAuthorizationRequest(HttpServletRequest req, HttpServletResponse resp, UserSession userSession, MessageSystem messageSystem) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username.equals("") || password.equals("")) {
            userSession.setUserState(UserState.WRONG_DATA_INPUT);
            resp.sendRedirect(AUTHORIZATION_PAGE);
            return;
        }
        UserDataSet user = new UserDataSet(username, password);
        Address frontendAddress = messageSystem.getAddressService().getFrontend();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService();
        String sessionId = userSession.getSessionId();
        messageSystem.sendMessage(new AuthorizationRequestMsg(frontendAddress, accountServiceAddress, user, sessionId));
        userSession.setUserState(UserState.WAITING_FOR_AUTHORIZATION);
        userSession.setUsername(username);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);
            TimeHelper.sleep(GlobalConstants.TICK_TIME);
        }
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
