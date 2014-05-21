package resource_system;

import utils.sax_parser.SAX;

/**
 * Created by Ivan on 18.05.2014 in 22:11.
 */
public class ResourceFactory {
    private static ResourceFactory instance;

    private ResourceFactory() {
    }

    public static ResourceFactory getInstance() {
        if (instance == null) {
            instance = new ResourceFactory();
        }
        return instance;
    }

    public Resource getResource(String path) {
        return (Resource) SAX.readXML(path);
    }
}
