/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.TypeValidator;

import junit.framework.TestCase;
/**
 * Accuracy tests for the TypeValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class TypeValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * The TypeValidator instance for testing.
     * </p>
     */
    private TypeValidator typeValidator;

    /**
     * <p>
     * The BundleInfo typeValidator for testing.
     * </p>
     */
    private BundleInfo bundleInfo;
    /**
     * <p>
     * the ObjectValidator typeValidator for testing.
     * </p>
     */
    private ObjectValidator validator;
    /**
     * <p>
     * set up the environment.
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test");
        bundleInfo.setDefaultMessage("accuracy test");
        validator = IntegerValidator.greaterThan(4);
        typeValidator = new TypeValidator(Integer.class);
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    protected void tearDown() {
        bundleInfo = null;
        validator=null;
        typeValidator = null;
    }
    /**
     * <p>
     * test the constructor for accuracy.
     * </p>
     *
     */
    public void testCtor_1(){
        assertNotNull("The typeValidator should not be null.", typeValidator);
    }
    /**
     * <p>
     * test the constructor with two parameters (class, bundleinfo)for accuracy.
     * </p>
     *
     */
    public void testCtor_2(){
        bundleInfo.setMessageKey("GreaterThan_Bundle_Key");
        typeValidator = new TypeValidator(String.class, bundleInfo);
        assertNotNull("The typeValidator should not be null.", typeValidator);
    }
    /**
     * <p>
     * test the constructor with two parameters (validator, Class) for accuracy.
     * </p>
     *
     */
    public void testCtor_3(){
        typeValidator = new TypeValidator(this.validator,String.class);
        assertNotNull("The typeValidator should not be null.", typeValidator);
    }
    /**
     * <p>
     * test the constructor for failure. the validator parameter is invalid.
     * So it should throw an IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor_3_Failure_1(){
        try{
            new TypeValidator(null,String.class);
            fail("there should be an IllegalArgumentException");
        }catch(IllegalArgumentException e){
            // good
        }
    }
    /**
     * <p>
     * test the constructor with three parameters for accuracy.
     * </p>
     *
     */
    public void testCtor_4(){
        this.bundleInfo.setMessageKey("GreaterThan_Bundle_Key");
        typeValidator = new TypeValidator(this.validator,String.class,this.bundleInfo);
        assertNotNull("The typeValidator should not be null.", typeValidator);
    }
    /**
     * <p>
     * test the method valid for accuracy.
     * </p>
     *
     */
    public void testValid_Accuracy_1(){
        assertFalse(typeValidator.valid(null));
    }
    /**
     * <p>
     * test the method valid for accuracy.
     * </p>
     *
     */
    public void testValid_Accuracy_2(){
        Object obj = new Object();
        assertFalse(typeValidator.valid(obj));
    }
    /**
     * <p>
     * test the method valid for accuracy.
     * </p>
     *
     */
    public void testValid_Accuracy_3(){
        Object obj = new Integer(5);
        assertTrue(typeValidator.valid(obj));
    }
    /**
     * <p>
     * test the method valid for accuracy.
     * </p>
     *
     */
    public void testValid_Accuracy_4(){
        Object obj = new Integer(5);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        assertTrue(typeValidator.valid(obj));
    }
    /**
     * <p>
     * test the method valid for accuracy.
     * </p>
     *
     */
    public void testValid_Accuracy_5(){
        Object obj = new Integer(1);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        assertFalse(typeValidator.valid(obj));
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessage_Accuracy_1(){
        String result = typeValidator.getMessage(null);
        assertEquals("the two string should be equal.","object is null",result);
        
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessage_Accuracy_2(){
        Object obj = new Object();
        String result = typeValidator.getMessage(obj);
        assertTrue(result.indexOf("not")>=0);
        
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessage_Accuracy_3(){
        Object obj = new Integer(5);
        String result = typeValidator.getMessage(obj);
        assertNull(result);
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessage_Accuracy_4(){
        Object obj = new Integer(5);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        String result = typeValidator.getMessage(obj);
        assertNull(result);
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessage_Accuracy_5(){
        Object obj = new Integer(2);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        String result = typeValidator.getMessage(obj);
        assertNotNull(result);
    }
    /**
     * <p>
     * test the method GetMessages for accuracy.
     * </p>
     *
     */
    public void testGetMessages_Accuracy_1(){
        String[] result = typeValidator.getMessages(null);
        assertNotNull(result);
        
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessages_Accuracy_2(){
        Object obj = new Object();
        String[] result = typeValidator.getMessages(obj);
        assertNotNull(result);
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessages_Accuracy_3(){
        Object obj = new Integer(5);
        String[] result = typeValidator.getMessages(obj);
        assertNull(result);
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessages_Accuracy_4(){
        Object obj = new Integer(5);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        String[] result = typeValidator.getMessages(obj);
        assertNull(result);
    }
    /**
     * <p>
     * test the method GetMessage for accuracy.
     * </p>
     *
     */
    public void testGetMessages_Accuracy_5(){
        Object obj = new Integer(2);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        String[] result = typeValidator.getMessages(obj);
        assertNotNull(result);
    }
    /**
     * <p>
     * test the method GetAllMessages for accuracy.
     * </p>
     *
     */
    public void testGetAllMessages_Accuracy_1(){
        String[] result = typeValidator.getAllMessages(null);
        assertNotNull(result);
        
    }
    /**
     * <p>
     * test the method GetAllMessage for accuracy.
     * </p>
     *
     */
    public void testGetAllMessages_Accuracy_2(){
        Object obj = new Object();
        String[] result = typeValidator.getAllMessages(obj);
        assertNotNull(result);
    }
    /**
     * <p>
     * test the method GetAllMessage for accuracy.
     * </p>
     *
     */
    public void testGetAllMessages_Accuracy_3(){
        Object obj = new Integer(5);
        String[] result = typeValidator.getAllMessages(obj);
        assertNull(result);
    }
    /**
     * <p>
     * test the method GetAllMessage for accuracy.
     * </p>
     *
     */
    public void testGetAllMessages_Accuracy_4(){
        Object obj = new Integer(5);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        String[] result = typeValidator.getAllMessages(obj);
        assertNull(result);
    }
    /**
     * <p>
     * test the method GetAllMessage for accuracy.
     * </p>
     *
     */
    public void testGetAllMessages_Accuracy_5(){
        Object obj = new Integer(2);
        typeValidator = new TypeValidator(this.validator,Integer.class);
        String[] result = typeValidator.getAllMessages(obj);
        assertNotNull(result);
    }
}
