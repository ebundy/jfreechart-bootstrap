package davidhxxx.teach.jfreecharttuto.util;

import org.junit.Test;

import junit.framework.Assert;

public class ApplicationDirsTest {

    @Test
    public void static_bloc_configuration_is_ok() throws Exception {

	String appBaseDir = ApplicationDirs.BASE_DIR;

	String expectedSubPath = "/src/test/resources/tutorial-trading-test";
	int lengthPath = expectedSubPath.length();
	String actualSubPath = appBaseDir.substring(appBaseDir.length() - lengthPath);
	Assert.assertEquals(expectedSubPath, actualSubPath);
    }
    
    

}
