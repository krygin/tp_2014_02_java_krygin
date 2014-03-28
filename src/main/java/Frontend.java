import models.UserDataSet;
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
import java.util.logging.Logger;

public class Frontend extends HttpServlet implements Runnable, Abonent {

    private Map<String, UserSession> sessions = new HashMap<>();
    private Address address;
    private MessageSystem messageSystem;

    private static final String AUTHORIZATION_PAGE = "/authform";
    private static final String REGISTRATION_PAGE = "/regform";
    private static final String USER_PAGE = "/user";
    private static final String INDEX_PAGE = "/index";

    private static final String AUTHORIZE_ACTION = "/authorize";
    private static final String REGISTER_ACTION = "/register";

    public Frontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        messageSystem.addService(this);
    }

    public static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = req.getSession();
        UserSession userSession = sessions.get(session.getId());
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
                messageSystem.sendMessage(new CreateNewUserMsg(getAddress(), ));
                break;
            case AUTHORIZE_ACTION:
                break;
            default:
                break;
        }
    }

    private void responseRegistrationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        if (userSession==null) {
            message="Добро пожаловать!!!";
        }
        else {
            switch (userSession.getUserState()){
                case LOGGED_IN:
                    message="Добро пожаловать, " + userSession.getUsername();
                    break;
                case WAITING_FOR_AUTHORIZATION:
                    message="Подождите, идет авторизация";
                    break;
                case WAITING_FOR_REGISTRATION:
                    message="Подождите, идет регистрация";
                    break;
                case AUTHORIZATION_ERROR:
                    message="Ошибка авторизации";
                    break;
                default:
                    message="ОШИБКА!!!";
            }
        }
        pageVariables.put("message", message);
        resp.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
    }
    private void responseAuthorizationPage(HttpServletResponse resp, UserSession userSession) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String message;
        if (userSession==null) {
            message="Добро пожаловать!!!";
        }
        else {
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
        if (userSession==null) {
            message="Добро пожаловать!!! Вы не авторизованы!!! ";
        }
        else {
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
}
