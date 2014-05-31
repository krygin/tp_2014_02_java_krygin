package resource_system.resources;

import resource_system.Resource;

/**
 * Created by Ivan on 21.05.2014 in 17:19.
 */
public class FrontendConfiguration implements Resource {
    int TICK_TIME;
    String AUTHORIZATION_PAGE;
    String REGISTRATION_PAGE;
    String USER_PAGE;
    String LOADING_PAGE;
    String INDEX_PAGE;
    String LOGOUT_ACTION;
    String AUTHORIZE_ACTION;
    String REGISTER_ACTION;

    String AUTHFORM_TEMPLATE;
    String REGFORM_TEMPLATE;
    String USER_TEMPLATE;
    String LOADING_TEMPLATE;
    String INDEX_TEMPLATE;
    String HTML_CONTENT_TYPE;
    String HTML_REFRESH_PERIOD;
    String HTML_REFRESH_PERIOD_PLACEHOLDER_NAME;
    String HTML_SERVER_TIME_PLACEHOLDER_NAME;
    String HTML_DATE_FORMAT;
    String HTML_USER_ID_PLACEHOLDER_NAME;
    String HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME;

    String RESPONSE_MESSAGE_WELCOME;
    String RESPONSE_MESSAGE_REGISTRATION_ERROR;
    String RESPONSE_MESSAGE_WRONG_REG_DATA_INPUT;
    String RESPONSE_MESSAGE_WRONG_AUTH_DATA_INPUT;
    String RESPONSE_MESSAGE_AUTHORIZATION_ERROR;
    String RESPONSE_MESSAGE_JUST_REGISTERED;
    String RESPONSE_MESSAGE_NO_ID_USER;
    String RESPONSE_MESSAGE_WAITING_FOR_AUTHORIZATION;
    String RESPONSE_MESSAGE_WAITING_FOR_REGISTRATION;
    String RESPONSE_MESSAGE_UNKNOWN_ERROR;
    String REGFORM_USERNAME_INPUT_NAME;
    String REGFORM_PASSWORD1_INPUT_NAME;
    String REGFORM_PASSWORD2_INPUT_NAME;
    String AUTHFORM_USERNAME_INPUT_NAME;
    String AUTHFORM_PASSWORD_INPUT_NAME;

    public String RESPONSE_MESSAGE_UNKNOWN_ERROR() {
        return RESPONSE_MESSAGE_UNKNOWN_ERROR;
    }

    public String RESPONSE_MESSAGE_WELCOME() {
        return RESPONSE_MESSAGE_WELCOME;
    }

    public String RESPONSE_MESSAGE_REGISTRATION_ERROR() {
        return RESPONSE_MESSAGE_REGISTRATION_ERROR;
    }

    public String RESPONSE_MESSAGE_WRONG_REG_DATA_INPUT() {
        return RESPONSE_MESSAGE_WRONG_REG_DATA_INPUT;
    }

    public String RESPONSE_MESSAGE_WRONG_AUTH_DATA_INPUT() {
        return RESPONSE_MESSAGE_WRONG_AUTH_DATA_INPUT;
    }

    public String RESPONSE_MESSAGE_AUTHORIZATION_ERROR() {
        return RESPONSE_MESSAGE_AUTHORIZATION_ERROR;
    }

    public String RESPONSE_MESSAGE_JUST_REGISTERED() {
        return RESPONSE_MESSAGE_JUST_REGISTERED;
    }

    public String RESPONSE_MESSAGE_NO_ID_USER() {
        return RESPONSE_MESSAGE_NO_ID_USER;
    }

    public String RESPONSE_MESSAGE_WAITING_FOR_AUTHORIZATION() {
        return RESPONSE_MESSAGE_WAITING_FOR_AUTHORIZATION;
    }

    public String RESPONSE_MESSAGE_WAITING_FOR_REGISTRATION() {
        return RESPONSE_MESSAGE_WAITING_FOR_REGISTRATION;
    }

    public String INDEX_TEMPLATE() {
        return INDEX_TEMPLATE;
    }

    public int TICK_TIME() {
        return TICK_TIME;
    }

    public String REGISTER_ACTION() {
        return REGISTER_ACTION;
    }

    public String AUTHORIZE_ACTION() {
        return AUTHORIZE_ACTION;
    }

    public String LOGOUT_ACTION() {
        return LOGOUT_ACTION;
    }

    public String LOADING_PAGE() {
        return LOADING_PAGE;
    }

    public String INDEX_PAGE() {
        return INDEX_PAGE;
    }

    public String USER_PAGE() {
        return USER_PAGE;
    }

    public String REGISTRATION_PAGE() {
        return REGISTRATION_PAGE;
    }

    public String AUTHORIZATION_PAGE() {
        return AUTHORIZATION_PAGE;
    }

    public String AUTHFORM_TEMPLATE() {
        return AUTHFORM_TEMPLATE;
    }

    public String REGFORM_TEMPLATE() {
        return REGFORM_TEMPLATE;
    }

    public String USER_TEMPLATE() {
        return USER_TEMPLATE;
    }

    public String LOADING_TEMPLATE() {
        return LOADING_TEMPLATE;
    }

    public String HTML_CONTENT_TYPE() {
        return HTML_CONTENT_TYPE;
    }

    public String HTML_REFRESH_PERIOD() {
        return HTML_REFRESH_PERIOD;
    }

    public String HTML_REFRESH_PERIOD_PLACEHOLDER_NAME() {
        return HTML_REFRESH_PERIOD_PLACEHOLDER_NAME;
    }

    public String HTML_SERVER_TIME_PLACEHOLDER_NAME() {
        return HTML_SERVER_TIME_PLACEHOLDER_NAME;
    }

    public String HTML_DATE_FORMAT() {
        return HTML_DATE_FORMAT;
    }

    public String HTML_USER_ID_PLACEHOLDER_NAME() {
        return HTML_USER_ID_PLACEHOLDER_NAME;
    }

    public String HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME() {
        return HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME;
    }

    public String REGFORM_USERNAME_INPUT_NAME() {
        return REGFORM_USERNAME_INPUT_NAME;
    }

    public String REGFORM_PASSWORD1_INPUT_NAME() {
        return REGFORM_PASSWORD1_INPUT_NAME;
    }

    public String REGFORM_PASSWORD2_INPUT_NAME() {
        return REGFORM_PASSWORD2_INPUT_NAME;
    }

    public String AUTHFORM_USERNAME_INPUT_NAME() {
        return AUTHFORM_USERNAME_INPUT_NAME;
    }

    public String AUTHFORM_PASSWORD_INPUT_NAME() {
        return AUTHFORM_PASSWORD_INPUT_NAME;
    }
}
