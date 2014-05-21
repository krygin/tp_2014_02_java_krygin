package resource_system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by Ivan on 18.05.2014 in 17:37.
 */
public class VFSTest {
    private VFS TEST_VFS;
    private String ROOT;

    @Before
    public void setUp() throws Exception {
        ROOT = "";
        TEST_VFS = new VFS(ROOT);
    }

    @Test
    public void testIsExist() throws Exception {
        File file = new File("test.txt");
        file.createNewFile();
        file.deleteOnExit();
        Boolean result = TEST_VFS.isExist("test.txt");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsDirectory() throws Exception {

    }

    @Test
    public void testGetAbsolutePath() throws Exception {

    }

    @Test
    public void testGetBytes() throws Exception {

    }

    @Test
    public void testGetUTF8Text() throws Exception {

    }

    @Test
    public void testGetIterator() throws Exception {

    }
}
