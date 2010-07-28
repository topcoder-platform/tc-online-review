/**
 *
 * Copyright (c) %{current_year}, TopCoder, Inc. All rights reserved
 */
package %{package.name};

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version %{version.major}.%{version.minor}
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        return suite;
    }

}
