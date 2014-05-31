package utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import resource_system.ResourceFactory;
import resource_system.VFS;
import resource_system.resources.FrontendConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by Ivan on 29.05.2014 in 23:22.
 */
public class PageGeneratorTest {
    private static Map<String, Object> pageVariables;
    private static FrontendConfiguration frontendConfiguration;
    private static VFS vfs;

    @BeforeClass
    public static void setUpOnce() {
        vfs = new VFS("src/main/resources/");
        frontendConfiguration = (FrontendConfiguration) ResourceFactory.getInstance().getResource(vfs.getPath("frontend.cfg.xml"));
        pageVariables = new HashMap<>();

    }

    @Before
    public void setUp() {
        pageVariables.clear();
    }

    @Test
    public void testIndexPageGeneration() {
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), "");
        String string = PageGenerator.getPage(frontendConfiguration.INDEX_TEMPLATE(), pageVariables);
        assertTrue(string.contains("<p>index page</p>"));
    }

    @Test
    public void testUserPageGeneration() {
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), "");
        pageVariables.put(frontendConfiguration.HTML_USER_ID_PLACEHOLDER_NAME(), "");
        pageVariables.put(frontendConfiguration.HTML_REFRESH_PERIOD_PLACEHOLDER_NAME(), "");
        pageVariables.put(frontendConfiguration.HTML_SERVER_TIME_PLACEHOLDER_NAME(), "");
        String string = PageGenerator.getPage(frontendConfiguration.USER_TEMPLATE(), pageVariables);
        assertTrue(string.contains("<p>user page</p>"));
    }

    @Test
    public void testRegistrationPageGeneration() {
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), "");
        String string = PageGenerator.getPage(frontendConfiguration.REGFORM_TEMPLATE(), pageVariables);
        assertTrue(string.contains("<p>registration page</p>"));
    }

    @Test
    public void testAuthorizationPageGeneration() {
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), "");
        String string = PageGenerator.getPage(frontendConfiguration.AUTHFORM_TEMPLATE(), pageVariables);
        assertTrue(string.contains("<p>authorization page</p>"));
    }

    @Test
    public void testLoadingPageGeneration() {
        pageVariables.put(frontendConfiguration.HTML_RESPONSE_MESSAGE_PLACEHOLDER_NAME(), "");
        pageVariables.put(frontendConfiguration.HTML_REFRESH_PERIOD_PLACEHOLDER_NAME(), "");
        String string = PageGenerator.getPage(frontendConfiguration.LOADING_TEMPLATE(), pageVariables);
        assertTrue(string.contains("<p>loading page</p>"));
    }
}
