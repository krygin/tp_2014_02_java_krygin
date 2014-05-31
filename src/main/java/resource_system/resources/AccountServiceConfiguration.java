package resource_system.resources;

import resource_system.Resource;

/**
 * Created by Ivan on 21.05.2014 in 18:23.
 */
public class AccountServiceConfiguration implements Resource {
    int TICK_TIME;
    String DATABASE_CONFIGURATION_FILE_PATH;
    String TEST_DATABASE_CONFIGURATION_FILE_PATH;

    public String TEST_DATABASE_CONFIGURATION_FILE_PATH() {
        return TEST_DATABASE_CONFIGURATION_FILE_PATH;
    }

    public int TICK_TIME() {
        return TICK_TIME;
    }

    public String DATABASE_CONFIGURATION_FILE_PATH() {
        return DATABASE_CONFIGURATION_FILE_PATH;
    }
}
