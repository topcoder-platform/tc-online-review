/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.BigDecimalConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.BlobConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.BooleanConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.ByteArrayConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.ByteConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.ClobConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.DateConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.DoubleConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.FloatConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.IntConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.LongConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.ShortConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.StringConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.TimeConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.TimestampConverterFailureTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion.URLConverterFailureTests;

/**
 * <p>This test case aggregates all failure test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // tests for package com.topcoder.util.sql.databaseabstraction
        suite.addTest(CustomResultSetFailureTests.suite());
        suite.addTest(DatabaseAbstractorFailureTests.suite());
        suite.addTest(OnDemandMapperFailureTests.suite());

        // tests for package com.topcoder.util.sql.databaseabstraction.ondemandconversion
        suite.addTest(TimeConverterFailureTests.suite());
        suite.addTest(TimestampConverterFailureTests.suite());
        suite.addTest(IntConverterFailureTests.suite());
        suite.addTest(ShortConverterFailureTests.suite());
        suite.addTest(DoubleConverterFailureTests.suite());
        suite.addTest(ByteConverterFailureTests.suite());
        suite.addTest(ClobConverterFailureTests.suite());
        suite.addTest(BlobConverterFailureTests.suite());
        suite.addTest(BooleanConverterFailureTests.suite());
        suite.addTest(FloatConverterFailureTests.suite());
        suite.addTest(DateConverterFailureTests.suite());
        suite.addTest(LongConverterFailureTests.suite());
        suite.addTest(BigDecimalConverterFailureTests.suite());
        suite.addTest(ByteArrayConverterFailureTests.suite());
        suite.addTest(StringConverterFailureTests.suite());
        suite.addTest(URLConverterFailureTests.suite());

        return suite;
    }

}
