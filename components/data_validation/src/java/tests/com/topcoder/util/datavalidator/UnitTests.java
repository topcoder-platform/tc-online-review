/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.bytevalidator.EqualToByteValidatorTestCases;
import com.topcoder.util.datavalidator.bytevalidator.GreaterThanByteValidatorTestCases;
import com.topcoder.util.datavalidator.bytevalidator.GreaterThanOrEqualToByteValidatorTestCases;
import com.topcoder.util.datavalidator.bytevalidator.InExclusiveRangeByteValidatorTestCases;
import com.topcoder.util.datavalidator.bytevalidator.InRangeByteValidatorTestCases;
import com.topcoder.util.datavalidator.bytevalidator.LessThanByteValidatorTestCases;
import com.topcoder.util.datavalidator.bytevalidator.LessThanOrEqualToByteValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.EqualToDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.GreaterThanDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.GreaterThanOrEqualToDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.InExclusiveRangeDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.InRangeDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.IsNegativeDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.IsPositiveDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.LessThanDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.doubletype.LessThanOrEqualToDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.EqualToFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.GreaterThanFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.GreaterThanOrEqualToFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.InExclusiveRangeFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.InRangeFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.IsNegativeFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.IsPositiveFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.LessThanFloatValidatorTestCases;
import com.topcoder.util.datavalidator.floatvalidator.LessThanOrEqualToFloatValidatorTestCases;
import com.topcoder.util.datavalidator.integer.EqualToIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.GreaterThanIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.GreaterThanOrEqualToIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.InExclusiveRangeIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.InRangeIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.IsEvenIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.IsNegativeIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.IsOddIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.IsPositiveIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.LessThanIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.integer.LessThanOrEqualToIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.GreaterThanLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.GreaterThanOrEqualToLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.InExclusiveRangeLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.InRangeLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.IsEvenLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.IsOddLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.IsPositiveLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.LessThanLongValidatorTestCases;
import com.topcoder.util.datavalidator.longtype.LessThanOrEqualToLongValidatorTestCases;
import com.topcoder.util.datavalidator.shorttype.GreaterThanOrEqualToShortValidatorTestCases;
import com.topcoder.util.datavalidator.shorttype.GreaterThanShortValidatorTestCases;
import com.topcoder.util.datavalidator.shorttype.InExclusiveRangeShortValidatorTestCases;
import com.topcoder.util.datavalidator.shorttype.InRangeShortValidatorTestCases;
import com.topcoder.util.datavalidator.shorttype.LessThanOrEqualToShortValidatorTestCases;
import com.topcoder.util.datavalidator.shorttype.LessThanShortValidatorTestCases;
import com.topcoder.util.datavalidator.string.ContainsWithStringValidatorTestCases;
import com.topcoder.util.datavalidator.string.EndsWithStringValidatorTestCases;
import com.topcoder.util.datavalidator.string.LengthStringValidatorTestCases;
import com.topcoder.util.datavalidator.string.RegexpStringValidatorTestCases;
import com.topcoder.util.datavalidator.string.StartsWithStringValidatorTestCases;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * TODO
     *
     * @return TODO
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(TypeValidatorTestCases.class);

        suite.addTestSuite(BundleInfoTestCases.class);
        suite.addTestSuite(CompareDirectionTestCases.class);

        suite.addTestSuite(IsEvenIntegerValidatorTestCases.class);
        suite.addTestSuite(IsOddIntegerValidatorTestCases.class);
        suite.addTestSuite(EqualToIntegerValidatorTestCases.class);
        suite.addTestSuite(GreaterThanIntegerValidatorTestCases.class);
        suite.addTestSuite(GreaterThanOrEqualToIntegerValidatorTestCases.class);
        suite.addTestSuite(LessThanIntegerValidatorTestCases.class);
        suite.addTestSuite(LessThanOrEqualToIntegerValidatorTestCases.class);
        suite.addTestSuite(IsNegativeIntegerValidatorTestCases.class);
        suite.addTestSuite(IsPositiveIntegerValidatorTestCases.class);
        suite.addTestSuite(InRangeIntegerValidatorTestCases.class);
        suite.addTestSuite(InExclusiveRangeIntegerValidatorTestCases.class);

        suite.addTestSuite(IsEvenLongValidatorTestCases.class);
        suite.addTestSuite(IsOddLongValidatorTestCases.class);
        suite.addTestSuite(GreaterThanLongValidatorTestCases.class);
        suite.addTestSuite(GreaterThanOrEqualToLongValidatorTestCases.class);
        suite.addTestSuite(LessThanLongValidatorTestCases.class);
        suite.addTestSuite(LessThanOrEqualToLongValidatorTestCases.class);
        suite.addTestSuite(IsNegativeIntegerValidatorTestCases.class);
        suite.addTestSuite(IsPositiveLongValidatorTestCases.class);
        suite.addTestSuite(InRangeLongValidatorTestCases.class);
        suite.addTestSuite(InExclusiveRangeLongValidatorTestCases.class);

        suite.addTestSuite(EqualToIntegerValidatorTestCases.class);
        suite.addTestSuite(GreaterThanShortValidatorTestCases.class);
        suite.addTestSuite(GreaterThanOrEqualToShortValidatorTestCases.class);
        suite.addTestSuite(LessThanShortValidatorTestCases.class);
        suite.addTestSuite(LessThanOrEqualToShortValidatorTestCases.class);
        suite.addTestSuite(InRangeShortValidatorTestCases.class);
        suite.addTestSuite(InExclusiveRangeShortValidatorTestCases.class);

        suite.addTestSuite(EqualToDoubleValidatorTestCases.class);
        suite.addTestSuite(GreaterThanDoubleValidatorTestCases.class);
        suite.addTestSuite(GreaterThanOrEqualToDoubleValidatorTestCases.class);
        suite.addTestSuite(LessThanDoubleValidatorTestCases.class);
        suite.addTestSuite(LessThanOrEqualToDoubleValidatorTestCases.class);
        suite.addTestSuite(IsNegativeDoubleValidatorTestCases.class);
        suite.addTestSuite(IsPositiveDoubleValidatorTestCases.class);
        suite.addTestSuite(InRangeDoubleValidatorTestCases.class);
        suite.addTestSuite(InExclusiveRangeDoubleValidatorTestCases.class);

        suite.addTestSuite(AndValidatorTestCases.class);
        suite.addTestSuite(OrValidatorTestCases.class);

        suite.addTestSuite(NotValidatorTestCases.class);
        suite.addTestSuite(NullValidatorTestCases.class);
        suite.addTestSuite(CharacterValidatorTestCases.class);
        suite.addTestSuite(PrimitiveValidatorTestCases.class);
        suite.addTestSuite(TypeValidatorTestCases.class);

        suite.addTestSuite(StartsWithStringValidatorTestCases.class);
        suite.addTestSuite(EndsWithStringValidatorTestCases.class);
        suite.addTestSuite(ContainsWithStringValidatorTestCases.class);
        suite.addTestSuite(LengthStringValidatorTestCases.class);
        suite.addTestSuite(RegexpStringValidatorTestCases.class);

        suite.addTestSuite(EqualToByteValidatorTestCases.class);
        suite.addTestSuite(GreaterThanByteValidatorTestCases.class);
        suite.addTestSuite(GreaterThanOrEqualToByteValidatorTestCases.class);
        suite.addTestSuite(LessThanByteValidatorTestCases.class);
        suite.addTestSuite(LessThanOrEqualToByteValidatorTestCases.class);
        suite.addTestSuite(InRangeByteValidatorTestCases.class);
        suite.addTestSuite(InExclusiveRangeByteValidatorTestCases.class);

        suite.addTestSuite(EqualToFloatValidatorTestCases.class);
        suite.addTestSuite(GreaterThanFloatValidatorTestCases.class);
        suite.addTestSuite(GreaterThanOrEqualToFloatValidatorTestCases.class);
        suite.addTestSuite(LessThanFloatValidatorTestCases.class);
        suite.addTestSuite(LessThanOrEqualToFloatValidatorTestCases.class);
        suite.addTestSuite(IsNegativeFloatValidatorTestCases.class);
        suite.addTestSuite(IsPositiveFloatValidatorTestCases.class);
        suite.addTestSuite(InRangeFloatValidatorTestCases.class);
        suite.addTestSuite(InExclusiveRangeFloatValidatorTestCases.class);

        suite.addTestSuite(BooleanValidatorTestCases.class);

        suite.addTestSuite(Demo.class);

        return suite;
    }
}
