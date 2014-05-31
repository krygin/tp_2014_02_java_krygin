package resource_system.resources;

import resource_system.Resource;

/**
 * Created by Ivan on 21.05.2014 in 14:35.
 */
public class ServerConfiguration implements Resource {
    int PORT;
    int NUMBER_OF_ACCOUNT_SERVICES;

    public int PORT() {
        return PORT;
    }

    public int NUMBER_OF_ACCOUNT_SERVICES() {
        return NUMBER_OF_ACCOUNT_SERVICES;
    }
}