package resource_system;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import resource_system.resources.TestResource;

/**
 * Created by Ivan on 31.05.2014 in 9:13.
 */
public class ResourceFactoryTest {
    private static VFS vfs;


    @BeforeClass
    public static void setUpOnce() {
        vfs = new VFS("src/main/resources/");
    }

    @Test
    public void testGetResource() throws Exception {
        TestResource resource = (TestResource) ResourceFactory.getInstance().getResource(vfs.getPath("test_resource.xml"));
        Assert.assertNotNull(resource.INTEGER_VALUE());
        Assert.assertNotNull(resource.STRING_VALUE());
    }


    @Test
    public void testResourceFileNotFound() {
        Assert.assertNull(ResourceFactory.getInstance().getResource(vfs.getPath("path.xml")));
    }


}
