/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.UploadFileSpec;

import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import com.topcoder.servlet.request.MockHttpServletRequest;
import com.topcoder.servlet.request.UploadedFile;

import com.topcoder.util.config.ConfigManager;

import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;


/**
 * <p>
 * Defines helper methods used in tests.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
final class AccuracyTestHelper {
    /** Represents the <code>ServletRunner</code> instance for testing. */
    private static ServletRunner runner = null;

    /** Represents the value of content type for testing. */
    private static final String CONTENT_TYPE = "multipart/form-data; boundary=--HttpUnit-part0-aSgQ2M";

    /**
     * Represents the url of the web server for testing. e.g. http://localhost:8080. The value can be arbitrary since
     * we do not do the real job in the web server.
     */
    private static final String URL = "http://localhost:8080/";

    /**
     * <p>
     * Prepares the <code>ServletRequest</code> instance for testing. It will has the input map values as the
     * parameters.
     * </p>
     *
     * @param files the files to set the parameter values into the request.
     * @param parameters the parameters to set the parameter values into the request.
     *
     * @return the <code>ServletRequest</code> instance.
     *
     * @throws Exception any exception to JUnit.
     */
    static ServletRequest prepareRequest(Map files, Map parameters)
        throws Exception {
        // create the webRequest
        PostMethodWebRequest webRequest = new PostMethodWebRequest(URL);
        webRequest.setMimeEncoded(true);

        for (Iterator iter = files.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            String name = (String) entry.getKey();
            List value = (List) entry.getValue();
            webRequest.setParameter(name, (UploadFileSpec[]) value.toArray(new UploadFileSpec[value.size()]));
        }

        for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            String name = (String) entry.getKey();
            List value = (List) entry.getValue();
            webRequest.setParameter(name, (String[]) value.toArray(new String[value.size()]));
        }

        // run this webRequest
        if (runner == null) {
            runner = new ServletRunner();
        }

        ServletUnitClient client = runner.newClient();
        InvocationContext ic = client.newInvocation(webRequest);

        return new MockHttpServletRequest(AccuracyTestHelper.readContent(ic.getRequest().getInputStream()), CONTENT_TYPE);
    }

    /**
     * <p>
     * Asserts the two given byte arrays to be equals. The two byte arrays will be regarded to be equals only if both
     * the length and the content are equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two byte arrays are not equals.
     * @param expected the expected byte array.
     * @param actual the actual byte array.
     */
    static void assertEquals(String errorMessage, byte[] expected, byte[] actual) {
        Assert.assertEquals(errorMessage, expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(errorMessage, expected[i], actual[i]);
        }
    }

    /**
     * <p>
     * Gets the byte content from the given input stream.
     * </p>
     *
     * @param inputStream the given input stream to get the content.
     *
     * @return the byte content from the given input stream.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    static byte[] readContent(InputStream inputStream)
        throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;

        while ((len = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }

        inputStream.close();
        output.close();

        return output.toByteArray();
    }

    /**
     * <p>
     * add the config of given config file.
     * </p>
     *
     * @param configFile the given config file.
     *
     * @throws Exception unexpected exception.
     */
    static void addConfig(String configFile) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(configFile);
    }

    /**
     * <p>
     * clear the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    static void clearConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance.
     * </p>
     *
     * @param type the class which the private field belongs to.
     * @param instance the instance which the private field belongs to.
     * @param name the name of the private field to be retrieved.
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     */
    static Object getPrivateField(Class type, Object instance, String name) {
        Field field = null;
        Object obj = null;

        try {
            // Get the reflection of the field and get the value
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // Reset the accessibility
                field.setAccessible(false);
            }
        }

        return obj;
    }

    /**
     * <p>
     * Asserts the two given object arrays to be equals. The two object arrays will be regarded to be equals only if
     * both the length and the content are equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two object arrays are not equals.
     * @param expected the expected object array.
     * @param actual the actual object array.
     */
    static void assertEquals(String errorMessage, Object[] expected, Object[] actual) {
        Assert.assertEquals(errorMessage, expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(errorMessage, expected[i], actual[i]);
        }
    }

    /**
     * <p>
     * Asserts the two given UploadedFile instances to be equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two object arrays are not equals.
     * @param expected the expected File instance.
     * @param actual the actual UploadedFile instance.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    static void assertEquals(String errorMessage, File expected, UploadedFile actual)
        throws Exception {
        Assert.assertEquals(errorMessage, expected.getName(), actual.getRemoteFileName());
        assertEquals(errorMessage, new FileInputStream(expected), actual.getInputStream());
    }

    /**
     * <p>
     * Asserts the two given InputStream instances to be equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two object arrays are not equals.
     * @param expected the expected InputStream instance.
     * @param actual the actual InputStream instance.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    static void assertEquals(String errorMessage, InputStream expected, InputStream actual)
        throws Exception {
        assertEquals(errorMessage, readContent(expected), readContent(actual));
    }

    /**
     * <p>
     * Instanciate a class by specified private constructor using reflection.
     * </p>
     *
     * @param classType the class which will be instanced.
     * @param parameterTypes the parameter types of specified constructor.
     * @param args the arguments to instance
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     */
    static Object instanciateClassByPrivateConstructor(Class classType, Class[] parameterTypes, Object[] args)
        throws Exception {
        Constructor constructor = classType.getDeclaredConstructor(parameterTypes);

        try {
            constructor.setAccessible(true);

            return constructor.newInstance(args);
        } finally {
            constructor.setAccessible(false);
        }
    }
}
