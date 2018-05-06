/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the RatingType class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class RatingTypeUnitTest extends TestCase {
    /**
     * <p>
     * The name of the architecture rating type, and the property name that stores the architecture phase number.
     * </p>
     */
    public static final String ARCHITECTURE_NAME = "Architecture";
    
    /**
     * <p>
     * The name of the development rating type, and the property name that stores the Assembly phase number.
     * </p>
     */
    public static final String ASSEMBLY_NAME = "Assembly";
    
    /**
     * <p>
     * The name of the conceptualization rating type, and the property name that stores the conceptualization
     * phase number.
     */
    public static final String CONCEPTUALIZATION_NAME = "Conceptualization";
    
    /**
     * <p>
     * The name of the design rating type, and the property name that stores the design phase number.
     * </p>
     */
    public static final String DESIGN_NAME = "Design";

    /**
     * <p>
     * The name of the development rating type, and the property name that stores the development phase number.
     * </p>
     */
    public static final String DEVELOPMENT_NAME = "Development";
    
    /**
     * <p>
     * The name of the RIA build rating type, and the property name that stores the RIA build phase number.
     * </p>
     */
    public static final String RIA_BUILD_NAME = "RIA Build";
    
    /**
     * <p>
     * The name of the specification rating type, and the property name that stores the specification phase number.
     * </p>
     */
    public static final String SPECIFICATION_NAME = "Specification";
    
    /**
     * <p>
     * The name of the test scenarios rating type, and the property name that stores the test scenarios phase number.
     * </p>
     */
    public static final String TEST_SCENARIOS_NAME = "Test Scenarios";
    
    /**
     * <p>
     * The name of the test suites rating type, and the property name that stores the test suites phase number.
     * </p>
     */
    public static final String TEST_SUITES_NAME = "Test Suites"; 
    
    /**
     * <p>
     * The name of the UI prototype rating type, and the property name that stores the UI prototype phase number.
     * </p>
     */
    public static final String UI_PROTOTYPE_NAME = "UI Prototype";
    
    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";
    
    /**
     * <p>
     * The default integer code of the architecture phase.
     * </p>
     */
    private static final int DEFAULT_ARCHITECTURE_CODE = 118;  
    
    /**
     * <p>
     * The default integer code of the assembly phase.
     * </p>
     */
    private static final int DEFAULT_ASSEMBLY_CODE = 125;
    
    /**
     * <p>
     * The default integer code of the conceptualization phase.
     * </p>
     */
    private static final int DEFAULT_CONCEPTUALIZATION_CODE = 134;
    
    /**
     * <p>
     * The default integer code of the development phase.
     * </p>
     */
    private static final int DEFAULT_DEV_CODE = 113;

    /**
     * <p>
     * The default integer code of the design phase.
     * </p>
     */
    private static final int DEFAULT_DESIGN_CODE = 112;
    
    /**
     * <p>
     * The default integer code of the RIA build phase.
     * </p>
     */
    private static final int DEFAULT_RIA_BUILD_CODE = 135;
    
    /**
     * <p>
     * The default integer code of the specification phase.
     * </p>
     */
    private static final int DEFAULT_SPECIFICATION_CODE = 117;
    
    /**
     * <p>
     * The default integer code of the test scenarios phase.
     * </p>
     */
    private static final int DEFAULT_TEST_SCENARIOS_CODE = 137;
    
    /**
     * <p>
     * The default integer code of the test suites phase.
     * </p>
     */
    private static final int DEFAULT_TEST_SUITES_CODE = 124;       
    
    /**
     * <p>
     * The default integer code of the UI prototype phase.
     * </p>
     */
    private static final int DEFAULT_UI_PROTOTYPE_CODE = 130;

    /**
     * <p>
     * An RatingType instance for testing.
     * </p>
     */
    private RatingType defaultRatingType = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.addConfig(CONFIG_FILE);

        defaultRatingType = RatingType.getRatingType(DESIGN_NAME);
    }

    /**
     * <p>
     * Set defaultRatingType to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        defaultRatingType = null;

        UnitTestHelper.clearConfig();

        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     */
    public void testGetRatingType_String() {

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", DESIGN_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_DESIGN_CODE, defaultRatingType.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured1() {

        defaultRatingType = RatingType.getRatingType(ASSEMBLY_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", ASSEMBLY_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_ASSEMBLY_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured2() {

        defaultRatingType = RatingType.getRatingType(ARCHITECTURE_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", ARCHITECTURE_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_ARCHITECTURE_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured3() {

        defaultRatingType = RatingType.getRatingType(CONCEPTUALIZATION_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", CONCEPTUALIZATION_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_CONCEPTUALIZATION_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured4() {

        defaultRatingType = RatingType.getRatingType(DESIGN_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", DESIGN_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_DESIGN_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured5() {

        defaultRatingType = RatingType.getRatingType(DEVELOPMENT_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", DEVELOPMENT_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_DEV_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured6() {

        defaultRatingType = RatingType.getRatingType(SPECIFICATION_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", SPECIFICATION_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_SPECIFICATION_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured7() {

        defaultRatingType = RatingType.getRatingType(TEST_SCENARIOS_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", TEST_SCENARIOS_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_TEST_SCENARIOS_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured8() {

        defaultRatingType = RatingType.getRatingType(TEST_SUITES_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", TEST_SUITES_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_TEST_SUITES_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured9() {

        defaultRatingType = RatingType.getRatingType(UI_PROTOTYPE_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", UI_PROTOTYPE_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_UI_PROTOTYPE_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_Configured10() {

        defaultRatingType = RatingType.getRatingType(RIA_BUILD_NAME);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", RIA_BUILD_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_RIA_BUILD_CODE, defaultRatingType.getId());
    }
    
    /**
     * <p>
     * Tests the accuracy of the getRatingType(String).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * The type has not been configured in the SampleConfig.xml
     * </p>
     */
    public void testGetRatingType_String_NonConfigured() {
        defaultRatingType = RatingType.getRatingType("None");

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", "None", defaultRatingType.getName());
        assertEquals("The id should be set correctly.", 0, defaultRatingType.getId());
    }

    /**
     * <p>
     * Tests the failure of the getRatingType(String).
     * </p>
     * <p>
     * If the given typeName is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testGetRatingType_String_NullTypeName() {

        try {
            RatingType.getRatingType(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the getRatingType(String).
     * </p>
     * <p>
     * If the given typeName is empty after trimmed, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testGetRatingType_String_EmptyTypeName() {

        try {
            RatingType.getRatingType("   ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the getRatingType(int).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     */
    public void testGetRatingType_Int() {

        defaultRatingType = RatingType.getRatingType(DEFAULT_DEV_CODE);

        assertNotNull("RatingInfo should be accurately created.", defaultRatingType);
        assertTrue("defaultRatingType should be instance of RatingType.", defaultRatingType instanceof RatingType);

        assertEquals("The name should be set correctly.", DEVELOPMENT_NAME, defaultRatingType.getName());
        assertEquals("The id should be set correctly.", DEFAULT_DEV_CODE, defaultRatingType.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the getRatingType(int).
     * </p>
     * <p>
     * The RatingType instance should be created successfully.
     * </p>
     * <p>
     * If the ratingType is not found, null should be returned.
     * </p>
     */
    public void testGetRatingType_Int_NotFound() {

        defaultRatingType = RatingType.getRatingType(100);

        assertNull("If the ratingType is not found, null should be returned.", defaultRatingType);
    }

    /**
     * <p>
     * Tests the failure of the getRatingType(int).
     * </p>
     * <p>
     * If the given id is not positive, IllegalArgumentException should be thrown.
     * </p>
     * <p>
     * This test gives a zero as the parameter.
     * </p>
     */
    public void testGetRatingType_Int_NotPositive1() {

        try {
            RatingType.getRatingType(0);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the getRatingType(int).
     * </p>
     * <p>
     * If the given id is not positive, IllegalArgumentException should be thrown.
     * </p>
     * <p>
     * This test gives a negative number as the parameter.
     * </p>
     */
    public void testGetRatingType_Int_NotPositive2() {

        try {
            RatingType.getRatingType(-5);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the getter getName().
     * </p>
     */
    public void testGetter_GetName() {

        assertEquals("The name should be got correctly.", DESIGN_NAME, defaultRatingType.getName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getId().
     * </p>
     */
    public void testGetter_GetId() {

        assertEquals("The id should be got correctly.", DEFAULT_DESIGN_CODE, defaultRatingType.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the method toString().
     * </p>
     */
    public void testToString() {

        assertEquals("The string should be the same.", DESIGN_NAME, defaultRatingType.toString());
    }

    /**
     * <p>
     * Tests the accuracy of the method hashCode().
     * </p>
     */
    public void testHashCode() {

        assertEquals("The hashCode should be the same.", DESIGN_NAME.hashCode(), defaultRatingType.hashCode());
    }

    /**
     * <p>
     * Tests the accuracy of the method equals().
     * </p>
     * <p>
     * The given parameter object is just equal to the defaultRatingType, they are both the RatingType and they have the
     * same hashCode.
     * </p>
     */
    public void testEquals_EqualsObject() {
        assertTrue("These two objects should be equal.", defaultRatingType
                .equals(RatingType.getRatingType(DESIGN_NAME)));
    }

    /**
     * <p>
     * Tests the accuracy of the method equals().
     * </p>
     * <p>
     * The given parameter object is not equal to the defaultRatingType, they are both the RatingType but they don't
     * have the same hashCode.
     * </p>
     */
    public void testEquals_NonEqualsObject1() {
        assertFalse("These two objects should be non-equal.", defaultRatingType.equals(RatingType
                .getRatingType(DEVELOPMENT_NAME)));
    }

    /**
     * <p>
     * Tests the accuracy of the method equals().
     * </p>
     * <p>
     * The given parameter object is not equal to the defaultRatingType, the given object is null.
     * </p>
     */
    public void testEquals_NonEqualsObject2() {
        assertFalse("These two objects should be non-equal.", defaultRatingType.equals(null));
    }

    /**
     * <p>
     * Tests the accuracy of the method equals().
     * </p>
     * <p>
     * The given parameter object is not equal to the defaultRatingType, the given object is not null but it is not the
     * RatingType instance.
     * </p>
     */
    public void testEquals_NonEqualsObject3() {
        assertFalse("These two objects should be non-equal.", defaultRatingType.equals(new Object()));
    }
}
