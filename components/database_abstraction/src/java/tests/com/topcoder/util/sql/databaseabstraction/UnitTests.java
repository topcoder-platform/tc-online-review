/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BigDecimalConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BlobConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BooleanConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteArrayConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ClobConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DateConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DoubleConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.FloatConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.IntConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.LongConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ShortConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimeConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimestampConverterTest;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.URLConverterTest;

/**
 * <p>
 * This test case aggregates all unit test cases.
 * </p>
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.0
 */
public class UnitTests extends TestCase {

    /**
     * All unit test cases.
     *
     * @return unit test.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // com.topcoder.util.sql.databaseabstraction
        suite.addTest(new TestSuite(AbstractionHelperTest.class));
        suite.addTest(new TestSuite(ColumnTest.class));
        suite.addTest(new TestSuite(CustomResultSetTest.class));
        suite.addTest(new TestSuite(CustomResultSetODBCTest.class));
        suite.addTest(new TestSuite(CustomResultSetDataTypesTest.class));
        suite.addTest(new TestSuite(CustomResultSetMetaDataTest.class));
        suite.addTest(new TestSuite(CustomResultSetMetaDataODBCTest.class));
        suite.addTest(new TestSuite(DatabaseAbstractorTest.class));
        suite.addTest(new TestSuite(DatabaseAbstractorODBCTest.class));
        suite.addTest(new TestSuite(MapperTest.class));
        suite.addTest(new TestSuite(OnDemandMapperTest.class));
        suite.addTest(new TestSuite(RowDataValueTest.class));

        // com.topcoder.util.sql.databaseabstraction.ondemandconversion
        suite.addTest(new TestSuite(BigDecimalConverterTest.class));
        suite.addTest(new TestSuite(BlobConverterTest.class));
        suite.addTest(new TestSuite(BooleanConverterTest.class));
        suite.addTest(new TestSuite(ByteArrayConverterTest.class));
        suite.addTest(new TestSuite(ByteConverterTest.class));
        suite.addTest(new TestSuite(ClobConverterTest.class));
        suite.addTest(new TestSuite(DateConverterTest.class));
        suite.addTest(new TestSuite(DoubleConverterTest.class));
        suite.addTest(new TestSuite(FloatConverterTest.class));
        suite.addTest(new TestSuite(IntConverterTest.class));
        suite.addTest(new TestSuite(LongConverterTest.class));
        suite.addTest(new TestSuite(ShortConverterTest.class));
        suite.addTest(new TestSuite(StringConverterTest.class));
        suite.addTest(new TestSuite(TimeConverterTest.class));
        suite.addTest(new TestSuite(TimestampConverterTest.class));
        suite.addTest(new TestSuite(URLConverterTest.class));

        // exceptions
        suite.addTest(new TestSuite(IllegalMappingExceptionTest.class));
        suite.addTest(new TestSuite(InvalidCursorStateExceptionTest.class));
        suite.addTest(new TestSuite(NullColumnValueExceptionTest.class));

        // demo
        suite.addTest(new TestSuite(Demo.class));

        return suite;
    }
};
