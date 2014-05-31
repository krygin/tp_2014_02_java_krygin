package resource_system.resources;

import resource_system.Resource;

/**
 * Created by Ivan on 31.05.2014 in 9:58.
 */
public class TestResource implements Resource {
    int INTEGER_VALUE;
    String STRING_VALUE;

    public int INTEGER_VALUE() {
        return INTEGER_VALUE;
    }

    public String STRING_VALUE() {
        return STRING_VALUE;
    }
}
