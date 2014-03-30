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
    private static final String INDEX_PAGE = "/index";
    private static final String AUTHORIZE_ACTION = "/authorize";
    private static final String REGISTER_ACTION = "/register";
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

    public void handleRegistrationResult(String sessionId, RegistrationResult result) {
        switch (result.getResult()) {
            case SUCCESS:
                System.out.print("Всё ништяк");
                break;
            case FAIL:
                System.out.print("Всё плохо");
                break;
        }
    }

    public void handleAuthorizationResult(String sessionId, AuthorizationResult result) {
        switch (result.getResult()) {
            case SUCCESS:
                System.out.print("Всё ништяк");
                break;
            case FAIL:
                System.out.print("Всё плохо");
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
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getPathInfo()) {
            case REGISTER_ACTION:
                String username = req.getParameter("username");
                String password1 = req.getParameter("password1");
                String password2 = req.getParameter("password2");
                if (username != null && password1 != null && password2 != null && password1.equals(password2)) {
                    HttpSession session = req.getSession();
                    String sessionId = session.getId();
                    if (sessions.get(sessionId) == null)
                        sessions.put(sessionId, new UserSession(username, sessionId));
                    sessions.get(sessionId).setUserState(UserState.WAITING_FOR_REGISTRATION);
                    Address frontendAddress = getAddress();
                    Address accountServiceAddress = messageSystem.getAddressService().getAccountService();
                    messageSystem.sendMessage(new RegistrationRequestMsg(frontendAddress, accountServiceAddress, username, password1, sessionId));
                    resp.sendRedirect(REGISTRATION_PAGE);
                }
                break;
            case AUTHORIZE_ACTION:
                String username1 = req.getParameter("username");
                String password = req.getParameter("password");
                if (username1 != null && password != null) {
                    HttpSession session = req.getSession();
                    String sessionId = session.getId();
                    if (sessions.get(sessionId) == null)
                        sessions.put(sessionId, new UserSession(username1, sessionId));
                    sessions.get(sessionId).setUserState(UserState.WAITING_FOR_AUTHORIZATION);
                    Address frontendAddress = getAddress();
                    Address accountServiceAddress = messageSystem.getAddressService().getAccountService();
                    messageSystem.sendMessage(new AuthorizationRequestMsg(frontendAddress, accountServiceAddress, username1, password, sessionId));
                    resp.sendRedirect(AUTHORIZATION_PAGE);
                }
                break;
            default:
                break;
        }
    }

    private void responseRegistrationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        if (userSession == null) {
            message = new String("Добро пожаловать!!!");
        } else {
            switch (userSession.getUserState()) {
                case LOGGED_IN:
                    message = new String("Добро пожаловать, " + userSession.getUsername());
                    break;
                case WAITING_FOR_AUTHORIZATION:
                    message = new String("Подождите, идет авторизация");
                    break;
                case WAITING_FOR_REGISTRATION:
                    message = new String("Подождите, идет регистрация");
                    break;
                case AUTHORIZATION_ERROR:
                    message = new String("Ошибка авторизации");
                    break;
                default:
                    message = new String("ОШИБКА!!!");
            }
        }
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
    }

    private void responseAuthorizationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        if (userSession == null) {
            message = "Добро пожаловать!!!";
        } else {
            switch (userSession.getUserState()) {
                case LOGGED_IN:
                    message = "Добро пожаловать, " + userSession.getUsername();
                    break;
                case WAITING_FOR_AUTHORIZATION:
                    message = "Подождите, идет авторизация";
                    break;
                case WAITING_FOR_REGISTRATION:
                    message = "Подождите, идет регистрация";
                    break;
                case AUTHORIZATION_ERROR:
                    message = "Ошибка авторизации";
                    break;
                default:
                    message = "ОШИБКА!!!";
            }
        }
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    private void responseUserPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        if (userSession == null) {
            message = new String("Добро пожаловать!!! Вы не авторизованы!!! ");
        } else {
            switch (userSession.getUserState()) {
                case IS_ANONYMOUS:
                    message = new String("КУКУ");
                    break;
                case LOGGED_IN:
                    message = "Добро пожаловать, " + userSession.getUsername();
                    break;
                case WAITING_FOR_AUTHORIZATION:
                    message = "Подождите, идет авторизация";
                    break;
                case WAITING_FOR_REGISTRATION:
                    message = "Подождите, идет регистрация";
                    break;
                case AUTHORIZATION_ERROR:
                    message = "Ошибка авторизации";
                    break;
                default:
                    message = "ОШИБКА!!!";
            }
        }
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("userId", "Ни");
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("user.tml", pageVariables));
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
