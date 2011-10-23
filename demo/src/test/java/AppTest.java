
import java.util.UUID;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest  extends TestCase
{
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(AppTest.class);
	//private final SAXReader saxReader = new SAXReader();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     * @throws Exception 
     */
	public AppTest( String testName ) throws Exception
    {
        super( testName );
        System.out.println(UUID.randomUUID().getLeastSignificantBits());
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
	
}
